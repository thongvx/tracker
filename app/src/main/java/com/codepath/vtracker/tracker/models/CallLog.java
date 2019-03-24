package com.codepath.vtracker.tracker.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class CallLog {
    public Date date;
    public String num;
    public String name;
    public String duration;
    public String callType;


    public CallLog(String num, String name, String duration, String callType, Date date) {
        this.num = num;
        this.name = name;
        this.duration = duration;
        this.callType = callType;
        this.date = date;
    }
}
