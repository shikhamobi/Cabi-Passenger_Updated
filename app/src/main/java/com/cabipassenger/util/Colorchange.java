package com.cabipassenger.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;

/**
 * this class is used to set the dynamic srings and colors commonly and used in whole project
 */
public class Colorchange {

    public static void ChangeColor(ViewGroup parentLayout, Context cc) {
        for (int count = 0; count < parentLayout.getChildCount(); count++) {
            View view = parentLayout.getChildAt(count);
            int color =0;
            Drawable background = view.getBackground();

          //  Log.d("background","background"+background);

            if (background instanceof ShapeDrawable) {
                // cast to 'ShapeDrawable'
                ShapeDrawable shapeDrawable = (ShapeDrawable) background;
                shapeDrawable.getPaint().setColor(CL.getResources().getColor(cc,R.color.com_facebook_button_background_color));
                color = ((ColorDrawable) background).getColor();

            } else if (background instanceof GradientDrawable) {
                // cast to 'GradientDrawable'
                GradientDrawable gradientDrawable = (GradientDrawable) background;

                gradientDrawable.setStroke(1,CL.getResources().getColor(cc,R.color.linebottom_dark));
             //   DrawableJava.draw_edittext_bg(view, CL.getColor(cc,R.color.white),CL.getColor(cc,R.color.header_bgcolor));
            }else if(background instanceof LayerDrawable){

                try {
                    LayerDrawable layerDrawable = (LayerDrawable) background;
                    GradientDrawable selectedItem = (GradientDrawable) layerDrawable.getDrawable(0);
                   // DrawableJava.draw_edittext_bg(view, CL.getColor(cc,R.color.white),CL.getColor(cc,R.color.header_bgcolor));
                    if(layerDrawable.getId(0)>0){
                        String name= cc.getResources().getResourceEntryName(layerDrawable.getId(0));
                       // Log.d("name","name"+name);
                        selectedItem.setStroke(2, CL.getResources().getColor(cc,R.color.button_accept));

                    }else{
                        selectedItem.setStroke(1, CL.getResources().getColor(cc,R.color.linebottom_dark));

                    }
                }catch (Exception e){

                }

            }else if(background instanceof StateListDrawable){
//                try {
//                    StateListDrawable gradientDrawable = (StateListDrawable) background;
//                    DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) gradientDrawable.getConstantState();
//                    Drawable[] children = drawableContainerState.getChildren();
//                    GradientDrawable selectedItem = (GradientDrawable) children[0];
//                    GradientDrawable unselectedItem = (GradientDrawable) children[1];
//                    GradientDrawable unselectedItem2 = (GradientDrawable) children[2];
//                    selectedItem.setColor(cc.getResources().getColor(R.color.button_reject));
//                    unselectedItem.setColor(cc.getResources().getColor(R.color.button_reject));
//                    unselectedItem2.setColor(cc.getResources().getColor(R.color.button_accept));
//
//                }catch (Exception e){
//
//                }
            }
            else if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//                int intColor11 = cc.getResources().getColor(R.color.linebottom_light);
//                String hexColor1 = String.format("#%06X", (0xFFFFFF & intColor11));


                if (view instanceof EditText || view instanceof TextView || view instanceof Button ) {
                    try {
                        String tt="";
                        String text="";

                        //  String tt = getStringResourceByName(((EditText) view).getHint().toString(), cc);
                        //  Field[] fields = R.string.class.getDeclaredFields(); // or Field[] fields = R.string.class.getFields();
//String txt=
//                   for (int  i =0; i < fields.length; i++) {
//                       int resId = fields[i];
                        //str += fields[i].getName() + " = ";
                        //   if (resId != 0) {
                        //  str += cc.getResources().getString(resId);
                        if(view instanceof EditText) {
                            EditText et=(EditText)(view);

                            if(et.getText().toString().trim().equals("")){
                                text=et.getHint().toString();
                                if(SplashActivity.fields_value.indexOf(text)!=-1){
                                    //  ((EditText)view).setTextColor(cc.getResources().getColor(R.color.hintcolor));
                                    String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                //    System.out.println("keyvalue__"+keyValue+"___"+NC.nfields_byName.get(keyValue));
                                    et.setHint(NC.nfields_byName.get(keyValue));


                                }
                            }
                            //  et.setT
                            else{
                                text=et.getText().toString();
                                if(SplashActivity.fields_value.indexOf(text)!=-1){
                                    //   ((EditText)view).setTextColor(cc.getResources().getColor(R.color.black));
                                    String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                  //  System.out.println("keyvalue__texttt"+keyValue+"___"+NC.nfields_byName.get(keyValue));
                                    et.setText(NC.nfields_byName.get(keyValue));

                                }
                            }
//                               if (((EditText) view).getHint().toString().equals(cc.getResources().getString(resId))) {
//                                   tt = fields[i].getName();
//                                   break;
//                               }
                        }else {

                            if(view instanceof TextView && !(view instanceof EditText)){

                                TextView tv=(TextView)(view);
                                text=(tv).getText().toString();




                                switch(hexColor) {
                                    case "#F5F5F5" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                        break;
                                    case "#404041" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.header_text));
                                        break;
                                    case "#C2C2C2" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                        break;
                                    case "#646464" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                        break;
                                    case "#666666" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                        break;
                                    case "#48BF27" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                        break;
                                    case "#00000000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.transparent));
                                        break;
                                    case "#66000000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                        break;
                                    case "#EE3324" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.button_accept));
                                        break;
                                    case "#8E1F16" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.button_reject));
                                        break;
                                    case "#00BFFF" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                        break;
                                    case "#ECBE2A" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                        break;
                                    case "#FE0000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                        break;
                                    case "#FF6666" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                        break;
                                    case "#A2A2A2" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                        break;
                                    case "#ffffff" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                        break;
                                    case "#FFFFFF" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.white));
                                        break;
                                    case "#000000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.black));
                                        break;
                                    case "#1C1C24" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                        break;
                                    case "#333333" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                        break;
                                    case "#00000F" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                    // You can have any number of case statements.

                                }
                                if(SplashActivity.fields_value.indexOf(text)>0){
                                    String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                  //  System.out.println("keyvalue__texttt"+keyValue+"___"+NC.nfields_byName.get(keyValue));
                                    tv.setText(NC.nfields_byName.get(keyValue));
                                    String hexColor11 = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));
                                    switch(hexColor11) {
                                        case "#F5F5F5":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                            break;
                                        case "#404041":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                            break;
                                        case "#C2C2C2":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                            break;
                                        case "#646464":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                            break;
                                        case "#666666":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                            break;
                                        case "#48BF27":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                            break;
                                        case "#00000000":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                            break;
                                        case "#66000000":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                            break;
                                        case "#EE3324":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                            break;
                                        case "#8E1F16":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                            break;
                                        case "#00BFFF":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                            break;
                                        case "#ECBE2A":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                            break;
                                        case "#FE0000":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                            break;
                                        case "#FF6666":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                            break;
                                        case "#A2A2A2":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                            break;
                                        case "#ffffff":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                            break;
                                        case "#FFFFFF":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.white));
                                            break;
                                        case "#000000":
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.black));
                                            break;
                                        case "#1C1C24" :
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                            break;
                                        case "#333333" :
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                            break;
                                        case "#00000F" :
                                            tv.setTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                        // You can have any number of case statements.

                                    }


                                }
                            }
                            else{
                                Button tv=(Button)(view);
                                //  String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                text=(tv).getText().toString();

                                String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                               // System.out.println("keyvalue__"+keyValue+"___"+NC.nfields_byName.get(keyValue));
                                tv.setText(NC.nfields_byName.get(keyValue));

                                String hexColor11 = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));

                                switch(hexColor) {
                                    case "#F5F5F5" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                        break;
                                    case "#404041" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.header_text));
                                        break;
                                    case "#C2C2C2" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                        break;
                                    case "#646464" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                        break;
                                    case "#666666" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                        break;
                                    case "#48BF27" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                        break;
                                    case "#00000000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.transparent));
                                        break;
                                    case "#66000000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                        break;
                                    case "#EE3324" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.button_accept));
                                        break;
                                    case "#8E1F16" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.button_reject));
                                        break;
                                    case "#00BFFF" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                        break;
                                    case "#ECBE2A" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                        break;
                                    case "#FE0000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                        break;
                                    case "#FF6666" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                        break;
                                    case "#A2A2A2" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                        break;
                                    case "#ffffff" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                        break;
                                    case "#FFFFFF" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.white));
                                        break;
                                    case "#000000" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.black));
                                        break;
                                    case "#1C1C24" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                        break;
                                    case "#333333" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                        break;
                                    case "#00000F" :
                                        tv.setBackgroundColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                    // You can have any number of case statements.



                                }


                                switch(hexColor11) {
                                    case "#F5F5F5" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                        break;
                                    case "#404041" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                        break;
                                    case "#C2C2C2" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                        break;
                                    case "#646464" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                        break;
                                    case "#666666" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                        break;
                                    case "#48BF27" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                        break;
                                    case "#00000000" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                        break;
                                    case "#66000000" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                        break;
                                    case "#EE3324" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                        break;
                                    case "#8E1F16" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                        break;
                                    case "#00BFFF" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                        break;
                                    case "#ECBE2A" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                        break;
                                    case "#FE0000" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                        break;
                                    case "#FF6666" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                        break;
                                    case "#A2A2A2" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                        break;
                                    case "#ffffff" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                        break;
                                    case "#FFFFFF" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.white));
                                        break;
                                    case "#000000" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.black));
                                        break;
                                    case "#1C1C24" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                        break;
                                    case "#333333" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                        break;
                                    case "#00000F" :
                                        tv.setTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                    // You can have any number of case statements.

                                }

                                //tv.setText(NC.nfields_byName.get(text));
                            }
