package com.cabipassenger.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.cabipassenger.R;

/**
 * This class used to show the dot progress in taxi search page with green and gray color dots.
 */
public class DotsProgressBarSearch extends View
{
	private float mRadius;
	private Paint mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Handler mHandler = new Handler();
	private int mIndex = 0;
	private int widthSize, heightSize;
	private int margin = 4;
	private int mDotCount = 3;

	public DotsProgressBarSearch(Context context)
	{
		super(context);
		init(context);
	}

	public DotsProgressBarSearch(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public DotsProgressBarSearch(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context)
	{
		mRadius = context.getResources().getDimension(R.dimen.circle_indicator_radius_search);
		// dot fill color
		mPaintFill.setStyle(Style.FILL);
		mPaintFill.setColor(CL.getResources().getColor(context,R.color.button_accept));
		// dot background color
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(getResources().getColor(R.color.white));
		start();
	}

	public void setDotsCount(int count)
	{
		mDotCount = count;
	}

	public void start()
	{
		mIndex = -1;
		mHandler.removeCallbacks(mRunnable);
		mHandler.post(mRunnable);
	}

	public void stop()
	{
		mHandler.removeCallbacks(mRunnable);
	}

	private int step = 1;
	private Runnable mRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			mIndex += step;
			if (mIndex < 0)
			{
				mIndex = 1;
				step = 1;
			}
			else if (mIndex > (mDotCount - 1))
			{
				if ((mDotCount - 2) >= 0)
				{
					mIndex = mDotCount - 2;
					step = -1;
				}
				else
				{
					mIndex = 0;
					step = 1;
				}
			}
			invalidate();
			mHandler.postDelayed(mRunnable, 300);
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		widthSize = MeasureSpec.getSize(widthMeasureSpec);
		heightSize = (int) mRadius * 2 + getPaddingBottom() + getPaddingTop();
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		float dX = (widthSize - mDotCount * mRadius * 2 - (mDotCount - 1) * margin) / 2.0f;
		float dY = heightSize / 2;
		for (int i = 0; i < mDotCount; i++)
		{
			if (i == mIndex)
			{
				canvas.drawCircle(dX, dY, mRadius, mPaintFill);
//				canvas.drawRect(10, 10, 10, 10, mPaintFill);
			}
			else
			{
				canvas.drawCircle(dX, dY, mRadius, mPaint);
//				canvas.drawRect(10, 10, 10, 10, mPaint);
			}
			dX += (2 * mRadius + margin);
		}
	}
}
