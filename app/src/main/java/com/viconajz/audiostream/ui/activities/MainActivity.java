package com.viconajz.audiostream.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.viconajz.audiostream.R;
import com.viconajz.audiostream.common.BaseAnimation;
import com.viconajz.audiostream.receivers.BufferingReceiver;
import com.viconajz.audiostream.receivers.StreamPlayStopReceiver;
import com.viconajz.audiostream.ui.fragments.main.MainFragment;
import com.viconajz.audiostream.ui.fragments.stream.StreamFragment;
import com.viconajz.audiostream.ui.fragments.stream.StreamingService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainActivityListener, StreamPlayStopReceiver.Listener, BufferingReceiver.Listener {

    public static final String BUFFERING = "BUFFERING";
    public static final String UPDATE_PLAYER = "UPDATE_PLAYER";
    private static final int REQUEST_WAKE_LOCK = 777;
    @BindView(R.id.fragment_container)
    FrameLayout flContainer;
    private String url;
    private String name;
    private StreamPlayStopReceiver receiver;
    private BufferingReceiver bufferingReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initMainFragment();
        IntentFilter filter = new IntentFilter();
        receiver = new StreamPlayStopReceiver();
        receiver.setListener(this);
        filter.addAction(StreamingService.ACTION_PLAY);
        filter.addAction(StreamingService.ACTION_PAUSE);
        registerReceiver(receiver, filter);
        IntentFilter intentFilter = new IntentFilter();
        bufferingReceiver = new BufferingReceiver();
        bufferingReceiver.setListener(this);
        intentFilter.addAction(BUFFERING);
        intentFilter.addAction(UPDATE_PLAYER);
        LocalBroadcastManager.getInstance(this).registerReceiver(bufferingReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
        if (bufferingReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(bufferingReceiver);
    }

    private void initMainFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = MainFragment.newInstance();
            String fragmentTag = fragment.getClass().getName();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, fragment, fragmentTag).commit();
        }
    }

    public void navigateToFragment(Fragment fragment, BaseAnimation baseAnimation, boolean clearPrev) {
        String fragmentTag = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = manager.beginTransaction();
            switch (baseAnimation) {
                case FadeIn:
                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    break;
                case SlideLeft:
                    ft.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
                    break;
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.fragment_container, fragment, fragmentTag);

            if (!clearPrev) {
                ft.addToBackStack(fragmentTag);
            }
            ft.commit();
        }
    }

    public void clearAndAddFragment(Fragment fragment) {
        String backStackName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, fragment, backStackName);

        ft.commit();
    }

    @Override
    public void startService(String url, String name) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            this.url = url;
            this.name = name;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, REQUEST_WAKE_LOCK);
        } else {
            Intent intent = new Intent(this, StreamingService.class);
            intent.putExtra("EXTRA_URL", url);
            intent.putExtra("EXTRA_NAME", name);
            intent.setAction(StreamingService.ACTION_PLAY);
            startService(intent);
        }
    }

    @Override
    public void startService(String action) {
        Intent intent = new Intent(this, StreamingService.class);
        intent.setAction(action);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WAKE_LOCK:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startService(url, name);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActionChanged(String action) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof StreamFragment) {
            ((StreamFragment) fragment).updateStream(action);
        }
    }

    @Override
    public void onBufferingUpdate(String action) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof StreamFragment) {
            ((StreamFragment) fragment).updateStream(action);
        }
    }
}
