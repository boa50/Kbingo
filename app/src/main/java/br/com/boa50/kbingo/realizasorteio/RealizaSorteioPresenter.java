package br.com.boa50.kbingo.realizasorteio;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by boa50 on 3/11/18.
 */

@ActivityScoped
public class RealizaSorteioPresenter implements RealizaSorteioContract.Presenter {

    private RealizaSorteioContract.View mView;

    private final AppDataSource mAppDataSource;

    private final BaseSchedulerProvider mScheduleProvider;

    private CompositeDisposable mCompositeDisposable;

    private ArrayList<Pedra> mPedras;

    private List<Integer> posicoes;

    @Inject
    RealizaSorteioPresenter(
            @NonNull AppDataSource appDataSource,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(BaseView view) {
        mView = (RealizaSorteioContract.View) view;
        carregarPedras();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mView = null;
    }

    @Override
    public void sortearPedra() {
        if (posicoes.isEmpty()) {
            mView.apresentarFimSorteio();
        } else {
            mView.apresentarPedra(mPedras.get(posicoes.get(0)));

            mPedras.get(posicoes.get(0)).setSorteada(true);

            mView.atualizarPedra(posicoes.get(0));

            posicoes.remove(0);
        }
    }

    @Override
    public void resetarPedras() {
        for (Pedra pedra : mPedras) {
            pedra.setSorteada(false);
        }

        preencherPosicoesSorteio();

        mView.reiniciarSorteio();
    }

    @Override
    public void setPedras(ArrayList<Pedra> mPedras) {
        this.mPedras = mPedras;
    }

    private void carregarPedras(){
        if (mPedras == null) {
            mCompositeDisposable.clear();
            mPedras = new ArrayList<>();

            Disposable disposable = mAppDataSource
                    .getPedras()
                    .flatMap(Flowable::fromIterable)
                    .toList()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            pedras -> {
                                mPedras.addAll(pedras);
                                mView.iniciarPedras(mPedras);
                                preencherPosicoesSorteio();
                            },
                            throwable -> mPedras = null
                    );

            mCompositeDisposable.add(disposable);
        } else {
            mView.iniciarPedras(mPedras);
            preencherPosicoesSorteio();
        }

        mView.iniciarLayout(mAppDataSource.getLetras());
    }

    private void preencherPosicoesSorteio() {
        if (posicoes == null)
            posicoes = new ArrayList<>();
        else
            posicoes.clear();

        for (int i = 0; i < mPedras.size(); i++) {
            if (!mPedras.get(i).isSorteada())
                posicoes.add(i);
        }

        Collections.shuffle(posicoes);
    }

    @VisibleForTesting
    List<Integer> getPosicoes() {
        return posicoes;
    }
}
