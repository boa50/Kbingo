package br.com.boa50.kbingo.di;

import br.com.boa50.kbingo.MainActivity;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioModule;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasActivity;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RealizaSorteioModule.class)
    abstract RealizaSorteioActivity realizaSorteioActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = VisualizaCartelasModule.class)
    abstract VisualizaCartelasActivity visualizaCartelasActivity();
}