//                               if(((TextView) view).getText().toString().equals(cc.getResources().getString(resId))){
//                                   tt = fields[i].getName();
//                                   break;
//                               }
                        }

                        if(SplashActivity.fields_value.contains(text)){
                            //  System.out.println("nagaaaaaaa___"+SplashActivity.fields_value.indexOf(text));
                       //     System.out.println("keyvalue__"+SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text)));

                        }


                        // }
                        //  str += "\n";
                        //  }

                        //  Log.d("tt", "tt" + tt+"=="+fields.length);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

              //  Log.d("hexColor","hexColor"+hexColor+"==="+color);

                switch(hexColor) {
                    case "#F5F5F5" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                        break;
                    case "#404041" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.header_text));
                        break;
                    case "#C2C2C2" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                        break;
                    case "#646464" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                        break;
                    case "#666666" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.hintcolor));
                        break;
                    case "#48BF27" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                        break;
                    case "#000000" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.transparent));
                        break;
                    case "#1C1C24" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                        break;
                    case "#333333" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                        break;
                    case "#00000F" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                    case "#660000" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                        break;
                    case "#EE3324" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.button_accept));
                        break;
                    case "#8E1F16" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.button_reject));
                        break;
                    case "#00BFFF" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.paymentcard));
                        break;
                    case "#ECBE2A" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                        break;
                    case "#FE0000" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                        break;
                    case "#FF6666" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                        break;
                    case "#A2A2A2" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                        break;
                    case "#ffffff" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                        break;
                    case "#FFFFFF" :
                        view.setBackgroundColor(CL.getResources().getColor(cc,R.color.white));
                        break;
