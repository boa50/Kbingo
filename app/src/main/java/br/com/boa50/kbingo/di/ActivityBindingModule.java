package br.com.boa50.kbingo.di;

import br.com.boa50.kbingo.BaseActivity;
import br.com.boa50.kbingo.MainActivity;
import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.conferecartelas.ConfereCartelasModule;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioModule;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RealizaSorteioModule.class, VisualizaCartelasModule.class})
    abstract BaseActivity baseActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ConfereCartelasModule.class)
    abstract ConfereCartelasActivity conferirCartelasActivity();
}
