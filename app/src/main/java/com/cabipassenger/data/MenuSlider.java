//package com.Taximobility.data;
//
//import android.app.Activity;
//import android.content.Context;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cabi.AboutAct;
//import com.cabi.FareAct;
//import com.cabi.FavouritesAct;
//import com.cabi.InviteFriendAct;
//import com.cabi.Language;
//import com.cabi.PaymentOptionsAct;
//import com.cabi.ProfileAct;
//import com.cabi.R;
//import com.cabi.TripHistoryAct;
//import com.cabi.WalletAct;
//import com.cabi.menu.SlidingMenu;
//import com.cabi.util.FontHelper;
//
////Menu slider UI declaration and definitions
//public class MenuSlider
//{
//	public Context mContext;
//	private SlidingMenu menu;
//
//	public MenuSlider(Context _context, SlidingMenu _menu)
//	{
//		mContext = _context;
//		menu = _menu;
//		slidemenu(mContext);
//	}
//
//	public SlidingMenu slidemenu(Context _context)
//	{
//		// TODO Auto-generated method stub
//		menu = new SlidingMenu(mContext);
//		menu.setMode(SlidingMenu.LEFT);
//		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//		menu.setBehindOffset(30);
//		menu.setShadowWidthRes(R.dimen.shadow_width);
//		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		menu.setFadeDegree(0.35f);
//		menu.attachToActivity((Activity) _context, SlidingMenu.SLIDING_CONTENT);
//		menu.setMenu(R.layout.sliderlay);
//		FontHelper.applyFont(mContext, menu.findViewById(R.id.slider_contain));
//		TextView aboutTxt = (TextView) menu.findViewById(R.id.menu_aboutTxt);
//		TextView user_name_txt = (TextView) menu.findViewById(R.id.menu_profile_name);
//		TextView meTxt = (TextView) menu.findViewById(R.id.menu_meTxt);
//		TextView paymentTxt = (TextView) menu.findViewById(R.id.menu_paymentTxt);
//		TextView historyTxt = (TextView) menu.findViewById(R.id.menu_historyTxt);
//		TextView favTxt = (TextView) menu.findViewById(R.id.menu_favouritesTxt);
//		TextView Txt2 = (TextView) menu.findViewById(R.id.Txt2);
//		TextView gettaxiTxt = (TextView) menu.findViewById(R.id.menu_gettaxiTxt);
//		TextView menu_fare_text = (TextView) menu.findViewById(R.id.menu_fare_text);
//		TextView menu_wallet_text = (TextView) menu.findViewById(R.id.menu_wallet_text);
//		TextView menu_invite_text = (TextView) menu.findViewById(R.id.menu_invite_text);
//		TextView tv_menu_settings = (TextView) menu.findViewById(R.id.tv_menu_settings);
//		ImageView imgmeTxt = (ImageView) menu.findViewById(R.id.img_meTxt);
//		ImageView imgpaymentTxt = (ImageView) menu.findViewById(R.id.img_paymentTxt);
//		ImageView imghistoryTxt = (ImageView) menu.findViewById(R.id.img_historyTxt);
//		ImageView imggettaxiTxt = (ImageView) menu.findViewById(R.id.img_gettaxiTxt);
//		ImageView imgfavouritesTxt = (ImageView) menu.findViewById(R.id.img_favouritesTxt);
//		ImageView imgaboutTxt = (ImageView) menu.findViewById(R.id.img_aboutTxt);
//		ImageView menu_fare_image = (ImageView) menu.findViewById(R.id.menu_fare_image);
//		ImageView menu_wallet_image = (ImageView) menu.findViewById(R.id.menu_wallet_image);
//		ImageView menu_invite_image = (ImageView) menu.findViewById(R.id.menu_invite_image);
//		ImageView img_settings = (ImageView) menu.findViewById(R.id.img_settings);
//		LinearLayout getTaxilay = (LinearLayout) menu.findViewById(R.id.menu_gettaxi);
//		LinearLayout aboutlay = (LinearLayout) menu.findViewById(R.id.menu_about);
//		LinearLayout favlay = (LinearLayout) menu.findViewById(R.id.menu_favourites);
//		LinearLayout historylay = (LinearLayout) menu.findViewById(R.id.menu_history);
//		LinearLayout melay = (LinearLayout) menu.findViewById(R.id.menu_me);
//		LinearLayout paymentlay = (LinearLayout) menu.findViewById(R.id.menu_payment);
//		LinearLayout menu_fare = (LinearLayout) menu.findViewById(R.id.menu_fare);
//		LinearLayout menu_wallet = (LinearLayout) menu.findViewById(R.id.menu_wallet);
//		LinearLayout menu_invite = (LinearLayout) menu.findViewById(R.id.menu_invite);
//		LinearLayout menu_settings = (LinearLayout) menu.findViewById(R.id.menu_settings);
//		if (mContext == Language.mlang)
//		{
//			tv_menu_settings.setTextColor(mContext.getResources().getColor(R.color.white));
//			menu_settings.setBackgroundResource(R.drawable.draw_menu_select);
//			img_settings.setImageResource(R.drawable.menu_settingsfocus);
//		}
//		if (mContext == FareAct.mfare)
//		{
//			menu_fare_text.setTextColor(mContext.getResources().getColor(R.color.white));
//			menu_fare.setBackgroundResource(R.drawable.draw_menu_select);
//			menu_fare_image.setImageResource(R.drawable.menu_farefocus);
//		}
//		if (mContext == InviteFriendAct.mInviteAct)
//		{
//			menu_invite_text.setTextColor(mContext.getResources().getColor(R.color.white));
//			menu_invite.setBackgroundResource(R.drawable.draw_menu_select);
//			menu_invite_image.setImageResource(R.drawable.menu_invitefrdfocus);
//		}
//		if (mContext == WalletAct.mWalletAct)
//		{
//			menu_wallet_text.setTextColor(mContext.getResources().getColor(R.color.white));
//			menu_wallet.setBackgroundResource(R.drawable.draw_menu_select);
//			menu_wallet_image.setImageResource(R.drawable.menu_walletfocus);
//		}
//		if (mContext == AboutAct.mAbout)
//		{
//			aboutTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//			aboutlay.setBackgroundResource(R.drawable.draw_menu_select);
//			imgaboutTxt.setImageResource(R.drawable.menu_aboutfocus);
//		}
//		if (mContext == FavouritesAct.mFavourite)
//		{
//			favTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//			favlay.setBackgroundResource(R.drawable.draw_menu_select);
//			imgfavouritesTxt.setImageResource(R.drawable.favourite_focus);
//		}
//		if (mContext == PaymentOptionsAct.mPayment)
//		{
//			paymentTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//			paymentlay.setBackgroundResource(R.drawable.draw_menu_select);
//			imgpaymentTxt.setImageResource(R.drawable.menu_paymentoptionfocus);
//		}
//		if (mContext == ProfileAct.mProfile)
//		{
//			meTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//			melay.setBackgroundResource(R.drawable.draw_menu_select);
//			imgmeTxt.setImageResource(R.drawable.menu_mefocus);
//		}
//		if (mContext == TripHistoryAct.mTriphistory)
//		{
//			historyTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//			historylay.setBackgroundResource(R.drawable.draw_menu_select);
//			imghistoryTxt.setImageResource(R.drawable.menu_triphistoryfocus);
//		}
//		return menu;
//	}
//}
