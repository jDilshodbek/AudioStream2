package com.viconajz.audiostream.ui.di.stream;

import com.viconajz.audiostream.ui.fragments.stream.StreamFragment;

import dagger.Subcomponent;

@StreamScope
@Subcomponent(modules = {StreamModule.class})
public interface IStreamComponent {
    void inject(StreamFragment view);
}
