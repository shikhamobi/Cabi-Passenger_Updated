package com.cabipassenger.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.cabipassenger.R;

/**
 * Created by developer on 4/6/16.
 */
public class LocationSearchIconView extends ImageView {
    int a;
    Animation b;
    public LocationSearchIconView(Context context) {
        super(context,null);
    }

    public LocationSearchIconView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public LocationSearchIconView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        b = AnimationUtils.loadAnimation(context, (R.anim.abc_fade_in));
        b.setInterpolator(new LinearInterpolator());
    }

    public void onFinishInflate()
    {
        super.onFinishInflate();
       // ButterKnife.inject(this);
    }
}
