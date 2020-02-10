package com.viconajz.audiostream.ui.fragments.main;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.viconajz.audiostream.App;
import com.viconajz.audiostream.R;
import com.viconajz.audiostream.api.modules.GsonApiModule;
import com.viconajz.audiostream.common.BaseAnimation;
import com.viconajz.audiostream.ui.di.main.MainModule;
import com.viconajz.audiostream.ui.fragments.CoreFragment;
import com.viconajz.audiostream.ui.fragments.stream.StreamFragment;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends CoreFragment implements IMainContract.View {
    @Inject
    IMainContract.Presenter mPresenter;

    @BindView(R.id.et_ip)
    EditText etIp;

    public static Fragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(Objects.requireNonNull(getContext())).applicationComponent().plus(new MainModule(this)).inject(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.btn_connect)
    public void onConnectClick(){
        mActivityListener.navigateToFragment(StreamFragment.newInstance(etIp.getText().toString()), BaseAnimation.FadeIn, false);
    }
}
