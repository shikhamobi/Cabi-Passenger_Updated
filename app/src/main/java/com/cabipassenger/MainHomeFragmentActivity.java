package com.cabipassenger;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.features.CToast;
import com.cabipassenger.features.Validation;
import com.cabipassenger.fragments.AboutFrag;
import com.cabipassenger.fragments.AddMoneyFrag;
import com.cabipassenger.fragments.BookTaxiNewFrag;
import com.cabipassenger.fragments.EditFavouriteFrag;
import com.cabipassenger.fragments.FareFrag;
import com.cabipassenger.fragments.FavouriteDriverFrag;
import com.cabipassenger.fragments.FavouriteFrag;
import com.cabipassenger.fragments.InviteFriendsFrag;
import com.cabipassenger.fragments.OnGoingFrag;
import com.cabipassenger.fragments.PaymentOptionFrag;
import com.cabipassenger.fragments.ProfileFrag;
import com.cabipassenger.fragments.SettingsFrag;
import com.cabipassenger.fragments.TripHistory;
import com.cabipassenger.fragments.WalletFrag;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.DriverLocation;
import com.cabipassenger.interfaces.FragPopFront;
import com.cabipassenger.interfaces.NetworkInterface;
import com.cabipassenger.interfaces.splitfareDialog;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.FirebaseService;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.DatePicker_CardExpiry;
import com.cabipassenger.util.Dialog_Common;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.Paymentgetway;
import com.cabipassenger.util.RoundedImageView;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.fabric.sdk.android.Fabric;

import static com.cabipassenger.SplashActivity.fields;
import static com.cabipassenger.SplashActivity.fields_id;
import static com.cabipassenger.SplashActivity.fields_value;

/**
 * this class is used as common activity to use all methods in whole project
 */


