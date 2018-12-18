package com.cabipassenger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.NetworkStatus;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.cabipassenger.util.TaxiUtil.Logout;
import com.bumptech.glide.Glide;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * <p>
 * this class is used to put some common methods<br>
 * which can access any part of the code
 * <p>
 * </p>
 *
 * @author developer
 */
public abstract class MainActivity extends FragmentActivity {
    public static Dialog mshowDialog;
    public static Dialog mgpsDialog;
    public static Dialog mlogoutDialog;
    public static Dialog sDialog;
    boolean i = false;
    static Calendar calendar;
    private static String returnString;
    public Bundle BsavedInstanceState;
    public ValidateAction validateAction = ValidateAction.NONE;
    private final static String CONTENT_FONT_NAME = "DroidSans.ttf";
    private static Typeface ContenttypeFace;
    public static String APP_VERSION;

    public enum ValidateAction {
        NONE, isValueNULL, isValidPassword, isValidSalutation, isValidFirstname, isValidLastname, isValidCard, isValidExpiry, isValidMail, isValidConfirmPassword, isNullPromoCode, isValidCvv, isNullMonth, isNullYear, isNullCardname, isValidphone
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BsavedInstanceState = savedInstanceState;
        MainHomeFragmentActivity.context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.firebase_topic_name));

        // if (this.getClass().getCanonicalName().equals("com.cabi.LoginAct") || this.getClass().getCanonicalName().equals("com.cabi.RegisterNewAct"))
        if (!SessionSave.getSession("facebook_key", MainActivity.this).equals(""))
            FacebookSdk.setApplicationId(SessionSave.getSession("facebook_key", MainActivity.this));
        else
            FacebookSdk.setApplicationId("1659604664078736");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Fabric.with(this, new Crashlytics());

        if (APP_VERSION == null) {
            PackageInfo info = null;
            PackageManager manager = getPackageManager();
            try {
                info = manager.getPackageInfo(getPackageName(), 0);
                APP_VERSION = info.versionName;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        setView();

    }


    /**
     * This is method for set the layout for the child activity
     */
    private void setView() {
        // TODO Auto-generated method stub
        int view = setLayout();
        if (view != 0) {
            setContentView(view);

//            !this.getClass().getCanonicalName().equals("com.cabi.BookTaxiAct")
            //!this.getClass().getCanonicalName().equals("com.cabi.OngoingTrip")

            priorChanges();
            Initialize();

            try {
                if (mshowDialog.isShowing())
                    mshowDialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        // else
        // Initialize();

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View v = super.onCreateView(parent, name, context, attrs);
        return v;
    }

    public abstract int setLayout();

    public void priorChanges() {

    }

    ;

    public abstract void Initialize();

    public Dialog alertmDialog;

    public Typeface setcontentTypeface() {

        if (ContenttypeFace == null)
            ContenttypeFace = Typeface.createFromAsset(this.getAssets(), CONTENT_FONT_NAME);
        return ContenttypeFace;
    }

    /**
     * Custom alert dialog used in entire project.can call from anywhere with the following
     *
     * @param title       set the title for alert dialog
     * @param message     set the message for alert dialog
     * @param success_txt set the success text in success button
     * @param failure_txt set the failure text in failure button
     */
    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);


            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
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

    /**
     * method which displays alert dialog when network connection is in offline
     *
     * @param isconnect a boolean variable to check the status
     */
    public static void isConnect(final Context mContext, final boolean isconnect) {
        try {

            if (!isconnect) {
                if (sDialog != null && sDialog.isShowing())
                    sDialog.dismiss();
                final View view = View.inflate(mContext, R.layout.netcon_lay, null);
                sDialog = new Dialog(mContext, R.style.dialogwinddow);
                sDialog.setContentView(view);
                sDialog.setCancelable(false);
                sDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(mContext, sDialog.findViewById(R.id.alert_id));
                sDialog.show();
                final TextView title_text = (TextView) sDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) sDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) sDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) sDialog.findViewById(R.id.button_failure);
                title_text.setText("" + mContext.getString(R.string.message));
                message_text.setText("" + mContext.getString(R.string.check_internet_connection));
                button_success.setText("" +  mContext.getString(R.string.try_again));
                button_failure.setText("" +  mContext.getString(R.string.cancel));
                button_success.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        if (!SessionSave.getSession("Id", mContext).equals("")) {

                            if (NetworkStatus.isOnline(mContext)) {

                                Intent intent = new Intent(mContext, mContext.getClass());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Activity activity = (Activity) mContext;
                                mContext.startActivity(intent);

                                activity.finish();
                                sDialog.dismiss();

                            } else {

                                Toast.makeText(mContext, NC.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT);
                            }
                        } else {

                        }
                    }
                });
                button_failure.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        sDialog.dismiss();
                        Activity activity = (Activity) mContext;
                        activity.finish();
                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);

                    }
                });
            } else {
//                try {
//                    sDialog.dismiss();
//                    if (mContext != null) {
//                        Intent intent = new Intent(mContext, mContext.getClass());
//                        mContext.startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void cancelLoading() {
        if (mshowDialog != null)
            if (mshowDialog.isShowing())
                mshowDialog.dismiss();
    }

    /**
     * method which shows alert dialog to turn on gps when gps is in offline mode
     */
    public static void gpsalert(final Context mContext, boolean isconnect) {
        if (!isconnect) {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            mgpsDialog = new Dialog(mContext, R.style.NewDialog);
            mgpsDialog.setContentView(view);
            FontHelper.applyFont(mContext, mgpsDialog.findViewById(R.id.alert_id));


            mgpsDialog.setCancelable(false);
            if (!mgpsDialog.isShowing())
                mgpsDialog.show();
            final TextView title_text = (TextView) mgpsDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mgpsDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mgpsDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mgpsDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText("" + NC.getResources().getString(R.string.location_disable));
            message_text.setText("" + NC.getResources().getString(R.string.location_enable));
            button_success.setText("" + NC.getResources().getString(R.string.enable));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(mIntent);
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mgpsDialog.dismiss();
                }
            });
        } else {
            try {

                if (mgpsDialog != null && mgpsDialog.isShowing())
                    mgpsDialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * This is method for check the mail is valid by the use of regex class.
     */
    public boolean validdmail(String string) {
        // TODO Auto-generated method stub
//        boolean isValid = false;
//        String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
//        Pattern pattern = Pattern.compile(expression);
//        Matcher matcher = pattern.matcher(string);
//        if (matcher.matches()) {
//            isValid = true;
//        }
        return isValidEmail(string);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * This is method for show the toast
     */
    public void ShowToast(Context context, String s) {
        Toast toast = Toast.makeText(context, "" + s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * This method used to call logout API.
     */
    public void logout(final Context context) {
        try {
            final View view = View.inflate(context, R.layout.netcon_lay, null);
            mlogoutDialog = new Dialog(context, R.style.dialogwinddow);
            mlogoutDialog.setContentView(view);
            mlogoutDialog.setCancelable(false);
            mlogoutDialog.show();
            FontHelper.applyFont(context, mlogoutDialog.findViewById(R.id.alert_id));
            final TextView title_text = (TextView) mlogoutDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mlogoutDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mlogoutDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mlogoutDialog.findViewById(R.id.button_failure);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.confirmlogout));
            button_success.setText("" + NC.getResources().getString(R.string.yes));
            button_failure.setText("" + NC.getResources().getString(R.string.no));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mlogoutDialog.dismiss();
                        // TODO Auto-generated method stub
                        JSONObject j = new JSONObject();
                        j.put("id", SessionSave.getSession("Id", context));
                        if (SessionSave.getSession("Logout", context).equals("")) {
                            new Logout("type=passenger_logout", context, j);
                            fbLogout();
                        } else
                            alert_view(context, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.bookedtaxi), "" + NC.getResources().getString(R.string.ok), "");
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    mlogoutDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is method for logout the user from their facebook login if they logged in using facebook.
     */
    public void fbLogout() {
        LoginManager.getInstance().logOut();
    }

    /**
     * This is method for set the language configuration.
     */
    public void setLocale() {
        if (SessionSave.getSession("Lang", MainActivity.this).equals("")) {
            SessionSave.saveSession("Lang", "en", MainActivity.this);

        }
        if (SessionSave.getSession("Lang_Country", MainActivity.this).equals("")) {
            SessionSave.saveSession("Lang_Country", "en_US", MainActivity.this);
        }


//        String languageToLoad  = "fa"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(new Locale("fa"));
//        Configuration config = new Configuration();
//        config.locale = locale;
//        MainActivity.this.getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());


        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", MainActivity.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(SessionSave.getSession("Lang", MainActivity.this), arry[1]);
        Locale.setDefault(new Locale(SessionSave.getSession("Lang", MainActivity.this), arry[1]));
        MainActivity.this.getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }

    /**
     * This is method for check the Internet connection
     */
    public boolean isOnline() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo element : info) {
                    if (element.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This is method to format date(dd-MM-yyyy hh:mm:ss a) eg:(21-MAY-2015 11:15:34 AM)
     */
    public static String date_conversion(String date_time) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            calendar = Calendar.getInstance(Locale.UK);
            java.util.Date yourDate = parser.parse(date_time);
            calendar.setTime(yourDate);
            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + timeformat);
        } catch (Exception e) {
            e.printStackTrace();
            calendar = Calendar.getInstance(Locale.UK);
            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + timeformat);
        }
        return returnString;
    }

    /**
     * This is method to format month value (MMM) eg:(MAY)
     */
    public static String monthFullName(int monthnumber) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, monthnumber - 1);
        SimpleDateFormat sf = new SimpleDateFormat("MMM");
        sf.setCalendar(cal);
        String monthName = sf.format(cal.getTime());
        return monthName;
    }

    /**
     * This is method to format month value (dd) eg:(21)
     */
    public static String daytwodigit(int daynumber) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, daynumber - 1);
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        sf.setCalendar(cal);
        String day2digit = sf.format(cal.getTime());
        return day2digit;
    }

    /**
     * This is method to format month value (hh:mm:ss a) eg:(11:15:34 AM)
     */
    private static String timetwodigit(int year, int month, int day, int hr, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hr, min, sec);
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss a");
        sf.setCalendar(cal);
        String time2digit = String.format(Locale.UK, sf.format(cal.getTime()));
        return time2digit.toUpperCase();
    }

    /**
     * This is method to validate the field like Mail,Password,Name,Salutation etc and show the appropriate alert message.
     */
    public boolean validations(ValidateAction VA, Context con, String stringtovalidate) {
        String message = "";
        boolean result = false;
        switch (VA) {
            case isValueNULL:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_mobile_number);
                else
                    result = true;
                break;
            case isValidPassword:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_password);
                else if (stringtovalidate.length() < 5)
                    message = "" + NC.getResources().getString(R.string.password_min_character);
                else if (stringtovalidate.length() > 32)
                    message = "" + NC.getResources().getString(R.string.password_max_character);
                else
                    result = true;
                break;
            case isValidSalutation:
                if (TextUtils.isEmpty(stringtovalidate) || stringtovalidate == null)
                    message = "" + NC.getResources().getString(R.string.please_select_your_salutation);
                else
                    result = true;
                break;
            case isValidFirstname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_first_name);
                else
                    result = true;
                break;
            case isValidLastname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_last_name);
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
                    message = "" + NC.getResources().getString(R.string.reg_enterprcode);
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
            case isValidphone:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_confirmation_phoneno);
                else if (stringtovalidate.length() < 9 || stringtovalidate.length() > 10)
                    message = "" + NC.getResources().getString(R.string.enter_the_confirmation_phoneno);
                else
                    result = true;
                break;


        }
        if (!message.equals("")) {
            //  alert_view(con, "" + NC.getResources().getString(R.string.message), "" + message, "" + getResources().getString(R.string.ok), "");
            ShowToast(con, message);
        }
        return result;
    }

    /**
     * Get the real path of selected image from camera or gallery.
     */
    public String getRealPathFromURI(final String contentURI) {
        final Uri contentUri = Uri.parse(contentURI);
        final Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else {
            cursor.moveToFirst();
            final int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /**
     * Get the name of selected image from camera or gallery.
     */
    public String getFilename() {
        final File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists())
            file.mkdirs();
        final String uriSting = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        return uriSting;
    }

    /**
     * This is method for show progress bar
     */
    public void showLoading(Context context) {
        try {
            if (context != null) {
                if (mshowDialog != null)
                    mshowDialog.dismiss();
                View view = View.inflate(context, R.layout.progress_bar, null);
                mshowDialog = new Dialog(context, R.style.dialogwinddow);
                mshowDialog.setContentView(view);
                mshowDialog.setCancelable(false);
                mshowDialog.show();

                ImageView iv = (ImageView) mshowDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(MainActivity.this)
                        .load(R.raw.loading_anim)
                        .into(iv);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert the selected image file into BITMAP file from camera or gallery.
     */
    public static Bitmap decodeImageFile(final File f) {
        try {
            final BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            final int REQUIRED_SIZE = 100;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (final FileNotFoundException e) {
        }
        return null;
    }

    /**
     * method which detect whether gps is enabled or disabled
     */
    public boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        else
            return false;
    }

    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.country_code);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = "+" + g[0];
                break;
            }
        }
        return CountryZipCode;
    }


    @Override
    protected void onStop() {
        TaxiUtil.closeDialog(mgpsDialog);
        TaxiUtil.closeDialog(mlogoutDialog);
        TaxiUtil.closeDialog(mshowDialog);
        TaxiUtil.closeDialog(alertmDialog);
        TaxiUtil.closeDialog(sDialog);
        super.onStop();
    }
}