package br.com.boa50.kbingo.sorteiocartela;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SorteioCartelaModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SorteioCartelaFragment sorteioCartelaFragment();

    @ActivityScoped
    @Binds
    abstract SorteioCartelaContract.Presenter sorteioCartelaPresenter(SorteioCartelaPresenter presenter);
}
