package cmpt276.as3.assignment3.UI;

//The welcome screen, which proceeds to the main menu after 4 seconds
//or can be skipped using the button on the UI

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cmpt276.as3.assignment3.R;

public class MainActivity extends AppCompatActivity {

    public static final int ANIMATION_TIME = 4000;
    public Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       welcomeDisplay();
        skipButton();
        rocketAnimation();
        spacemanAnimation();
    }

    private void menuIntent() {
        Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(menuIntent);
    }

    // Used to launch the menu screen -> https://www.geeksforgeeks.org/android-creating-a-splash-screen/
    private void welcomeDisplay() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuIntent();
                MainActivity.this.finish();
            }
        }, ANIMATION_TIME);
    }

    //used to tell handler from welcome display to stop the queue,
    //to prevent app from launching menu screen after being closed
    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.this.finish();
        handler.removeCallbacksAndMessages(null);
    }

    // For Handler references -> https://stackoverflow.com/questions/22718951/stop-handler-postdelayed
    // https://stackoverflow.com/questions/5883635/how-to-remove-all-callbacks-from-a-handler
    private void skipButton() {
        Button skipBtn = (Button) findViewById(R.id.skipButton);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuIntent();
                MainActivity.this.finish();
                handler.removeCallbacksAndMessages(null);
            }
        });
    }

    private void rocketAnimation() {
        ImageView iv = (ImageView) findViewById(R.id.rocketImageView);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    private void spacemanAnimation() {
        ImageView iv = (ImageView)  findViewById(R.id.astronautImageView) ;

        ObjectAnimator rotate = ObjectAnimator.ofFloat(iv, "rotation", 180f, 0f);
        rotate.setRepeatCount(100);
        rotate.setDuration(4200);
        rotate.start();
    }
}

