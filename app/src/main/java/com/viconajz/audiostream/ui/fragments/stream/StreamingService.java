package com.viconajz.audiostream.ui.fragments.stream;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.viconajz.audiostream.R;
import com.viconajz.audiostream.ui.activities.MainActivity;

import java.util.Objects;

/**
 * Service that controls the player and the notification that represents it.
 */
public class StreamingService extends Service implements AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = StreamingService.class.getSimpleName();

    private final IBinder playerBinder = new PlayerBinder();
    public static final String ACTION_PLAY = "com.viconajz.audiostream.services.PLAY";
    public static final String ACTION_PAUSE = "com.viconajz.audiostream.services.PAUSE";
    private static final String ACTION_CLOSE = "com.viconajz.audiostream.services.APP_CLOSE";
    public static final String ACTION_CLOSE_IF_PAUSED = "com.viconajz.audiostream.services.APP_CLOSE_IF_PAUSED";
    private static final int NOTIFICATION_ID = 4223;
    private AudioManager mAudioManager = null;
    private String mStreamUrl;
    private String mStreamName;
    private SimpleExoPlayer player;

    //Wifi Lock to ensure the wifi does not ge to sleep while we are streaming music.
    private WifiManager.WifiLock mWifiLock;

    enum State {
        Stopped,  //player is stopped and not prepared to play
        Preparing, //player is preparing to play
        Playing,  //playback is active.
        // There is a chance that the player is actually paused here if we do not have audio focus.
        // We stay in this state so we know to resume when we gain audio focus again.
        Paused //playback is paused
    }

    private State mState = State.Stopped;

    enum AudioFocus {
        NoFocusNoDuck, // service does not have audio focus and cannot duck
        NoFocusCanDuck, // we don't have focus but we can play at low volume ("ducking")
        Focused  //player has full audio focus
    }

    private AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mAudioFocus = AudioFocus.Focused;
                // resume playback
                if (mState == State.Playing) {
                    startPlayer();
                    player.setVolume(1.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mAudioFocus = AudioFocus.NoFocusNoDuck;
                // Lost focus for an unbounded amount of time: stop playback and release player
                stopPlayer();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mAudioFocus = AudioFocus.NoFocusNoDuck;
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the player because playback
                // is likely to resume
                processPauseRequest();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mAudioFocus = AudioFocus.NoFocusCanDuck;
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (player.isPlaying()) player.setVolume(1.0f);
                break;
        }
    }

    private void setupAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
    }

    private void setupWifiLock() {
        if (mWifiLock == null) {
            mWifiLock = ((WifiManager) Objects.requireNonNull(getApplicationContext().getSystemService(Context.WIFI_SERVICE)))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mediaplayerlock");
        }
    }

    private void setupPlayer() {
        if (player == null) {

            player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "Audio Stream"));
            // This is the MediaSource representing the media to be played.
            MediaSource audioSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mStreamUrl));

            // Prepare the player with the source.
            player.prepare(audioSource);
            player.setPlayWhenReady(true);
        }
    }

    /**
     * The radio streaming service runs in foreground mode to keep the Android OS from killing it.
     * The OnStartCommand is called every time there is a call to start service and the service is
     * already started. By Passing an intent to the onStartCommand we can play and pause the music.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }
        if (action != null) {
            switch (action) {
                case ACTION_PLAY:
                    if (intent.getStringExtra("EXTRA_URL") != null) {
                        if (mStreamUrl != null) {
                            boolean isUrlChanged = false;
                            if (!mStreamUrl.equals(intent.getStringExtra("EXTRA_URL"))) {
                                isUrlChanged = true;
                            }
                            if (isUrlChanged) {
                                mStreamUrl = intent.getStringExtra("EXTRA_URL");
                                mStreamName = intent.getStringExtra("EXTRA_NAME");
                                stopPlayer();
                                processPlayRequest();
                            } else {
                                mStreamUrl = intent.getStringExtra("EXTRA_URL");
                                mStreamName = intent.getStringExtra("EXTRA_NAME");
                                stopPlayer();
                                processPlayRequest();
                            }
                        } else {
                            mStreamUrl = intent.getStringExtra("EXTRA_URL");
                            mStreamName = intent.getStringExtra("EXTRA_NAME");
                            processPlayRequest();
                        }
                    } else processPlayRequest();
                    break;
                case ACTION_PAUSE:
                    processPauseRequest();
                    break;
                case ACTION_CLOSE_IF_PAUSED:
                    closeIfPaused();
                    break;
                case ACTION_CLOSE:
                    close();
                    break;
            }




        }
        return START_STICKY; //do not restart service if it is killed.
    }

    //if the player is paused or stopped and this method has been triggered then stop the service.
    private void closeIfPaused() {
        if (mState == State.Paused || mState == State.Stopped) {
            close();
        }
    }

    private void close() {
        removeNotification();
        stopSelf();
    }

    private void initPlayer() {
        setupPlayer();
        requestResources();
    }

    /**
     * Check if the player was initialized and we have audio focus.
     * Without audio focus we do not start the player.
     * change state and start to prepare async
     */
    private void configAndPreparePlayer() {
        initPlayer();
        mState = State.Preparing;
        buildNotification(true);
    }

    /*
        Check if the media player is available and start it.
     */
    private void startPlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            sendUpdatePlayerIntent();
            mState = State.Playing;
            buildNotification(false);
        }
    }

    private void sendUpdatePlayerIntent() {
        Log.d(TAG, "updatePlayerIntent");
        Intent updatePlayerIntent = new Intent(MainActivity.UPDATE_PLAYER);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updatePlayerIntent);
    }

    /*
        Request audio focus and acquire a wifi lock. Returns true if audio focus was granted.
     */
    private void requestResources() {
        setupAudioManager();
        setupWifiLock();
        mWifiLock.acquire();

        tryToGetAudioFocus();
    }

    private void tryToGetAudioFocus() {
        if (mAudioFocus != AudioFocus.Focused && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN))
            mAudioFocus = AudioFocus.Focused;
    }

    /**
     * if the player is playing then stop it. Change the state and relax the wifi lock and
     * audio focus.
     */
    private void stopPlayer() {
        // Lost focus for an unbounded amount of time: stop playback and release player
        if (player != null) {
            if (player.isPlaying()) player.stop();
            player.release();
            player = null;
        }
        mState = State.Stopped;
        //relax the resources because we no longer need them.
        relaxResources();
        giveUpAudioFocus();
    }

    private void processPlayRequest() {
        if (mState == State.Stopped) {
            sendBufferingIntent();
            configAndPreparePlayer();
        } else if (mState == State.Paused) {
            requestResources();
            startPlayer();
        } else if (mState == State.Playing) {
            stopPlayer();
            sendBufferingIntent();
            configAndPreparePlayer();
        }
    }

    //send an intent telling any activity listening to this intent that the player is buffering.
    private void sendBufferingIntent() {
        Intent bufferingPlayerIntent = new Intent(MainActivity.BUFFERING);
        LocalBroadcastManager.getInstance(this).sendBroadcast(bufferingPlayerIntent);
    }

    private void processPauseRequest() {
        if (player != null && player.isPlaying()) {
            player.setPlayWhenReady(false);

            sendUpdatePlayerIntent();
            mState = State.Paused;


            relaxResources();
            buildNotification(false);
        }
    }

    /**
     * There is no media style notification for operating systems below api 21. So This method builds
     * a simple compat notification that has a play or pause button depending on if the player is
     * paused or played. if foreGroundOrUpdate then the service should go to the foreground. else
     * just update the notification.
     */
    private void buildNotification(boolean startForeground) {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);

        notificationLayout.setTextViewText(R.id.notification_title, mStreamName);
        notificationLayoutExpanded.setTextViewText(R.id.notification_title, mStreamName);

        if (mState == State.Paused || mState == State.Stopped) {
            Intent playButton = new Intent(ACTION_PLAY);
            playButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0, playButton, 0);
            notificationLayoutExpanded.setOnClickPendingIntent(R.id.iv_play, pendingPlayIntent);
            notificationLayoutExpanded.setViewVisibility(R.id.iv_play, View.VISIBLE);
        } else {
            Intent stopButton = new Intent(ACTION_PAUSE);
            stopButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, 0, stopButton, 0);
            notificationLayoutExpanded.setOnClickPendingIntent(R.id.iv_stop, pendingStopIntent);
            notificationLayoutExpanded.setViewVisibility(R.id.iv_stop, View.VISIBLE);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Audio Stream");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setCustomContentView(notificationLayout);
        builder.setCustomBigContentView(notificationLayoutExpanded);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "notification_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Audio Streaming Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        Notification notification = builder.build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (startForeground)
            startForeground(NOTIFICATION_ID, builder.build());
        else
            notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return playerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        stopPlayer();
    }

    //give up wifi lock if it is held and stop the service from being a foreground service.
    private void relaxResources() {
        //Release the WifiLock resource
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }
        // stop service from being a foreground service. Passing true removes the notification as well.
        stopForeground(true);
    }

    private void removeNotification() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    private void giveUpAudioFocus() {
        if ((mAudioFocus == AudioFocus.Focused || mAudioFocus == AudioFocus.NoFocusCanDuck) &&
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this)) {
            mAudioFocus = AudioFocus.NoFocusNoDuck;
        }
    }

    public class PlayerBinder extends Binder {
        public StreamingService getService() {
            return StreamingService.this;
        }
    }
}