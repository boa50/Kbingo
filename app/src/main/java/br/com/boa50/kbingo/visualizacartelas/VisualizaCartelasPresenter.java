package br.com.boa50.kbingo.visualizacartelas;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.helper.EnviromentHelper;
import br.com.boa50.kbingo.util.CartelaUtils;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VisualizaCartelasPresenter implements VisualizaCartelasContract.Presenter {

    private VisualizaCartelasContract.View mView;

    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private final EnviromentHelper mEnviroment;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    VisualizaCartelasPresenter(@NonNull AppDataSource appDataSource,
                               @NonNull BaseSchedulerProvider schedulerProvider,
                               @NonNull EnviromentHelper enviroment) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;
        mEnviroment = enviroment;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(@NonNull VisualizaCartelasContract.View view) {
        mView = view;
        iniciarLayout();
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mCompositeDisposable.clear();
    }

    private void iniciarLayout() {
        Disposable disposable = mAppDataSource
                .getLetras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        letras -> mView.iniciarLayout(letras)
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void carregarCartela(int id, boolean confereCartela) {
        Disposable disposable = mAppDataSource
                .getPedrasByCartelaId(id)
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        cartelaPedras -> {
                            if (cartelaPedras.size() > 0) {
                                if (confereCartela) {
                                    Disposable disposable2 = mAppDataSource
                                            .getPedras()
                                            .subscribeOn(mScheduleProvider.io())
                                            .observeOn(mScheduleProvider.ui())
                                            .subscribe(
                                                    pedras -> mView.apresentarCartela(cartelaPedras, pedras)
                                            );

                                    mCompositeDisposable.add(disposable2);
                                } else {
                                    mView.apresentarCartela(cartelaPedras, null);
                                }
                            } else {
                                Disposable disposable2 = mAppDataSource
                                        .getCartelaUltimoId()
                                        .subscribeOn(mScheduleProvider.io())
                                        .observeOn(mScheduleProvider.ui())
                                        .subscribe(
                                                cartelaId ->{
                                                    carregarCartela(cartelaId, confereCartela);
                                                    mView.apresentarMaximoIdCartela(cartelaId);
                                                }
                                        );

                                mCompositeDisposable.add(disposable2);
                            }
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void prepararDialogExportar(int idInicial, int idFinal) {
        if (idInicial > 0 && idFinal > 0) {
            mView.abrirDialogExportarCartelas(idInicial, idFinal);
        } else {
            mCompositeDisposable.add(mAppDataSource
                    .getCartelaUltimoId()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            cartelaId -> mView.abrirDialogExportarCartelas(1, cartelaId)
                    ));
        }
    }

    @Override
    public void exportarCartelas(int idInicial, int idFinal) {
        if (idInicial <= idFinal) {
            mView.mostrarMensagemInicioExportacao();

            List<Integer> cartelasIds = new ArrayList<>();
            for (int i = idInicial; i <= idFinal; i++) {
                cartelasIds.add(i);
            }

            mCompositeDisposable.add(mAppDataSource
                    .getLetras()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            letras -> {
                                List<String> letrasLetra = new ArrayList<>();
                                for (Letra letra : letras) {
                                    letrasLetra.add(letra.getNome());
                                }

                                mCompositeDisposable.add(mAppDataSource
                                        .getPedrasByCartelasIds(cartelasIds)
                                        .subscribeOn(mScheduleProvider.io())
                                        .observeOn(mScheduleProvider.ui())
                                        .subscribe(
                                                cartelaPedras -> mView.realizarDownload(
                                                        CartelaUtils.gerarCartelas(letrasLetra,
                                                                cartelaPedras,
                                                                mEnviroment.getDowloadDirectory()))
                                        ));
                            }
                    ));
        }
    }
}
