// Generated code from Butter Knife. Do not modify!
package com.viconajz.audiostream.ui.fragments.main;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.viconajz.audiostream.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainFragment_ViewBinding implements Unbinder {
  private MainFragment target;

  private View view7f080045;

  @UiThread
  public MainFragment_ViewBinding(final MainFragment target, View source) {
    this.target = target;

    View view;
    target.etIp = Utils.findRequiredViewAsType(source, R.id.et_ip, "field 'etIp'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_connect, "method 'onConnectClick'");
    view7f080045 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onConnectClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etIp = null;

    view7f080045.setOnClickListener(null);
    view7f080045 = null;
  }
}
