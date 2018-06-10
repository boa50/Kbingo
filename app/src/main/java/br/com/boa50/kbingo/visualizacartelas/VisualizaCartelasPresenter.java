package br.com.boa50.kbingo.visualizacartelas;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VisualizaCartelasPresenter implements VisualizaCartelasContract.Presenter {

    private VisualizaCartelasContract.View mView;

    private final AppDataSource mAppDataSource;

    private final BaseSchedulerProvider mScheduleProvider;

    private CompositeDisposable mCompositeDisposable;

    @Inject
    VisualizaCartelasPresenter(
            @NonNull AppDataSource appDataSource,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(BaseView view) {
        mView = (VisualizaCartelasContract.View) view;
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
                        letras -> {
                            mView.iniciarLayout(letras);
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void carregarCartela(String id) {
        Disposable disposable = mAppDataSource
                .getPedrasByCartelaId(id)
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        cartelaPedras -> {
                            mView.apresentarCartela(cartelaPedras);
                        }
                );

        mCompositeDisposable.add(disposable);
    }
}
