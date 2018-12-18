package com.cabipassenger.util;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


/**
 * This class is for get the crash report from the app to registered mail id by using ACRA library.
 *
 * @author developer
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        //Fabric.with(this, new Crashlytics());
//		ACRA.init(this);
        mInstance = this;
        //MobileAds.initialize(this, getString(R.string.add_appid));
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

//	public RequestQueue getRequestQueue()
//	{
//		if (mRequestQueue == null)
//		{
//			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//		}
//		return mRequestQueue;
//	}

    // public ImageLoader getImageLoader() {
    // getRequestQueue();
    // if (mImageLoader == null) {
    // mImageLoader = new ImageLoader(this.mRequestQueue,
    // new LruBitmapCache());
    // }
    // return this.mImageLoader;
    // }

}
