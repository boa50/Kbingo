package br.com.boa50.kbingo.realizasorteio;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
public class RealizaSorteioPresenter implements RealizaSorteioContract.Presenter {

    private RealizaSorteioContract.View mView;
    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private CompositeDisposable mCompositeDisposable;

    private List<Integer> mIdsSorteio;

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
        mView = view;
        iniciarLayout();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mView = null;
    }

    @Override
    public void sortearPedra() {
        if (mIdsSorteio.isEmpty()) {
            mView.apresentarFimSorteio();
        } else {
            Disposable disposable = mAppDataSource
                    .getPedra(mIdsSorteio.get(0))
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(pedra -> {
                        mAppDataSource.updatePedraSorteada(pedra.getId());

                        mView.apresentarPedra(pedra);
                        mView.atualizarPedra(pedra.getId());
                        atualizarCartelasGanhadoras();

                        mIdsSorteio.remove(0);
                    });
            mCompositeDisposable.add(disposable);
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
        if (mIdsSorteio.isEmpty())  mView.apresentarFimSorteio();
        else if (mAppDataSource.getUltimaPedraSorteada() != null) {
            mView.apresentarPedra(mAppDataSource.getUltimaPedraSorteada());
        }
    }

    @Override
    public void resetarPedras() {
        mAppDataSource.cleanPedrasSorteadas();
        mAppDataSource.cleanCartelasGanhadoras();

        preencherIdsSorteio();
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
        return mAppDataSource.hasPedraSorteada();
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
        final List<Letra> outLetras = new ArrayList<>();
        final ArrayList<Pedra> outPedras = new ArrayList<>();

        Disposable disposable = mAppDataSource
                .getLetras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        letras -> {
                            outLetras.addAll(letras);
                            if (!outPedras.isEmpty()) {
                                iniciarLayout(outLetras, outPedras);
                            }
                        }
                );
        mCompositeDisposable.add(disposable);

        Disposable disposable2 = mAppDataSource
                .getPedras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        pedras -> {
                            outPedras.addAll(pedras);
                            if (!outLetras.isEmpty()) {
                                iniciarLayout(outLetras, outPedras);
                                preencherIdsSorteio();
                            } else {
                                iniciarLayout();
                            }
                        }
                );
        mCompositeDisposable.add(disposable2);
    }

    private void iniciarLayout(List<Letra> letras, ArrayList<Pedra> pedras) {
        mView.iniciarLayout(letras, pedras);
    }

    private void preencherIdsSorteio() {
        if (mIdsSorteio == null)
            mIdsSorteio = new ArrayList<>();
        else
            mIdsSorteio.clear();

        Disposable disposable = mAppDataSource
                .getPedras()
                .flatMap(pedras -> Flowable.fromIterable(pedras)
                    .filter(pedra -> !pedra.isSorteada()))
                    .doOnNext(pedra -> mIdsSorteio.add(pedra.getId()))
                    .toList()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(pedras -> {
                    Collections.shuffle(mIdsSorteio);
                    apresentarUltimaPedraSorteada();
                    carregarCartelas();
                });
        mCompositeDisposable.add(disposable);
    }

    @VisibleForTesting
    List<Integer> getIdsSorteio() {
        return mIdsSorteio;
    }
}
