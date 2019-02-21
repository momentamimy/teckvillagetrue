package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import teckvillage.developer.khaled_pc.teckvillagetrue.FragMessageOthers;
import teckvillage.developer.khaled_pc.teckvillagetrue.model.MessageInfo;

public class SMSBReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            MessageInfo messageInfo = null;
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                //messageInfo=new MessageInfo(null,address,smsBody,null);
            }
            //Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            //this will update the UI with message
            FragMessageOthers inst = FragMessageOthers.instance();
            inst.updateList(messageInfo);
        }
    }
}
