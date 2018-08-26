package br.com.boa50.kbingo.sorteiocartela;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.di.ActivityScoped;

@ActivityScoped
public class SorteioCartelaPresenter implements SorteioCartelaContract.Presenter {

    private SorteioCartelaContract.View mView;

    private ArrayList<String> mCartelasSorteaveis;

    @Inject
    SorteioCartelaPresenter() {}

    @Override
    public void subscribe(@NonNull SorteioCartelaContract.View view) {
        mView = view;
        mCartelasSorteaveis = new ArrayList<>();
    }

    @Override
    public void unsubscribe() {
        mView = null;
    }

    @Override
    public void sortearCartela() {
        if (mCartelasSorteaveis.isEmpty()) {
            for (int i = 1; i <= 200; i++) {
                mCartelasSorteaveis.add(String.format(
                        Locale.ENGLISH,
                        Constant.FORMAT_CARTELA,
                        i));
            }
        }

        mView.apresentarCartela(mCartelasSorteaveis.get(
                new Random().nextInt(mCartelasSorteaveis.size())));
    }
}
