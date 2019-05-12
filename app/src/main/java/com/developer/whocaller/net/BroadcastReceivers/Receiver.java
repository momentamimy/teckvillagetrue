package com.developer.whocaller.net.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("adadadada","receive");
        long l=intent.getLongExtra("time",0);
        ContentValues values = new ContentValues();
        values.put("seen", true);
        values.put("read", true);
        values.put("date",l);
        values.put("type",5);
        values.put("status",32);//not sent
        context.getContentResolver().update(Uri.parse("content://sms/"), values, "date=?", new String[] {String.valueOf(l)});
    }

}
