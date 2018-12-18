package com.cabipassenger.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * this class shows the alert to cancel the trip
 */
public class ReasonListFrag extends DialogFragment implements View.OnClickListener {
    ImageView img1, img2, img3, img4, img5, img6;
    TextView txt1, txt2, txt3, txt4, txt5, txt6;
    public String sReason;
    public Dialog r_mDialog, cvv_Dialog;
    private Dialog alertmDialog;
    public int mTripid, from = 1;
    View v;
    private ViewGroup lay_reason_six, lay_reason_one,
            lay_reason_three,
            lay_reason_two,
            lay_reason_four,
            lay_reason_five, reasonotherlay;
    EditText reasonother;
    Button reson_submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.reason_list, container, false);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Bundle bb = getArguments();
        if (bb != null) {
            mTripid = Integer.parseInt(bb.getString("trip_id"));
            from = Integer.parseInt(bb.getString("From"));
        } else if(!SessionSave.getSession("trip_id", getActivity()).trim().equals("")){
            mTripid = Integer.parseInt(SessionSave.getSession("trip_id", getActivity()));
        }
        img1 = (ImageView) v.findViewById(R.id.check1);
        img2 = (ImageView) v.findViewById(R.id.check2);
        img3 = (ImageView) v.findViewById(R.id.check3);
        img4 = (ImageView) v.findViewById(R.id.check4);
        img5 = (ImageView) v.findViewById(R.id.check5);
        img6 = (ImageView) v.findViewById(R.id.check6);

        txt1 = (TextView) v.findViewById(R.id.reason1);
        txt2 = (TextView) v.findViewById(R.id.reason2);
        txt3 = (TextView) v.findViewById(R.id.reason3);
        txt4 = (TextView) v.findViewById(R.id.reason4);
        txt5 = (TextView) v.findViewById(R.id.reason5);
        txt6 = (TextView) v.findViewById(R.id.reason6);

        lay_reason_one = (ViewGroup) v.findViewById(R.id.lay_reason_one);
        lay_reason_three = (ViewGroup) v.findViewById(R.id.lay_reason_three);
        lay_reason_two = (ViewGroup) v.findViewById(R.id.lay_reason_two);
        lay_reason_four = (ViewGroup) v.findViewById(R.id.lay_reason_four);
        lay_reason_five = (ViewGroup) v.findViewById(R.id.lay_reason_five);
        lay_reason_six = (ViewGroup) v.findViewById(R.id.lay_reason_six);
        reasonotherlay = (ViewGroup) v.findViewById(R.id.reasonotherlay);
        reasonother = (EditText) v.findViewById(R.id.reasonother);
        reson_submit = (Button) v.findViewById(R.id.reson_submit);
        ClickMethod();

        return v;
    }

    public void ClickMethod() {

        lay_reason_one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUI(1);
                sReason = txt1.getText().toString();

                //if (SessionSave.getSession("Credit_Card", getActivity()).equals("1") && getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof OnGoingFrag)
                  if(false)
                    getCVV();
                else {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("passenger_log_id", mTripid);
                        j.put("travel_status", "4");
                        j.put("remarks", sReason);
                        j.put("pay_mod_id", "2");
                        j.put("creditcard_cvv", "");
                        new Cancel_afterTrip("type=cancel_trip", j);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        lay_reason_two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUI(2);
                sReason = txt2.getText().toString();

               // if (SessionSave.getSession("Credit_Card", getActivity()).equals("1") && getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof OnGoingFrag)
                if(false)
                    getCVV();
                else {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("passenger_log_id", mTripid);
                        j.put("travel_status", "4");
                        j.put("remarks", sReason);
                        j.put("pay_mod_id", "2");
                        j.put("creditcard_cvv", "");
                        new Cancel_afterTrip("type=cancel_trip", j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        lay_reason_three.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUI(3);
                sReason = txt3.getText().toString();

                //if (SessionSave.getSession("Credit_Card", getActivity()).equals("1") && getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof OnGoingFrag)
                if(false)
                    getCVV();
                else {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("passenger_log_id", mTripid);
                        j.put("travel_status", "4");
                        j.put("remarks", sReason);
                        j.put("pay_mod_id", "2");
                        j.put("creditcard_cvv", "");
                        new Cancel_afterTrip("type=cancel_trip", j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        lay_reason_four.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUI(4);
                sReason = txt4.getText().toString();

               // if (SessionSave.getSession("Credit_Card", getActivity()).equals("1") && getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof OnGoingFrag)
                if(false)
                    getCVV();
                else {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("passenger_log_id", mTripid);
                        j.put("travel_status", "4");
                        j.put("remarks", sReason);
                        j.put("pay_mod_id", "2");
                        j.put("creditcard_cvv", "");
                        new Cancel_afterTrip("type=cancel_trip", j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        lay_reason_five.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUI(5);
                sReason = txt5.getText().toString();

                //if (SessionSave.getSession("Credit_Card", getActivity()).equals("1") && getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof OnGoingFrag)
                if(false)
                    getCVV();
                else {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("passenger_log_id", mTripid);
                        j.put("travel_status", "4");
                        j.put("remarks", sReason);
                        j.put("pay_mod_id", "2");
                        j.put("creditcard_cvv", "");
                        new Cancel_afterTrip("type=cancel_trip", j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        lay_reason_six.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUI(6);

            }
        });

        reson_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sReason = reasonother.getText().toString();
                if (!sReason.trim().equals("")) {
                    //if (SessionSave.getSession("Credit_Card", getActivity()).equals("1") && getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof OnGoingFrag)
                    if(false)
                        getCVV();
                    else {
                        try {
                            JSONObject j = new JSONObject();
                            j.put("passenger_log_id", mTripid);
                            j.put("travel_status", "4");
                            j.put("remarks", sReason);
                            j.put("pay_mod_id", "2");
                            j.put("creditcard_cvv", "");
                            new Cancel_afterTrip("type=cancel_trip", j);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Toast.makeText(getActivity(), NC.getString(R.string.enter_valid_comment), Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*switch (v.getId()) {
            case R.id.check1:
                UpdateUI(1);
                break;
            case R.id.check2:
                UpdateUI(2);
                break;
            case R.id.check3:
                UpdateUI(3);
                break;
            case R.id.check4:
                UpdateUI(4);
                break;
            case R.id.check5:
                UpdateUI(5);
                break;
            case R.id.check6:
                UpdateUI(6);
                break;
            default:
                UpdateUI(0);


        }*/

    }

    /**
     * this method gets the cvv pin from user
     */

    public void getCVV() {

       // System.out.println("creditcard status" + SessionSave.getSession("Credit_Card", getActivity()));
        try {
            if (SessionSave.getSession("Credit_Card", getActivity()).equalsIgnoreCase("1")) {
                //getDialog().dismiss();
                v.setVisibility(View.GONE);
                final View view = View.inflate(getActivity(), R.layout.forgot_popup, null);
                cvv_Dialog = new Dialog(getActivity(), R.style.NewDialog);
                FontHelper.applyFont(getActivity(), view);
                cvv_Dialog.setContentView(view);
                cvv_Dialog.setCancelable(true);
                cvv_Dialog.show();
                final TextView title = (TextView) cvv_Dialog.findViewById(R.id.f_textview);
                final EditText cvv = (EditText) cvv_Dialog.findViewById(R.id.forgotmail);
                final Button OK = (Button) cvv_Dialog.findViewById(R.id.okbtn);
                final Button Cancel = (Button) cvv_Dialog.findViewById(R.id.cancelbtn);
                title.setText("" + NC.getResources().getString(R.string.enter_the_cvv));
                cvv.setHint("" + NC.getResources().getString(R.string.enter_the_cvv));
                cvv.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(4);
                cvv.setFilters(filterArray);
                OK.setOnClickListener(new View.OnClickListener() {
                    private String sCvv;

                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        try {
                            sCvv = cvv.getText().toString();
                            if (sCvv.trim().length() == 0 || sCvv.trim().length() > 4)
                                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_the_valid_CVV), "" + getResources().getString(R.string.ok), "");
                            else {
                                JSONObject j = new JSONObject();
                                j.put("passenger_log_id", mTripid);
                                j.put("travel_status", "4");
                                j.put("remarks", sReason);
                                j.put("pay_mod_id", "2");
                                j.put("creditcard_cvv", sCvv);
                                new Cancel_afterTrip("type=cancel_trip", j);


                                //  ((OnGoingFrag)getParentFragment()).CancelTrip(mTripid,sReason);

                                cvv.setText("");

                                cvv_Dialog.dismiss();

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
                        //getDialog().dismiss();
                        cvv_Dialog.dismiss();
                        getDialog().dismiss();
                    }
                });
            } else {
                r_mDialog.dismiss();
                JSONObject j = new JSONObject();
                j.put("passenger_log_id", mTripid);
                j.put("travel_status", "4");
                j.put("remarks", sReason);
                j.put("pay_mod_id", "2");
                j.put("creditcard_cvv", "");
                new Cancel_afterTrip("type=cancel_trip", j);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * this method is used to check the image which is selected
     */

    public void UpdateUI(int num) {
        img1.setImageResource(R.drawable.tick_unfocus);
        img2.setImageResource(R.drawable.tick_unfocus);
        img3.setImageResource(R.drawable.tick_unfocus);
        img4.setImageResource(R.drawable.tick_unfocus);
        img5.setImageResource(R.drawable.tick_unfocus);
        img6.setImageResource(R.drawable.tick_unfocus);
        reasonotherlay.setVisibility(View.GONE);

        switch (num) {
            case 1:
                img1.setImageResource(R.drawable.tick_focus);
                break;

            case 2:
                img2.setImageResource(R.drawable.tick_focus);
                break;

            case 3:
                img3.setImageResource(R.drawable.tick_focus);
                break;

            case 4:
                img4.setImageResource(R.drawable.tick_focus);
                break;


            case 5:
                img5.setImageResource(R.drawable.tick_focus);
                break;

            case 6:
                img6.setImageResource(R.drawable.tick_focus);
                reasonotherlay.setVisibility(View.VISIBLE);
                break;

            default:
                img1.setImageResource(R.drawable.tick_unfocus);
                img2.setImageResource(R.drawable.tick_unfocus);
                img3.setImageResource(R.drawable.tick_unfocus);
                img4.setImageResource(R.drawable.tick_unfocus);
                img5.setImageResource(R.drawable.tick_unfocus);
                img6.setImageResource(R.drawable.tick_unfocus);

        }

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
     * this class call the api after the trip is cancelled
     */

    private class Cancel_afterTrip implements APIResult {

        String alert_message = "";

        public Cancel_afterTrip(String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + url).execute();
            // new APIService_Volley_JSON(getActivity(), getActivity(), data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                   // Log.d("result", "result" + result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("trip_id", "", getActivity());
                        SessionSave.saveSession("TaxiStatus", "", getActivity());
                        alert_message = json.getString("message") + "\n" + NC.getResources().getString(R.string.canceled_amount) + " " + SessionSave.getSession("Currency", getActivity()) + json.getString("cancellation_amount") + "\n" + getResources().getString(R.string.canceled_from) + " " + json.getString("cancellation_from");
                        Intent intent = new Intent(getContext(), GetPassengerUpdate.class);
                        getContext().stopService(intent);
                        if (from == 2) {
                            //  dismiss();
                            TaxiUtil.close = 1;
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new TripHistory()).commit();
                        } else {
//                            Intent i_gettaxi = new Intent(getActivity(), MainHomeFragmentActivity.class);
//                            i_gettaxi.putExtra("alert_message", alert_message);
//                            startActivity(i_gettaxi);
                            Toast.makeText(getActivity(), alert_message, Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commitAllowingStateLoss();
                            //  getActivity().finish();
                        }


                    } else if (json.getInt("status") == 2) {
                        SessionSave.saveSession("trip_id", "", getActivity());
                        SessionSave.saveSession("TaxiStatus", "", getActivity());
                        alert_message = json.getString("message");
                        Intent intent = new Intent(getContext(), GetPassengerUpdate.class);
                        getContext().stopService(intent);

                        if (from == 2) {
                            //   dismiss();
                            TaxiUtil.close = 1;
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new TripHistory()).commit();
                        } else {
                            Intent i_gettaxi = new Intent(getActivity(), MainHomeFragmentActivity.class);
                            i_gettaxi.putExtra("alert_message", alert_message);
//                            startActivity(i_gettaxi);
//                           getActivity().finish();
                            Toast.makeText(getActivity(), alert_message, Toast.LENGTH_LONG).show();

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commit();
                            //    getActivity().finish();
                        }
                    } else if (json.getInt("status") == -1) {
                        CToast.ShowToast(getActivity(), json.getString("message"));
                        SessionSave.saveSession("TaxiStatus", "", getActivity());
                        SessionSave.saveSession("trip_id", "", getActivity());
                        alert_message = json.getString("message");
                        Intent intent = new Intent(getContext(), GetPassengerUpdate.class);
                        getContext().stopService(intent);

                        if (from == 2) {
                            TaxiUtil.close = 1;
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new TripHistory()).commit();
                        } else {
                            Intent i_gettaxi = new Intent(getActivity(), MainHomeFragmentActivity.class);
                            i_gettaxi.putExtra("alert_message", alert_message);
                            Toast.makeText(getActivity(), alert_message, Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commit();
                            //     getActivity().finish();
                        }
                    } else if (json.getInt("status") == 3) {
                        CToast.ShowToast(getActivity(), json.getString("message"));
                    } else {
                        CToast.ShowToast(getActivity(), json.getString("message"));
                    }
                    // if (from == 2) {
                    if (getDialog() != null)
                        dismiss();
//                    } else {
//
//
//                    }

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
