package br.com.boa50.kbingo.realizasorteio;

import android.support.annotation.NonNull;
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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
public class RealizaSorteioPresenter implements RealizaSorteioContract.Presenter {

    private RealizaSorteioContract.View mView;

    private final AppDataSource mAppDataSource;

    private final BaseSchedulerProvider mScheduleProvider;

    private CompositeDisposable mCompositeDisposable;

    private ArrayList<Pedra> mPedras;

    private List<Integer> mPosicoes;

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
        if (mPosicoes.isEmpty()) {
            mView.apresentarFimSorteio();
        } else {
            mView.apresentarPedra(mPedras.get(mPosicoes.get(0)));

            mPedras.get(mPosicoes.get(0)).setSorteada(true);

            mView.atualizarPedra(mPosicoes.get(0));

            mPosicoes.remove(0);
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
            Disposable disposable = mAppDataSource
                    .getPedras()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            pedras -> {
                                mPedras = new ArrayList<>();
                                mPedras.addAll(pedras);
                                iniciarLayout();
                            },
                            throwable -> mPedras = null
                    );

            mCompositeDisposable.add(disposable);
        } else {
            iniciarLayout();
        }
    }

    private void iniciarLayout() {
        preencherPosicoesSorteio();

        Disposable disposable = mAppDataSource
                .getLetras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        letras -> mView.iniciarLayout(letras, mPedras)
                );

        mCompositeDisposable.add(disposable);
    }

    private void preencherPosicoesSorteio() {
        if (mPosicoes == null)
            mPosicoes = new ArrayList<>();
        else
            mPosicoes.clear();

        for (int i = 0; i < mPedras.size(); i++) {
            if (!mPedras.get(i).isSorteada())
                mPosicoes.add(i);
        }

        Collections.shuffle(mPosicoes);
    }

    @VisibleForTesting
    List<Integer> getmPosicoes() {
        return mPosicoes;
    }
}
