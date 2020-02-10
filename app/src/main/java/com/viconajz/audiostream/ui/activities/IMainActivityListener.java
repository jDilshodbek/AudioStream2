package com.viconajz.audiostream.ui.activities;

import androidx.fragment.app.Fragment;

import com.viconajz.audiostream.common.BaseAnimation;

public interface IMainActivityListener {
    void navigateToFragment(Fragment fragment, BaseAnimation baseAnimation, boolean clearOld);
    void startService(String url, String name);
    void startService(String action);
}
