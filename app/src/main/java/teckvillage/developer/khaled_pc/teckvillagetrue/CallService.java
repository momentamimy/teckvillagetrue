package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.IntentService;
import android.content.Intent;

public class CallService extends IntentService {
    public CallService() {
        super(CallService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}