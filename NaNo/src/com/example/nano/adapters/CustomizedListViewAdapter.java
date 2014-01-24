package com.example.nano.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nano.R;
import com.example.nano.activities.NotesSubCategoryListViewFragment;

public class CustomizedListViewAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String,String>> myList;
	private static LayoutInflater inflater = null;
	
	public CustomizedListViewAdapter(Context context,ArrayList<HashMap<String, String>> myList){
		this.context = context;
		this.myList = myList;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = inflater.inflate(R.layout.customized_list_row, null);
		}
		TextView title = (TextView)convertView.findViewById(R.id.lv_title);
		TextView content = (TextView)convertView.findViewById(R.id.lv_content);
		TextView dobnote = (TextView)convertView.findViewById(R.id.lv_dobnote);
		ImageView thumb_image = (ImageView)convertView.findViewById(R.id.lv_image);
		
		HashMap<String,String> noteItem = new HashMap<String, String>();
		noteItem = myList.get(position);
		title.setText(noteItem.get(NotesSubCategoryListViewFragment.KEY_TITLE));
		content.setText(noteItem.get(NotesSubCategoryListViewFragment.KEY_CONTENT));
		dobnote.setText(noteItem.get(NotesSubCategoryListViewFragment.KEY_DOBNOTE));
		thumb_image.setImageResource(Integer.parseInt(noteItem.get(NotesSubCategoryListViewFragment.KEY_IMAGE)));
		
		return convertView;
	}
}
