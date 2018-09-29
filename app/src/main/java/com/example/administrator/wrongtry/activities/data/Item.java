package com.example.administrator.wrongtry.activities.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-03-19.
 */

public class Item implements Serializable{
    private String Date;
    private String Time;
    private long Record;

    public Item(String date, String time, long record) {
        this.Date = date;
        this.Time = time;
        this.Record = record;
    }

    public long getRecord() {
        return Record;
    }

    public void setRecord(long record) {
        Record = record;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
