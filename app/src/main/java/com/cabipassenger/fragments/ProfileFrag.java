package com.cabipassenger.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;

/**
 * Created by developer on 4/22/16.
 */


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.data.Autocortctryname;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.util.AppCacheImage;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.ImageUtils;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.RoundedImageView;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.cabipassenger.util.TaxiUtil.Logout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * this class is used for user profile details
 */

public class ProfileFrag extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 112;

    private Dialog alertmDialog;
    private LinearLayout profile_bottom;
    private TextView Spinnertext;
    private String destinationFileName = "ProfileImage";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profilelay, container, false);
        Initialize(v);
        //  Colorchange.ChangeColor((ViewGroup)v,getActivity());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Colorchange.ChangeColor((ViewGroup)(PaymentOptionFrag.this.v),getActivity());
                Colorchange.ChangeColor((ViewGroup) (((ViewGroup) getActivity()
                        .findViewById(android.R.id.content)).getChildAt(0)), getActivity());
            }
        }, 50);
        return v;
    }

    //hello hfff
    // Class members declarations.
    private LinearLayout SaveImg;
    private TextView HeadTitle;
    private LinearLayout SlideImg;

    private EditText FirstnameEdt;
    private EditText LastnameEdt;
    private EditText EmailEdt;
    private EditText MobileEdt;
    private EditText PasswordEdt;
    private RoundedImageView ProfileImg;
    private TextView CancelTxt;
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Mobile;
    private String Password;
    private String f_Firstname;
    private String f_Lastname;
    private String f_Email;
    private String f_Picture;
    private String f_Mobile;
    private String f_Password;
    private String f_countrycode;
    private String f_Salutation, f_login_from;
    private DisplayImageOptions options;
    private String encodedImage = "";
    private Uri imageUri;
    private Bitmap mBitmap;
    private Spinner Sal_Spn;
    private ArrayAdapter<String> adapter;
    private String Salutation;
    private TaxiUtil mUtil;
    private LinearLayout LogoutBtn;
    // public static ProfileAct mProfile;
    ImageView linechange;
    TextView chngepwd;
    TextView back_text;
    String convertMD5_Value;
    private Bitmap downImage;
    private Spinner mobilecodespn;

    private ArrayAdapter<String> mobileno_adapter;
    private String countrycode = "";
    private String[] mobilenoary;
    private String[] countryCodeArray;
    private int positionFirst;
    private List<String> val = new ArrayList<String>();
    private List<String> code = new ArrayList<String>();
    private List<String> country = new ArrayList<String>();
    private List<String> arryaCountryList = new ArrayList<String>();
    public static AppCacheImage appcache;
    LinearLayout main;
    private Dialog mshowDialog;

    /**
     * Set the layout to activity.
     */


    public void priorChanges(View v) {
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.menu_profile));
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.headlayout));
    }

    public void showLoading(Context context) {
        View view = View.inflate(context, R.layout.progress_bar, null);
        mshowDialog = new Dialog(context, R.style.dialogwinddow);
        mshowDialog.setContentView(view);
        mshowDialog.setCancelable(false);
        mshowDialog.show();


        ImageView iv = (ImageView) mshowDialog.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);
    }

    @Override
    public void onStop() {
        closeLoading();
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
        super.onStop();

    }

    void closeLoading() {
        try {
            if (mshowDialog.isShowing())
                mshowDialog.dismiss();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    // Initialize the views on layout
    @SuppressLint("NewApi")
    public void Initialize(View v) {
        // TODO Auto-generated method stub

        appcache = new AppCacheImage();
        TaxiUtil.current_act = "ProfileAct";
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.profile_contain));
        main = (LinearLayout) v.findViewById(R.id.profile_lay);
        profile_bottom = (LinearLayout) v.findViewById(R.id.profile_bottom);
        mUtil = new TaxiUtil(getActivity());
        final File photo = new File(Environment.getExternalStorageDirectory() + "/Taxi/Send");
        if (!photo.exists())
            photo.mkdirs();
        TaxiUtil.mActivitylist.add(getActivity());
        SaveImg = (LinearLayout) v.findViewById(R.id.save);
        SaveImg.setVisibility(View.VISIBLE);
        SlideImg = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        ProfileImg = (RoundedImageView) v.findViewById(R.id.profile_img);
        FirstnameEdt = (EditText) v.findViewById(R.id.firstText1);
        LastnameEdt = (EditText) v.findViewById(R.id.lastText2);
        EmailEdt = (EditText) v.findViewById(R.id.emailText1);
        MobileEdt = (EditText) v.findViewById(R.id.mobileText2);
        PasswordEdt = (EditText) v.findViewById(R.id.passwordText3);
        Sal_Spn = (Spinner) v.findViewById(R.id.salSpn);
        chngepwd = (TextView) v.findViewById(R.id.chgepwdTxt);
        linechange = (ImageView) v.findViewById(R.id.linechange);
        LogoutBtn = (LinearLayout) v.findViewById(R.id.logoutBtn);
        Spinnertext = (TextView) v.findViewById(R.id.spinner_value);

        ImageView iv = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        ProfileImg.setVisibility(View.GONE);
        // Open the popup to change the password
        MobileEdt.setClickable(false);
        MobileEdt.setFocusable(false);
        Sal_Spn.setClickable(false);
        Sal_Spn.setFocusable(false);

        chngepwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                changePwdDialog();
            }
        });
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loadingimage).showImageForEmptyUri(null).showImageOnFail(null).resetViewBeforeLoading(false).delayBeforeLoading(1000).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).handler(new Handler()).build();
        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
        mobilecodespn = (Spinner) v.findViewById(R.id.mobilecodespn);
        countryCodeArray = getResources().getStringArray(R.array.country_code);

        for (String s : countryCodeArray) {

            val.add(s);

        }

        String coutryList;

        for (int i = 0; i < val.size(); i++) {
           /* countryCodeArray = val.get(i).toString().replaceAll("\\s", "").split("&");

            code.add(countryCodeArray[0]);
            country.add(countryCodeArray[1]);

            String coutryList = countryCodeArray[0] + "       " + countryCodeArray[1];
            arryaCountryList.add(coutryList);*/
            countryCodeArray = val.get(i).toString().trim().split("&");
            code.add(countryCodeArray[0].trim());
            country.add(countryCodeArray[1].trim());


            coutryList = Autocortctryname.Autocortct(countryCodeArray[0], countryCodeArray[1]);

            arryaCountryList.add(coutryList);

        }


        Spinnertext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //     mobilecodespn.performClick();
            }
        });
        mobileno_adapter = new FontHelper.MySpinnerAdapter(getActivity(), R.layout.monthitem_spinnerlay_grey, arryaCountryList);
        // mobileno_adapter = new ArrayAdapter<String>(getActivity(), R.layout.monthitem_spinnerlay, arryaCountryList);
        mobileno_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mobilecodespn.setAdapter(mobileno_adapter);

        Point pointSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(pointSize);
        mobilecodespn.setDropDownWidth(pointSize.x);
        mobilecodespn.setEnabled(false);
        mobilecodespn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                mobilenoary = parent.getItemAtPosition(arg2).toString().split("\\s+");
                TextView txt = ((TextView) view);
                if (txt != null) {
                    txt.setTextColor(getResources().getColor(R.color.black));
                    txt.setGravity(Gravity.CENTER_VERTICAL);
                    txt.setText("" + mobilenoary[0]);
                    Spinnertext.setText("" + mobilenoary[0]);
                    countrycode = mobilenoary[0];


                    int width = 0;
                    if (mobilenoary[0].length() > 4)
                        width = 100;
                    else if (mobilenoary[0].length() == 4)
                        width = 75;
                    else
                        width = 60;
                }

