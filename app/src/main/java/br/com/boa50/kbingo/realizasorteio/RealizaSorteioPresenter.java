package br.com.boa50.kbingo.realizasorteio;

import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.data.source.PedrasRepository;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boa50 on 3/11/18.
 */

@ActivityScoped
public class RealizaSorteioPresenter implements RealizaSorteioContract.Presenter {

    private RealizaSorteioContract.View mView;

    private final PedrasRepository mPedrasRespository;

    private final BaseSchedulerProvider mScheduleProvider;

    private CompositeDisposable mCompositeDisposable;

    private List<Pedra> mPedras;

    private List<Integer> posicoes;

    @Inject
    RealizaSorteioPresenter(PedrasRepository pedrasRespository, BaseSchedulerProvider schedulerProvider) {
        mPedrasRespository = checkNotNull(pedrasRespository, "Pedras Repository cannot be null");
        mScheduleProvider = checkNotNull(schedulerProvider, "Schedule Provider cannot null");

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

            mPedras.get(posicoes.get(0)).setmSorteada(true);

            posicoes.remove(0);

            mView.atualizarPedras();
        }
    }

    @Override
    public void resetarPedras() {
        for (Pedra pedra : mPedras) {
            pedra.setmSorteada(false);
        }

        preencherPosicoesSorteio();

        mView.reiniciarSorteio();
    }

    private void carregarPedras(){
        if (mPedras == null) {
            mCompositeDisposable.clear();

            Disposable disposable = mPedrasRespository
                    .getPedras()
                    .flatMap(Flowable::fromIterable)
                    .toList()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            pedras -> {
                                mPedras = pedras;
                                mView.iniciarPedras(mPedras);
                                preencherPosicoesSorteio();
                            },
                            throwable -> mPedras = null
                    );

            mCompositeDisposable.add(disposable);
        }
    }

    private void preencherPosicoesSorteio() {
        if (posicoes == null)
            posicoes = new ArrayList<>();
        else
            posicoes.clear();

        for (int i = 0; i < mPedras.size(); i++) {
            posicoes.add(i);
        }
        Collections.shuffle(posicoes);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    List<Integer> getPosicoes() {
        return posicoes;
    }
}
