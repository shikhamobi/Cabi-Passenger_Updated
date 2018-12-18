package com.cabipassenger.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cabipassenger.R;


/**
 * Created by developer on 15/3/17.
 */
public class CustomMarker {

    public static Bitmap getMarkerBitmapFromView(String time, Context c, String address) {

//        View customMarkerView = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pickup_custom, null);
//        TextView time_to_reach = (TextView) customMarkerView.findViewById(R.id.time_to_reach);
//      //  TextView pickupIcon = (TextView) customMarkerView.findViewById(R.id.pickup_icons);
//        time_to_reach.setText(time + "\n" + "MIN");
//TextView time_to_reach = (TextView) customMarkerView.findViewById(R.id.time_to_reach);
//        if (time.equals("0"))
//            time_to_reach.setVisibility(View.GONE);
//        else
//            time_to_reach.setVisibility(View.VISIBLE);
//
////        pickupIcon.setVisibility(View.VISIBLE);
////        Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
////        Point size = new Point();
////        display.getSize(size);
////        int width = size.x;
////        Resources r = c.getResources();
////        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, r.getDisplayMetrics());
////        System.out.println("setttingcardView_width" + px+"__"+time);
////        pickupIcon.setWidth((int) ((width / 2) - px));
////        customMarkerView.findViewById(R.id.drop_icon).setVisibility(View.GONE);
//        ((TextView) customMarkerView.findViewById(R.id.location_name)).setText(address);
//        //  markerImageView.setImageResource(resId);
//        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//
//        customMarkerView.layout(0, 0, 0, 0);
//        customMarkerView.buildDrawingCache();
//        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        Drawable drawable = customMarkerView.getBackground();
//        if (drawable != null)
//            drawable.draw(canvas);
//        customMarkerView.draw(canvas);


        View customMarkerView = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pickup_custom, null);
        ((TextView) customMarkerView.findViewById(R.id.location_name)).setText(address);
        TextView time_to_reach = (TextView) customMarkerView.findViewById(R.id.time_to_reach);
        if (time.equals("0"))
            time_to_reach.setVisibility(View.INVISIBLE);
        else
            time_to_reach.setVisibility(View.VISIBLE);
        time_to_reach.setText(Math.round(Double.parseDouble(time)) + "\n" + "MIN");
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);


        return returnedBitmap;
    }

    public static Bitmap getMarkerBitmapFromViewForDrop(String address, Context c) {

        View customMarkerView = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        ((TextView) customMarkerView.findViewById(R.id.location_name)).setText(address);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
}