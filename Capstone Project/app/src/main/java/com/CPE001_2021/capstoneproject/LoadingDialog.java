package com.CPE001_2021.capstoneproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;
    private String LoadingText;
    LoadingDialog(Activity myActivity,String loadingText){
        activity = myActivity;
        LoadingText = loadingText;
    }

    void startLoadingDialog(){
        TextView loadingText = (TextView) activity.findViewById(R.id.loadingText);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
