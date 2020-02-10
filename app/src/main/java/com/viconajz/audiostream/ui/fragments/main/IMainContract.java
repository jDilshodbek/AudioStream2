package com.viconajz.audiostream.ui.fragments.main;

import com.viconajz.audiostream.ui.infrastructures.IBasePresenter;
import com.viconajz.audiostream.ui.infrastructures.IBaseView;

public interface IMainContract {
    interface Presenter extends IBasePresenter {
    }

    interface View extends IBaseView<Presenter> {
    }
}
