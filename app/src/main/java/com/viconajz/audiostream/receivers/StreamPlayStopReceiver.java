package com.viconajz.audiostream.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.viconajz.audiostream.ui.fragments.stream.StreamingService;

public class StreamPlayStopReceiver extends BroadcastReceiver {
    private Listener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent playStopIntent = new Intent(context, StreamingService.class);
        String action = intent.getAction();
        playStopIntent.setAction(action);
        if (listener != null) listener.onActionChanged(action);
        context.startService(playStopIntent);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onActionChanged(String action);
    }
}
