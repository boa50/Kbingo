package br.com.boa50.kbingo.realizasorteio;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.PedraUtils;
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
    private Pedra mUltimaPedraSorteada;
    private int mTipoSorteio;

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
    public void subscribe(@NonNull RealizaSorteioContract.View view) {
        subscribe(view, null);
    }

    @Override
    public void subscribe(@NonNull RealizaSorteioContract.View view, @Nullable RealizaSorteioContract.State state) {
        mView = view;

        if (state != null) {
            mPedras = state.getPedras();
            mUltimaPedraSorteada = state.getUltimaPedraSorteada();
            mTipoSorteio = state.getTipoSorteio();
        } else {
            mPedras = null;
            mUltimaPedraSorteada = null;
            mTipoSorteio = TipoSorteioDTO.CARTELA_CHEIA;
        }

        carregarPedras();
    }

    @NonNull
    @Override
    public RealizaSorteioContract.State getState() {
        return new RealizaSorteioState(mPedras, mUltimaPedraSorteada, mTipoSorteio);
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
            mUltimaPedraSorteada = mPedras.get(mPosicoes.get(0));
            mUltimaPedraSorteada.setSorteada(true);
            mView.apresentarPedra(mUltimaPedraSorteada);

            mView.atualizarPedra(mPosicoes.get(0));
            mPosicoes.remove(0);
        }
    }

    private void apresentarUltimaPedraSorteada() {
        if (mPosicoes.isEmpty())  mView.apresentarFimSorteio();
        else if (mUltimaPedraSorteada != null) mView.apresentarPedra(mUltimaPedraSorteada);
    }

    @Override
    public void resetarPedras() {
        for (Pedra pedra : mPedras) {
            pedra.setSorteada(false);
        }

        preencherPosicoesSorteio();

        mUltimaPedraSorteada = null;

        mView.reiniciarSorteio();
    }

    @Override
    public void alterarTipoSorteio(int tipoSorteio) {
        mTipoSorteio = tipoSorteio;

        mView.apresentarTipoSorteio(true);
    }

    @Override
    public boolean hasPedraSorteada() {
        return mPedras != null && PedraUtils.hasPedraSorteda(mPedras);
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
                        letras -> {
                            mView.iniciarLayout(letras);
                            apresentarUltimaPedraSorteada();
                        }
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
