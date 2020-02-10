package com.viconajz.audiostream.ui.di.application;

import android.content.Context;

import com.viconajz.audiostream.api.AudioApi;
import com.viconajz.audiostream.api.modules.GsonApiModule;
import com.viconajz.audiostream.ui.di.main.IMainComponent;
import com.viconajz.audiostream.ui.di.main.MainModule;
import com.viconajz.audiostream.ui.di.stream.IStreamComponent;
import com.viconajz.audiostream.ui.di.stream.StreamModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, GsonApiModule.class})
@Singleton
public interface IAppComponent {
    Context context();

    AudioApi audioApi();

    IMainComponent plus(MainModule mainModule);

    IStreamComponent plus(StreamModule streamModule);
}
