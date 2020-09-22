package cmpt276.as3.assignment3.UI;

//Used to make the links clickable in the help UI

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import cmpt276.as3.assignment3.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //grabbing the text views and making the links clickable
        TextView aboutAuthor = findViewById(R.id.aboutAuthor);
        aboutAuthor.setMovementMethod(LinkMovementMethod.getInstance());
        TextView links = findViewById(R.id.links);
        links.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
