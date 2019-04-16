package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import teckvillage.developer.khaled_pc.teckvillagetrue.MainActivity;
import teckvillage.developer.khaled_pc.teckvillagetrue.Model.Get_Calls_Log;
import teckvillage.developer.khaled_pc.teckvillagetrue.R;

public class MyFirebaseMessageingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented=remoteMessage.getData().get("sented");

        if (!TextUtils.isEmpty(remoteMessage.getData().get("messageId")))
        {
            Log.w("yaaaaaaaaaaaaaa1","messageId");
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d("mashyya3mmashy", "key, " + key + " value " + value);
            }
        }
        else if (!TextUtils.isEmpty(remoteMessage.getData().get("notificationId")))
        {
            Log.w("yaaaaaaaaaaaaaa1","notificationId");
        }
        //sendNotification1(remoteMessage);

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
            updateToken(s);
    }

    private void updateToken(String refreshToken) {

    }


}
