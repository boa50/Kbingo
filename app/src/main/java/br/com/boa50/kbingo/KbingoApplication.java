package br.com.boa50.kbingo;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.source.LetrasRepository;
import br.com.boa50.kbingo.data.source.PedrasRepository;
import br.com.boa50.kbingo.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by boa50 on 3/25/18.
 */

public class KbingoApplication extends DaggerApplication {

    @Inject
    PedrasRepository pedrasRepository;

    @Inject
    LetrasRepository letrasRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
