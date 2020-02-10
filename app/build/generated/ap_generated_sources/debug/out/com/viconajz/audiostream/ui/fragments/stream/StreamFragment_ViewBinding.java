// Generated code from Butter Knife. Do not modify!
package com.viconajz.audiostream.ui.fragments.stream;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.viconajz.audiostream.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StreamFragment_ViewBinding implements Unbinder {
  private StreamFragment target;

  @UiThread
  public StreamFragment_ViewBinding(StreamFragment target, View source) {
    this.target = target;

    target.rvStreams = Utils.findRequiredViewAsType(source, R.id.rv_streams, "field 'rvStreams'", RecyclerView.class);
    target.tvError = Utils.findRequiredViewAsType(source, R.id.tv_error, "field 'tvError'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StreamFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvStreams = null;
    target.tvError = null;
  }
}
