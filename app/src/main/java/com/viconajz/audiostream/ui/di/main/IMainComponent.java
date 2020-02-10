package com.viconajz.audiostream.ui.di.main;

import com.viconajz.audiostream.ui.fragments.main.MainFragment;

import dagger.Subcomponent;

@MainScope
@Subcomponent(modules = {MainModule.class})
public interface IMainComponent {
    void inject(MainFragment view);
}
