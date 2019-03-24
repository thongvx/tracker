package com.codepath.vtracker.tracker.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class TrackerInfo {
    public String lat;
    public String lon;
    public String contactList;
    public String inboxMessage;
    public String sentMessage;
    public String drafMessage;
    public List<CallLog> callLogs;

    public TrackerInfo() {

    }

    public TrackerInfo(String lat, String lon, String contactList, String inboxMessage,
                       String sentMessage,
                       String drafMessage,
                       List<CallLog> callLogs) {
        this.lat = lat;
        this.lon = lon;
        this.contactList = contactList;
        this.inboxMessage = inboxMessage;
        this.sentMessage = sentMessage;
        this.drafMessage = drafMessage;
        this.callLogs = callLogs;
    }

    public TrackerInfo(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
