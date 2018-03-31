package br.com.boa50.kbingo.di;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = RealizaSorteioModule.class)
    abstract RealizaSorteioActivity realizaSorteioActivity();
}
