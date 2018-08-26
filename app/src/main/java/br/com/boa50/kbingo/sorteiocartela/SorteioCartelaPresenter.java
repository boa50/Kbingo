package br.com.boa50.kbingo.sorteiocartela;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import br.com.boa50.kbingo.di.ActivityScoped;

@ActivityScoped
public class SorteioCartelaPresenter implements SorteioCartelaContract.Presenter {

    private SorteioCartelaContract.View mView;

    @Inject
    SorteioCartelaPresenter() {}

    @Override
    public void subscribe(@NonNull SorteioCartelaContract.View view) {
        mView = view;
    }

    @Override
    public void unsubscribe() {
        mView = null;
    }
}
