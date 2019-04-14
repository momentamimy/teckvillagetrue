package teckvillage.developer.khaled_pc.teckvillagetrue.Model.retrofit;

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