public class MainHomeFragmentActivity extends AppCompatActivity implements splitfareDialog, DatePicker_CardExpiry.DateDialogInterface
        , DriverLocation, NetworkInterface {

    public static Dialog gpsAlert;
    private Dialog networkAlert;
    public Toolbar toolbar;
    private Dialog mlogoutDialog;
    private DrawerLayout drawer_layout;
    public ImageView toolbar_logo;
    public TextView toolbar_title;
    public ImageButton left_icon;
    private AboutFrag aboutfrag;
    private TextView menu_profile_name, menu_pno;
    private ImageView menu_profile_img;
    private Dialog mDialog;
    private String requestTripID;
    private ImageButton fav_add;
    private SwitchCompat fav_switch;
    private Dialog alertmDialog;
    private boolean isArab;
    private boolean booktaxi = true;
    private String s;
    public LinearLayout toolbar_titletm;
    public TextView cancel_b;
    public boolean cancelbtn = false;
    public static Context context;
    //siva 05/03/2018
    private String promoCode = "";
    private LinearLayout menu_promo;


    @Override
    public void driverLocationUpdate(final double lat, final double lng) {

        final Fragment ff = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        if (ff instanceof OnGoingFrag) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ((OnGoingFrag) ff).moveCameraToDriverLoc(lat, lng);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public void networkError(boolean isConnected) {
        isConnect(isConnected);
    }


    /**
     * To transfer data from SplitFareDialog fragment to BookTaxiNewFrag fragment via this activity
     */
    @Override
    public void onSplitSuccess(double primary_Percent, double f1, double f2, double f3, double fa1, double fa2, double fa3) {
        BookTaxiNewFrag frag = (BookTaxiNewFrag) getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        frag.onSplitSuccess(primary_Percent, f1, f2, f3, fa1, fa2, fa3);
    }

    /**
     * To transfer data from DatePicker_CardExpiry  dialog fragment to PaymentOptionFrag fragment
     */
    @Override
    public void onSuccess(int month, int year) {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        if (frag.getClass().equals(PaymentOptionFrag.class)) {
            ((PaymentOptionFrag) frag).onSuccess(month, year);
        }
    }

    @Override
    public void failure(String inputText) {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        if (frag.getClass().equals(PaymentOptionFrag.class)) {
            ((PaymentOptionFrag) frag).failure(inputText);
        }
    }


    public enum ValidateAction {
        NONE, isValueNULL, isValidPassword, isValidSalutation, isValidFirstname, isValidLastname, isValidCard, isValidExpiry, isValidMail, isValidConfirmPassword, isNullPromoCode, isValidCvv, isNullMonth, isNullYear, isNullCardname
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment ff = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        if (ff != null) {
            ff.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void setLocale() {
        if (SessionSave.getSession("Lang", MainHomeFragmentActivity.this).equals("")) {
            SessionSave.saveSession("Lang", "en", MainHomeFragmentActivity.this);
            SessionSave.saveSession("Lang_Country", "en_US", MainHomeFragmentActivity.this);
        }
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", MainHomeFragmentActivity.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(SessionSave.getSession("Lang", MainHomeFragmentActivity.this), arry[1]);
        Locale.setDefault(new Locale(SessionSave.getSession("Lang", MainHomeFragmentActivity.this), arry[1]));
        MainHomeFragmentActivity.this.getBaseContext().getResources().updateConfiguration(config, MainHomeFragmentActivity.this.getResources().getDisplayMetrics());
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Locale languageType = new Locale("ar");
//       // super.attachBaseContext(newBase);
//        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setLocale();

        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.firebase_topic_name));

        Fabric.with(this, new Crashlytics());
        if (!SessionSave.getSession("wholekey", MainHomeFragmentActivity.this).trim().equals("")) {
            getAndStoreStringValues(SessionSave.getSession("wholekey", this));
            getAndStoreColorValues(SessionSave.getSession("wholekeyColor", this));
        }
        TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", this);
//        TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", this);
//        TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", this);

        if (SessionSave.getSession("Lang", MainHomeFragmentActivity.this).equals("ar") || SessionSave.getSession("Lang", MainHomeFragmentActivity.this).equals("fa"))
            isArab = true;


        setContentView(R.layout.homepage_drawer);

        GetPassengerUpdate.context = MainHomeFragmentActivity.this;
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar_logo = (ImageView) findViewById(R.id.toolbar_logo);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        cancel_b = (TextView) findViewById(R.id.cancel_b);


        toolbar_titletm = (LinearLayout) findViewById(R.id.toolbar_titletm);
        toolbar_title.setTextSize(20);
        fav_add = (ImageButton) findViewById(R.id.right_icon);
        fav_switch = (SwitchCompat) findViewById(R.id.switch_right_icon);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        LinearLayout invitefriends_layout = (LinearLayout) findViewById(R.id.menu_invite);
        int inviteFriend = 0;

        try {

            inviteFriend = Integer.parseInt(SessionSave.getSession("referral_settings", this));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (inviteFriend == 0)
            invitefriends_layout.setVisibility(View.GONE);
        else
            invitefriends_layout.setVisibility(View.VISIBLE);


        menu_profile_name = (TextView) findViewById(R.id.menu_profile_name);
        menu_pno = (TextView) findViewById(R.id.menu_pno);
        menu_profile_img = (ImageView) findViewById(R.id.menu_profile_img);
        menu_profile_img.setVisibility(View.GONE);
        String f_Firstname = SessionSave.getSession("ProfileName", this);
        f_Firstname = f_Firstname.substring(0, 1).toUpperCase() + f_Firstname.substring(1);
        menu_profile_name.setText(f_Firstname);
        menu_pno.setText(SessionSave.getSession("CountyCode", this) + " " + SessionSave.getSession("Phone", this));
        Picasso.with(this).load(SessionSave.getSession("ProfileImage", this)).into(menu_profile_img);
        left_icon = (ImageButton) findViewById(R.id.left_icon);
        menu_promo = (LinearLayout) findViewById(R.id.menu_promo);

        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp_imagename = String.valueOf(left_icon.getTag());


                if (temp_imagename.equals("menu")) {
//                    if (isArab)
//                        drawer_layout.openDrawer(GravityCompat.END);
//                    else
                    drawer_layout.openDrawer(GravityCompat.START);

                } else if (temp_imagename.equals("backarrow")) {

                    Fragment ff = getSupportFragmentManager().findFragmentById(R.id.mainFrag);

                    onBackPressed();

                }

            }
        });

        TaxiUtil.sContext = this;

        drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // FontHelper.applyFont(getApplicationContext(), drawer_layout);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                FontHelper.applyFont(getApplicationContext(), drawer_layout);
            }
        });
        if (!SessionSave.getSession("trip_id", MainHomeFragmentActivity.this).equals("")) {
            Bundle alert_bundle;
            OnGoingFrag ongoing = new OnGoingFrag();
            GetPassengerUpdate.context = MainHomeFragmentActivity.this;
            alert_bundle = getIntent().getExtras();
            String alert_msg = "";

            if (alert_bundle != null) {

                alert_msg = alert_bundle.getString("alert_message");
            }
            if (alert_msg != null && alert_msg.length() != 0) {

                Bundle bundle = new Bundle();
                bundle.putString("alert_message", alert_msg);
                ongoing.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, ongoing).commit();
        } else {
            try {
                if (getIntent() != null) {
                    s = getIntent().getStringExtra("goto");
                    if (s != null) {
                        if (s.equals("favdriv"))
                            getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new FavouriteDriverFrag()).commit();
                    } else {
                        Bundle alert_bundle;
                        BookTaxiNewFrag booknew = new BookTaxiNewFrag();
                        alert_bundle = getIntent().getExtras();
                        String alert_msg = "";

                        if (alert_bundle != null) {

                            alert_msg = alert_bundle.getString("alert_message");
                        }
                        if (alert_msg != null && alert_msg.length() != 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString("alert_message", alert_msg);
                            booknew.setArguments(bundle);
                        }
                        getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, booknew).commitAllowingStateLoss();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        else
            return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Paymentgetway.paymentstatus) {
            alert_view(MainHomeFragmentActivity.this, "" + "", "" + "Amount paid sucessfully", "" + NC.getResources().getString(R.string.ok), "");
            Paymentgetway.paymentstatus = false;
        } else {
            // do
        }

        gpsalert(this, isGpsEnabled(this));

        TaxiUtil.sContext = this;
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), MainHomeFragmentActivity.this);
//        Colorchange.ChangeColor((ViewGroup) findViewById(R.id.slider_contain), MainHomeFragmentActivity.this);

        Glide.with(MainHomeFragmentActivity.this).load(SessionSave.getSession("image_path", MainHomeFragmentActivity.this) + "fare_focus.png").into((ImageView) findViewById(R.id.menu_fare_image));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkGCM();
            }
        }, 1000);

        FirebaseService.MAIN_ACT = MainHomeFragmentActivity.this;
        final FrameLayout mainFrag = (FrameLayout) findViewById(R.id.mainFrag);
        mainFrag.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {


            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof BookTaxiNewFrag) {
                    toolbar.setVisibility(View.GONE);
                } else
                    toolbar.setVisibility(View.VISIBLE);
            }
        }, 200);


    }

    public void enableSlide() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disableSlide() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * To Check for any pending GCM notification message
     */
    public void checkGCM() {
        String dialogMessage = SessionSave.getSession("GCMnotification", this);

        try {
            if (dialogMessage != null && !dialogMessage.trim().equals("")) {
                show_dialog(dialogMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ss = SessionSave.getSession("GCMnotificationPopup", this);

        try {
            if (ss != null && !ss.trim().equals("")) {
                alert_view(MainHomeFragmentActivity.this, "" + NC.getResources().getString(R.string.message), "" + ss, "" + NC.getResources().getString(R.string.ok), "");
                SessionSave.saveSession("GCMnotificationPopup", "", this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void show_dialog(String mm) {
        final View view = View.inflate(MainHomeFragmentActivity.this, R.layout.splitfare_accept, null);
        mDialog = new Dialog(MainHomeFragmentActivity.this, R.style.dialogwinddow);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        String splitwith = "", splitcharge = "", pickup = "", drop = "", name = "", totalfare = "", image = "";


        try {
            JSONObject jo = new JSONObject(mm);
            name = jo.getString("passenger_name");
            splitcharge = jo.getString("split_fare");
            totalfare = jo.getString("total_fare");
            pickup = jo.getString("pickup_location");
            drop = jo.getString("drop_location");
            image = jo.getString("primary_passenger_profile");
            requestTripID = jo.getString("trip_id");
            SessionSave.saveSession("Pass_Tripid", requestTripID, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FontHelper.applyFont(MainHomeFragmentActivity.this, mDialog.findViewById(R.id.main));

        final TextView split_with = (TextView) mDialog.findViewById(R.id.split_with);
        final TextView split_charged = (TextView) mDialog.findViewById(R.id.split_charged);
        final TextView currentlocTxt = (TextView) mDialog.findViewById(R.id.currentlocTxt);
        final TextView droplocEdt = (TextView) mDialog.findViewById(R.id.droplocEdt);
        final RoundedImageView profile_img = (RoundedImageView) mDialog.findViewById(R.id.profile_img);
        Picasso.with(MainHomeFragmentActivity.this).load(image).into(profile_img);
        split_with.setText(NC.getResources().getString(R.string.z_split_fare_with) + " " + name);
        split_charged.setText(NC.getResources().getString(R.string.z_split_charged) + " "
                + SessionSave.getSession("Currency", MainHomeFragmentActivity.this) + splitcharge);
        currentlocTxt.setText(" " + pickup);
        droplocEdt.setText(drop);
        currentlocTxt.setSelected(true);
        droplocEdt.setSelected(true);
        final TextView OK = (TextView) mDialog.findViewById(R.id.okbtn);
        final TextView Cancel = (TextView) mDialog.findViewById(R.id.cancelbtn);
        OK.setText(NC.getString(R.string.accept));
        Cancel.setText(NC.getString(R.string.cancel));
        OK.setOnClickListener(new View.OnClickListener() {
            private String mobilenumber;

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {
                    //  Toast.makeText(MainHomeFragmentActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    approvReques("A", requestTripID);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                approvReques("D", requestTripID);

            }
        });
        if (!mDialog.isShowing())
            mDialog.show();
    }


    public void small_title(boolean toSmall) {
        if (toSmall)
            toolbar_title.setTextSize(16);
        else
            toolbar_title.setTextSize(20);

    }

    /**
     * this method is used to set the click method for navigation drawer
     */

    public void ClickMethod(View v) {
//        if (!isArab)
//            drawer_layout.closeDrawer(GravityCompat.START);
//        else
        drawer_layout.closeDrawer(GravityCompat.START);

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Intent i;
        toolbarRightIcon(false);
        toolbar.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.book_taxi:
                toolbar_title.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new BookTaxiNewFrag()).addToBackStack(null).commit();
                break;
            case R.id.menu_me:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.menu_profile));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new ProfileFrag()).addToBackStack(null).commit();
                break;
            case R.id.menu_payment:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.payment));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new PaymentOptionFrag(), "PaymentOptionFrag").addToBackStack(null).commit();

                break;
            case R.id.menu_gettaxi:
                cancel_b.setVisibility(View.GONE);

                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);

                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.on_going));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new OnGoingFrag()).addToBackStack(null).commit();

                break;
            case R.id.menu_favourites:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.menu_favourites));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new FavouriteFrag()).addToBackStack(null).commit();

                break;
            case R.id.menu_history:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.mybookings));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new TripHistory()).addToBackStack(null).commit();

                break;
            case R.id.menu_about:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.menu_about_us));
                aboutfrag = new AboutFrag();
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, aboutfrag).addToBackStack(null).commit();
                break;
            case R.id.menu_logout:
                try {
                    logout(MainHomeFragmentActivity.this);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            case R.id.menu_fare:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.menu_fare));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new FareFrag()).addToBackStack(null).commit();

                break;
            case R.id.menu_settings:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.menu_settings));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new SettingsFrag()).addToBackStack(null).commit();
                break;
            case R.id.menu_wallet:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.wallet));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new WalletFrag()).addToBackStack(null).commit();
                break;

            //siva 05/03/2018
            case R.id.menu_promo:
                homePage_title();
                ShowPromoDilaog();
                break;

            case R.id.menu_invite:
                cancel_b.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_titletm.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title.setText(NC.getResources().getString(R.string.invite_friend));
                getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new InviteFriendsFrag()).addToBackStack(null).commit();

            case R.id.menu_support:
                showsupportdialog();
        }
    }

    public void clickMethod(View v) {
        if (aboutfrag != null) {
            aboutfrag.clickMethod(v);
        }

    }

    /**
     * Call addonClick() method in FavouriteFrag while the add button in tool bar clicked
     */

    public void toolbarRightIcon(boolean visible) {
        if (visible) {
            fav_add.setVisibility(View.VISIBLE);

            final Fragment ff = (getSupportFragmentManager().findFragmentById(R.id.mainFrag));
            if (ff instanceof FavouriteFrag) {
                fav_add.setImageResource(R.drawable.plus);
                fav_add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((FavouriteFrag) ff).addonClick();
                    }
                });
            } else if (ff instanceof EditFavouriteFrag) {
                fav_add.setImageResource(R.drawable.edit_favourite_delete);
                fav_add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((EditFavouriteFrag) ff).deleteonClick();
                    }
                });

            } else if (ff instanceof FavouriteDriverFrag) {

                //  fav_add.setLayoutParams(new FrameLayout.LayoutParams(50,25));
                fav_add.setVisibility(View.GONE);
                fav_switch.setVisibility(View.VISIBLE);
                if (SessionSave.getSession(TaxiUtil.isFavDriverOn, this, false))
                    fav_switch.setChecked(true);
                else
                    fav_switch.setChecked(false);

                fav_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    }
                });
                fav_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callApi(!SessionSave.getSession(TaxiUtil.isFavDriverOn, MainHomeFragmentActivity.this, true));