//                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(dpToPx(width), LinearLayout.LayoutParams.WRAP_CONTENT);
//                mobilecodespn.setLayoutParams(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // Call the profile API when the layout loaded.
        try {
            if (TaxiUtil.isOnline(getActivity())) {
                JSONObject j = new JSONObject();
                j.put("userid", SessionSave.getSession("Id", getActivity()));
                new GetProfile("type=passenger_profile", j);
                //  showLoading(getActivity());
            } else {
                // alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet_connection), "" + NC.getResources().getString(R.string.ok), "");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        ArrayList<String> sal = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sal_type)));
        adapter = new FontHelper.MySpinnerAdapter(getActivity(), R.layout.spinneritem_lay, sal);
        //  adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinneritem_lay, NC.getResources().getStringArray(R.array.sal_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sal_Spn.setAdapter(adapter);
        // To show the Slider menu for move from one activity to another activity.

        // To change passenger salutation
        Sal_Spn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View v, final int pos, final long arg3) {
                // TODO Auto-generated method stub
                Colorchange.ChangeColor(parent, getActivity());
                Salutation = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        // To Update the passenger profile
        SaveImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {
                    Firstname = FirstnameEdt.getText().toString().trim();
                    Lastname = LastnameEdt.getText().toString().trim();
                    Email = EmailEdt.getText().toString().trim();
                    Mobile = MobileEdt.getText().toString().trim();
                    Password = PasswordEdt.getText().toString().trim();
                    Drawable drawable = ProfileImg.getDrawable();
                    Bitmap bitmap = ImageUtils.drawableToBitmap(drawable);
                    if (Mobile.equalsIgnoreCase(SessionSave.getSession("Phone", getActivity())) && Firstname.equalsIgnoreCase(SessionSave.getSession("FName", getActivity())) && Lastname.equalsIgnoreCase(SessionSave.getSession("Lname", getActivity())) && Salutation.equalsIgnoreCase(SessionSave.getSession("Salutation", getActivity())) && Email.equalsIgnoreCase(SessionSave.getSession("Email", getActivity())) && countrycode.equalsIgnoreCase(SessionSave.getSession("CountyCode", getActivity())) && bitmap == downImage) {
                        //alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.no_changes), "" + NC.getResources().getString(R.string.ok), "");
                        CToast.ShowToast(getActivity(), NC.getResources().getString(R.string.no_changes));
                    } else if (((MainHomeFragmentActivity) getActivity()).validations(MainHomeFragmentActivity.ValidateAction.isValidFirstname, getActivity(), Firstname))
                        if (((MainHomeFragmentActivity) getActivity()).validations(MainHomeFragmentActivity.ValidateAction.isValidLastname, getActivity(), Lastname))
                            if (((MainHomeFragmentActivity) getActivity()).validations(MainHomeFragmentActivity.ValidateAction.isValidMail, getActivity(), Email))
                                if (((MainHomeFragmentActivity) getActivity()).validations(MainHomeFragmentActivity.ValidateAction.isValueNULL, getActivity(), Mobile))
                                    if (!f_login_from.equals("3")) {
                                        if (((MainHomeFragmentActivity) getActivity()).validations(MainHomeFragmentActivity.ValidateAction.isValidPassword, getActivity(), Password)) {
                                            JSONObject j = new JSONObject();
                                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                                            j.put("email", Email);
                                            j.put("phone", Mobile);
                                            j.put("country_code", countrycode);
                                            j.put("salutation", Salutation);
                                            j.put("firstname", Firstname);
                                            j.put("lastname", Lastname);
                                            j.put("password", "");
                                            j.put("profile_image", encodedImage);
                                            new EditProfile(j);
                                        }
                                    } else {
                                        JSONObject j = new JSONObject();
                                        j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                                        j.put("email", Email);
                                        j.put("phone", Mobile);
                                        j.put("country_code", countrycode);
                                        j.put("salutation", Salutation);
                                        j.put("firstname", Firstname);
                                        j.put("lastname", Lastname);
                                        j.put("password", "");
                                        j.put("profile_image", encodedImage);
                                        new EditProfile(j);
                                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        // To update the passenger profile picture from gallery or camera view.
        ProfileImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {


                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                        return;
                    } else
                        getCamera();


                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        // To logout from the application.
        LogoutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                logout();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        System.out.println("____naga"+grantResults.length+"__"+grantResults[0]+"__"+grantResults[1]+"__"+requestCode);
//        String s=("____naga"+grantResults.length+"__"+grantResults[0]+"__"+grantResults[1]+"__"+requestCode);
//        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getCamera();
                }
            }
            break;

        }
    }

    /**
     * this method is used to get the image from gallery or camera
     */

    private void getCamera() {
        AlertDialog d = new AlertDialog.Builder(getActivity()).setMessage("" + NC.getResources().getString(R.string.choose_an_image)).setTitle("" + NC.getResources().getString(R.string.profile_image)).setCancelable(true).setNegativeButton("" + NC.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                System.gc();
                final Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 0);
                dialog.cancel();
            }
        }).setPositiveButton("" + NC.getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                final File photo = new File(Environment.getExternalStorageDirectory() + "/TaxiMobility/img");
                if (!photo.exists())
                    photo.mkdirs();
                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                imageUri = Uri.fromFile(mediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            }
        }).show();
