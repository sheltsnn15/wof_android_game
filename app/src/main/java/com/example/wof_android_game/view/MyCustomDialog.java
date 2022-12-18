package com.example.wof_android_game.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.wof_android_game.R;

public class MyCustomDialog {

    public void showDialog(Activity activity, String header, String body) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_help_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(header);
        TextView text2 = (TextView) dialog.findViewById(R.id.text_dialog2);
        text2.setText(body);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog_okay);
        dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}