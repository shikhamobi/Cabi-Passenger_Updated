package com.cabipassenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
/**
 * This adapter used to show the card details into list view.
 */

public class CreditCardAdapter extends BaseAdapter
{
	private final Context context;
	private final LayoutInflater mInflater;
	int header_bgcolor;

	// constructor
	public CreditCardAdapter(Context paymentOptionAct)
	{
		// TODO Auto-generated constructor stub
		context = paymentOptionAct;
		mInflater = LayoutInflater.from(context);
	}

	/**
	 *   Return list size.
 	 */

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return TaxiUtil.mCreditcardlist.size();
	}

	// It returns the item detail with select position.
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return TaxiUtil.mCreditcardlist.get(position);
	}

	/**
	 * It returns the item id with select position.
	 * @param position
     * @return
     */
	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	// Get the view for each row in the list used view holder.
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder mHolder;


		if (convertView == null)
		{

			convertView = mInflater.inflate(R.layout.creditcard_item, null);
			mHolder = new ViewHolder();
			FontHelper.applyFont(context, convertView.findViewById(R.id.credit_contain));
			mHolder.Card = (TextView) convertView.findViewById(R.id.CardTxt);
			mHolder.img_lineup = (ImageView) convertView.findViewById(R.id.img_lineup);
			mHolder.img_linebottom = (ImageView) convertView.findViewById(R.id.img_linebottom);
			convertView.setTag(mHolder);
		}
		else
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		Colorchange.ChangeColor((ViewGroup) convertView,context);

		try
		{
			if (position == 0)
			{
				header_bgcolor = context.getResources().getColor(R.color.white);
				mHolder.img_lineup.setBackgroundResource(R.drawable.line1);
				mHolder.img_linebottom.setVisibility(View.GONE);
			}
			else if (position == (TaxiUtil.mCreditcardlist.size() - 1))
			{
				header_bgcolor = context.getResources().getColor(R.color.white);
				mHolder.img_lineup.setBackgroundResource(R.drawable.line2);
				mHolder.img_linebottom.setBackgroundResource(R.drawable.line1);
			}
			else
			{
				header_bgcolor = context.getResources().getColor(R.color.white);
				mHolder.img_lineup.setBackgroundResource(R.drawable.line2);
				mHolder.img_linebottom.setVisibility(View.GONE);
			}
			if (TaxiUtil.mCreditcardlist.size() == 1)
			{
				mHolder.img_lineup.setBackgroundResource(R.drawable.line1);
				mHolder.img_linebottom.setBackgroundResource(R.drawable.line2);
			}
			String Type = TaxiUtil.mCreditcardlist.get(position).getType();
			String isDef = TaxiUtil.mCreditcardlist.get(position).getDefault_card();
			if (isDef.equals("1"))
			{
				if (SessionSave.getSession("Lang", context).equals("ar")||SessionSave.getSession("Lang",context).equals("fa")){
					mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tick,0,  R.drawable.credit_card, 0);
					mHolder.Card.setCompoundDrawablePadding(10);}
				else {
					mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, R.drawable.tick, 0);
					mHolder.Card.setCompoundDrawablePadding(10);
				}
			}
//			else if (isDef.equals("0"))
//			{
//				mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//			}
			else
			{
				if (SessionSave.getSession("Lang", context).equals("ar")||SessionSave.getSession("Lang",context).equals("fa")) {
					mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, R.drawable.credit_card, 0);
					//mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);}
					mHolder.Card.setCompoundDrawablePadding(10);
				}
				else{
				//	mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard, 0, 0, 0);
					mHolder.Card.setCompoundDrawablePadding(10);
					mHolder.Card.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, R.drawable.right_arrow, 0);}
			}
			if (Type.equals("P"))
			{
				mHolder.Card.setText("" + TaxiUtil.mCreditcardlist.get(position).getCard());
			}
			else if (Type.equals("B"))
			{
				mHolder.Card.setText(NC.getResources().getString(R.string.businesscard) + " " + TaxiUtil.mCreditcardlist.get(position).getCard());
			}
			else if (Type.equals(""))
			{
				mHolder.Card.setText(TaxiUtil.mCreditcardlist.get(position).getCard());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return convertView;
	}

	/**
	 * View holder class member this contains in every row in list.
	 */
	//
	class ViewHolder
	{
		public TextView Card;
		public ImageView img_lineup;
		public ImageView img_linebottom;
	}
}