//        System.out.println("__________"+getResources().getResourceEntryName(););
//
//        Colorchange.ChangeColor((ViewGroup)d.findViewById(R.id.layout_root),getActivity());
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // ResultActivity.startWithUri(SampleActivity.this, resultUri);
           // System.out.println("Hellow" + resultUri);
            new ImageCompressionAsyncTask().execute(resultUri.toString());
        } else {
            // Toast.makeText(SampleActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Function for getting image from sd card or camera
     */
    @Override
    public void onActivityResult(final int requestcode, final int resultcode, final Intent data) {
        try {
         //   System.out.println("cameeeeeeev" + requestcode + "__" + CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
//            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultcode == AppCompatActivity.RESULT_OK) {
//
//                Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
//                System.out.println("cameeeeeee"+imageUri.toString());
//                // For API >= 23 we need to check specifically that we have permissions to read external storage,
//                // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
//                boolean requirePermissions = false;
//                if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
//                    System.out.println("cameeeeeeedd"+imageUri.toString());
//                    // request permissions and handle the result in onRequestPermissionsResult()
//                    requirePermissions = true;
//                    new ImageCompressionAsyncTask().execute(imageUri.toString());
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
//                } else {
//                    System.out.println("cameeeeeeeuu"+imageUri.toString());
//                    new ImageCompressionAsyncTask().execute(imageUri.toString());
//                   // mCurrentFragment.setImageUri(imageUri);
//                }
//            }
            if (requestcode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (resultcode == getActivity().RESULT_OK) {
                System.gc();
                switch (requestcode) {
                    case 0:
                        try {
                            UCrop uCrop = UCrop.of(Uri.fromFile(new File(getRealPathFromURI(data.getDataString()))), Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)))
                                    .withAspectRatio(4, 4)
                                    .withMaxResultSize(400, 400);
                            UCrop.Options options = new UCrop.Options();
                            options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.header_bgcolor));
                            options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.header_text));
                            options.setToolbarWidgetColor(ContextCompat.getColor(getActivity(), R.color.header_text));
                            uCrop.withOptions(options);
                            uCrop.start(getActivity());
                            //new ImageCompressionAsyncTask().execute(data.getDataString());
//                            CropImage.activity( Uri.parse(data.getDataString()))
//                                    .start(getContext(),this);
                        } catch (final Exception e) {
                        }
                        break;
                    case 1:
                        try {
                            //  new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
//                            CropImage.activity(imageUri)
//                                    .start(getContext(),this);

                            UCrop.of(imageUri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)))
                                    .withAspectRatio(4, 4)
                                    .withMaxResultSize(400, 400)
                                    .start(getActivity());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        } catch (final Exception e) {
        }
    }

    /**
     * getActivity() asyncTask helps to update the selected image to image view from camera or gallery
     */
    private class ImageCompressionAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private Dialog mDialog;
        private String result;
        private int orientation;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            final View view = View.inflate(getActivity(), R.layout.progress_bar, null);
            mDialog = new Dialog(getActivity(), R.style.NewDialog);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();

            ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
            Glide.with(getActivity())
                    .load(R.raw.loading_anim)
                    .into(imageViewTarget);
        }

        @Override
        protected Bitmap doInBackground(final String... params) {
            try {
                result = getRealPathFromURI(params[0]);
                final File file = new File(result);
              //  System.out.println("Imageee....." + file.getTotalSpace() + params[0]);
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                mBitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
//                mBitmap = decodeImageFile(file);
//                System.out.println("Imageee.....b" + mBitmap.getByteCount());
//
//                if (mBitmap != null) {
//                    final Matrix matrix = new Matrix();
//                    try {
//                        final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
//                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                        if (orientation == 3) {
//                            matrix.postRotate(180);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        } else if (orientation == 6) {
//                            matrix.postRotate(90);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        } else if (orientation == 8) {
//                            matrix.postRotate(270);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        }
//                    } catch (final IOException e) {
//                    }
//                }
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                if (mBitmap.getByteCount() > 100000 && mBitmap.getByteCount() < 200000)
//                    mBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
//                else if (mBitmap.getByteCount() > 200000)
//                    mBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
//                else
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] image = stream.toByteArray();
                encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
            //    System.out.println("Imageee" + encodedImage.getBytes().length);
             //   System.out.println("Imageeen" + encodedImage.length());
            } catch (final Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), NC.getResources().getString(R.string.please_select_image_from_valid_path), Toast.LENGTH_SHORT).show();
                        //  ShowToast(getApplicationContext(), "" + NC.getResources().getString(R.string.please_select_image_from_valid_path));
                    }
                });
            }
            return mBitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mDialog.isShowing())
                mDialog.dismiss();

            ProfileImg.setBackgroundResource(0);
            if (result != null)
                ProfileImg.setImageBitmap(result);
        }
    }

    /**
     * get image path from uri
     */

    public String getRealPathFromURI(final String contentURI) {
        final Uri contentUri = Uri.parse(contentURI);
        final Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else {
            cursor.moveToFirst();
            final int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /**
     * convert the image to bitmap
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
     * getActivity() Class helps to call the Profile API and process the response to update the UI.
     */
    private class GetProfile implements APIResult {
        public GetProfile(final String string, final JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON_NoProgress(getActivity(), this, data, false).execute(string);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess)
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        final JSONArray jarry = json.getJSONArray("detail");
                        final int length = jarry.length();
                        for (int i = 0; i < length; i++) {
                            f_Firstname = jarry.getJSONObject(i).getString("name");
                            f_Firstname = f_Firstname.substring(0, 1).toUpperCase() + f_Firstname.substring(1);
                            f_Lastname = jarry.getJSONObject(i).getString("lastname");
                            f_Email = jarry.getJSONObject(i).getString("email");
                            f_Picture = jarry.getJSONObject(i).getString("profile_image");
                            f_Mobile = jarry.getJSONObject(i).getString("phone");
                            f_Password = jarry.getJSONObject(i).getString("password");
                            f_countrycode = jarry.getJSONObject(i).getString("country_code");
                            SessionSave.saveSession("ProfileImage", f_Picture, getActivity());
                            SessionSave.saveSession("encrypt_password", "" + jarry.getJSONObject(i).getString("password"), getActivity());
                            f_Salutation = jarry.getJSONObject(i).getString("salutation");
                            f_login_from = jarry.getJSONObject(i).getString("login_from");
                            SessionSave.saveSession("ProfileName", f_Firstname, getActivity());
                            SessionSave.saveSession("Email", f_Email, getActivity());
                            SessionSave.saveSession("Phone", f_Mobile, getActivity());
                            SessionSave.saveSession("FName", f_Firstname, getActivity());
                            SessionSave.saveSession("Lname", f_Lastname, getActivity());
                            SessionSave.saveSession("CountyCode", f_countrycode, getActivity());
                            SessionSave.saveSession("Salutation", f_Salutation, getActivity());
                            ((MainHomeFragmentActivity) getActivity()).profileEdited();
                            try {


                                for (int j = 0; j < code.size(); j++) {
                                  //  System.out.println("couuuu"+String.format(Locale.UK, String.valueOf(f_countrycode))+"___"+String.format(Locale.UK, String.valueOf(code.get(j).toString().trim())).trim().replaceAll("\\s","")+"__***"+String.format(Locale.UK, String.valueOf(f_countrycode)).equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(j).toString()))));
                                    if (String.format(Locale.UK, String.valueOf(f_countrycode)).equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(j).toString().trim())).trim().replaceAll("\\s",""))) {
                                        //findPosition
                                        positionFirst = j;
                                        mobilecodespn.setSelection(positionFirst);
                              //          System.out.println("positionFirst " + positionFirst);
                                        break;
                                    }
                                    // Setting Default as Kuwait if not get Country
                                    else {
                                        positionFirst = 93;
                                    }
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            if (f_login_from.equals("0") || f_login_from.equals("1")) {
                                chngepwd.setVisibility(View.VISIBLE);
                                linechange.setVisibility(View.VISIBLE);
                            } else if (f_login_from.equals("3")) {
                                chngepwd.setVisibility(View.VISIBLE);
                                linechange.setVisibility(View.VISIBLE);
                            } else {
                                chngepwd.setVisibility(View.VISIBLE);
                                linechange.setVisibility(View.VISIBLE);
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * getActivity() Class helps to call the Edit Profile API and process the response to update the UI.
     */
    private class EditProfile implements APIResult {
        public EditProfile(final JSONObject data) {
            // TODO Auto-generated constructor stub
            // new APIService_HTTP_JSON_WO(getActivity(), getActivity(), data, false, TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + "type=edit_passenger_profile").execute();
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute("type=edit_passenger_profile");
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("Email", "" + EmailEdt.getText().toString().trim(), getActivity());
                        SessionSave.saveSession("Phone", "" + MobileEdt.getText().toString().trim(), getActivity());
                        SessionSave.saveSession("FName", "" + FirstnameEdt.getText().toString().trim(), getActivity());
                        SessionSave.saveSession("ProfileName", FirstnameEdt.getText().toString(), getActivity());
                        SessionSave.saveSession("Lname", "" + LastnameEdt.getText().toString().trim(), getActivity());
                        SessionSave.saveSession("CountyCode", countrycode, getActivity());
                        SessionSave.saveSession("Salutation", Salutation, getActivity());
                        SessionSave.saveSession("ProfileImage", json.getString("profile_image"), getActivity());
                        ((MainHomeFragmentActivity) getActivity()).profileEdited();
                        Drawable drawable = ProfileImg.getDrawable();
                        downImage = ImageUtils.drawableToBitmap(drawable);

                    } else
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final Exception e) {
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    /**
     * Handler to update the UI.
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    final String imgpath = "" + f_Picture;
//                    if (!f_Picture.equals("")) {
//
//                        if (!AppCacheImage.loadBitmap(imgpath, ProfileImg)) {
//                            System.out.println("Image... not avail in cache");
//                            new DownloadImageTask(ProfileImg).execute(imgpath);
//                            SessionSave.saveSession("ProfileImage", f_Picture, getActivity());
//                        }
//                    } else {
//                        ProfileImg.setImageResource(R.drawable.profile_image);
//                    }
                    Picasso.with(getActivity()).load(imgpath).error(R.drawable.loadingimage).placeholder(R.drawable.loadingimage).into(ProfileImg);
//                    closeLoading();
                    main.setVisibility(View.VISIBLE);
                    profile_bottom.setVisibility(View.VISIBLE);
                    FirstnameEdt.setText(f_Firstname);
                    LastnameEdt.setText(f_Lastname);
                    EmailEdt.setText(f_Email);
                    MobileEdt.setText(f_Mobile);
                    PasswordEdt.setText(f_Password);
                    if (f_Salutation.equalsIgnoreCase("Miss"))
                        Sal_Spn.setSelection(2);
                    if (f_Salutation.equalsIgnoreCase("Mrs"))
                        Sal_Spn.setSelection(1);
                    if (f_Salutation.equalsIgnoreCase("Mr"))
                        Sal_Spn.setSelection(0);
                    SessionSave.saveSession("Email", f_Email, getActivity());
                    SessionSave.saveSession("Phone", f_Mobile, getActivity());
                    SessionSave.saveSession("FName", f_Firstname, getActivity());
                    SessionSave.saveSession("Lname", f_Lastname, getActivity());
                    SessionSave.saveSession("Salutation", Salutation, getActivity());
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }

        ;
    };

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


    // getActivity() AsyncTask used to download the image from given URL and Update into image view.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(final ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(final String... urls) {
            final String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                final InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            super.onPostExecute(result);
           // System.out.println("Image... stroing to cache" + f_Picture);
            AppCacheImage.addBitmapToMemoryCache(f_Picture, result);
            AppCacheImage.loadBitmap(f_Picture, ProfileImg);
            //  bmImage.setImageBitmap(result);
            downImage = result;
        }
    }

    // Slider menu used to move from one activity to another activity.


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(getActivity());

        if (mshowDialog != null && mshowDialog.isShowing()) {
            mshowDialog.dismiss();
            mshowDialog = null;

        }
        super.onDestroy();
    }


    private Dialog dialog;

    /**
     * getActivity() method used to display the dialog window to change the password
     * <p>
     * getActivity() method used to display the dialog window to change the password
     * </p>
     */
    public void changePwdDialog() {
        final View view = View.inflate(getActivity(), R.layout.changepwd, null);
        dialog = new Dialog(getActivity(), R.style.dialogwinddow);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        Colorchange.ChangeColor((ViewGroup) view, getActivity());
        FontHelper.applyFont(getActivity(), dialog.findViewById(R.id.rootlay));
        final EditText oldpwd = (EditText) dialog.findViewById(R.id.oldpwd);
        final EditText newpwd = (EditText) dialog.findViewById(R.id.newpwd);
        final EditText confirmpwd = (EditText) dialog.findViewById(R.id.confirmpwd);
        final TextView hideoldpwd = (TextView) dialog.findViewById(R.id.hideoldpwd);
        final TextView hidenewpwd = (TextView) dialog.findViewById(R.id.hidenewpwd);
        final TextView hideconfirmpwd = (TextView) dialog.findViewById(R.id.hideconfirmpwd);
        final Button submit = (Button) dialog.findViewById(R.id.submit);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        confirmpwd.setImeOptions(EditorInfo.IME_ACTION_DONE);
        dialog.setCanceledOnTouchOutside(false);





         /* newly add for password hide and show*/

        hidenewpwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hidenewpwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hidenewpwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(getActivity(), newpwd);

                } else {
                    newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hidenewpwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(getActivity(), newpwd);

                }
            }
        });


        newpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {

                    if (newpwd.getText().toString().length() > 0)
                        hidenewpwd.setVisibility(View.VISIBLE);
                    else
                        hidenewpwd.setVisibility(View.INVISIBLE);


                } else {
                    hidenewpwd.setVisibility(View.GONE);
                    newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(getActivity(), newpwd);
                    if (hidenewpwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        hidenewpwd.setText("" + NC.getResources().getString(R.string.show));


                    }
                }
            }
        });
        newpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (newpwd.getText().toString().length() > 0) {
                    hidenewpwd.setVisibility(View.VISIBLE);
                } else {
                    hidenewpwd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        hideoldpwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hideoldpwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    oldpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hideoldpwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(getActivity(), oldpwd);

                } else {
                    oldpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hideoldpwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(getActivity(), oldpwd);
                }
            }
        });
        oldpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {

                    if (oldpwd.getText().toString().length() > 0)
                        hideoldpwd.setVisibility(View.VISIBLE);
                    else
                        hideoldpwd.setVisibility(View.INVISIBLE);


                } else {
                    hideoldpwd.setVisibility(View.GONE);
                    oldpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(getActivity(), oldpwd);
                    if (hideoldpwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        hideoldpwd.setText("" + NC.getResources().getString(R.string.show));
                    }

                }
            }
        });

        oldpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (oldpwd.getText().toString().length() > 0) {
                    hideoldpwd.setVisibility(View.VISIBLE);
                } else {
                    hideoldpwd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        hideconfirmpwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hideconfirmpwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    confirmpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hideconfirmpwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(getActivity(), confirmpwd);

                } else {
                    confirmpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hideconfirmpwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(getActivity(), confirmpwd);

                }
            }
        });
        confirmpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {

                    if (confirmpwd.getText().toString().length() > 0)
                        hideconfirmpwd.setVisibility(View.VISIBLE);
                    else
                        hideconfirmpwd.setVisibility(View.INVISIBLE);


                } else {
                    hideconfirmpwd.setVisibility(View.GONE);
                    confirmpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(getActivity(), confirmpwd);

                    if (hideconfirmpwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        hideconfirmpwd.setText("" + NC.getResources().getString(R.string.show));
                    }
                }
            }
        });

        confirmpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (confirmpwd.getText().toString().length() > 0) {
                    hideconfirmpwd.setVisibility(View.VISIBLE);
                } else {
                    hideconfirmpwd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                try {


                    final String opwd = oldpwd.getText().toString().trim();
                    final String npwd = newpwd.getText().toString().trim();
                    final String cpwd = confirmpwd.getText().toString().trim();
                    if (opwd.length() <= 0) {
                        Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.enter_the_old_password), Toast.LENGTH_SHORT).show();
                        oldpwd.requestFocus();
                      //  oldpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    //    oldpwd.clearFocus();
                    }else if (npwd.length() <= 0) {
                        Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.enter_the_new_password), Toast.LENGTH_SHORT).show();
                        newpwd.requestFocus();
                        newpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        newpwd.clearFocus();
                    } else if (npwd.length() <= 3) {
                        Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.password_min_character), Toast.LENGTH_SHORT).show();
                        newpwd.requestFocus();
                        newpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        newpwd.clearFocus();
                    }else if (cpwd.length() <= 0) {
                        Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.enter_the_confirmation_password), Toast.LENGTH_SHORT).show();
                        confirmpwd.requestFocus();
