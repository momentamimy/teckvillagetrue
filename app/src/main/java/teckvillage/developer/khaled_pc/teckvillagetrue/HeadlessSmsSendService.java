package teckvillage.developer.khaled_pc.teckvillagetrue;

import android.app.IntentService;
import android.content.Intent;

public class HeadlessSmsSendService extends IntentService {
    public HeadlessSmsSendService() {
        super(HeadlessSmsSendService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}