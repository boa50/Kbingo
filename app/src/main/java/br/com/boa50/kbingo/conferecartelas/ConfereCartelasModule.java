package br.com.boa50.kbingo.conferecartelas;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ConfereCartelasModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract ConfereCartelasFragment confereCartelasFragment();

    @ActivityScoped
    @Binds
    abstract ConfereCartelasContract.Presenter
        confereCartelasPresenter(ConfereCartelasPresenter confereCartelasPresenter);
}
