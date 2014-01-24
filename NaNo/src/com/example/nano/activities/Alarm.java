package com.example.nano.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.nano.R;

public class Alarm extends Activity implements OnClickListener,
		OnTimeChangedListener {

	TimePicker t;
	CheckBox vib, sil;
	Button set, cancel;
	int hours, minutes;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		Bundle alarmInfo = new Bundle();
		alarmInfo = getIntent().getBundleExtra("alarmInfo");
		message = alarmInfo.getString("alarmMessage","Get up");
		t = (TimePicker) findViewById(R.id.tp);
		vib = (CheckBox) findViewById(R.id.cbVibration);
		sil = (CheckBox) findViewById(R.id.cbSilent);
		set = (Button) findViewById(R.id.bSet);
		cancel = (Button) findViewById(R.id.bCancel);
		t.setOnTimeChangedListener(this);
		set.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bSet:
			setAlarm(hours, minutes, "Please read " + message, vib.isChecked(),
					sil.isChecked());
			setNotification("Reminder set for:\n " +  message + "\nat " + hours + ":" + minutes );
			finish();
			break;
		case R.id.bCancel:
			finish();
			break;
		}
	}

	@Override
	public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		t.clearFocus();
		hours = t.getCurrentHour();
		minutes = t.getCurrentMinute();
	}

	private void setAlarm(int hours, int minutes, String message,
			boolean vibration, boolean silent) {
		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
		i.putExtra(AlarmClock.EXTRA_HOUR, hours);
		i.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
		i.putExtra(AlarmClock.EXTRA_MESSAGE, message);
		i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		i.putExtra(AlarmClock.EXTRA_VIBRATE, vibration);
		i.putExtra(AlarmClock.VALUE_RINGTONE_SILENT, silent);
		startActivity(i);
	}
	
	private void setNotification(String message){
		NotificationManager notificationManager = 
				(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Notification n = new Notification.Builder(this)
								.setContentTitle("MyNote Reminder")
								.setContentText(message)
								.setSmallIcon(R.drawable.ic_launcher)
								.setAutoCancel(true).build();
		notificationManager.notify(0,n);
	}

}
