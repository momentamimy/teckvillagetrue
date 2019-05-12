package com.developer.whocaller.net;

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