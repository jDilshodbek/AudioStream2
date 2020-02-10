package com.viconajz.audiostream.ui.infrastructures;

import com.viconajz.audiostream.api.AudioApi;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenterImp<T extends IBaseView> implements IBasePresenter {
    protected T mView;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AudioApi audioApi;

    public BasePresenterImp(T view, AudioApi audioApi) {
        mView = view;
        this.audioApi = audioApi;
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
    }
}

