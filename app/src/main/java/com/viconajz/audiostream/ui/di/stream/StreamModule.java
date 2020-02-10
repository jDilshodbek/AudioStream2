package com.viconajz.audiostream.ui.di.stream;

import com.viconajz.audiostream.api.AudioApi;
import com.viconajz.audiostream.ui.fragments.stream.IStreamContract;
import com.viconajz.audiostream.ui.fragments.stream.StreamPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class StreamModule {
    private final IStreamContract.View mView;

    public StreamModule(IStreamContract.View view) {
        this.mView = view;
    }

    @Provides
    @StreamScope
    IStreamContract.View provideStreamView() {
        return this.mView;
    }

    @Provides
    @StreamScope
    IStreamContract.Presenter provideStreamPresenter(AudioApi audioApi) {
        return new StreamPresenter(this.mView, audioApi);
    }
}
