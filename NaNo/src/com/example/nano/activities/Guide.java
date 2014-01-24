package com.example.nano.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.nano.R;
import com.example.nano.adapters.CustomPagerAdapter;

public class Guide extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_control);
		// Create and set adapter
		CustomPagerAdapter adapter = new CustomPagerAdapter();
		ViewPager myPager = (ViewPager) findViewById(R.id.customviewpager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(0);
	}
	



}
