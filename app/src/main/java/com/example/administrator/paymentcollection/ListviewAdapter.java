package com.example.administrator.paymentcollection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {

	public ArrayList<Model> ledgerList;
	Activity activity;

	public ListviewAdapter(Activity activity, ArrayList<Model> ledgerList) {
		super();
		this.activity = activity;
		this.ledgerList = ledgerList;
	}

	@Override
	public int getCount() {
		return ledgerList.size();
	}

	@Override
	public Object getItem(int position) {
		return ledgerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView lDate;
		TextView lDesc;
		TextView lDebit;
		TextView lCredit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_row, null);
			holder = new ViewHolder();
			holder.lDate = (TextView) convertView.findViewById(R.id.lDate);
			holder.lDesc = (TextView) convertView.findViewById(R.id.lDesc);
			holder.lDebit = (TextView) convertView.findViewById(R.id.lDebit);
			holder.lCredit = (TextView) convertView.findViewById(R.id.lCredit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Model item = ledgerList.get(position);
		holder.lDate.setText(item.getsNo().toString());
		holder.lDesc.setText(item.getProduct().toString());
		holder.lDebit.setText(item.getCategory().toString());
		holder.lCredit.setText(item.getPrice().toString());

		return convertView;
	}

}