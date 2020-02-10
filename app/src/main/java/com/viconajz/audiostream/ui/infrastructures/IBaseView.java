package com.viconajz.audiostream.ui.infrastructures;

import android.content.Context;
import android.view.View;

public interface IBaseView<T> {
    Context getContext();

    View getCurrentView();
}
