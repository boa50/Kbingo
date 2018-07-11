package br.com.boa50.kbingo.realizasorteio;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RealizaSorteioModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract RealizaSorteioFragment realizaSorteioFragment();

    @ActivityScoped
    @Binds
    abstract RealizaSorteioContract.Presenter realizaSorteioPresenter(RealizaSorteioPresenter presenter);
}
