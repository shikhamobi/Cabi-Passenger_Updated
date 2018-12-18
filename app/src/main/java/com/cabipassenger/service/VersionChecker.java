package com.cabipassenger.service;

import android.os.AsyncTask;

import com.cabipassenger.BuildConfig;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by developer on 27/6/17.
 */


public class VersionChecker extends AsyncTask<String, String, String> {

    String newVersion;

    @Override
    protected String doInBackground(String... params) {

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" +  BuildConfig.APPLICATION_ID+ "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newVersion;
    }
}