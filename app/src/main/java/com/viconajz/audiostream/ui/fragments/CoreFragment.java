package com.viconajz.audiostream.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.viconajz.audiostream.ui.activities.IMainActivityListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class CoreFragment extends Fragment {
    protected IMainActivityListener mActivityListener;
    protected View mView;
    private Unbinder unbinder;

    @LayoutRes
    protected abstract int getFragmentLayout();

    protected abstract void initView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityListener = (IMainActivityListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(this.getFragmentLayout(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        mActivityListener = null;
        super.onDestroy();
    }

    public View getCurrentView() {
        return mView;
    }
}
