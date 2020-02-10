// Generated by Dagger (https://google.github.io/dagger).
package com.viconajz.audiostream.ui.di.stream;

import com.viconajz.audiostream.ui.fragments.stream.IStreamContract;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class StreamModule_ProvideStreamViewFactory implements Factory<IStreamContract.View> {
  private final StreamModule module;

  public StreamModule_ProvideStreamViewFactory(StreamModule module) {
    this.module = module;
  }

  @Override
  public IStreamContract.View get() {
    return provideInstance(module);
  }

  public static IStreamContract.View provideInstance(StreamModule module) {
    return proxyProvideStreamView(module);
  }

  public static StreamModule_ProvideStreamViewFactory create(StreamModule module) {
    return new StreamModule_ProvideStreamViewFactory(module);
  }

  public static IStreamContract.View proxyProvideStreamView(StreamModule instance) {
    return Preconditions.checkNotNull(
        instance.provideStreamView(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
