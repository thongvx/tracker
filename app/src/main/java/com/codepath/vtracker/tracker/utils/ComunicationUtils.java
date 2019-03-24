package com.codepath.vtracker.tracker.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.codepath.vtracker.tracker.models.CallLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ComunicationUtils {
    public static String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e("imei", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }
    public static String getContactList(Context context) {
        String s = "";
        ContentResolver cr =  context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        s = s+ "ten: " + name+",";
                        s = s+  "sdt: " + phoneNo+"\n";
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return s;
    }
    public static String getSMS(String uri, Context context){
        Cursor cursor = context.getContentResolver().query(Uri.parse(uri), null, null, null, null);
        String msgData = "";
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {

                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        return msgData;
    }
    public static List<CallLog> getCallLog(Context context) {
        List<CallLog> callLogs = new ArrayList<>();
        @SuppressLint("MissingPermission")
        Cursor cursor = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            //Co the them vao
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String stringtype = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case android.provider.CallLog.Calls.OUTGOING_TYPE:
                    stringtype = "Gọi đi";
                    break;
                case android.provider.CallLog.Calls.INCOMING_TYPE:
                    stringtype = "Gọi đến";
                    break;

                case android.provider.CallLog.Calls.MISSED_TYPE:
                    stringtype = "Gọi nhỡ";
                    break;
            }
            //Todo: Map giua phone number voi ten user, android chi sho phone number
            CallLog callLog = new CallLog(phNumber, "",callDuration, stringtype, callDayTime);
            callLogs.add(callLog);
        }
        cursor.close();
        return callLogs;
    }
}