//                    case "#000000" :
//                        view.setBackgroundColor(cc.getResources().getColor(cc,R.color.transparent));
//                        break;
                    // You can have any number of case statements.



                }






            }



//            switch(hexColor) {
//                case "#C2C2C2" :
//                    view.setBackgroundColor(cc.getResources().getColor(cc,R.color.green));
//                    break;
//
//            }

//            if (view instanceof EditText) {
//            //    ((EditText) view).setTextColor(cc.getResources().getColor(cc,R.color.green));
//            } else if (view instanceof ViewGroup) {
//
//                ChangeColor((ViewGroup) view,cc);
//            }


            if (view instanceof EditText || view instanceof TextView || view instanceof Button ) {
                try {
                    String tt="";
                    String text="";

                    //  String tt = getStringResourceByName(((EditText) view).getHint().toString(), cc);
                    //  Field[] fields = R.string.class.getDeclaredFields(); // or Field[] fields = R.string.class.getFields();
//String txt=
//                   for (int  i =0; i < fields.length; i++) {
//                       int resId = fields[i];
                    //str += fields[i].getName() + " = ";
                    //   if (resId != 0) {
                    //  str += cc.getResources().getString(resId);
                    if(view instanceof EditText) {
                        EditText tv=(EditText)(view);

                        if(tv!=null && tv.getHint()!= null && !tv.getHint().toString().trim().equals("")){
                            text=tv.getHint().toString();
                            if(SplashActivity.fields_value.indexOf(text)!=-1) {
                                String keyValue = SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                ((EditText) view).setHint(NC.nfields_byName.get(keyValue));


                            } }else{
                               // System.out.println("keyvalue__");
                            }
                            String hexColor = String.format("#%06X", (0xFFFFFF & tv.getCurrentHintTextColor()));


                            switch(hexColor) {
                                case "#F5F5F5" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                    break;
                                case "#404041" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                    break;
                                case "#C2C2C2" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                    break;
                                case "#646464" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                    break;
                                case "#666666" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                    break;
                                case "#48BF27" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                    break;
                                case "#00000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                    break;
                                case "#66000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                    break;
                                case "#EE3324" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                    break;
                                case "#00BFFF" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                    break;
                                case "#ECBE2A" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                    break;
                                case "#FE0000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                    break;
                                case "#FF6666" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                    break;
                                case "#A2A2A2" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                    break;
                                case "#ffffff" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                    break;
                                case "#FFFFFF" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.white));
                                    break;
                                case "#000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.black));
                                    break;
                                case "#1C1C24" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                    break;
                                case "#333333" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                    break;
                                case "#00000F" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                // You can have any number of case statements.




                            }











                            //  et.setHint(NC.nfields_byName.get(text));


                        //  et.setT
                        //else{

                            if(!tv.getText().toString().trim().equals("")) {
                                text = tv.getText().toString();
                                if (SplashActivity.fields_value.indexOf(text) != -1) {
                                    String keyValue = SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                    ((EditText) view).setText(NC.nfields_byName.get(keyValue));
                                }
                            }
                            String hexColort = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));

                            switch(hexColort) {
                                case "#F5F5F5" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                    break;
                                case "#404041" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                    break;
                                case "#C2C2C2" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                    break;
                                case "#646464" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                    break;
                                case "#666666" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                    break;
                                case "#48BF27" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                    break;
                                case "#00000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                    break;
                                case "#66000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                    break;
                                case "#EE3324" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                    break;
                                case "#00BFFF" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                    break;
                                case "#ECBE2A" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                    break;
                                case "#FE0000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                    break;
                                case "#FF6666" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                    break;
                                case "#A2A2A2" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                    break;
                                case "#ffffff" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                    break;
                                case "#FFFFFF" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.white));
                                    break;
                                case "#000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.black));
                                    break;
                                case "#1C1C24" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                    break;
                                case "#333333" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                    break;
                                case "#00000F" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                // You can have any number of case statements.




                            }

                       // }
