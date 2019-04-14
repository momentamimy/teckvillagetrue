package teckvillage.developer.khaled_pc.teckvillagetrue.BroadcastReceivers;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;


import java.util.ArrayList;
import java.util.List;

import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;
import teckvillage.developer.khaled_pc.teckvillagetrue.SMS_MessagesChat;

import static android.content.Context.MODE_PRIVATE;
import static teckvillage.developer.khaled_pc.teckvillagetrue.Message_Fragment.SMSCHANGE;

public class SMSReceiver extends BroadcastReceiver{
    private NotificationManager notifManager;
    private NotificationChannel mChannel;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus=(Object[])intent.getExtras().get("pdus");
        String sender="";
        long date;
        StringBuilder text=new StringBuilder();
        // get sender from first PDU
        SmsMessage shortMessage=SmsMessage.createFromPdu((byte[]) pdus[0]);
        sender=shortMessage.getOriginatingAddress();
        date=System.currentTimeMillis();
        for(int i=0;i<pdus.length;i++){
            shortMessage=SmsMessage.createFromPdu((byte[]) pdus[i]);
            text.append(shortMessage.getDisplayMessageBody());
        }
        if (context.getSharedPreferences("notifications_mute",MODE_PRIVATE).getBoolean("state",false)==false){
            notification(text.toString(),sender,context);
        }
        insert_sms_to_inbox(sender,text.toString(),context,date);
//this will update the UI with message

    }

    private void notification(String message, String sender, Context context){
        SMSCHANGE=true;
        //FragMessageOthers.instance().refreshSmsInbox();
        //SMS_MessagesChat.instance().refresh();
        Intent intent;
        PendingIntent pendingIntent = null;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }

        intent = new Intent (context, MainActivity.class);
        intent.putExtra("NOTIFICATION",true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                NotificationChannel mChannel = new NotificationChannel
                        ("0",sender,importance);
                mChannel.setDescription (message);
                mChannel.enableVibration (true);
                mChannel.setVibrationPattern (new long[]
                        {100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel (mChannel);
            }
            builder = new NotificationCompat.Builder (context,"0");

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity (context, 0, intent, 0);
            builder.setContentTitle (sender)  // flare_icon_30
                    .setSmallIcon (R.drawable.logo_notification) // required
                    .setContentText (message)  // required
                    .setDefaults (Notification.DEFAULT_ALL)
                    .setGroup("messages")
                    .setAutoCancel (true)
                    .setColor((context.getResources().getColor(R.color.colorPrimary)))
                    .setContentIntent (pendingIntent)
                    .setSound (RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate (new long[]{100, 200, 300, 400,
                            500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder (context);


            Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSmallIcon(R.drawable.logo);
            builder.setContentTitle(sender);
            builder.setGroup("messages");
            builder.setContentText(message);
            builder.setColor((context.getResources().getColor(R.color.colorPrimary)));
            builder.setSound(sound);
            builder.setVibrate (new long[]{100, 200, 300, 400,
                    500, 400, 300, 200, 400});

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                resultIntent.putExtra("NOTIFICATION",true);
                TaskStackBuilder stackBuilder = null;
                stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = null;
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
            }

        }

        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context, "0");
                summaryBuilder.setSmallIcon(R.drawable.logo_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setColor((context.getResources().getColor(R.color.colorPrimary)))
                .setGroup("messages")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(sender+" "+message);
            inboxStyle.setSummaryText("SMS Messages");
            summaryBuilder.setStyle(inboxStyle);
        }
        Notification summaryNotification = summaryBuilder.build();


        Notification notification = builder.build ();
        int id = (int) System.currentTimeMillis();
        notifManager.notify (id, notification);
        notifManager.notify(444,summaryNotification);
    }

    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return true;
                }
            }
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void insert_sms_to_inbox(String phone, String sms, Context context, long date){
        ContentValues values = new ContentValues();
        values.put("address",phone);
        values.put("body", sms);
        values.put("date",date);

        context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
        refresh(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void refresh(Context context){
        if(isAppRunning(context)) {
            SMS_MessagesChat inst = SMS_MessagesChat.instance();
            //conversations inst2 = conversations.instance();
            if (inst!=null) {
                inst.refresh();
            }
            /*if (inst2!=null){
                inst2.getEveryLastMessages();
            }*/
        }

    }
}