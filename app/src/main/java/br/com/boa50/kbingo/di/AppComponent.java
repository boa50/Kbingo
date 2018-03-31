package br.com.boa50.kbingo.di;

import android.app.Application;

import javax.inject.Singleton;

import br.com.boa50.kbingo.KbingoApplication;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {RealizaSorteioModule.class})
public interface AppComponent extends AndroidInjector<KbingoApplication>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
