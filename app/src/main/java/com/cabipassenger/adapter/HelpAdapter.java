package com.cabipassenger.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.R;

import com.cabipassenger.data.apiData.ApiRequestData;
import com.cabipassenger.data.apiData.HelpResponse;
import com.cabipassenger.data.apiData.StandardResponse;
import com.cabipassenger.interfaces.DialogInterface;
import com.cabipassenger.service.CoreClient;
import com.cabipassenger.service.ServiceGenerator;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by developer on 1/11/16.
 * use to populate the help list directed from trip detail page
 */
public class HelpAdapter extends BaseAdapter implements DialogInterface {
    private final Context context;
    private final LayoutInflater mInflater;
    HelpResponse data;
    String trip_id;

    // constructor
    public HelpAdapter(Context helpadapter, HelpResponse data, String trip_id) {
        // TODO Auto-generated constructor stub
        context = helpadapter;
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.trip_id = trip_id;

    }

    @Override
    public int getCount() {
        return data.details.size();
    }

    @Override
    public Object getItem(int i) {
        return TaxiUtil.mHelplist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    // View holder class member this contains in every row in list.
    class ViewHolder {
        public TextView Place;
        public View img_lineup;
        public View img_linebottom;
        public TextView BookBtn;
        public ImageView place_image, drop_pin;
        private EditText comment;
        private Button pay_submitBtn;
        public LinearLayout help_comments_containter;
        public TextView helptext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        final HelpAdapter.ViewHolder mHolder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.helpfrag_item, viewGroup, false);

            mHolder = new ViewHolder();
            FontHelper.applyFont(context, convertView.findViewById(R.id.help_container));
            mHolder.helptext = (TextView) convertView.findViewById(R.id.helptext);
            mHolder.helptext.setText(data.details.get(position).help_content);
            mHolder.pay_submitBtn = (Button) convertView.findViewById(R.id.pay_submitBtn);
            mHolder.help_comments_containter = (LinearLayout) convertView.findViewById(R.id.help_comments_containter);
            mHolder.comment = (EditText) convertView.findViewById(R.id.comment);
            convertView.setTag(mHolder);
        } else {
            mHolder = (HelpAdapter.ViewHolder) convertView.getTag();
        }
        Colorchange.ChangeColor((ViewGroup) convertView,context);

        mHolder.helptext.setTag(mHolder.help_comments_containter);
        mHolder.pay_submitBtn.setTag(mHolder.comment);
        mHolder.comment.setTag(data.details.get(position).help_id);
        mHolder.helptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout ll = ((LinearLayout) view.getTag());
                if (ll.getVisibility() == View.VISIBLE) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                }
            }
        });
        mHolder.pay_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText tt = ((EditText) view.getTag());
                if (tt.getText().toString().trim().equals(""))
                    Toast.makeText(context, NC.getString(R.string.enter_valid_comment), Toast.LENGTH_SHORT).show();
                else
                    callHelpSubmit(tt.getText().toString(), tt.getTag().toString());
            }
        });
//        mHolder.comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    //got focus
//                    System.out.println("+_________ff");
//                } else {
//                    //lost focus
//                    System.out.println("+_________uff");
//                }
//            }
//        });
//        mHolder.comment.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mHolder.comment.requestFocus();
//                        if(true){
//
//                        }
//                        //iCount++;
//                        break;
//
//                    default:
//                        break;
//                }
//               // if(iCount >= 2) return false;
//                 return true;
//            }
//        });

        return convertView;
    }

    /**
     * Calls api for submit resson for help and user comment
     * @param comment  ---> comments by user.
     * @param help_id  ---> Help reason id.
     */
    private void callHelpSubmit(String comment, String help_id) {
        CoreClient client = new ServiceGenerator(context).createService(CoreClient.class);
        ApiRequestData.HelpSubmit request = new ApiRequestData.HelpSubmit();
        request.setTrip_id(trip_id);
        request.setHelp_id(help_id);
        request.setHelp_comment(comment);
//        TaxiUtil.COMPANY_KEY= SessionSave.getSession("api_key", context);
//        TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", context);
        Call<StandardResponse> response = client.helpSubmit(TaxiUtil.COMPANY_KEY,TaxiUtil.DYNAMIC_AUTH_KEY,request, SessionSave.getSession("Lang", context));
        response.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if (data != null)
                    if (context != null) {
                        if (data.status.toString().trim().equals("1")) {
                            ((AppCompatActivity) context).onBackPressed();
                        }
                        Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onSuccess(View view, Dialog dialog, String resultcode) {
        dialog.dismiss();

    }

    @Override
    public void onFailure(View view, Dialog dialog, String resultcode) {
        dialog.dismiss();

    }

}
