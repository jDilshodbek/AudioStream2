package com.viconajz.audiostream.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BufferingReceiver extends BroadcastReceiver {
    private Listener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onBufferingUpdate(String action);
    }
}
