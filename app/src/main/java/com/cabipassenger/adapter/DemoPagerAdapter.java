//package com.Taximobility.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.cabi.R;
//import com.cabi.UserHomeAct;
//import com.cabi.util.Colorchange;
//import com.cabi.util.FontHelper;
//
//public class DemoPagerAdapter extends PagerAdapter {
//
//	int[] tutorial;
//	Context context;
//	LayoutInflater inflater;
//	boolean ishelp;
//
//	public DemoPagerAdapter(Context con, int[] tutorial, boolean ishelp) {
//		// TODO Auto-generated constructor stub
//		this.context = con;
//		this.tutorial = tutorial;
//		this.ishelp = ishelp;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return tutorial.length;
//	}
//
//	@Override
//	public boolean isViewFromObject(View view, Object object) {
//		// TODO Auto-generated method stub
//		return view == ((FrameLayout) object);
//	}
//
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		// TODO Auto-generated method stub
//		ImageView img;
//		Colorchange.ChangeColor(container,context);
//
//		TextView skipTxt;
//		inflater = (LayoutInflater) context
//				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//		View row = inflater.inflate(R.layout.demoitem, container, false);
//		FontHelper.applyFont(context, row.findViewById(R.id.lay_demoimg));
//		img = (ImageView) row.findViewById(R.id.demoimg);
//		skipTxt = (TextView) row.findViewById(R.id.skipTxt);
//		img.setBackgroundResource(tutorial[position]);
//		((ViewPager) container).addView(row);
//		if (tutorial.length - 1 == position)
//			skipTxt.setText("Done");
//		else
//			skipTxt.setText("Skip");
//		skipTxt.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (!ishelp) {
//					((Activity) context).startActivity(new Intent(context,
//							UserHomeAct.class));
//				}
//				((Activity) context).finish();
//			}
//		});
//		return row;
//	}
//
//	@Override
//	public void destroyItem(View container, int position, Object object) {
//		// TODO Auto-generated method stub
//		((ViewPager) container).removeView((FrameLayout) object);
//	}
//}
