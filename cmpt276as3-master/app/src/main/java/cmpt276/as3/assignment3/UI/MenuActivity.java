package cmpt276.as3.assignment3.UI;

//The main navigation screen, users can explore the help menu, options menu, and gameplay menu

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.as3.assignment3.R;
import cmpt276.as3.assignment3.model.GameActivity;
import cmpt276.as3.assignment3.model.OptionsData;

public class MenuActivity extends AppCompatActivity {

    private OptionsData optionsData;
    private final String optionOne = "4 x 6";
    private final String optionTwo = "5 x 10";
    private final String optionThree = "6 x 15";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        optionsData = OptionsData.getInstance();

        refreshScreen();
        selectGame();
        selectOptions();
        selectHelp();
    }

    private void selectGame() {
        Button selectGame = (Button) findViewById(R.id.playButton);
        selectGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(gameIntent);
            }
        });
    }

    private void selectOptions() {
        Button selectOptions = (Button) findViewById(R.id.optionsButton);
        selectOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent optionsIntent = new Intent(MenuActivity.this, OptionsActivity.class);
                startActivity(optionsIntent);
            }
        });
    }

    private void selectHelp() {
        Button selectHelp = (Button) findViewById(R.id.helpButton);
        selectHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent helpIntent = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(helpIntent);
            }
        });
    }

    //Make sure values are selecting into class, in addition default values if not going to options screen
    private void refreshScreen() {
        optionsData.setMines(OptionsActivity.getSaveMineOption(this));
        String givenBoardSize = OptionsActivity.getSaveBoardOption(this);
        if(givenBoardSize.equals(optionOne)) {
            optionsData.setRow(4);
            optionsData.setColumn(6);
        }else if(givenBoardSize.equals(optionTwo)) {
            optionsData.setRow(5);
            optionsData.setColumn(10);
        }else if(givenBoardSize.equals(optionThree)) {
            optionsData.setRow(6);
            optionsData.setColumn(15);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshScreen();
    }
}
