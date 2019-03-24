package com.codepath.vtracker.tracker.receivers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;


import static android.app.Activity.RESULT_OK;


public class MySimpleReceiver extends ResultReceiver {
	private Receiver receiver;

	public MySimpleReceiver(Handler handler) {
		super(handler);
	}


	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}


	public interface Receiver {
		public void onReceiveResult(int resultCode, Bundle resultData);
	}

	public static MySimpleReceiver setupServiceReceiver(final Context context) {
		MySimpleReceiver receiverForSimple = new MySimpleReceiver(new Handler());

		receiverForSimple.setReceiver(new MySimpleReceiver.Receiver() {
			@Override
			public void onReceiveResult(int resultCode, Bundle resultData) {
				if (resultCode == RESULT_OK) {
					String resultValue = resultData.getString("resultValue");
					Toast.makeText(context, resultValue, Toast.LENGTH_SHORT).show();
				}
			}
		});
		return receiverForSimple;
	}


	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (receiver != null) {
			receiver.onReceiveResult(resultCode, resultData);
		}
	}
}
