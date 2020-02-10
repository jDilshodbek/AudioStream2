package com.viconajz.audiostream.ui.fragments.stream;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viconajz.audiostream.App;
import com.viconajz.audiostream.R;
import com.viconajz.audiostream.ui.di.stream.StreamModule;
import com.viconajz.audiostream.ui.fragments.CoreFragment;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;

public class StreamFragment extends CoreFragment implements IStreamContract.View {

    private static final String EXTRA_IP = "EXTRA_IP";
    private String ip;

    @Inject
    IStreamContract.Presenter mPresenter;

    @BindView(R.id.rv_streams)
    RecyclerView rvStreams;

    @BindView(R.id.tv_error)
    TextView tvError;

    public static Fragment newInstance(String ip) {
        Bundle args = new Bundle();
        args.putString(EXTRA_IP, ip);
        Fragment fragment = new StreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(Objects.requireNonNull(getContext())).applicationComponent().plus(new StreamModule(this)).inject(this);
        ip = Objects.requireNonNull(getArguments()).getString(EXTRA_IP);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_stream;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStreams.setLayoutManager(layoutManager);
        mPresenter.loadStreams(ip);
    }

    @Override
    public void setAdapter(StreamAdapter adapter) {
        rvStreams.setAdapter(adapter);
    }

    @Override
    public void setText(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void play(String url, String name) {
        mActivityListener.startService(url, name);
    }

    @Override
    public void pause() {
        mActivityListener.startService(StreamingService.ACTION_PAUSE);
    }

    @Override
    public Context getApplicationContext() {
        return getApplicationContext();
    }

    public void updateStream(String action) {
        mPresenter.updateStream(action);
    }
}
