package teckvillage.developer.khaled_pc.teckvillagetrue.model.retrofit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented=remoteMessage.getData().get("sented");
        Log.w("yaaaaaaaaaaaaaa1","a5eeeeeeeeern");
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
