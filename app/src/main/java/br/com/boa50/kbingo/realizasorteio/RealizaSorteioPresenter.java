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

    private List<Integer> mPosicoes;

    private ArrayList<Pedra> mPedras;
    private Pedra mUltimaPedraSorteada;

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
        } else {
            mPedras = null;
            mUltimaPedraSorteada = null;
        }

        carregarPedras();
        carregarCartelas();
    }

    @NonNull
    @Override
    public RealizaSorteioContract.State getState() {
        return new RealizaSorteioState(mPedras, mUltimaPedraSorteada/*, mCartelas*/);
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

            mAppDataSource.updateCartelas(mUltimaPedraSorteada);
            atualizarCartelasGanhadoras();

            mView.atualizarPedra(mPosicoes.get(0));
            mPosicoes.remove(0);
        }
    }

    @Override
    public void atualizarCartelasGanhadoras() {
        Disposable disposable = mAppDataSource
                .getCartelasGanhadoras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(cartelas -> mView.atualizarCartelasGanhadorasBadge(cartelas.size()));

        mCompositeDisposable.add(disposable);
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
        mAppDataSource.cleanCartelasGanhadoras();
        atualizarCartelasGanhadoras();

        mView.reiniciarSorteio();
    }

    @Override
    public TipoSorteioDTO getTipoSorteio() {
        return mAppDataSource.getTipoSorteio();
    }

    @Override
    public int getTipoSorteioId() {
        return mAppDataSource.getTipoSorteioId();
    }

    @Override
    public void alterarTipoSorteio(int tipoSorteio) {
        mAppDataSource.setTipoSorteioId(tipoSorteio);
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

    private void carregarCartelas() {
        Disposable disposable = mAppDataSource
                .getCartelas()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe();

        mCompositeDisposable.add(disposable);
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
