package com.cabipassenger.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.interfaces.DialogInterface;

/**
 * this class is used to set common alert dialog
 */
public class Dialog_Common {

    public static Dialog mCustomDialog;

    /**
     * This method used to call logout API.
     */
    public static void setmCustomDialog(final Context context, final DialogInterface dialogInterface,
                                 String title, String message, String ok, String cancel, final String resultCode) {
        try {
            final View view = View.inflate(context, R.layout.netcon_lay, null);
            mCustomDialog = new Dialog(context, R.style.dialogwinddow);
            mCustomDialog.setContentView(view);
            mCustomDialog.setCancelable(false);
            mCustomDialog.show();
            FontHelper.applyFont(context, mCustomDialog.findViewById(R.id.alert_id));
            final TextView title_text = (TextView) mCustomDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mCustomDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mCustomDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mCustomDialog.findViewById(R.id.button_failure);
//            title_text.setText("" + context.getResources().getString(R.string.message));
//            message_text.setText("" + context.getResources().getString(R.string.confirmlogout));
//            button_success.setText("" + context.getResources().getString(R.string.yes));
//            button_failure.setText("" + context.getResources().getString(R.string.no));

            title_text.setText("" + title);
            message_text.setText("" + message);
            button_success.setText("" + ok);
            button_failure.setText("" + cancel);


            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    dialogInterface.onSuccess(v,mCustomDialog,resultCode);
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    dialogInterface.onFailure(v,mCustomDialog,resultCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void setmCustomDialog(final Context context, final DialogInterface dialogInterface,
                                        String title, String message, String ok, String cancel){
        setmCustomDialog( context,dialogInterface,
         title,  message,  ok,  cancel,"0");
    }
}