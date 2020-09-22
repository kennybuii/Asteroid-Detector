package cmpt276.as3.assignment3.UI;

//Post-victory screen that appears when user has finished their game

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmpt276.as3.assignment3.R;

public class CongratulationsFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.congratulations_layout, null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("GAME OVER")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}