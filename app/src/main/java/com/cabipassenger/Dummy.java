package com.cabipassenger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cabipassenger.util.FontHelper;

/**
 * Created by developer on 1/7/17.
 */

public class Dummy extends AppCompatActivity {
    private Dialog errorDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlay);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isOnline()) {
        }else
            errorInSplash("heeee");
    }

    public boolean isOnline() {
        System.out.println("onstartOnline");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            } else {
                return false;
                // not connected to the internet
            }
        }
        return false;
    }
    public void errorInSplash(String message) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();
                System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(Dummy.this, R.layout.netcon_lay, null);
                errorDialog = new Dialog(Dummy.this, R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(Dummy.this, errorDialog.findViewById(R.id.alert_id));
                errorDialog.show();
                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
                title_text.setText("" + getResources().getString(R.string.message));
                message_text.setText("" + message);
                button_success.setText("" + getResources().getString(R.string.try_again));
                button_failure.setText("" + getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        errorDialog.dismiss();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub


                        Activity activity = (Activity) Dummy.this;

                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        errorDialog.dismiss();

                    }
                });
            } else {
                try {
                    errorDialog.dismiss();
                    if (Dummy.this != null) {
                        Intent intent = new Intent(Dummy.this, Dummy.this.getClass());
                        Dummy.this.startActivity(intent);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }}