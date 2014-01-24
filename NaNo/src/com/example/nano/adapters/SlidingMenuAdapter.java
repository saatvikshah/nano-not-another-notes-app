package com.example.nano.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nano.R;
import com.example.nano.models.NavDrawerItem;

public class SlidingMenuAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public SlidingMenuAdapter(Context context,ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}
	
	//return number of items in the ArrayList = no of menuitems
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater mInflater = (LayoutInflater)
					context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.slider_menu_row_design, null);
		}
		
		ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
		TextView title = (TextView)convertView.findViewById(R.id.title);
		
		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		title.setText(navDrawerItems.get(position).getTitle());
		return convertView;
	}
}
