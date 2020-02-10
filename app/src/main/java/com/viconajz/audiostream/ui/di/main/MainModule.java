package com.viconajz.audiostream.ui.di.main;

import com.viconajz.audiostream.api.AudioApi;
import com.viconajz.audiostream.ui.fragments.main.IMainContract;
import com.viconajz.audiostream.ui.fragments.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private final IMainContract.View mView;

    public MainModule(IMainContract.View view) {
        this.mView = view;
    }

    @Provides
    @MainScope
    IMainContract.View provideMainView() {
        return this.mView;
    }

    @Provides
    @MainScope
    IMainContract.Presenter provideMainPresenter(AudioApi audioApi) {
        return new MainPresenter(this.mView, audioApi);
    }
}
