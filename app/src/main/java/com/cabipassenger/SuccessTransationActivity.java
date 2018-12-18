package com.cabipassenger;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;


import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.response.status.StatusResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class SuccessTransationActivity extends MainActivity {

    private TextView mTextView;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successtransaction);
    }*/

    @Override
    public int setLayout() {
        return R.layout.activity_successtransaction;
    }

    @Override
    public void Initialize() {

        Intent intent = getIntent();
        StatusResponse status = (StatusResponse) intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
        ;

        TextView textView = (TextView)findViewById(R.id.text_payment_result);
        textView.setText(textView.getText() +" : " +  status.getAuth().getTranref());

        if(status.getAuth()!= null) {
            status.getAuth().getStatus();   // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
            status.getAuth().getAvs();      /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
            status.getAuth().getCode();     // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
            status.getAuth().getMessage();  // The authorisation or processing error message.
            status.getAuth().getCa_valid();
            status.getAuth().getCardcode(); // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
            status.getAuth().getCardlast4();// The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
            status.getAuth().getCvv();      /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
            status.getAuth().getTranref(); //The payment gateway transaction reference allocated to this request.

            try {
                JSONObject j = new JSONObject();
                j.put("passenger_id", SessionSave.getSession("Id", SuccessTransationActivity.this));
                j.put("creditcard_last4no",  status.getAuth().getCardlast4());
                j.put("trans_refno",  status.getAuth().getTranref());
                j.put("auth_status", status.getAuth().getStatus());
                j.put("money", TaxiUtil.amount);
                j.put("payment_mode", SessionSave.getSession("paymode",SuccessTransationActivity.this));

                j.put("card_typedesc", status.getAuth().getCardcode());
                j.put("promo_code", "");

                final String url = "type=wallet_addmoney";
                new AddMoney(url, j);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
            
            
            


    }

    @Override
    protected void onStart() {
        super.onStart();
     
    }

    public void closeWindow(View view){
        this.finish();
    }

    private class AddMoney implements APIResult {
        private AddMoney(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(SuccessTransationActivity.this, this, data, false).execute(url);
            //new APIService_Retrofit_JSON(SuccessTransationActivity.this, this, true,TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", SuccessTransationActivity.this) + url).execute();

        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {

                TaxiUtil.amount = " ";
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Credit_Card", "" + json.getString("credit_card_status"), SuccessTransationActivity.this);
                        //   startActivity(new Intent(SuccessTransationActivity.this, WalletAct.class));
                        // finish();

                        alert_view(SuccessTransationActivity.this, "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                     finish();
                    } else
                        alert_view(SuccessTransationActivity.this, "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
//                alert_view(SuccessTransationActivity.this, "Message", "" + result, "" + getResources().getString(R.string.ok), "");
              runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(SuccessTransationActivity.this, result);
                    }
                });
            }
        }
    }
}
