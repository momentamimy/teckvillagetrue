package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Meeesshhyyy",
                Toast.LENGTH_SHORT).show();
    }

}
