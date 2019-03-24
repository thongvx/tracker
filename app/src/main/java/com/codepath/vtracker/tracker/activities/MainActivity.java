package com.codepath.vtracker.tracker.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.vtracker.tracker.R;
import com.codepath.vtracker.tracker.receivers.MyAlarmReceiver;
public class MainActivity extends Activity {
	private PendingIntent alarmPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkForMessage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onStartAlarm(View v) {
	    Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
	    alarmPendingIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
	        intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    long firstMillis = System.currentTimeMillis();
	    int intervalMillis = 60000;
	    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, alarmPendingIntent);
	}
	
	public void onStopAlarm(View v) {
		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		if (alarmPendingIntent != null) {
		  alarm.cancel(alarmPendingIntent);
		}
	}
	

	private void checkForMessage() {
		String message = getIntent().getStringExtra("message");
		if (message != null) {
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	}

}
