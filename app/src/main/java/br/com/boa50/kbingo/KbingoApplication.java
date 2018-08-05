package br.com.boa50.kbingo;

import br.com.boa50.kbingo.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class KbingoApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }

    private int tipoSorteio = -1;

    public int getTipoSorteio() {
        return tipoSorteio;
    }

    public void setTipoSorteio(int tipoSorteio) {
        this.tipoSorteio = tipoSorteio;
    }
}
