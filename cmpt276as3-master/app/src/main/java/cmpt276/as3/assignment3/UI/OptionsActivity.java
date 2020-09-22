package cmpt276.as3.assignment3.UI;

//Allows for user to input their game configuration
//being able to manipulate board size and the number of asteroids present,
//also presents current highscores and can erase number of games played

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cmpt276.as3.assignment3.R;
import cmpt276.as3.assignment3.model.GameActivity;
import cmpt276.as3.assignment3.model.OptionsData;

public class OptionsActivity extends AppCompatActivity {
    private OptionsData optionsData;
    private final String optionOne = "4 x 6";
    private final String optionTwo = "5 x 10";
    private final String optionThree = "6 x 15";
    private final static String boardKey = "Board Chosen";
    private final static String APP_PREFS = "AppPrefs";
    private final static String mineKey = "Mine Chosen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        optionsData = OptionsData.getInstance();

        eraseButton();
        createBoardOptions();
        createMineOptions();
        eraseButton();
        highScoreDisplay();
    }

    //Create the Radio group for Buttons to display board options
    private void createBoardOptions() {
        final RadioGroup group = findViewById(R.id.boardOptions);
        String[] boardSizes = getResources().getStringArray(R.array.board_options);

        for(int i = 0; i < boardSizes.length; i++) {
            final String chosenBoard = boardSizes[i];

            RadioButton button = new RadioButton(this);
            button.setText(chosenBoard);
            button.setTextColor(Color.WHITE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chosenBoard.equals(optionOne)) {
                        optionsData.setRow(4);
                        optionsData.setColumn(6);
                        saveBoardOption(chosenBoard);
                    }else if(chosenBoard.equals(optionTwo)) {
                        optionsData.setRow(5);
                        optionsData.setColumn(10);
                        saveBoardOption(chosenBoard);
                    }else if(chosenBoard.equals(optionThree)) {
                        optionsData.setRow(6);
                        optionsData.setColumn(15);
                        saveBoardOption(chosenBoard);
                    }
                }
            });
            group.addView(button);

            if(chosenBoard.equals(getSaveBoardOption(this))) {
                button.setChecked(true);
            }
        }
    }

    // Create options for Mines (Asteroids) for grouping inside of Radio group
    private void createMineOptions() {
        RadioGroup group = findViewById(R.id.mineOptions);

        int[] mineOptions = getResources().getIntArray(R.array.mine_options);

        for(int i = 0; i < mineOptions.length; i++) {
            final int chosenNum = mineOptions[i];

            RadioButton button = new RadioButton(this);
            button.setText(chosenNum + " Asteroids");
            button.setTextColor(Color.WHITE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionsData.setMines(chosenNum);
                    saveMineOption(chosenNum);
                }
            });
            group.addView(button);

            if(chosenNum == getSaveMineOption(this)) {
                button.setChecked(true);
            }
        }
    }

    // Set up erase button to clear Times Played for game
    private void eraseButton() {

        Button button = (Button) findViewById(R.id.eraseButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView highScoreOne = (TextView) findViewById(R.id.highScoresOne);
                TextView highScoreTwo = (TextView) findViewById(R.id.highScoresTwo);
                TextView highScoreThree = (TextView) findViewById(R.id.highScoresThree);

                optionsData.setEraseButton(true);
                highScoreOne.setText("4 x 6 = 0");
                highScoreTwo.setText("5 x 10 = 0");
                highScoreThree.setText("6 x 15 = 0");
            }
        });
    }

    // Display High Score buttons as results from GameActivity
    private void highScoreDisplay() {
        TextView highScoreOne = (TextView) findViewById(R.id.highScoresOne);
        TextView highScoreTwo = (TextView) findViewById(R.id.highScoresTwo);
        TextView highScoreThree = (TextView) findViewById(R.id.highScoresThree);

        highScoreOne.setText("4 x 6 = 0");
        highScoreTwo.setText("5 x 10 = 0");
        highScoreThree.setText("6 x 15 = 0");

        if(GameActivity.getSaveScanOne(this) != 0 || GameActivity.getSaveScanTwo(this) != 0
           || GameActivity.getSaveScanThree(this) != 0){

            highScoreOne.setText("4 x 6 = " + GameActivity.getSaveScanOne(this));
            highScoreTwo.setText("5 x 10 = " + GameActivity.getSaveScanTwo(this));
            highScoreThree.setText("6 x 15 = " + GameActivity.getSaveScanThree(this));
        }
    }

    //Save board option for default
    private void saveBoardOption(String chosenBoard) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(boardKey, chosenBoard);
        editor.apply();
    }

    static public String getSaveBoardOption(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        String default_value = context.getResources().getString(R.string.default_row_and_columns);
        return prefs.getString(boardKey, default_value);
    }

    // Save mine (asteroids) option for default
    private void saveMineOption(int chosenMine) {
        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(mineKey, chosenMine);
        editor.apply();
    }

    static public int getSaveMineOption(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        int default_value = context.getResources().getInteger(R.integer.default_mine);
        return prefs.getInt(mineKey, default_value);
    }
}