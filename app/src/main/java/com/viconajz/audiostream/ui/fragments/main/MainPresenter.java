package com.viconajz.audiostream.ui.fragments.main;

import com.viconajz.audiostream.api.AudioApi;
import com.viconajz.audiostream.ui.infrastructures.BasePresenterImp;

public class MainPresenter extends BasePresenterImp<IMainContract.View> implements IMainContract.Presenter{

    private AudioApi audioApi;

    public MainPresenter(IMainContract.View view, AudioApi audioApi) {
        super(view, audioApi);
        this.audioApi = audioApi;
    }
}