//                        confirmpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
//                        confirmpwd.clearFocus();
                    }   else if (cpwd.length() <= 3) {
                        Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.password_min_character), Toast.LENGTH_SHORT).show();
                        newpwd.requestFocus();
                        newpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        newpwd.clearFocus();
                    } else if (!npwd.equals(cpwd)) {
                        Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.confirmation_password_mismatch_with_password), Toast.LENGTH_SHORT).show();
                        newpwd.requestFocus();
                        newpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        newpwd.clearFocus();
                    }else {
                        convertMD5(opwd);
                        if (SessionSave.getSession("encrypt_password", getActivity()).equals(convertMD5_Value)) {
                            convertMD5(npwd);
                            new Chngepwd(dialog, opwd, npwd);
                        } else
                            Toast.makeText(getActivity(), "" + NC.getResources().getString(R.string.old_password_mismatch_with_password), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                confirmpwd.requestFocus();
                confirmpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
                dialog.dismiss();
            }
        });
    }

    /**
     * getActivity() class helps to call the change the password.
     */
    class Chngepwd implements APIResult {
        private Dialog dialog;

        public Chngepwd(final Dialog dialog, final String oldpwd, final String newpwd) {
            try {
                this.dialog = dialog;
                JSONObject j = new JSONObject();
                j.put("id", SessionSave.getSession("Id", getActivity()));
                j.put("new_password", newpwd);
                j.put("confirm_password", newpwd);
                j.put("old_password", SessionSave.getSession("encrypt_password", getActivity()));
                new APIService_Retrofit_JSON(getActivity(), this, j, false).execute("type=chg_password_passenger");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            try {
                if (isSuccess) {
                    final JSONObject obj = new JSONObject(result);
                    final String msg = obj.getString("message");
                    final String status = obj.getString("status");
                    SessionSave.saveSession("encrypt_password", "" + convertMD5_Value, getActivity());
                    if (status.equals("1"))
                        dialog.cancel();
                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), msg, "" + NC.getResources().getString(R.string.ok), "");
                } else
                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), result, "" + NC.getResources().getString(R.string.ok), "");
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * getActivity() method used to convert the string into md5
     * <p>
     * getActivity() method used to convert the string into md5
     * </p>
     *
     * @param text
     * @return string
     */
    public void convertMD5(final String text) {
        try {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(text.getBytes());
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
                sb.append(Integer.toHexString(array[i] & 0xFF | 0x100).substring(1, 3));
            // return sb.toString();
            convertMD5_Value = sb.toString();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        // return "";
    }

    /**
     * getActivity() method used to call logout API.
     */
    public void logout() {
        try {
            final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
            final Dialog mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();
            FontHelper.applyFont(getActivity(), mDialog.findViewById(R.id.alert_id));
            final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.confirmlogout));
            button_success.setText("" + NC.getResources().getString(R.string.menu_logout));
            button_failure.setText("" + NC.getResources().getString(R.string.cancel));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mDialog.dismiss();
                        // TODO Auto-generated method stub
                        JSONObject j = new JSONObject();
                        j.put("id", SessionSave.getSession("Id", getActivity()));
                        if (SessionSave.getSession("Logout", getActivity()).equals("")) {
                            new Logout("type=passenger_logout", getActivity(), j);
                            ((MainHomeFragmentActivity) getActivity()).fbLogout();
                        } else
                            Toast.makeText(getActivity(), NC.getResources().getString(R.string.bookedtaxi), Toast.LENGTH_SHORT).show();
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
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).toolbar_title.setText(NC.getString(R.string.menu_profile));
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);

    }


}