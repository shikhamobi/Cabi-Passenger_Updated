package com.cabipassenger.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by developer on 4/22/16.
 * To change language for the whole app using locale
 */


public class LanguageFrag extends Fragment {

    TextView headertxt, leftIcon, back_text;
    private ListView Language_list;
    private ArrayAdapter<String> listAdapter;
    String[] langs = new String[]{"English", "Türk", "العربية", "русский", "Deutsch", "Español"};// "Indonesian", "French",
    ArrayList<String> langList = new ArrayList<String>();
    protected String selectedIndex;
    public static Activity mlang;
    private Dialog mshowDialog;
    private int types = 1;
    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.language_lay, container, false);
        priorChanges(v);
        Initialize();

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


    public void priorChanges(View v) {

        FontHelper.applyFont(getActivity(), v.findViewById(R.id.about_containlay));
        //headlayout
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.headlayout));

        headertxt = (TextView) v.findViewById(R.id.header_titleTxt);
        headertxt.setText(NC.getResources().getString(R.string.settings));
        leftIcon = (TextView) v.findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        Language_list = (ListView) v.findViewById(R.id.language_list);

        String totalLang[] = (SessionSave.getSession("lang_json", getActivity())).trim().split("____");
        langList.addAll(Arrays.asList(totalLang));

        listAdapter = new FontHelper.MySpinnerAdapter(getActivity(), R.layout.simplerows, langList);
        Language_list.setAdapter(listAdapter);
        // Colorchange.ChangeColor(,getActivity());
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getActivity().getResources().getString(R.string.Language));

    }

    /**
     * this method is used to select the language to change the language format in whole app
     */

    public void Initialize() {
        // TODO Auto-generated method stub
/*
        Language_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                selectedIndex = langList.get(position).toString();
                types = position + 1;
                // showLoading();
                Intent i;
                switch (position) {
                    case 0:
                        if (!SessionSave.getSession("", getActivity()).equals("en")) {

                            SessionSave.saveSession("Lang", "en", getActivity());
                            SessionSave.saveSession("Lang_Country", "en_GB", getActivity());
                            //setLocale();
                            SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_English", getActivity()), getActivity());
                            new callString("strings.xml");

                        }
                        break;
                    case 1:
                        if (!SessionSave.getSession("Lang", getActivity()).equals("tr")) {


                            SessionSave.saveSession("Lang", "tr", getActivity());
                            SessionSave.saveSession("Lang_Country", "tr_TR", getActivity());
                            SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Turkish", getActivity()), getActivity());
                            //setLocale();
                            new callString("strings_tr.xml");
                        }
                        break;
                    case 2:
                        if (!SessionSave.getSession("Lang", getActivity()).equals("ar")) {
                            SessionSave.saveSession("Lang", "ar", getActivity());
                            SessionSave.saveSession("Lang_Country", "ar_EG", getActivity());
                            SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Arabic", getActivity()), getActivity());
                            //setLocale();
                            new callString("strings_ar.xml");
                        }
                        break;
                    case 3:
                        if (!SessionSave.getSession("Lang", getActivity()).equals("ru")) {
                            SessionSave.saveSession("Lang", "ru", getActivity());
                            SessionSave.saveSession("Lang_Country", "ru_RU", getActivity());
                            SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Russian", getActivity()), getActivity());
                            //setLocale();
                            new callString("strings_ru.xml");
                        }
                        break;
                    case 4:
                        if (!SessionSave.getSession("Lang", getActivity()).equals("de")) {
                            SessionSave.saveSession("Lang", "de", getActivity());
                            SessionSave.saveSession("Lang_Country", "de_DE", getActivity());
                            SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_German", getActivity()), getActivity());
                            //setLocale();
                            new callString("strings_de.xml");
                        }

                        break;
                    case 5:
                        if (!SessionSave.getSession("Lang", getActivity()).equals("es")) {
                            SessionSave.saveSession("Lang", "es", getActivity());
                            SessionSave.saveSession("Lang_Country", "es_US", getActivity());
                            SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Spanish", getActivity()), getActivity());
                            //setLocale();
                            new callString("strings_es.xml");
                        }

                        break;
                    // case "Russian":
                    // SessionSave.saveSession("Lang", "ru", getActivity());
                    // SessionSave.saveSession("Lang_Country", "ru_RU", getActivity());
                    // i = new Intent(getActivity(), BookTaxiAct.class);
                    // startActivity(i);
                    // break;
                    //
                    // case "Urdu":
                    // SessionSave.saveSession("Lang", "ur", getActivity());
                    // SessionSave.saveSession("Lang_Country", "ur_PK", getActivity());
                    // i = new Intent(getActivity(), BookTaxiAct.class);
                    // startActivity(i);
                    // break;
                    //
                    // case "Indonesian":
                    // SessionSave.saveSession("Lang", "id", getActivity());
                    // SessionSave.saveSession("Lang_Country", "id_ID", getActivity());
                    // i = new Intent(getActivity(), BookTaxiAct.class);
                    // startActivity(i);
                    // break;
                    default:
                        break;
                }
            }
        });
*/


        Language_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
              //  System.out.println("currentLang"+SessionSave.getSession("Lang",getActivity())+"Ask"+(SessionSave.getSession("LANGCode" + String.valueOf(position), getActivity())));

                //new callString("strings.xml");

                if(!SessionSave.getSession("Lang",getActivity()).equalsIgnoreCase(SessionSave.getSession("LANGCode" + String.valueOf(position), getActivity())))
                try {
                    JSONObject j = new JSONObject();
                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                    j.put("language", SessionSave.getSession("LANGCode" + String.valueOf(position), getActivity()));
                    String urls = "type=passenger_language_update";
                    new UpdateUserLang(urls, j, position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * this method is used set the language based by country
     */

    public void setLocale() {
        if (SessionSave.getSession("Lang", getActivity()).equals("")) {
            SessionSave.saveSession("Lang", "en", getActivity());
            SessionSave.saveSession("Lang_Country", "en_US", getActivity());
        }


//        String languageToLoad  = "fa"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(new Locale("fa"));
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getActivity().getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());


        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", getActivity());
        String arry[] = langcountry.split("_");
        config.locale = new Locale(arry[0], arry[1]);
        Locale.setDefault(new Locale(arry[0], arry[1]));
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getResources().getString(R.string.menu_settings));

    }

    //    private void showLoading() {
//        View view = View.inflate(getActivity(), R.layout.progress_bar, null);
//        mshowDialog = new Dialog(getActivity(), R.style.dialogwinddow);
//        mshowDialog.setContentView(view);
//        mshowDialog.setCancelable(false);
////        mshowDialog.show();
//    }

    /**
     * this method is used to refresh and changed the language
     */

    private void RefreshAct() {

        String temptype = SessionSave.getSession("LANGTemp" + String.valueOf(types), getActivity());
        SessionSave.saveSession("Lang", SessionSave.getSession("LANGCode" + types, getActivity()), getActivity());
        //System.out.println("___________________" + SessionSave.getSession("Lang", getActivity()));

        if (temptype.equals("LTR")) {
            //  SessionSave.saveSession("Lang", "en",  getActivity());
            SessionSave.saveSession("Lang_Country", "en_US", getActivity());
        } else {
            //SessionSave.saveSession("Lang", "ar",  getActivity());
            SessionSave.saveSession("Lang_Country", "ar_EG", getActivity());

        }

       // System.out.println("LLLLTTTTT" + temptype + "____" + types);
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", getActivity());
        String arry[] = langcountry.split("_");
        config.locale = new Locale(SessionSave.getSession("Lang", getActivity()), arry[1]);
        Locale.setDefault(new Locale(SessionSave.getSession("Lang", getActivity()), arry[1]));
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());


        getActivity().finish();

        Intent intent = new Intent(getContext(), MainHomeFragmentActivity.class);
        showLoading(getActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        startActivity(intent);
    }

    public void showLoading(Context context) {
        View view = View.inflate(context, R.layout.progress_bar, null);
        mshowDialog = new Dialog(context, R.style.dialogwinddow);
        mshowDialog.setContentView(view);
        mshowDialog.setCancelable(false);

        ImageView iv = (ImageView) mshowDialog.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);
        //  mshowDialog.show();
    }


    private class UpdateUserLang implements APIResult {
        int pos;

        public UpdateUserLang(final String url, JSONObject data, int pos) {
            // TODO Auto-generated constructor stub
            String urls = SessionSave.getSession("currentStringUrl", getActivity());
            this.pos = pos;
//            if (urls.equals(""))
//                urls = SessionSave.getSession("Lang_English", getActivity());
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);

            //new APIService_Retrofit_JSON(getActivity(), this, null, true,"http://192.168.1.169:1009/public/uploads/android/language/passenger/" + url).execute();
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                try {
                    JSONObject j = new JSONObject(result);
                    if (j.getString("status").equals("1")) {
                  //      System.out.println("Lannnn___" + result);
                        selectedIndex = langList.get(pos).toString();
                        types = pos;
                        String url = SessionSave.getSession(SessionSave.getSession("LANG" + String.valueOf(pos), getActivity()), getActivity());
                    //    System.out.println("current_url" + url);
                        SessionSave.saveSession("currentStringUrl", url, getActivity());
                        new callString("strings.xml");
                    } else {
                        Toast.makeText(getActivity(), j.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                NC.nfields_byID.clear();
//                NC.nfields_byName.clear();
//                SplashActivity.fields.clear();
//                SplashActivity.fields_value.clear();
//                SplashActivity.fields_id.clear();
//                setLocale();
//                // System.out.println("nagaaaa"+result);
//                SessionSave.saveSession("wholekey", result, getActivity());
//                getAndStoreStringValues(result);


                // RefreshAct();

            }

        }
    }


    /**
     * this class is used to call the api
     */

    private class callString implements APIResult {
        public callString(final String url) {
            // TODO Auto-generated constructor stub
            String urls = SessionSave.getSession("currentStringUrl", getActivity());
            new APIService_Retrofit_JSON(getActivity(), this, null, true, urls, true).execute();


        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {

                NC.nfields_byID.clear();
                NC.nfields_byName.clear();
                SplashActivity.fields.clear();
                SplashActivity.fields_value.clear();
                SplashActivity.fields_id.clear();
                setLocale();
                // System.out.println("nagaaaa"+result);
                SessionSave.saveSession("wholekey", result, getActivity());
                getAndStoreStringValues(result);


                RefreshAct();

            }

        }
    }

    /**
     * replace the string value with selected language string
     *
     * @param result
     */
    private synchronized void getAndStoreStringValues(String result) {
        try {

            switch (types) {
                case 1:
                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_English", getActivity()), getActivity());
                    break;
                case 2:
                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Turkish", getActivity()), getActivity());
                    break;
                case 3:
                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Arabic", getActivity()), getActivity());
                    break;
                case 4:
                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Russian", getActivity()), getActivity());
                    break;
                case 5:
                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_German", getActivity()), getActivity());
                    break;
                case 6:
                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Spanish", getActivity()), getActivity());
                    break;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

          //  System.out.println("___len" + nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    if (element2.getAttribute("name").equals("pressBack"))
                     //   System.out.println("___ize" + chhh + "___" + element2.getTextContent());

                    //  NC.nfields_value.add(element2.getTextContent());
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
           // System.out.println("___sizelllll" + SplashActivity.fields_id.size());
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used to get the string files
     */

    synchronized void getValueDetail() {
        Field[] fieldss = R.string.class.getDeclaredFields();
        // fields =new int[fieldss.length];
       // System.out.println("***" + fieldss.length + "___" + NC.nfields_byName.size());
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getActivity().getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                SplashActivity.fields.add(fieldss[i].getName());
                SplashActivity.fields_value.add(getResources().getString(id));
                SplashActivity.fields_id.put(fieldss[i].getName(), id);
            //    System.out.println("&&&***" + fieldss[i].getName() + "____" + getResources().getString(id));

            } else {
            //    System.out.println("test" + fieldss[i].getName());
            }
        }

        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
          //  System.out.println("&&&" + SplashActivity.fields_id.get(h) + "____" + NC.nfields_byName.get(h));
            NC.nfields_byID.put(SplashActivity.fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

        //System.out.println("field__" + SplashActivity.fields.size() + "___" + SplashActivity.fields_value.size() + "___" + SplashActivity.fields_id.size());
    }
}

