package com.cabipassenger.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.features.CToast;
import com.cabipassenger.features.Validation;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.FragPopFront;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by developer on 4/26/16.
 * Class for wallet page fragment
 *
 */
public class WalletFrag extends Fragment implements View.OnClickListener,FragPopFront {


    private LinearLayout SlideImg;

    private LinearLayout Donelay;
    private TextView BackBtn;
    private TextView leftIcon;
    private TextView HeadTitle;
    private TextView walletbalTxt;
    private Button addmoneyBut;
    private EditText addmoneyEdt;
    private TextView monoption1;
    private TextView monoption2;
    private TextView monoption3;
    private TextView procodeTxt;
    private String promoCode = "";
    private Dialog mDialog;
    private int addMoney = 0;
    private Double walletAmount;
    private String wallet_amount_range = "";
    private String wallet_amount1 = "";
    private String wallet_amount2;
    private String wallet_amount3;
    private int range1;
    private int range2;
    private Dialog alertmDialog;
    private Dialog mshowDialog;
    private LinearLayout loading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.walletlay, container, false);
        findViewById(v);
        return v;
    }

    public void findViewById(View v) {
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.rootContain));
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.wallet));
        leftIcon = (TextView) v.findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        BackBtn = (TextView) v.findViewById(R.id.back_text);
        BackBtn.setVisibility(View.VISIBLE);

        Glide.with(this).load(SessionSave.getSession("image_path",getActivity())+"walletPageIcon.png").error(R.drawable.wallet2).into((ImageView) v.findViewById(R.id.walletPageIcon));
        Initialize(v);
    }

    public void Initialize(View v) {
        // TODO Auto-generated method stub

        Colorchange.ChangeColor((ViewGroup) v,getActivity());

        SlideImg = (LinearLayout) v.findViewById(R.id.leftIconTxt);

        Donelay = (LinearLayout) v.findViewById(R.id.rightlay);
        Donelay.setVisibility(View.INVISIBLE);
        ((TextView) v.findViewById(R.id.cur_sym)).setText(SessionSave.getSession("Currency", getActivity()));

        walletbalTxt = (TextView) v.findViewById(R.id.walletbalTxt);
        loading = (LinearLayout) v.findViewById(R.id.loading);
        ImageView iv= (ImageView)v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        addmoneyBut = (Button) v.findViewById(R.id.addmoneyBut);
        addmoneyEdt = (EditText) v.findViewById(R.id.addmoneyEdt);
        monoption1 = (TextView) v.findViewById(R.id.monoption1);
        monoption2 = (TextView) v.findViewById(R.id.monoption2);
        monoption3 = (TextView) v.findViewById(R.id.monoption3);
        procodeTxt = (TextView) v.findViewById(R.id.procodeTxt);
//        addmoneyEdt.setHint("" + getResources().getString(R.string.amount_between) + " " + SessionSave.getSession("Currency", getActivity()) + "100" + "-" + SessionSave.getSession("Currency", getActivity()) + "2000");
//        monoption1.setText("" + SessionSave.getSession("Currency", getActivity()) + "100");
//        monoption2.setText("" + SessionSave.getSession("Currency", getActivity()) + "200");
//        monoption3.setText("" + SessionSave.getSession("Currency", getActivity()) + "500");

        setOnclickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (addmoneyEdt!= null)
//            addmoneyEdt.setText("");

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        CheckWallet();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Override
    public void onStart() {

        super.onStart();
      //  Colorchange.ChangeColor((ViewGroup) getView(),getActivity());
    }

    private void setOnclickListener() {

        BackBtn.setOnClickListener(this);
        addmoneyBut.setOnClickListener(this);
        monoption1.setOnClickListener(this);
        monoption2.setOnClickListener(this);
        monoption3.setOnClickListener(this);
        procodeTxt.setOnClickListener(this);

        SpannableString content = new SpannableString(NC.getString(R.string.have_promocode).trim());
        content.setSpan(new UnderlineSpan(), 0, NC.getString(R.string.have_promocode).trim().length(), 0);
        procodeTxt.setText(Html.fromHtml("<p><u>"+(NC.getString(R.string.have_promocode).trim())+"<p><u>"));
        //procodeTxt.setPaintFlags(procodeTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * Calls api for wallet amount enqury
     */
    private void CheckWallet() {

        try {
            JSONObject j = new JSONObject();
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            String url = "type=passenger_wallet";
            new WalletBal(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void trigger_FragPopFront() {
        CheckWallet();
        ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);
        if (addmoneyEdt!=null)
            addmoneyEdt.setText("");
        if(monoption1!=null){
            monoption1.setBackgroundResource(R.drawable.draw_edittext_bg);
            monoption2.setBackgroundResource(R.drawable.draw_edittext_bg);
            monoption3.setBackgroundResource(R.drawable.draw_edittext_bg);
            monoption1.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
            monoption2.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
            monoption3.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
        }
    }

    /**
     * This class used to check wallet balance
     * <p>
     * This class used to check wallet balance
     * <p>
     *
     * @author developer
     */
    private class WalletBal implements APIResult {
        private WalletBal(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    loading.setVisibility(View.GONE);
                    addmoneyBut.setVisibility(View.VISIBLE);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        walletAmount = Double.parseDouble(json.getString("wallet_amount"));
                        wallet_amount_range = json.getJSONObject("amount_details").getString("wallet_amount_range");
                        wallet_amount1 = json.getJSONObject("amount_details").getString("wallet_amount1");
                        wallet_amount2 = json.getJSONObject("amount_details").getString("wallet_amount2");
                        wallet_amount3 = json.getJSONObject("amount_details").getString("wallet_amount3");
                        String[] rangeary = wallet_amount_range.split("-");

                      //  Log.e("range__", rangeary[0]);

                        if (rangeary.length > 1) {
                            range1 = Integer.parseInt(rangeary[0]);
                            range2 = Integer.parseInt(rangeary[1]);
                        }
                        addmoneyEdt.setHint("" + NC.getResources().getString(R.string.amount_between) + " " + SessionSave.getSession("Currency", getActivity()) + range1 + "-" + SessionSave.getSession("Currency", getActivity()) + range2);
                        monoption1.setText("" + SessionSave.getSession("Currency", getActivity()) + wallet_amount1);
                        monoption2.setText("" + SessionSave.getSession("Currency", getActivity()) + wallet_amount2);
                        monoption3.setText("" + SessionSave.getSession("Currency", getActivity()) + wallet_amount3);
                        walletbalTxt.setText("" + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", walletAmount));
                    } else {
                        walletbalTxt.setText("" + SessionSave.getSession("Currency", getActivity()) + "0.00");
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                /*getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), result);
                    }
                });*/
                alert_view(getActivity(), "Message", "" + result, "" +NC.getResources().getString(R.string.ok), "");
            }
        }
    }

    /**
     * This class used to check valid promo code
     * <p>
     * This class used to check valid promo code
     * <p>
     *
     * @author developer
     */
    private class CheckPromoCode implements APIResult {
        private CheckPromoCode(final String url, JSONObject data) {
            // new APIService_Volley_JSON(CardRegisterAct.this, this, data, false).execute(url);
            new APIService_Retrofit_JSON(getActivity(), this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY+"/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + url).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(getActivity(), "Message", "" + json.getString("message"), "" +NC.getResources().getString(R.string.ok), "");
                    } else {
                        //alert_view(getActivity(), "Message", "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    CToast.ShowToast(getActivity(), json.getString("message"));
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
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                    }
                });
//                alert_view(getActivity(), "Message", "" + result, "" + getResources().getString(R.string.ok), "");
            }
        }
    }

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id),getActivity());
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
 * this method is used to show the promo code dialog and execute api
 */

    private void ShowPromoDilaog() {

        try {

            final View view = View.inflate(getActivity(), R.layout.alert_view, null);
            mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.show();
            Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id),getActivity());
            FontHelper.applyFont(getActivity(), mDialog.findViewById(R.id.alert_id));
            final TextView titleTxt = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView msgTxt = (TextView) mDialog.findViewById(R.id.message_text);
            msgTxt.setVisibility(View.GONE);
            final EditText promocodeEdt = (EditText) mDialog.findViewById(R.id.forgotmail);
            final Button OK = (Button) mDialog.findViewById(R.id.button_success);
            final Button Cancel = (Button) mDialog.findViewById(R.id.button_failure);
            Cancel.setVisibility(View.GONE);
            promocodeEdt.setVisibility(View.VISIBLE);
            OK.setText("" + NC.getResources().getString(R.string.ok));
            titleTxt.setText("" + NC.getResources().getString(R.string.reg_promocode));
            int maxLengthpromoCode = getResources().getInteger(R.integer.promoMaxLength);
            promocodeEdt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            promocodeEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthpromoCode)});
            InputFilter[] editFilters =promocodeEdt.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] =new InputFilter.AllCaps();
            promocodeEdt.setFilters(newFilters);
            promocodeEdt.setHint("" + NC.getResources().getString(R.string.reg_enterprcode));
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    try {
                        promoCode = promocodeEdt.getText().toString();
                        if (Validation.validations(Validation.ValidateAction.isNullPromoCode, getActivity(), promoCode)) {
                            mDialog.dismiss();
                            JSONObject j = new JSONObject();
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("promo_code", promoCode);
                            String url = "type=check_valid_promocode";
                            new CheckPromoCode(url, j);
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    //

    /**
     * Slider menu used to move from one activity to another activity.
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_text:
                // menu.toggle();
                break;
            case R.id.addmoneyBut:
                if (addmoneyEdt.getText().toString().trim().length() != 0) {
                    addMoney = Integer.parseInt(addmoneyEdt.getText().toString());
                 /*   System.out.println("range1" + range1);
                    System.out.println("range2" + range2);*/
                    if (addMoney < range1 || addMoney > range2)
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + getResources().getString(R.string.amount_between) + " " + SessionSave.getSession("Currency", getActivity()) + range1 + "-" + SessionSave.getSession("Currency", getActivity()) + range2, "" + getResources().getString(R.string.ok), "");
                    else {
                        //Intent monIntent = new Intent(getActivity(), AddMoneyAct.class);
                        AddMoneyFrag addMoneyFrag = new AddMoneyFrag();
                        Bundle bundle = new Bundle();
                        bundle.putString("MONEY", "" + addMoney);
                        bundle.putString("PROMOCODE", promoCode);
                        addMoneyFrag.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, addMoneyFrag).addToBackStack(null).commit();
//                        getActivity().finish();
                    }
                } else {
                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_amount), "" + NC.getResources().getString(R.string.ok), "");
                }
                break;
            case R.id.monoption1:
                monoption1.setBackgroundResource(R.drawable.draw_select_bg);
                monoption2.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption3.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption1.setTextColor(CL.getColor(getActivity(),R.color.button_accept));
                monoption2.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
                monoption3.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
                addmoneyEdt.setText("" + wallet_amount1);
                break;
            case R.id.monoption2:
                monoption1.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption2.setBackgroundResource(R.drawable.draw_select_bg);
                monoption3.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption2.setTextColor(CL.getColor(getActivity(),R.color.button_accept));
                monoption1.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
                monoption3.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
                addmoneyEdt.setText("" + wallet_amount2);
                break;
            case R.id.monoption3:
                monoption1.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption2.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption3.setBackgroundResource(R.drawable.draw_select_bg);
                monoption3.setTextColor(CL.getColor(getActivity(),R.color.button_accept));
                monoption2.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
                monoption1.setTextColor(CL.getColor(getActivity(),R.color.hintcolor));
                addmoneyEdt.setText("" + wallet_amount3);
                break;
            case R.id.procodeTxt:
                ShowPromoDilaog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(this);
        try {
            if (mshowDialog != null && mshowDialog.isShowing()) {
                mshowDialog.dismiss();
                mshowDialog = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}


