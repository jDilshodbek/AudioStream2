// Generated code from Butter Knife. Do not modify!
package com.viconajz.audiostream.ui.fragments.stream;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.viconajz.audiostream.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StreamHolder_ViewBinding implements Unbinder {
  private StreamHolder target;

  @UiThread
  public StreamHolder_ViewBinding(StreamHolder target, View source) {
    this.target = target;

    target.btnStream = Utils.findRequiredViewAsType(source, R.id.btn_stream, "field 'btnStream'", Button.class);
    target.ivPlay = Utils.findRequiredViewAsType(source, R.id.iv_play, "field 'ivPlay'", ImageView.class);
    target.ivStop = Utils.findRequiredViewAsType(source, R.id.iv_stop, "field 'ivStop'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StreamHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnStream = null;
    target.ivPlay = null;
    target.ivStop = null;
  }
}
