package com.cabipassenger.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cabipassenger.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Apply specified font for all text views (including nested ones) in the specified root view.
 */

public class FontHelper {
    //  NEUROPOL.ttf
//DroidSans.ttf
    //HelveticaLT35Thin.ttf

    private static final String TAG = FontHelper.class.getSimpleName();
    public final static String FONT_TYPEFACE = "Roboto_Regular.ttf";
    public final static String FONT_TYPEFACE_Bold = "Roboto_Medium.ttf";
    public static Typeface tf;

//    public static void applyFont(final Context context, final View root, final String fontPath) {
//        try {
//            if (root instanceof ViewGroup) {
//                ViewGroup viewGroup = (ViewGroup) root;
//                int childCount = viewGroup.getChildCount();
//                for (int i = 0; i < childCount; i++)
//                    applyFont(context, viewGroup.getChildAt(i));
//            } else if (root instanceof TextView || root instanceof EditText)
//                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void applyFont(final Context context, final View root) {
        try {
            if (tf == null)
                tf = Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE);
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    applyFont(context, viewGroup.getChildAt(i));
            } else if (root instanceof TextView || root instanceof EditText)
                ((TextView) root).setTypeface(tf);
//            else if(root instanceof TextInputLayout){
//                ((TextInputLayout)root).setTypeface(tf);
//                ViewGroup viewGroup = (ViewGroup) root;
//                int childCount = viewGroup.getChildCount();
//                for (int i = 0; i < childCount; i++)
//                    applyFont(context, viewGroup.getChildAt(i));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static View applyFont(final Context context, final View root, int p) {
//        try {
//            if (root instanceof ViewGroup) {
//                ViewGroup viewGroup = (ViewGroup) root;
//                int childCount = viewGroup.getChildCount();
//                for (int i = 0; i < childCount; i++)
//                    applyFont(context, viewGroup.getChildAt(i), 0);
//            } else if (root instanceof TextView || root instanceof EditText)
//                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return root;
//    }

    public static void applyBold(final Context context, final View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    applyFont(context, viewGroup.getChildAt(i));
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(((TextView) root).getTypeface(), Typeface.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
//        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/Blambot.otf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);
        Context context;

        public MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
            this.context = context;
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = (TextView) super.getView(position, convertView, parent);
            FontHelper.applyFont(context, view);
//            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            FontHelper.applyFont(context, view);
            //view.setTypeface(font);
            return view;
        }
    }

    public static class MySpinnerAdapterWhite extends ArrayAdapter<String> {
        // Initialise custom font, for example:
//        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/Blambot.otf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);
        Context context;

        public MySpinnerAdapterWhite(Context context, int resource, List<String> items) {
            super(context, resource, items);
            this.context = context;
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = (TextView) super.getView(position, convertView, parent);
            view.setBackgroundResource(R.color.white);
            FontHelper.applyFont(context, view);
//            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            FontHelper.applyFont(context, view);
            //view.setTypeface(font);
            return view;
        }
    }

    public static void overrideFonts(Context context, View v) {
        ViewGroup picker;
        try {
            picker = (DatePicker) v;
        } catch (Exception e) {
            picker = (TimePicker) v;
        }
        ViewGroup layout1 = (ViewGroup) picker.getChildAt(0);
        if (picker instanceof TimePicker) {
            if (layout1.getChildAt(1) instanceof NumberPicker) {
                NumberPicker v1 = (NumberPicker) layout1.getChildAt(1);
                final int count = v1.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = v1.getChildAt(i);

                    try {
                        Field wheelpaint_field = v1.getClass().getDeclaredField("mSelectorWheelPaint");
                        wheelpaint_field.setAccessible(true);
                        ((Paint) wheelpaint_field.get(v1)).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                        ((Paint) wheelpaint_field.get(v1)).setColor(context.getResources().getColor(R.color.black));
                        ((EditText) child).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                        v1.invalidate();
                    } catch (Exception e) {
                        //TODO catch.
                        //If java cant find field then it will catch here and app wont crash.
                    }
                }
            }
            if (layout1.getChildAt(2) instanceof NumberPicker) {
                NumberPicker v1 = (NumberPicker) layout1.getChildAt(1);
                final int count = v1.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = v1.getChildAt(i);

                    try {
                        Field wheelpaint_field = v1.getClass().getDeclaredField("mSelectorWheelPaint");
                        wheelpaint_field.setAccessible(true);
                        ((Paint) wheelpaint_field.get(v1)).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                        ((Paint) wheelpaint_field.get(v1)).setColor(context.getResources().getColor(R.color.black));
                        ((EditText) child).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                        v1.invalidate();
                    } catch (Exception e) {
                        //TODO catch.
                        //If java cant find field then it will catch here and app wont crash.
                    }
                }
            }
            if (layout1.getChildAt(0) instanceof NumberPicker) {
                NumberPicker v1 = (NumberPicker) layout1.getChildAt(1);
                final int count = v1.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = v1.getChildAt(i);

                    try {
                        Field wheelpaint_field = v1.getClass().getDeclaredField("mSelectorWheelPaint");
                        wheelpaint_field.setAccessible(true);
                        ((Paint) wheelpaint_field.get(v1)).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                        ((Paint) wheelpaint_field.get(v1)).setColor(context.getResources().getColor(R.color.black));
                        ((EditText) child).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                        v1.invalidate();
                    } catch (Exception e) {
                        //TODO catch.
                        //If java cant find field then it will catch here and app wont crash.
                    }
                }
            }
        }
        ViewGroup layout = (ViewGroup) layout1.getChildAt(0);
//        if(layout1.getChildAt(0) instanceof LinearLayout)
//            layout = (LinearLayout) layout1.getChildAt(0);
//        else if(layout1.getChildAt(0) instanceof RelativeLayout)
//            layout = (RelativeLayout) layout1.getChildAt(0);
        if (layout != null)
            for (int j = 0; j < 3; j++) {
                try {
                    if (layout.getChildAt(j) instanceof NumberPicker) {
                        NumberPicker v1 = (NumberPicker) layout.getChildAt(j);
                        final int count = v1.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View child = v1.getChildAt(i);

                            try {
                                Field wheelpaint_field = v1.getClass().getDeclaredField("mSelectorWheelPaint");
                                wheelpaint_field.setAccessible(true);
                                ((Paint) wheelpaint_field.get(v1)).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                                ((Paint) wheelpaint_field.get(v1)).setColor(context.getResources().getColor(R.color.black));
                                ((EditText) child).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_TYPEFACE));
                                v1.invalidate();
                            } catch (Exception e) {
                                //TODO catch.
                                //If java cant find field then it will catch here and app wont crash.
                            }
                        }
                    }
                } catch (Exception e) {
                    //TODO catch.
                    //If java cant find field then it will catch here and app wont crash.
                }
            }

    }

    public static String convertfromArabic(String value) {
        String newValue = ((((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", ".")));
        newValue = newValue.replace("\",", "*&^");
        newValue = newValue.replace(",", ".");
        newValue = newValue.replace("*&^", "\",");
        return newValue;
    }
}
