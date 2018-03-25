package br.com.boa50.kbingo;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by boa50 on 3/25/18.
 */

public class KbingoApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        //TODO caçar o DaggerAppComponent
        return null;
    }
}
