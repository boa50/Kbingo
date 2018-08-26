package br.com.boa50.kbingo.sorteiocartela;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
public class SorteioCartelaPresenter implements SorteioCartelaContract.Presenter {

    private SorteioCartelaContract.View mView;

    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private CompositeDisposable mCompositeDisposable;

    private ArrayList<String> mCartelasSorteaveis;

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
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void sortearCartela() {
        if (mCartelasSorteaveis.isEmpty()) {
            Disposable disposable = mAppDataSource
                    .getCartelaUltimoId()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            cartelaId ->{
                                preencherCartelasSorteaveis(cartelaId);
                                mView.apresentarCartela(mCartelasSorteaveis.get(
                                        new Random().nextInt(mCartelasSorteaveis.size())));
                            }
                    );

            mCompositeDisposable.add(disposable);
        }else {
            mView.apresentarCartela(mCartelasSorteaveis.get(
                    new Random().nextInt(mCartelasSorteaveis.size())));
        }
    }

    private void preencherCartelasSorteaveis(int maxId) {
        for (int i = 1; i <= maxId; i++) {
            mCartelasSorteaveis.add(String.format(
                    Locale.ENGLISH,
                    Constant.FORMAT_CARTELA,
                    i));
        }
    }
}
