package br.com.boa50.kbingo.conferecartelas;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import javax.inject.Inject;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.ArrayUtils;

@ActivityScoped
public class ConfereCartelasPresenter implements ConfereCartelasContract.Presenter {

    private ConfereCartelasContract.View mView;
    private ArrayList<String> mCartelasGanhadorasBackup;
    private ArrayList<String> mCartelasGanhadoras;
    private String mTextoPadrao;

    @Inject
    ConfereCartelasPresenter() {}

    @Override
    public void subscribe(@NonNull ConfereCartelasContract.View view,
                          @NonNull ArrayList<String> cartelasGanhadoras,
                          @NonNull String textoPadrao) {
        mView  = view;
        mCartelasGanhadorasBackup = cartelasGanhadoras;
        mTextoPadrao = textoPadrao;
        organizarCartelas();
    }

    @Override
    public ArrayList<String> getCartelasGanhadoras() {
        return mCartelasGanhadoras;
    }

    @Override
    public int getCartelasGanhadorasSize() {
        if (mCartelasGanhadorasBackup.get(0).equalsIgnoreCase(mTextoPadrao)) {
            return mCartelasGanhadorasBackup.size() - 1;
        }

        return mCartelasGanhadorasBackup.size();
    }

    @Override
    public void subscribe(@NonNull ConfereCartelasContract.View view) {
        subscribe(view, new ArrayList<>(), "");
    }

    @Override
    public void unsubscribe() {
        mView = null;
    }

    @Override
    public void filtrarCartelas(String filtro) {
        mCartelasGanhadoras.clear();
        mCartelasGanhadoras.addAll(ArrayUtils.filtrar(mCartelasGanhadorasBackup, filtro));
        if (mCartelasGanhadoras.isEmpty() || !mCartelasGanhadoras.get(0).equalsIgnoreCase(mTextoPadrao)) {
            mCartelasGanhadoras.add(0, mTextoPadrao);
        }

        mView.apresentarCartelasFiltradas();
    }

    private void organizarCartelas() {
        if (!mCartelasGanhadorasBackup.get(0).equalsIgnoreCase(mTextoPadrao)) {
            mCartelasGanhadorasBackup.add(0, mTextoPadrao);
        }
        mCartelasGanhadoras = new ArrayList<>(mCartelasGanhadorasBackup);

        mView.apresentarCartelas();
    }
}
