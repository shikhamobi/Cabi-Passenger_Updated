package com.cabipassenger;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.PageIndicator;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 *this class ask the user to sign in or sign up
 */

public class UserHomeAct extends MainActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout SigninLay;
    private LinearLayout RegisterLay;
    private LinearLayout SlideTxt;
    private TextView DoneTxt;
    private TextView Title;
    public static Activity mAct;
    int[] tutorial;
    private ViewPager _pager;
    private PagerAdapter adapter;
    private PageIndicator mIndicator;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        setLocale();
        return R.layout.home_defaultlay;
    }

    @Override
    public void onStart() {

        super.onStart();
        /*
         * Connect the client. Don't re-start any requests here; instead, wait for onResume()
       */
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
            //  setfetch_address();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     *this method is used to apply font for all fields
     */

    @Override
    public void priorChanges() {
        FontHelper.applyFont(this, findViewById(R.id.userhome_contains));
        super.priorChanges();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void Initialize() {

        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.firebase_topic_name));

        // TODO Auto-generated method stub
       /* Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)),UserHomeAct.this);*/
        TaxiUtil.mActivitylist.add(this);
//        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
//                .findViewById(android.R.id.content)).getChildAt(0)),UserHomeAct.this);
        mAct = this;
        if (!TaxiUtil.isTutorial) {
            SlideTxt = (LinearLayout) findViewById(R.id.leftIconTxt);
            DoneTxt = (TextView) findViewById(R.id.rightIconTxt);
            DoneTxt.setVisibility(View.GONE);
            SlideTxt.setVisibility(View.INVISIBLE);
            Title = (TextView) findViewById(R.id.header_titleTxt);
            Title.setText(NC.getResources().getString(R.string.taxiname));
        }
//        } else {
//            tutorial = new int[]{R.drawable.demo1, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5, R.drawable.demo6};
//            //				{ R.drawable.layer_1, R.drawable.layer_2,R.drawable.layer_3,R.drawable.layer_4,R.drawable.layer_5,R.drawable.layer_6,R.drawable.layer_7,R.drawable.layer_8};
//            _pager = (ViewPager) findViewById(R.id.pager);
//            adapter = new DemoPagerAdapter(UserHomeAct.this, tutorial, false);
//            _pager.setAdapter(adapter);
//            mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
//            mIndicator.setViewPager(_pager);
//        }
        SigninLay = (LinearLayout) findViewById(R.id.signinlay);
        RegisterLay = (LinearLayout) findViewById(R.id.signuplay);

      //  Glide.with(this).load(SessionSave.getSession("image_path",this)+"signInLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.body_iv));
        SigninLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(UserHomeAct.this, LoginAct.class);
                startActivity(i);
                finish();
            }
        });
        RegisterLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                SessionSave.saveSession("IsOTPSend", "", UserHomeAct.this);
                SessionSave.saveSession("f_name", "", UserHomeAct.this);
                SessionSave.saveSession("l_name", "", UserHomeAct.this);
                SessionSave.saveSession("e_mail", "", UserHomeAct.this);
                SessionSave.saveSession("m_no", "", UserHomeAct.this);
                SessionSave.saveSession("p_wd", "", UserHomeAct.this);
                SessionSave.saveSession("cp_wd", "", UserHomeAct.this);
                SessionSave.saveSession("ref_txt", "", UserHomeAct.this);

                Intent i = new Intent(UserHomeAct.this, RegisterNewAct.class);
                startActivity(i);
                //finish();
            }
        });

//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        if (imm.isAcceptingText()) {
//            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//            im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//        }



        if (servicesConnected()) {
            buildGoogleApiClient();
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double P_latitude = mLastLocation.getLatitude();
                double P_longitude = mLastLocation.getLongitude();
                SessionSave.saveSession("PLAT", "" + P_latitude, UserHomeAct.this);
                SessionSave.saveSession("PLNG", P_longitude + "", UserHomeAct.this);
            }
        }
        final LinearLayout bottomlay = (LinearLayout) findViewById(R.id.userhome_contain);
        bottomlay.post(new Runnable() {
            @Override
            public void run() {
                Animation zoomout = AnimationUtils.loadAnimation(UserHomeAct.this, R.anim.zoom_out);
                bottomlay.startAnimation(zoomout);
            }
        });
    }
/**
 *
 */

    private boolean servicesConnected() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (SessionSave.getSession("Register", UserHomeAct.this).equals("1") || SessionSave.getSession("Register", UserHomeAct.this).equals("2")) {
            SessionSave.saveSession("Register", "", UserHomeAct.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        TaxiUtil.mActivitylist.remove(this);
    }

    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double P_latitude = mLastLocation.getLatitude();
                double P_longitude = mLastLocation.getLongitude();
                SessionSave.saveSession("PLAT", "" + P_latitude, UserHomeAct.this);
                SessionSave.saveSession("PLNG", P_longitude + "", UserHomeAct.this);
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
