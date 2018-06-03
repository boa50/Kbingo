package br.com.boa50.kbingo.visualizacartelas;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class VisualizaCartelasModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract VisualizaCartelasFragment visualizaCartelasFragment();

    @ActivityScoped
    @Binds
    abstract VisualizaCartelasContract.Presenter
        visualizaCartelasPresenter(VisualizaCartelasPresenter visualizaCartelasPresenter);
}
