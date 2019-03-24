package com.codepath.vtracker.tracker.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.codepath.vtracker.tracker.DemoApplication;
import com.codepath.vtracker.tracker.R;
import com.codepath.vtracker.tracker.activities.MainActivity;

public class MySimpleService extends JobIntentService {
	public static final int JOB_ID = 100;
	public static final int NOTIF_ID = 56;
	long timestamp;

	String longText;

	public static void enqueueWork(Context context, Intent work) {
		enqueueWork(context, MySimpleService.class, JOB_ID, work);
	}

	// This describes what will happen when service is triggered
	@Override
	protected void onHandleWork(@NonNull Intent intent) {
		Log.d("DEBUG", "MySimpleService triggered");
		timestamp =  System.currentTimeMillis();
	    // Extract additional values from the bundle
	    String val = intent.getStringExtra("foo");
		// Extract the receiver passed into the service
	    ResultReceiver rec = intent.getParcelableExtra("receiver");
		// Send result to activity
	    sendResultValue(rec, val);
		//Xoa di
	    createNotification(val);
	}

	// Send result to activity using ResultReceiver
	private void sendResultValue(ResultReceiver rec, String val) {
		// To send a message to the Activity, create a pass a Bundle
	    Bundle bundle = new Bundle();
	    bundle.putString("resultValue", "My Result Value. You Passed in: " + val + " with timestamp: " + timestamp);
	    // Here we call send passing a resultCode and the bundle of extras
	    rec.send(Activity.RESULT_OK, bundle);		
	}

	private void createNotification(String val) {

		// Construct pending intent to serve as action for notification item
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("message", "Launched via notification with message: " + val + " and timestamp " + timestamp);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// Create notification

		Notification noti =
		        new NotificationCompat.Builder(this, DemoApplication.CHANNEL_ID)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("New Result!")
		        .setContentText("")
		        .setStyle(new NotificationCompat.BigTextStyle().bigText("Đã nhận data mới !"))
		        .setContentIntent(pIntent)
		        .build();

		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(NOTIF_ID, noti);
	}
}