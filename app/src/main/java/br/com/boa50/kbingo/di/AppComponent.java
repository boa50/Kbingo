package br.com.boa50.kbingo.di;

import javax.inject.Singleton;

import br.com.boa50.kbingo.KbingoApplication;
import br.com.boa50.kbingo.data.DataModule;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        DataModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<KbingoApplication>{
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<KbingoApplication> {}
}