//                        if (!SessionSave.getSession(TaxiUtil.isFavDriverOn, MainHomeFragmentActivity.this, true)) {
//                            //  fav_add.setImageResource(R.drawable.on_btn);
//                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, MainHomeFragmentActivity.this);
//                            ShowToast.center(MainHomeFragmentActivity.this, NC.getString(R.string.fav_driver_off));
//                            //  Toast.makeText(MainHomeFragmentActivity.this, getString(R.string.fav_driver_on), Toast.LENGTH_SHORT).show();
//                        } else {
//                            // fav_add.setImageResource(R.drawable.off_btn);
//                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, MainHomeFragmentActivity.this);
//                            ShowToast.center(MainHomeFragmentActivity.this, NC.getString(R.string.fav_driver_off));
//                            //  Toast.makeText(MainHomeFragmentActivity.this, getString(R.string.fav_driver_off), Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                // fav_add.setImageResource(R.drawable.edit_favourite_delete);
//                fav_add.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        if (!SessionSave.getSession(TaxiUtil.isFavDriverOn, MainHomeFragmentActivity.this, true)) {
//                            fav_add.setImageResource(R.drawable.on_btn);
//                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, MainHomeFragmentActivity.this);
//                        } else {
//                            fav_add.setImageResource(R.drawable.off_btn);
//                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, MainHomeFragmentActivity.this);
//                        }
//                    }
//                });
            } else if (ff instanceof ProfileFrag) {
                fav_add.setImageResource(R.drawable.ic_notification);
                fav_add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        System.out.println("____________PROMO_LIST" + SessionSave.getSession(TaxiUtil.PROMO_LIST, MainHomeFragmentActivity.this));
//                        if (!SessionSave.getSession(TaxiUtil.PROMO_LIST, MainHomeFragmentActivity.this).trim().equals("")) {
//                            Fragment ffs = new PromoList();
//                            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, ffs).commit();
//                        } else {
//                            Toast.makeText(MainHomeFragmentActivity.this, getString(R.string.no_offer_available), Toast.LENGTH_LONG).show();
//                        }
                    }
                });

            }
        } else {
//            fav_add.setLayoutParams(new FrameLayout.LayoutParams(android.R.attr.actionBarSize,android.R.attr.actionBarSize));

            fav_add.setVisibility(View.GONE);
            fav_switch.setVisibility(View.GONE);
        }
    }
