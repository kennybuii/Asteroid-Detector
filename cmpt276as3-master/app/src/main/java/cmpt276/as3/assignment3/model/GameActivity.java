package cmpt276.as3.assignment3.model;

//This class supports all things related to the gameplay of Asteroid Detector, such as the UI
//and logic of the game

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

import cmpt276.as3.assignment3.UI.CongratulationsFragment;
import cmpt276.as3.assignment3.R;

public class GameActivity extends AppCompatActivity {
    private static int NUM_ROWS = 0;
    private static int NUM_COLS = 0;
    Button buttons[][];
    private static int NUM_ASTEROID = 0;

    private static int[] randomRowArray;
    private static int[] randomColArray;

    private static int[][] asteroidBoard;

    private static int asteroidsFound = 0;
    private static int scans = 0;
    private static int gamesPlayed = 0;

    private static String APP_PREFS = "AppPrefs";
    private static String GAMES_PLAYED = "Games Played";
    private static String APP_ONE = "AppOne";
    private static String SCAN_ONE = "Scan One";
    private static String APP_TWO = "AppTwo";
    private static String SCAN_TWO = "Scan Two";
    private static String APP_THREE = "AppThree";
    private static String SCAN_THREE = "Scan Three";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //reset the data
        asteroidsFound = 0;
        scans = 0;

        //grab the data
        OptionsData data = OptionsData.getInstance();
        NUM_ROWS = data.getRow();
        NUM_COLS = data.getColumn();
        NUM_ASTEROID = data.getMines();
        buttons = new Button[NUM_ROWS][NUM_COLS];

        //If user pressed erase button in options, set all values to 0 (save and high score)
        if(data.isEraseButton()) {
            gamesPlayed = 0;
            saveGamesPlayed(0);
            data.setEraseButton(false);
            saveScanOne(0);
            saveScanTwo(0);
            saveScanThree(0);
        }else {
            gamesPlayed = getSaveGamesPlayed(this);
        }

        updateUI(asteroidsFound, scans);

        //insert the asteroids
        insertAsteroid();

        //create the board
        asteroidBoard = createBoard();

