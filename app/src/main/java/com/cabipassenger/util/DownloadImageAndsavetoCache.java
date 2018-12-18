package com.cabipassenger.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * This AsyncTask used to download the image from given URL and Update into image view.
 */


public class DownloadImageAndsavetoCache extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String urldisplay;
    public DownloadImageAndsavetoCache(final ImageView bmImage) {

        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(final String... urls) {

        urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            final InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(final Bitmap result) {

        super.onPostExecute(result);
        if(bmImage!=null)
            bmImage.setImageBitmap(result);
        AppCacheImage.addBitmapToMemoryCache(urldisplay, result);
       // System.out.println("Image... Saving invite");

    }
}