//    public void toolbarRightIcon_favDriver(boolean visible) {
//        if (visible) {
//            final Fragment ff = (getSupportFragmentManager().findFragmentById(R.id.mainFrag));
//
//        } else
//            fav_add.setVisibility(View.GONE);
//    }


    public void callApi(boolean isChecked) {
        JSONObject j = new JSONObject();
        try {
            j.put("passenger_id", SessionSave.getSession("Id", MainHomeFragmentActivity.this));

            if (isChecked)
                j.put("value", "1");
            else
                j.put("value", "2");
            j.put("type", 2);


            new SaveSplitType("type=set_split_fare", j);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class SaveSplitType implements APIResult {


        public SaveSplitType(final String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(MainHomeFragmentActivity.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {


            try {
                final JSONObject json = new JSONObject(result);
                if (isSuccess) {
                    Toast.makeText(MainHomeFragmentActivity.this, json.getString("message"),
                            Toast.LENGTH_SHORT).show();
                    if (json.getInt("status") == 1) {
//                        if (typeOfApi == 1) {
//                            SessionSave.saveSession(TaxiUtil.isSplitOn, true, getActivity());
//                            spliton_off.setChecked(true);
//                        } else if (typeOfApi == 2) {
                        SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, MainHomeFragmentActivity.this);
                        fav_add.setImageResource(R.drawable.on_btn);
                        //    favoritedriveron_off.setChecked(true);
//                        } else if (typeOfApi == 3) {
//                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, true, getActivity());
//                            skip_drop_on_off.setChecked(true);
//                        }
                    } else {
//                        if (typeOfApi == 1) {
//                            SessionSave.saveSession(TaxiUtil.isSplitOn, false,MainHomeFragmentActivity.this);
//                            spliton_off.setChecked(false);
//                        } else if (typeOfApi == 2) {
                        SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, MainHomeFragmentActivity.this);
                        fav_add.setImageResource(R.drawable.off_btn);
//                        } else if (typeOfApi == 3) {
//                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, false, MainHomeFragmentActivity.this);
//                            skip_drop_on_off.setChecked(false);
//                        }
//                        SessionSave.saveSession(TaxiUtil.isSplitOn, false, getActivity());
//                        spliton_off.setChecked(false);
                    }

                } else {
                    MainHomeFragmentActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(MainHomeFragmentActivity.this, json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * To notify slider menu about the changes in profile image
     */

    public void profileEdited() {
        menu_profile_name.setText(SessionSave.getSession("ProfileName", this));
        Picasso.with(this).load(SessionSave.getSession("ProfileImage", this)).into(menu_profile_img);

    }

    @Override
    public void onBackPressed() {
        Fragment ff = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
//        toolbar.setVisibility(View.GONE);
        boolean isNeedTriger = false;
        if (ff instanceof PaymentOptionFrag)
            ((PaymentOptionFrag) ff).onBackPressed();
        else {
            if (ff instanceof FavouriteDriverFrag || ff instanceof EditFavouriteFrag || ff instanceof AddMoneyFrag || ff instanceof BookTaxiNewFrag || ff instanceof SettingsFrag) {
                isNeedTriger = true;
            }

            if (ff instanceof BookTaxiNewFrag) {
                // ((BookTaxiNewFrag) ff).trigger_FragPopFront();
                ((BookTaxiNewFrag) ff).onBackPress();


            } else if (ff instanceof OnGoingFrag) {
                // ((BookTaxiNewFrag) ff).trigger_FragPopFront();
//                ((BookTaxiNewFrag) ff).onBackPress();
            }


//        else if (ff instanceof OnGoingFrag
//                || ff instanceof TripHistory
//                || ff instanceof WalletFrag
//                || ff instanceof FavouriteFrag
//                || ff instanceof FareFrag
//                || ff instanceof InviteFriendsFrag
//                || ff instanceof SettingsFrag
//                || ff instanceof AboutFrag
//                || ff instanceof ProfileFrag
//                || ff instanceof FareEstimateFrag
//                ) {
//            homePage();
//        } else if (ff instanceof PaymentOptionFrag) {
//            if (toolbar_title.getText().toString().trim().equals(getString(R.string.add_card)) ||
//                    toolbar_title.getText().toString().trim().equals(getString(R.string.edit_card))) {
//                ((PaymentOptionFrag) ff).back_Clicked();
//            } else
//                homePage();
//        }
            else {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    if (!(ff instanceof BookTaxiNewFrag)) {
                        booktaxi = false;
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commitAllowingStateLoss();
                    }

                }
                if (booktaxi) {
                    super.onBackPressed();
                } else {
                    booktaxi = true;

                }
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    //  getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(1).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                    if (TaxiUtil.close == 1) {
                        TaxiUtil.close = 0;
                    } else {


                        homePage_title();
                        toolbarRightIcon(false);

                        left_icon.setImageResource(R.drawable.menu);
                        left_icon.setTag("menu");

                        enableSlide();
                    }
                }

                try {

                    if (getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof BookTaxiNewFrag) {
                        homePage_title();
                        toolbarRightIcon(false);

                        left_icon.setImageResource(R.drawable.menu);
                        left_icon.setTag("menu");


                        enableSlide();
                    }


                    if (true) {
                        //  isNeedTriger = false;
                        if (getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof FragPopFront)
                            ((FragPopFront) getSupportFragmentManager().findFragmentById(R.id.mainFrag)).trigger_FragPopFront();
//                        else
//                            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commit();
                }


            }
        }
    }

    /**
     * Toolbar changes while loading BookTaxiNewFrag fragment
     */
    public void homePage() {
        homePage_title();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commitAllowingStateLoss();
    }


    public void homePage_title() {
        toolbar.setVisibility(View.GONE);
//        toolbar_title.setVisibility(View.GONE);
////        toolbar_title.setTextSize(26);
////        toolbar_title.setText("tm");
//
//        if (this != null)
//            Glide.with(this).load(SessionSave.getSession("image_path", this) + "headerLogo.png").error(R.drawable.header_logo).into((ImageView) findViewById(R.id.imageee));
//
//        System.out.println("imageURL" + SessionSave.getSession("image_path", this) + "headerLogo.png");
//        toolbar_titletm.setVisibility(View.VISIBLE);
//        toolbar_logo.setVisibility(View.GONE);
//
//        if (cancelbtn)
//            cancel_b.setVisibility(View.VISIBLE);
//        else
//            cancel_b.setVisibility(View.GONE);


    }


    private synchronized void getAndStoreStringValues(String result) {
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            //     System.out.println("nagaSsss___ize"+nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                //  System.out.println("nagaSsss___agg"+node.getTextContent()+"___");
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    // if (element2.getAttribute("name").equals("pressBack"))
                    //  System.out.println("nagaSsss___ize"+ chhh+"___"+element2.getTextContent());
//
                    //  NC.nfields_value.add(element2.getTextContent());
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
            // System.out.println("nagaSsss___izelllll"+ fields_id.size());
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    synchronized void getValueDetail() {
        // System.out.println("NagarrrHIIIIIII");
        Field[] fieldss = R.string.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        //  System.out.println("NagarrrHIIIIIII***" + fieldss.length + "___" + NC.nfields_byName.size());
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                fields.add(fieldss[i].getName());
                fields_value.add(NC.getResources().getString(id));
                fields_id.put(fieldss[i].getName(), id);

            } else {
                //     System.out.println("Imissedthepunchrefree" + fieldss[i].getName());
            }
//        for(int h=0;h<NC.nfields_byName.size();h++){
//            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
//        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
        }

        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            //  System.out.println("NagarrrHIIIIIII&&&" + fields_id.get(h) + "____" + NC.nfields_byName.get(h));
            NC.nfields_byID.put(fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

        //System.out.println("NagarrrBye" + fields.size() + "___" + fields_value.size() + "___" + fields_id.size());
    }


    /**
     * To approve split fare request
     *
     * @param appr          -(A--> Approved  D-->Decliend)
     * @param requestTripID
     */
    private void approvReques(String appr, String requestTripID) {
        JSONObject j = null;
        try {
            j = new JSONObject();
            j.put("friend_id", SessionSave.getSession("Id", MainHomeFragmentActivity.this));
            j.put("approve_status", appr);
            j.put("trip_id", requestTripID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = "type=splitfare_approval";
        new AcceptRequest(url, j);
    }

    public class AcceptRequest implements APIResult {
        public AcceptRequest(final String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(MainHomeFragmentActivity.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            if (mDialog != null)
                mDialog.dismiss();
            SessionSave.saveSession("GCMnotification", "", MainHomeFragmentActivity.this);

            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(MainHomeFragmentActivity.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("trip_id", json.getString("trip_id"), MainHomeFragmentActivity.this);
                        Intent intent = new Intent(MainHomeFragmentActivity.this, GetPassengerUpdate.class);
                        startService(intent);
                        final Intent i = new Intent(MainHomeFragmentActivity.this, MainHomeFragmentActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", "" + json.getString("message"));
                        i.putExtras(extras);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        MainHomeFragmentActivity.this.finish();
                    }

                    if (json.getInt("status") == 2) {
                        alert_view(MainHomeFragmentActivity.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                    if (json.getInt("status") == 3) {
                        alert_view(MainHomeFragmentActivity.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {
                MainHomeFragmentActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(MainHomeFragmentActivity.this, getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    /**
     * set logout dialog box and call logout api
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
            button_success.setText("" + NC.getResources().getString(R.string.menu_logout));
            button_failure.setText("" + NC.getResources().getString(R.string.cancel));
            button_success.setOnClickListener(new View.OnClickListener() {
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
                            new TaxiUtil.Logout("type=passenger_logout", context, j);
                            fbLogout();
                        } else
                            alert_view(context, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.bookedtaxi), "" + NC.getResources().getString(R.string.ok), "");
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
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
     * set toolbar items which can be dynamically changed
     */

    public void setTitle_m(String title) {

        toolbar_logo.setVisibility(View.GONE);
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText(title);
    }


    @Override
    protected void onStop() {
        //To prevent window leakage error close all dialogs before activity stops.
        TaxiUtil.closeDialog(alertmDialog);
        TaxiUtil.closeDialog(mlogoutDialog);
        TaxiUtil.closeDialog(mDialog);
        TaxiUtil.closeDialog(Dialog_Common.mCustomDialog);
        TaxiUtil.closeDialog(networkAlert);
        TaxiUtil.closeDialog(gpsAlert);
        super.onStop();
    }

    /**
     * This is method for logout the user from their facebook login if they logged in using facebook.
     */
    public void fbLogout() {
        LoginManager.getInstance().logOut();
    }

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
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


    /**
     * Handle validation action
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
        }
        if (!message.equals("")) {
            alert_view(con, "" + NC.getResources().getString(R.string.message), "" + message, "" + NC.getResources().getString(R.string.ok), "");
        }
        return result;
    }


    /**
     * @param string email string is passed as parameter
     * @boolean check the valid email id
     */
    public boolean validdmail(String string) {
        // TODO Auto-generated method stub
        return isValidEmail(string);
    }

    /**
     * @param target email string is passed as parameter
     * @boolean check the valid email id
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        /*send onActivityResult data to which fragment requested*/
        fragment.onActivityResult(requestCode, resultCode, data);
    }


//    public void isConnect( boolean isconnect) {
//        try {
//            if (!isconnect) {
//                mContext = this;
//                if (netConnectionDialog == null) {
//                    final View view = View.inflate(mContext, R.layout.netcon_lay, null);
//                    netConnectionDialog = new Dialog(mContext, R.style.dialogwinddow);
//                    netConnectionDialog.setContentView(view);
//                    netConnectionDialog.setCancelable(true);
//                    FontHelper.applyFont(mContext, netConnectionDialog.findViewById(R.id.alert_id));
//                    netConnectionDialog.show();
//                    final TextView title_text = (TextView) netConnectionDialog.findViewById(R.id.title_text);
//                    final TextView message_text = (TextView) netConnectionDialog.findViewById(R.id.message_text);
//                    final Button button_success = (Button) netConnectionDialog.findViewById(R.id.button_success);
//                    final Button button_failure = (Button) netConnectionDialog.findViewById(R.id.button_failure);
//                    title_text.setText("" + mNC.getResources().getString(R.string.message));
//                    message_text.setText("" + mNC.getResources().getString(R.string.check_internet_connection));
//                    button_success.setText("" + mNC.getResources().getString(R.string.try_again));
//                    button_failure.setText("" + mNC.getResources().getString(R.string.cancel));
//                    button_success.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(final View v) {
//                            // TODO Auto-generated method stub
//                            netConnectionDialog.dismiss();
//                            if (!SessionSave.getSession("Id", mContext).equals("")) {
//                                if (NetworkStatus.isOnline(mContext)) {
//                                    Intent intent = new Intent(mContext, mContext.getClass());
//                                    Activity activity = (Activity) mContext;
//                                    activity.finish();
//                                    mContext.startActivity(intent);
//                                } else {
//                                    Toast.makeText(mContext, mNC.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT);
//                                }
//                            }
//                        }
//                    });
//                    button_failure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(final View v) {
//                            // TODO Auto-generated method stub
//
//                            netConnectionDialog.dismiss();
//                            Activity activity = (Activity) mContext;
//                            activity.finish();
//                            final Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_HOME);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//
//                        }
//                    });
//                } else {
//                    if (!netConnectionDialog.isShowing())
//                        netConnectionDialog.show();
//                }
//            } else {
//                try {
//                    netConnectionDialog.dismiss();
//                    if (mContext != null) {
//                        Intent intent = new Intent(mContext, mContext.getClass());
//                        mContext.startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }


    /**
     * this class is used to parse the color values in xml
     */

    private synchronized void getAndStoreColorValues(String result) {
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            //   System.out.println("lislength"+nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                //  System.out.println("nagaSsss___agg"+node.getTextContent()+"___");
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    // if(element2.getAttribute("name").equals("pressBack"))
                    //    System.out.println("nagaSsss___ize_fbn"+ chhh+"___"+element2.getAttribute("name")+"_____"+element2.getTextContent());

                    //  NC.nfields_value.add(element2.getTextContent());
                    CL.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
            //   System.out.println("nagaSsss___izelllllccc"+ CL.fields_id.size());

            getColorValueDetail();
        } catch (Exception e) {
            //  System.out.println("nagaSsss___izelllllssssss"+ CL.fields_id.size());
            e.printStackTrace();
        }

    }

    /**
     * this method is used to get color codes from api
     */

    synchronized void getColorValueDetail() {
        // System.out.println("NagarrrHIIIIIII");
        Field[] fieldss = R.color.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        // System.out.println("NagarrrHIIIIIIIvvvvvv***"+fieldss.length+"___"+CL.nfields_byName.size());

//        for(int i=0;i<CL.nfields_byName.size();i++)
//            System.out.println("nagaSsss___ize_fffgetc"+fieldss.length+"___"+CL.nfields_byName.get(i));
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "color", getPackageName());

            if (CL.nfields_byName.containsKey(fieldss[i].getName())) {
                CL.fields.add(fieldss[i].getName());
                CL.fields_value.add(NC.getResources().getString(id));
                CL.fields_id.put(fieldss[i].getName(), id);
                //  System.out.println("NagarrrHIIIIIIIvvvvvv***___"+fieldss[i].getName()+"___"+NC.getResources().getString(id)+"___"+id);
            } else {
                System.out.println("Clvalue__" + fieldss[i].getName());
            }
//        for(int h=0;h<NC.nfields_byName.size();h++){
//            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
//        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
        }

        for (Map.Entry<String, String> entry : CL.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            // System.out.println("NagarrrHIIIIIII&&&"+CL.fields_id.get(h)+"____"+CL.nfields_byName.get(h));
            CL.nfields_byID.put(CL.fields_id.get(h), CL.nfields_byName.get(h));
            //  System.out.println("NagarrrHIIIIIII&&&___"+CL.nfields_byID.size());
            // do stuff
        }

        // System.out.println("NagarrrBye"+CL.fields.size()+"___"+CL.fields_value.size()+"___"+ CL.fields_id.size());
    }

    public static boolean isNetworkEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public static void gpsalert(final Context mContext, boolean isconnect) {
        if (!isconnect) {
            if (gpsAlert != null && gpsAlert.isShowing())
                gpsAlert.cancel();
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            gpsAlert = new Dialog(mContext, R.style.NewDialog);
            gpsAlert.setContentView(view);
            FontHelper.applyFont(mContext, gpsAlert.findViewById(R.id.alert_id));


            gpsAlert.setCancelable(false);
            if (!gpsAlert.isShowing())
                gpsAlert.show();
            final TextView title_text = (TextView) gpsAlert.findViewById(R.id.title_text);
            final TextView message_text = (TextView) gpsAlert.findViewById(R.id.message_text);
            final Button button_success = (Button) gpsAlert.findViewById(R.id.button_success);
            final Button button_failure = (Button) gpsAlert.findViewById(R.id.button_failure);

            title_text.setText("" + NC.getResources().getString(R.string.location_disable));
            String message = "";
            if (!isNetworkEnabled(mContext)) {
                message = mContext.getString(R.string.location_enable);
                button_failure.setVisibility(View.GONE);
            } else {
                message = mContext.getString(R.string.change_network);
                button_failure.setVisibility(View.VISIBLE);
                button_failure.setText(context.getString(R.string.cancel));
                view.findViewById(R.id.sepView).setVisibility(View.VISIBLE);
            }

            message_text.setText("" + message);
            button_success.setText("" + NC.getResources().getString(R.string.enable));
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    gpsAlert.dismiss();
                    Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(mIntent);
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    gpsAlert.dismiss();
                }
            });
        } else {
            try {

                if (gpsAlert != null && gpsAlert.isShowing())
                    gpsAlert.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    public void isConnect(final boolean isconnect) {


        try {
            if (!isconnect) {
                if (networkAlert != null && networkAlert.isShowing())
                    networkAlert.dismiss();
                final View view = View.inflate(MainHomeFragmentActivity.this, R.layout.netcon_lay, null);
                networkAlert = new Dialog(MainHomeFragmentActivity.this, R.style.dialogwinddow);
                networkAlert.setContentView(view);
                networkAlert.setCancelable(false);
                networkAlert.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(MainHomeFragmentActivity.this, networkAlert.findViewById(R.id.alert_id));
                networkAlert.show();
                final TextView title_text = (TextView) networkAlert.findViewById(R.id.title_text);
                final TextView message_text = (TextView) networkAlert.findViewById(R.id.message_text);
                final Button button_success = (Button) networkAlert.findViewById(R.id.button_success);
                final Button button_failure = (Button) networkAlert.findViewById(R.id.button_failure);
                title_text.setText("" + getResources().getString(R.string.message));
                message_text.setText("" + getResources().getString(R.string.check_internet_connection));
                button_success.setText("" + getResources().getString(R.string.try_again));
                button_failure.setText("" + getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        if (!SessionSave.getSession("Id", MainHomeFragmentActivity.this).equals("")) {

                            //if (NetworkStatus.isOnline(MainHomeFragmentActivity.this)) {

                            Intent intent = new Intent(MainHomeFragmentActivity.this, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            Activity activity = (Activity) TaxiUtil.sContext;
//
//                            System.out.println("tryAgain2");
                            //   ActivityCompat.finishAffinity(MainHomeFragmentActivity.this);
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            startActivity(intent);
                            networkAlert.dismiss();

                            finish();


//                            } else {
//                                System.out.println("tryAgain3");
//                                Toast.makeText(MainHomeFragmentActivity.this, NC.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT);
//                            }
                        } else {

                        }
                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        networkAlert.dismiss();
                        finish();
                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
            } else {
//                try {
//                    networkAlert.dismiss();
//                    if (MainHomeFragmentActivity.this != null) {
//                        Intent intent = new Intent(MainHomeFragmentActivity.this, MainHomeFragmentActivity.this.getClass());
//                        MainHomeFragmentActivity.this.startActivity(intent);
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

    /*siva 05/03/2018 to dispay promo menu based on passenger sign up */
    public void promoVisibility(String promostatus) {
        if (promostatus.equals("0")) {
            menu_promo.setVisibility(View.GONE);
        } else if (promostatus.equals("1")) {
            menu_promo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * this method is used to show the promo code dialog and execute api
     */

    private void ShowPromoDilaog() {

        try {

            final View view = View.inflate(MainHomeFragmentActivity.this, R.layout.promo_view, null);
            mDialog = new Dialog(MainHomeFragmentActivity.this, R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.show();
            Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id), MainHomeFragmentActivity.this);
            FontHelper.applyFont(MainHomeFragmentActivity.this, mDialog.findViewById(R.id.alert_id));
            final TextView titleTxt = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView msgTxt = (TextView) mDialog.findViewById(R.id.message_text);
            msgTxt.setVisibility(View.GONE);
            final EditText promocodeEdt = (EditText) mDialog.findViewById(R.id.forgotmail);
            final Button OK = (Button) mDialog.findViewById(R.id.button_success);
            final Button Cancel = (Button) mDialog.findViewById(R.id.button_failure);
            final ImageView promoinfo = (ImageView) mDialog.findViewById(R.id.promoinfo);
            promoinfo.setVisibility(View.GONE);
            Cancel.setVisibility(View.GONE);
            promocodeEdt.setVisibility(View.VISIBLE);
            OK.setText("" + NC.getResources().getString(R.string.ok));
            titleTxt.setText("" + NC.getResources().getString(R.string.reg_promocode));
            int maxLengthpromoCode = getResources().getInteger(R.integer.promoMaxLength);
            promocodeEdt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            promocodeEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthpromoCode)});
            InputFilter[] editFilters = promocodeEdt.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.AllCaps();
            promocodeEdt.setFilters(newFilters);
            promocodeEdt.setHint("" + NC.getResources().getString(R.string.reg_enterprcode));
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    try {
                        promoCode = promocodeEdt.getText().toString();
                        if (Validation.validations(Validation.ValidateAction.isNullPromoCode, MainHomeFragmentActivity.this, promoCode)) {
                            mDialog.dismiss();
                            JSONObject j = new JSONObject();
                            j.put("passenger_id", SessionSave.getSession("Id", MainHomeFragmentActivity.this));
                            j.put("promo_code", promoCode);
                            String url = "type=voucher_recharge";
                            new CheckPromoCode(url, j);
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mDialog.dismiss();
                }
            });
            promoinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainHomeFragmentActivity.this, "Enter promo code here to earn amount in wallet", Toast.LENGTH_LONG).show();
//                    CToast.ShowToast(MainHomeFragmentActivity.this,"Enter promo code here to earn amount in wallet" );
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    private void showsupportdialog(){
        try{
            View view=View.inflate(MainHomeFragmentActivity.this, R.layout.customer_support, null);
            mDialog = new Dialog(MainHomeFragmentActivity.this, R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.show();
            Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id), MainHomeFragmentActivity.this);
            FontHelper.applyFont(MainHomeFragmentActivity.this, mDialog.findViewById(R.id.alert_id));
            TextView titleTxt = (TextView) mDialog.findViewById(R.id.title_text);
            TextView Email = (TextView) mDialog.findViewById(R.id.tv_support_email);
            TextView call = (TextView) mDialog.findViewById(R.id.tv_support_call);
            TextView whatsapp = (TextView) mDialog.findViewById(R.id.tv_support_whatsapp);
            Email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mailto = "mailto:bob@example.org" +
                            "?cc=" + "alice@example.com" +
                            "&subject=" + Uri.encode("subject") +
                            "&body=" + Uri.encode("bodyy");

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse(mailto));

                    try {
                        startActivity(emailIntent);
                    } catch (ActivityNotFoundException e) {
                        //TODO: Handle case where no email app is available
                    }
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class CheckPromoCode implements APIResult {
        private CheckPromoCode(final String url, JSONObject data) {
            // new APIService_Volley_JSON(CardRegisterAct.this, this, data, false).execute(url);
            new APIService_Retrofit_JSON(MainHomeFragmentActivity.this, this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", MainHomeFragmentActivity.this) + "&" + url).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        CToast.ShowToast(MainHomeFragmentActivity.this, json.getString("message"));
                        toolbar.setVisibility(View.VISIBLE);
                        menu_promo.setVisibility(View.GONE);
                        cancel_b.setVisibility(View.GONE);
                        toolbar_logo.setVisibility(View.GONE);
                        toolbar_titletm.setVisibility(View.GONE);
                        toolbar_title.setVisibility(View.VISIBLE);
                        toolbar_title.setText(NC.getResources().getString(R.string.wallet));
                        getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new WalletFrag()).addToBackStack(null).commit();

//                        alert_view(MainHomeFragmentActivity.this, "Message", "" + json.getString("message"), "" +NC.getResources().getString(R.string.ok), "");
                    } else {
                        //alert_view(getActivity(), "Message", "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    CToast.ShowToast(MainHomeFragmentActivity.this, json.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        promoCode = "";
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    promoCode = "";
                }
            } else {
                promoCode = "";
                runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(MainHomeFragmentActivity.this, getString(R.string.server_con_error));
                    }
                });
//                alert_view(getActivity(), "Message", "" + result, "" + getResources().getString(R.string.ok), "");
            }
        }
    }

}
