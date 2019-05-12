package com.developer.whocaller.net.Model;

public class UpdateMessageInfo {

    long date;
    String sms;

    public UpdateMessageInfo() {

    }

    public UpdateMessageInfo(long date, String sms) {
        this.date = date;
        this.sms = sms;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

}
