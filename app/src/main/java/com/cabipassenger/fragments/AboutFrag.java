package com.cabipassenger.fragments;

/**
 * This class contains the fragment for AboutUs page
 *
 * @author developer
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.TermsAndConditions;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONObject;

public class AboutFrag extends Fragment {
    // Class members declarations.
    private LinearLayout CancelBtn;
    private TextView DoneBtn;
    private TextView HeadTitle;
    private TextView CancelTxt;
    private TextView SubTitleTxt;
    private TextView fblinkTxt;
    private TextView twlinkTxt;
    //    private MenuSlider menu1;
//    private SlidingMenu menu;
    public static Activity mAbout;
    private final boolean t_status = false;
    private TextView back_text;
    private Dialog mshowDialog;
    private TextView version_code;
    private PackageManager manager;

    // Set the layout to activity.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.aboutactlay, container, false);
        priorChanges(v);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());
        return v;
    }

    /**
     * method to render view and intialize necessary view for About us page
     *
     * @param v -->fragment View
     */

    public void priorChanges(View v) {

        mAbout = getActivity();
        TaxiUtil.sContext = getActivity();
        TaxiUtil.mActivitylist.add(getActivity());
        // TODO Auto-generated method stub

        FontHelper.applyFont(getActivity(), v.findViewById(R.id.about_contain));
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.headlayout));


        CancelBtn = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        DoneBtn = (TextView) v.findViewById(R.id.rightIconTxt);
        DoneBtn.setVisibility(View.GONE);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.menu_about));
        SubTitleTxt = (TextView) v.findViewById(R.id.bold_title_below);
        fblinkTxt = (TextView) v.findViewById(R.id.fblink);
        fblinkTxt.setText("" + SessionSave.getSession("facebook_share", getActivity()));
        twlinkTxt = (TextView) v.findViewById(R.id.twlink);
        twlinkTxt.setText("" + SessionSave.getSession("twitter_share", getActivity()));
        SubTitleTxt.setText("" + Html.fromHtml(SessionSave.getSession("About", getActivity())));
        version_code = (TextView) v.findViewById(R.id.version_code);

        try {
            manager = getActivity().getPackageManager();
            int curVersion = manager.getPackageInfo(getActivity().getPackageName(), 0).versionCode;
            version_code.setText("Version" + " " + ":" + " " + curVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Method to perform the user select operations(Social shares like linkedin,fb,twitter etc).
     *
     * @param v --> layout view of the about us page
     */

    //
    public void clickMethod(View v) {

        switch (v.getId()) {
            case R.id.rateapplay:
                Package pack = getActivity().getClass().getPackage();
                String packtxt = pack.toString();
                String packarr[] = packtxt.split(" ");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
                startActivity(browserIntent);
                break;
            case R.id.likfblay:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SessionSave.getSession("facebook_share", getActivity())));
                startActivity(intent);
                break;
            case R.id.fllwtwlay:
                Intent intent_t = new Intent(Intent.ACTION_VIEW, Uri.parse(SessionSave.getSession("twitter_share", getActivity())));
                startActivity(intent_t);
                break;
            case R.id.termslay:
                try {

                    String url = "&type=dynamic_page&pagename=3&device_type=1";
                    new ShowWebpage(url, null, 1);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;
            case R.id.privacypolicylay:
                try {
                    String url = "&type=dynamic_page&pagename=9&device_type=1";
                    new ShowWebpage(url, null, 2);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;
            case R.id.fblinklay:
                if (fblinkTxt.getText().toString().trim().length() > 0) {
                    Intent intent_fb = new Intent(Intent.ACTION_VIEW, Uri.parse(fblinkTxt.getText().toString()));
                    startActivity(intent_fb);
                }
                break;
            case R.id.twlinklay:
                if (twlinkTxt.getText().toString().trim().length() > 0) {
                    Intent intent_tw = new Intent(Intent.ACTION_VIEW, Uri.parse(twlinkTxt.getText().toString()));
                    startActivity(intent_tw);
                }
                break;
            default:
                break;
        }
    }

    /**
     * ShowWebpage class is helps to call API to get the term & conditions and privacy policy web page.
     */

    private class ShowWebpage implements APIResult {
        int type;

        public ShowWebpage(String string, JSONObject data, int i) {
            // TODO Auto-generated constructor stub
            type = i;
            //   new APIService_Retrofit_JSON(getActivity(), this, "", true).execute(TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", getActivity()) + string);
            new APIService_Retrofit_JSON(getActivity(), this, true, SessionSave.getSession("base_url", getActivity()) + SessionSave.getSession("api_key", getActivity()) + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&encode=" + SessionSave.getSession("encode", getActivity()) + string).execute();
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    Intent intent = new Intent(getActivity(), TermsAndConditions.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("content", "" + result);
                    if (type == 1) {
                        bundle.putString("name", NC.getString(R.string.termcond));
                        bundle.putBoolean("status", t_status);
                    } else if (type == 2) {
                        bundle.putString("name", NC.getString(R.string.policy));
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

//    /**
//     *
//     * @return
//     */
//    // findTwitterClient() method helps to move the control to external twitter web application.
//    public Intent findTwitterClient() {
//
//        final String[] twitterApps = {"com.twitter.android", "com.twidroid", "com.handmark.tweetcaster", "com.thedeck.android"};
//        Intent tweetIntent = new Intent();
//        tweetIntent.setType("text/plain");
//        final PackageManager packageManager = getActivity().getPackageManager();
//        List<ResolveInfo> list = packageManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        for (int i = 0; i < twitterApps.length; i++) {
//            for (ResolveInfo resolveInfo : list) {
//                String p = resolveInfo.activityInfo.packageName;
//                if (p != null && p.startsWith(twitterApps[i])) {
//                    tweetIntent.setPackage(p);
//                    return tweetIntent;
//                }
//            }
//        }
//        return null;
//    }

//    // findFacebookClient() method helps to move the control to external facebook web application.
//    public Intent findFacebookClient() {
//
//        final String[] twitterApps = {"com.facebook.katana"};
//        Intent tweetIntent = new Intent();
//        tweetIntent.setType("text/plain");
//        final PackageManager packageManager = getActivity().getPackageManager();
//        List<ResolveInfo> list = packageManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        for (int i = 0; i < twitterApps.length; i++) {
//            for (ResolveInfo resolveInfo : list) {
//                String p = resolveInfo.activityInfo.packageName;
//                if (p != null && p.startsWith(twitterApps[i])) {
//                    tweetIntent.setPackage(p);
//                    return tweetIntent;
//                }
//            }
//        }
//        return null;
//    }
//
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
////        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            if (menu.isMenuShowing()) {
////                menu.toggle();
////                return true;
////            }
////        }
//        return super.getActivity().onKeyDown(keyCode, event);
//    }


    /**
     * this method is used to dismiss the dialog when activity destroys
     */

    @Override
    public void onDestroy() {

        if (mshowDialog != null && mshowDialog.isShowing()) {
            mshowDialog.dismiss();
            mshowDialog = null;

        }
        TaxiUtil.mActivitylist.remove(getActivity());
        super.onDestroy();
    }

    /**
     * this method is used to set back icon when activity resumes
     */
    @Override
    public void onResume() {
        super.onResume();

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");

        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);


    }
}
