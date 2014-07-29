package com.jason.usedcar.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jason.usedcar.R;
import com.jason.usedcar.adapter.holder.SaleCarViewHolder;
import com.jason.usedcar.model.UsedCarModel;
import com.jason.usedcar.model.db.Brand;
import com.jason.usedcar.model.db.Series;

public class SeriersAdapter extends BaseAdapter {

	private Context mContext;

	private LayoutInflater mInflater;

	private UsedCarModel<Series> mBrandModel;

	public SeriersAdapter(Context context, UsedCarModel<Series> model) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mBrandModel = model;
		mBrandModel.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				notifyDataSetChanged();
			}

			@Override
			public void onInvalidated() {
				notifyDataSetInvalidated();
			}
		});
	}

	@Override
	public int getCount() {
		return mBrandModel == null ? 0 : mBrandModel.size();
	}

	@Override
	public Series getItem(int position) {
		return mBrandModel == null ? null : mBrandModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO add the seriers layout, viewholder and implement the event
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_shopping_car_layout,
					parent, false);
		}
		SaleCarViewHolder viewHolder = (SaleCarViewHolder) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new SaleCarViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		Series param = getItem(position);
		// viewHolder.preSaleText.setText(context.getString(
		// R.string.sale_car_pre_sale_price, param.getListPrice()));
		// viewHolder.mileageText.setText(context.getString(
		// R.string.sale_car_mileage, param.getOdometer()));
		// viewHolder.buyTimeText.setText(context.getString(
		// R.string.sale_car_buy_time, param.getPurchaseDate()));
		return convertView;
	}
}