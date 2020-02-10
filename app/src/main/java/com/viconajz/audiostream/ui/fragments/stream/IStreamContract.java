package com.viconajz.audiostream.ui.fragments.stream;

import android.content.Context;

import com.viconajz.audiostream.ui.fragments.main.IMainContract;
import com.viconajz.audiostream.ui.infrastructures.IBasePresenter;
import com.viconajz.audiostream.ui.infrastructures.IBaseView;

public interface IStreamContract {
    interface Presenter extends IBasePresenter {
        void loadStreams(String ip);
        void updateStream(String action);
    }

    interface View extends IBaseView<IMainContract.Presenter> {
        void setAdapter(StreamAdapter adapter);
        void setText(String message);
        void play(String url, String name);
        void pause();
        Context getApplicationContext();
    }
}
