package com.cabipassenger.util;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by developer on 16/2/17.
 */
public class DrawableJava {

    public static void draw_edittext_bg(View v, int backgroundColor, int borderColor)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape.setColor(backgroundColor);
        shape.setStroke(3, borderColor);
        v.setBackground(shape);
    }
}
