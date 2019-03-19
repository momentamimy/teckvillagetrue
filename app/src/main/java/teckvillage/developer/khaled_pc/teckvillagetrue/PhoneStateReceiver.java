package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        Toast.makeText(context,"Num : "+number+"State : "+state,Toast.LENGTH_LONG).show();
        Log.d("hlaclaclclclclclclc","Num : "+number+"State : "+state);
    }
}