//                               if (((EditText) view).getHint().toString().equals(cc.getResources().getString(resId))) {
//                                   tt = fields[i].getName();
//                                   break;
//                               }
                        // et.setText(NC.nfields_byName.get(text));
                    }else {

                        if(view instanceof TextView){

                            TextView tv=(TextView)(view);
                            text=(tv).getText().toString();
                            if(SplashActivity.fields_value.indexOf(text)!=-1){
                                String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                              //  System.out.println("keyvalue__"+keyValue+"___"+NC.nfields_byName.get(keyValue));
                                tv.setText(NC.nfields_byName.get(keyValue));


                            }

                            String hexColor = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));


                            switch(hexColor) {
                                case "#F5F5F5" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                    break;
                                case "#404041" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                    break;
                                case "#C2C2C2" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                    break;
                                case "#646464" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                    break;
                                case "#666666" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                    break;
                                case "#48BF27" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                    break;
                                case "#00000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                    break;
                                case "#66000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                    break;
                                case "#EE3324" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                    break;
                                case "#00BFFF" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                    break;
                                case "#ECBE2A" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                    break;
                                case "#FE0000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                    break;
                                case "#FF6666" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                    break;
                                case "#A2A2A2" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                    break;
                                case "#ffffff" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                    break;
                                case "#FFFFFF" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.white));
                                    break;
                                case "#000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.black));
                                    break;
                                case "#1C1C24" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                    break;
                                case "#333333" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                    break;
                                case "#00000F" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                // You can have any number of case statements.




                            }

                            String hexColort = String.format("#%06X", (0xFFFFFF & tv.getCurrentHintTextColor()));


                            switch(hexColort) {
                                case "#F5F5F5" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                    break;
                                case "#404041" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                    break;
                                case "#C2C2C2" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                    break;
                                case "#646464" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                    break;
                                case "#666666" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                    break;
                                case "#48BF27" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                    break;
                                case "#00000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                    break;
                                case "#66000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                    break;
                                case "#EE3324" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                    break;
                                case "#00BFFF" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                    break;
                                case "#ECBE2A" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                    break;
                                case "#FE0000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                    break;
                                case "#FF6666" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                    break;
                                case "#A2A2A2" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                    break;
                                case "#ffffff" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                    break;
                                case "#FFFFFF" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.white));
                                    break;
                                case "#000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.black));
                                    break;
                                case "#1C1C24" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                    break;
                                case "#333333" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                    break;
                                case "#00000F" :
                                    tv.setHintTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                    // You can have any number of case statements.




                            }

                        }
                        else{
                            Button tv=(Button)(view);
                            text=(tv).getText().toString();
                            if(SplashActivity.fields_value.indexOf(text)!=-1){
                                String keyValue=SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text));
                                tv.setText(NC.nfields_byName.get(keyValue));}

                            String hexColor = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));


                            switch(hexColor) {
                                case "#F5F5F5" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.header_bgcolor));
                                    break;
                                case "#404041" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.header_text));
                                    break;
                                case "#C2C2C2" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_light));
                                    break;
                                case "#646464" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.linebottom_dark));
                                    break;
                                case "#666666" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.hintcolor));
                                    break;
                                case "#48BF27" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.pickupheadertext));
                                    break;
                                case "#00000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.transparent));
                                    break;
                                case "#66000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.semi_transparent));
                                    break;
                                case "#EE3324" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.button_reject));
                                    break;
                                case "#00BFFF" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.paymentcard));
                                    break;
                                case "#ECBE2A" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.MatThemectrlActive));
                                    break;
                                case "#F0000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.cancelbtntxtcolor));
                                    break;
                                case "#FF6666" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.buttonnormaltheme));
                                    break;
                                case "#A2A2A2" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.textviewcolor_light));
                                    break;
                                case "#ffffff" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.buttontextcolor));
                                    break;
                                case "#FFFFFF" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.white));
                                    break;
                                case "#000000" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.black));
                                    break;
                                case "#1C1C24" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_bg));
                                    break;
                                case "#333333" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.home_drawer_head_bg));
                                    break;
                                case "#00000F" :
                                    tv.setTextColor(CL.getResources().getColor(cc,R.color.edittextcolor));
                                // You can have any number of case statements.




                            }

                            //tv.setText(NC.nfields_byName.get(text));
                        }
//                               if(((TextView) view).getText().toString().equals(cc.getResources().getString(resId))){
//                                   tt = fields[i].getName();
//                                   break;
//                               }
                    }

                    if(SplashActivity.fields_value.contains(text)){
                        //  System.out.println("nagaaaaaaa___"+SplashActivity.fields_value.indexOf(text));
                     //   System.out.println("keyvalue__"+SplashActivity.fields.get(SplashActivity.fields_value.indexOf(text)));

                    }


                    // }
                    //  str += "\n";
                    //  }

                    //  Log.d("tt", "tt" + tt+"=="+fields.length);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (view instanceof ViewGroup) {

                ChangeColor((ViewGroup) view,cc);
            }





        }
    }


    private static String getStringResourceByName(String aString,Context cc) {
      //  Log.d("tt","tt"+aString);
//        String packageName = cc.getPackageName();
//        int resId = cc.getResources().getIdentifier(aString, "string", packageName);
        String resId = cc.getResources().getString(cc.getResources().getIdentifier(aString, "string", cc.getPackageName()));

        return resId;
    }






}