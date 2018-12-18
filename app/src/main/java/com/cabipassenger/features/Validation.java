package com.cabipassenger.features;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.ShowToast;

/**
 * Created by developer on 4/26/16.
 * used to validate the field in accordance with the type
 */
public class Validation {
    private static Dialog alertmDialog;

    public enum ValidateAction {
        NONE, isValueNULL, isValidPassword, isValidSalutation, isValidFirstname, isValidLastname, isValidCard, isValidExpiry, isValidMail, isValidConfirmPassword, isNullPromoCode, isValidCvv, isNullMonth, isNullYear, isNullCardname
    }

    public static boolean validations(ValidateAction VA, Context con, String stringtovalidate) {
        String message = "";
        boolean result = false;
        switch (VA) {
            case isValueNULL:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + con.getResources().getString(R.string.enter_the_mobile_number);
                else
                    result = true;
                break;
            case isValidPassword:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + con.getResources().getString(R.string.enter_the_password);
                else if (stringtovalidate.length() < 5)
                    message = "" + con.getResources().getString(R.string.password_min_character);
                else if (stringtovalidate.length() > 32)
                    message = "" + con.getResources().getString(R.string.password_max_character);
                else
                    result = true;
                break;
            case isValidSalutation:
                if (TextUtils.isEmpty(stringtovalidate) || stringtovalidate == null)
                    message = "" + con.getResources().getString(R.string.please_select_your_salutation);
                else
                    result = true;
                break;
            case isValidFirstname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + con.getResources().getString(R.string.enter_the_first_name);
                else
                    result = true;
                break;
            case isValidLastname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + con.getResources().getString(R.string.enter_the_last_name);
                else
                    result = true;
                break;
            case isValidCard:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_card_number);
                else if (stringtovalidate.length() < 9 || stringtovalidate.length() > 16)
                    message = "" + NC.getResources().getString(R.string.enter_the_valid_card_number);
                else
                    result = true;
                break;
            case isValidExpiry:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_expiry_date);
                else
                    result = true;
                break;
            case isValidMail:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_email);
                else if (!validdmail(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_valid_email);
                else
                    result = true;
                break;
            case isValidConfirmPassword:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_confirmation_password);
                else
                    result = true;
                break;
            case isNullPromoCode:
                if (TextUtils.isEmpty(stringtovalidate))
                    CToast.ShowToast(con, NC.getResources().getString(R.string.reg_enterprcode));
                    //message = "" + con.getResources().getString(R.string.reg_enterprcode);
                else
                    result = true;
                break;
            case isNullMonth:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.reg_expmonth);
                else
                    result = true;
                break;
            case isNullYear:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.reg_expyear);
                else
                    result = true;
                break;
            case isValidCvv:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_valid_CVV);
                else
                    result = true;
                break;
            case isNullCardname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.reg_entercardname);
                else
                    result = true;
                break;
        }
        if (!message.equals("")) {
            //alert_view(con, "" + con.getResources().getString(R.string.message), "" + message, "" + con.getResources().getString(R.string.ok), "");
           if(message!=null)
            ShowToast.center(con,message);
            else
               ShowToast.center(con,NC.getString(R.string.server_con_error));
        }
        return result;
    }


    public static boolean validdmail(String string) {
        // TODO Auto-generated method stub
        return isValidEmail(string);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
