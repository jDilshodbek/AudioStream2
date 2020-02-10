// Generated by Dagger (https://google.github.io/dagger).
package com.viconajz.audiostream.ui.di.application;

import android.content.Context;
import com.viconajz.audiostream.api.AudioApi;
import com.viconajz.audiostream.api.modules.GsonApiModule;
import com.viconajz.audiostream.api.modules.GsonApiModule_ProvideApiServiceFactory;
import com.viconajz.audiostream.ui.di.main.IMainComponent;
import com.viconajz.audiostream.ui.di.main.MainModule;
import com.viconajz.audiostream.ui.di.main.MainModule_ProvideMainPresenterFactory;
import com.viconajz.audiostream.ui.di.stream.IStreamComponent;
import com.viconajz.audiostream.ui.di.stream.StreamModule;
import com.viconajz.audiostream.ui.di.stream.StreamModule_ProvideStreamPresenterFactory;
import com.viconajz.audiostream.ui.fragments.main.IMainContract;
import com.viconajz.audiostream.ui.fragments.main.MainFragment;
import com.viconajz.audiostream.ui.fragments.main.MainFragment_MembersInjector;
import com.viconajz.audiostream.ui.fragments.stream.IStreamContract;
import com.viconajz.audiostream.ui.fragments.stream.StreamFragment;
import com.viconajz.audiostream.ui.fragments.stream.StreamFragment_MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerIAppComponent implements IAppComponent {
  private Provider<Context> provideContextProvider;

  private Provider<AudioApi> provideApiServiceProvider;

  private DaggerIAppComponent(Builder builder) {
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {
    this.provideContextProvider =
        DoubleCheck.provider(AppModule_ProvideContextFactory.create(builder.appModule));
    this.provideApiServiceProvider =
        DoubleCheck.provider(GsonApiModule_ProvideApiServiceFactory.create(builder.gsonApiModule));
  }

  @Override
  public Context context() {
    return provideContextProvider.get();
  }

  @Override
  public AudioApi audioApi() {
    return provideApiServiceProvider.get();
  }

  @Override
  public IMainComponent plus(MainModule mainModule) {
    return new IMainComponentImpl(mainModule);
  }

  @Override
  public IStreamComponent plus(StreamModule streamModule) {
    return new IStreamComponentImpl(streamModule);
  }

  public static final class Builder {
    private AppModule appModule;

    private GsonApiModule gsonApiModule;

    private Builder() {}

    public IAppComponent build() {
      if (appModule == null) {
        throw new IllegalStateException(AppModule.class.getCanonicalName() + " must be set");
      }
      if (gsonApiModule == null) {
        this.gsonApiModule = new GsonApiModule();
      }
      return new DaggerIAppComponent(this);
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder gsonApiModule(GsonApiModule gsonApiModule) {
      this.gsonApiModule = Preconditions.checkNotNull(gsonApiModule);
      return this;
    }
  }

  private final class IMainComponentImpl implements IMainComponent {
    private MainModule mainModule;

    private Provider<IMainContract.Presenter> provideMainPresenterProvider;

    private IMainComponentImpl(MainModule mainModule) {
      initialize(mainModule);
    }

    @SuppressWarnings("unchecked")
    private void initialize(final MainModule mainModule) {
      this.mainModule = Preconditions.checkNotNull(mainModule);
      this.provideMainPresenterProvider =
          DoubleCheck.provider(
              MainModule_ProvideMainPresenterFactory.create(
                  mainModule, DaggerIAppComponent.this.provideApiServiceProvider));
    }

    @Override
    public void inject(MainFragment view) {
      injectMainFragment(view);
    }

    private MainFragment injectMainFragment(MainFragment instance) {
      MainFragment_MembersInjector.injectMPresenter(instance, provideMainPresenterProvider.get());
      return instance;
    }
  }

  private final class IStreamComponentImpl implements IStreamComponent {
    private StreamModule streamModule;

    private Provider<IStreamContract.Presenter> provideStreamPresenterProvider;

    private IStreamComponentImpl(StreamModule streamModule) {
      initialize(streamModule);
    }

    @SuppressWarnings("unchecked")
    private void initialize(final StreamModule streamModule) {
      this.streamModule = Preconditions.checkNotNull(streamModule);
      this.provideStreamPresenterProvider =
          DoubleCheck.provider(
              StreamModule_ProvideStreamPresenterFactory.create(
                  streamModule, DaggerIAppComponent.this.provideApiServiceProvider));
    }

    @Override
    public void inject(StreamFragment view) {
      injectStreamFragment(view);
    }

    private StreamFragment injectStreamFragment(StreamFragment instance) {
      StreamFragment_MembersInjector.injectMPresenter(
          instance, provideStreamPresenterProvider.get());
      return instance;
    }
  }
}