        //populate the board
        populateButtons(asteroidBoard);
    }

    //updates the number of scans, asteroids found and games played on the screen
    private void updateUI(int asteroids, int scans) {
        TextView asteroidText = findViewById(R.id.asteroidText);
        TextView satelliteText = findViewById(R.id.satelliteText);
        TextView gamesPlayedText = findViewById(R.id.playedText);
        asteroidText.setText("Found " + asteroidsFound + " of " + NUM_ASTEROID + " asteroids");
        satelliteText.setText("Satellite scans used: " + scans);
        gamesPlayedText.setText("Games played: " + gamesPlayed);
    }

    //randomizes asteroids that will appear on the baord
    private void insertAsteroid() {
        Random random = new Random();

        //loading a randomly generated row and col number
        int randomRow = random.nextInt(NUM_ROWS);
        int randomCol = random.nextInt(NUM_COLS);

        randomRowArray = new int[NUM_ASTEROID];
        randomColArray = new int[NUM_ASTEROID];

        //setting all values on board to -1
        for (int i = 0; i < NUM_ASTEROID; i++) {
            randomRowArray[i] = -1;
            randomColArray[i] = -1;
        }
        //putting coordinates of random asteroids into the two arrays randomRow and randomCol,
        //if the newly generated numbers are the same as last iteration, then we want to compare with all
        //previous coordinates so we set j = -1 and generate new random numbers
        for (int count = 0; count < NUM_ASTEROID; count++) {
            for (int j = 0; j < NUM_ASTEROID; j++) {
                if ( (randomRow == randomRowArray[j]) && (randomCol == randomColArray[j]) ){
                    j = -1;
                    randomRow = random.nextInt(NUM_ROWS);
                    randomCol = random.nextInt(NUM_COLS);

                }
            }
            randomRowArray[count] = randomRow;
            randomColArray[count] = randomCol;
        }
    }

    //creates the board
    private int[][] createBoard() {
        int[][] asteroidBoard = new int[NUM_ROWS][NUM_COLS];

        //setting the entire board as 0
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                asteroidBoard[i][j] = 0;
            }
        }

        //going through the board again, and whenever our asteroid coordinates are the same as
        //the current coordiantes, then we set that board's coordinate to 1, so then anywhere a 1
        //appears, that means an asteroid is there
        for (int count = 0; count < NUM_ASTEROID; count++ ) {
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    if (i == randomRowArray[count] && j == randomColArray[count]) {
                        asteroidBoard[i][j] = 1;
                    }
                }
            }
        }
        return asteroidBoard;
    }

    private void populateButtons(int[][] board) {

        TableLayout table = findViewById(R.id.tableForButtons);
        for (int row = 0; row < NUM_ROWS; row++) {
            //sets up new row for the table
            TableRow tableRow = new TableRow(this);

            //scaling table to fill out the space
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);

            for (int col = 0; col < NUM_COLS; col++) {
                //creating clone vars as final, so we can pass them within our internal methods
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                final int[] FINAL_ROW_ARRAY = randomRowArray;
                final int[] FINAL_COL_ARRAY = randomColArray;
                final int[][] ASTEROID_BOARD = board;
                final int SCANS = scans;

                //add new button for each column
                Button button = new Button(this);

                //scaling the buttons to fill out space
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT,
                        1.0f));

                //make text not clip
                button.setPadding(0,0,0,0);

                //there are 3 cases when user selects a button:
                //they clicked on an asteroid, they clicked on nothing, or they clicked on an already revealed asteroid
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //our end game condition is checked after every time an asteroid is checked
                        if(ASTEROID_BOARD[FINAL_ROW][FINAL_COL] == 1) {
                            asteroidFound(ASTEROID_BOARD, FINAL_COL, FINAL_ROW, FINAL_COL_ARRAY, FINAL_ROW_ARRAY);
                            ASTEROID_BOARD[FINAL_ROW][FINAL_COL]= 2;
                            updateButtons(FINAL_COL, FINAL_ROW);
                            finishGame();
                            updateUI(asteroidsFound, scans);
                        }
                        else if (ASTEROID_BOARD[FINAL_ROW][FINAL_COL] == 2)
                        {
                            buttonAnimation(FINAL_COL, FINAL_ROW);
                            emptyButton(ASTEROID_BOARD, FINAL_COL, FINAL_ROW);
                            updateUI(asteroidsFound, scans);
                        }
                        else {
                            buttonAnimation(FINAL_COL, FINAL_ROW);
                            emptyButton(ASTEROID_BOARD, FINAL_COL, FINAL_ROW);
                            updateUI(asteroidsFound, scans);
                        }
                    }
                });
                tableRow.addView((button));
                buttons[row][col] = button;
            }
        }
    }

    //if the uesr clicked on an asteroid, then we need to change the background image of that button to an asteroid
    private void asteroidFound(int[][] board, int col, int row, int[] colAsteroid, int[] rowAsteroid ) {
        Button button = buttons[row][col];
        for (int i = 0; i < NUM_ASTEROID; i++ ) {
            if (colAsteroid[i] == col && rowAsteroid[i] == row) {

                //lock button sizes
                lockButtonSizes();

                //scale image to button
                int newWidth = button.getWidth();
                int newHeight = button. getHeight();
                Bitmap orginalBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
                Bitmap scaledBitMap = Bitmap.createScaledBitmap(orginalBitMap, newWidth,newHeight, true);
                Resources resource = getResources();
                button.setBackground(new BitmapDrawable(resource,scaledBitMap));
            }
        }
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.explosion);
        mp.start();
        asteroidsFound++;
    }

    //if the user did not click on an asteroid, we need to go through all the already found
    //asteroids in the same row and column of this button and display the number of asteroids
    //left in that button's row and column
    private void emptyButton(int[][] board, int col, int row) {
        int currentAsteroids = 0;
        Button button = buttons[row][col];

        for(int i = 0; i < NUM_ROWS; i++) {
            if ( board[i][col] == 1) {
                currentAsteroids++;
            }
        }
        for(int i = 0; i < NUM_COLS; i++) {
            if ( board[row][i] == 1) {
                currentAsteroids++;
            }
        }

        button.setText("" + currentAsteroids);
        button.setEnabled(false);
        button.setTextColor(Color.BLACK);
        scans++;
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sattelitescan);
        mp.start();
    }

    //locks the button sizes so the background image doesn't distort the board
    private void lockButtonSizes() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0 ; col < NUM_COLS; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);

            }
        }
    }

    //every time a scan is used, across the entire row and column of that button,
    //we apply an animation
    private void buttonAnimation(int col, int row) {

        for (int i = 0; i < NUM_ROWS; i++) {
            Button button = buttons[i][col];
            final Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            button.startAnimation(myAnim);
        }

        for (int j = 0; j < NUM_COLS; j++) {
            Button button = buttons[row][j];
            final Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            button.startAnimation(myAnim);
        }

    }

    //every time a scan is performed, all revealed tiles on that row and column need to have their text updated
    //to the current amount of asteroids in that tile's row and column
    private void updateButtons(int col, int row) {

        for(int i = 0; i < NUM_ROWS; i++) {
            if (buttons[i][col].getText() != ""){
                int asteroidsinRow = Integer.parseInt(buttons[i][col].getText().toString());
                asteroidsinRow--;
                buttons[i][col].setText("" + asteroidsinRow);
            }
        }

        for(int i = 0; i < NUM_COLS; i++) {
            if (buttons[row][i].getText() != ""){
                int asteroidsinCol = Integer.parseInt(buttons[row][i].getText().toString());
                asteroidsinCol--;
                buttons[row][i].setText("" + asteroidsinCol);
            }
        }
    }

    //opens up our congratulations screen (alert dialog) when all asteroids are found
    private void finishGame( ){
        if (asteroidsFound == NUM_ASTEROID) {
            gamesPlayed++;
            FragmentManager manager = getSupportFragmentManager();
            CongratulationsFragment dialog = new CongratulationsFragment();
            dialog.show(manager, "CongratuluationsDialog");


            saveGamesPlayed(gamesPlayed); //Save games played
            highScoreDisplay();
        }
    }

    //Display high score in Options application
    private void highScoreDisplay() {
        OptionsData data = OptionsData.getInstance();

        if(getSaveGamesPlayed(this) == 1) {
            if(data.getRow() == 4 && data.getColumn() == 6) {
                saveScanOne(scans);
            }else if(data.getRow() == 5 && data.getColumn() == 10) {
                saveScanTwo(scans);
            }else if(data.getRow() == 6 && data.getColumn() == 15) {
                saveScanThree(scans);
            }
        }else if(getSaveGamesPlayed(this) > 1){
            if(data.getRow() == 4 && data.getColumn() == 6) {
                if(getSaveScanOne(this) == 0) {
                    saveScanOne(scans);
                }else if(scans < getSaveScanOne(this)){
                    saveScanOne(scans);
                }
            }else if(data.getRow() == 5 && data.getColumn() == 10) {
                if(getSaveScanTwo(this) == 0) {
                    saveScanTwo(scans);
                }else if(scans < getSaveScanTwo(this)) {
                    saveScanTwo(scans);
                }
            }else if(data.getRow() == 6 && data.getColumn() == 15) {
                if(getSaveScanThree(this) == 0) {
                    saveScanThree(scans);
                }else if(scans < getSaveScanThree(this)) {
                    saveScanThree(scans);
                }
            }
        }
    }

    //Save times game played
    private void saveGamesPlayed(int gamesPlayed) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAMES_PLAYED, gamesPlayed);
        editor.apply();
    }

    static public int getSaveGamesPlayed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        return prefs.getInt(GAMES_PLAYED, 0);
    }

    // Save first high score (4x6)
    private void saveScanOne(int numberScans) {
        SharedPreferences prefs = this.getSharedPreferences(APP_ONE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SCAN_ONE, numberScans);
        editor.apply();
    }

    static public int getSaveScanOne(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_ONE, MODE_PRIVATE);
        return prefs.getInt(SCAN_ONE, 0);
    }

    // Save second high score (5x10)
    private void saveScanTwo(int numberScans) {
        SharedPreferences prefs = this.getSharedPreferences(APP_TWO, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SCAN_TWO, numberScans);
        editor.apply();
    }

    static public int getSaveScanTwo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_TWO, MODE_PRIVATE);
        return prefs.getInt(SCAN_TWO, 0);
    }

    // Save third high score (6x15)
    private void saveScanThree(int numberScans) {
        SharedPreferences prefs = this.getSharedPreferences(APP_THREE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SCAN_THREE, numberScans);
        editor.apply();
    }

    static public int getSaveScanThree(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_THREE, MODE_PRIVATE);
        return prefs.getInt(SCAN_THREE, 0);
    }
}

