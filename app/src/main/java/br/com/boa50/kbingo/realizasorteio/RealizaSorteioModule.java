package br.com.boa50.kbingo.realizasorteio;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by boa50 on 21/03/18.
 */

@Module
public abstract class RealizaSorteioModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract RealizaSorteioFragment realizaSorteioFragment();

    @ActivityScoped
    @Binds
    abstract RealizaSorteioContract.Presenter realizaSorteioPresenter(RealizaSorteioPresenter presenter);
}
