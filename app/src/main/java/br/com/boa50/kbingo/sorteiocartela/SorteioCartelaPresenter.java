package br.com.boa50.kbingo.sorteiocartela;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.CartelaUtils;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
public class SorteioCartelaPresenter implements SorteioCartelaContract.Presenter {

    private SorteioCartelaContract.View mView;

    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private CompositeDisposable mCompositeDisposable;

    private ArrayList<Integer> mCartelasSorteaveis;

    @Inject
    SorteioCartelaPresenter(@NonNull AppDataSource appDataSource,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(@NonNull SorteioCartelaContract.View view) {
        mView = view;
        mCartelasSorteaveis = new ArrayList<>();
        recuperarCartelasSorteaveis();
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void sortearCartela() {
        mView.apresentarCartela(mCartelasSorteaveis.get(
                new Random().nextInt(mCartelasSorteaveis.size())));
    }

    @Override
    public void carregarFiltroCartelasSorteaveis() {
        carregarFiltroCartelasSorteaveis("", false);
    }

    @Override
    public void carregarFiltroCartelasSorteaveis(String filtro, boolean apenasGanhadoras) {
        Disposable disposable = mAppDataSource
                .getCartelasFiltro()
                .flatMap(cartelasFiltro -> Flowable.fromIterable(cartelasFiltro)
                        .filter(cartelaFiltroDTO -> {
                            if (apenasGanhadoras) {
                               return  CartelaUtils.formatarNumeroCartela(cartelaFiltroDTO.getCartelaId())
                                       .contains(filtro) &&
                                       cartelaFiltroDTO.isGanhadora();
                            } else {
                                return CartelaUtils.formatarNumeroCartela(cartelaFiltroDTO.getCartelaId())
                                        .contains(filtro);
                            }
                        })
                        .toList()
                        .toFlowable())
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(cartelasFiltro -> mView.preencherCartelasFiltro(cartelasFiltro));

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void atualizarCartelasSorteaveis(int id, boolean selecionada) {
        mAppDataSource.updateCartelasFiltro(id, selecionada);
        recuperarCartelasSorteaveis();
        mView.retornarPadraoTela();
    }

    @Override
    public void limparCartelasSorteaveis() {
        mAppDataSource.cleanCartelasFiltro();
        recuperarCartelasSorteaveis();
        mView.retornarPadraoTela();
    }

    private void recuperarCartelasSorteaveis() {
        Disposable disposable = mAppDataSource
                .getCartelasSorteaveis()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        cartelasSorteaveis -> {
                            mView.preencherCartelasSorteaveis(cartelasSorteaveis);
                            preencherCartelasSorteaveis(cartelasSorteaveis);
                        }
                );
        mCompositeDisposable.add(disposable);
    }

    private void preencherCartelasSorteaveis(List<Integer> cartelasSorteaveis) {
        if (mCartelasSorteaveis == null) mCartelasSorteaveis = new ArrayList<>();
        else mCartelasSorteaveis.clear();

        if (cartelasSorteaveis.isEmpty() || cartelasSorteaveis.get(0) == -1) {
            Disposable disposable = mAppDataSource
                    .getCartelaUltimoId()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(this::preencherCartelasSorteaveis);
            mCompositeDisposable.add(disposable);
        } else {
            mCartelasSorteaveis.addAll(cartelasSorteaveis);
        }
    }

    private void preencherCartelasSorteaveis(int maxId) {
        for (int i = 1; i <= maxId; i++) {
            mCartelasSorteaveis.add(i);
        }
    }
}
