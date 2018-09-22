package br.com.boa50.kbingo.conferecartelas;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.CartelaUtils;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@ActivityScoped
public class ConfereCartelasPresenter implements ConfereCartelasContract.Presenter {

    private ConfereCartelasContract.View mView;
    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private CompositeDisposable mCompositeDisposable;
//    private ArrayList<String> mCartelasGanhadorasBackup;
//    private ArrayList<String> mCartelasGanhadoras;
//    private String mTextoPadrao;

    @Inject
    ConfereCartelasPresenter(
            @NonNull AppDataSource appDataSource,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

//    @Override
//    public void subscribe(@NonNull ConfereCartelasContract.View view,
//                          @NonNull ArrayList<String> cartelasGanhadoras,
//                          @NonNull String textoPadrao) {
//        mView  = view;
//        mCartelasGanhadorasBackup = cartelasGanhadoras;
//        mTextoPadrao = textoPadrao;
//        organizarCartelas();
//    }

//    @Override
//    public void subscribe(@NonNull ConfereCartelasContract.View view
////                          @NonNull ArrayList<String> cartelasGanhadoras,
//                          /*@NonNull String textoPadrao*/) {
//        mView  = view;
////        mCartelasGanhadorasBackup = cartelasGanhadoras;
////        mTextoPadrao = textoPadrao;
//        organizarCartelas();
//    }

//    @Override
//    public ArrayList<String> getCartelasGanhadoras() {
//        return mCartelasGanhadoras;
//    }
//
//    @Override
//    public int getCartelasGanhadorasSize() {
//        if (mCartelasGanhadorasBackup.get(0).equalsIgnoreCase(mTextoPadrao)) {
//            return mCartelasGanhadorasBackup.size() - 1;
//        }
//
//        return mCartelasGanhadorasBackup.size();
//    }

    @Override
    public void subscribe(@NonNull ConfereCartelasContract.View view) {
//        subscribe(view, new ArrayList<>(), "");
//        subscribe(view, /*new ArrayList<>(),*/ "");
        mView  = view;
        apresentarCartelas();
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void filtrarCartelas(String filtro) {
//        mCartelasGanhadoras.clear();
//        if (mCartelasGanhadoras.isEmpty() || !mCartelasGanhadoras.get(0).equalsIgnoreCase(mTextoPadrao)) {
//            mCartelasGanhadoras.add(0, mTextoPadrao);
//        }

        Disposable disposable = mAppDataSource
                .getCartelasGanhadoras()
                .flatMap(cartelas -> Flowable.fromIterable(cartelas)
                .filter(cartela ->
                        CartelaUtils.formatarNumeroCartela(cartela.getCartelaId()).contains(filtro)))
                .toList()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(cartelas -> mView.apresentarCartelasFiltradas(obterIdsCartelas(cartelas)));

        mCompositeDisposable.add(disposable);

//        mView.apresentarCartelasFiltradas();
    }

    private void apresentarCartelas() {
//        if (!mCartelasGanhadorasBackup.get(0).equalsIgnoreCase(mTextoPadrao)) {
//            mCartelasGanhadorasBackup.add(0, mTextoPadrao);
//        }
//        mCartelasGanhadoras = new ArrayList<>(mCartelasGanhadorasBackup);
        Disposable disposable = mAppDataSource
                .getCartelasGanhadoras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(cartelas -> mView.apresentarCartelas(obterIdsCartelas(cartelas)));

        mCompositeDisposable.add(disposable);
//        mView.apresentarCartelas();
    }

    private ArrayList<String> obterIdsCartelas(List<CartelaDTO> cartelas) {
        ArrayList<String> idsCartelas = new ArrayList<>();
        for (CartelaDTO cartela : cartelas) {
            idsCartelas.add(CartelaUtils.formatarNumeroCartela(cartela.getCartelaId()));
        }
        return idsCartelas;
    }

//    private ArrayList<String> tratarCartelasGanhadoras(ArrayList<String> cartelasGanhadoras) {
//        if (!cartelasGanhadoras.get(0).equalsIgnoreCase(mTextoPadrao)) {
//            cartelasGanhadoras.add(0, mTextoPadrao);
//        }
//        return cartelasGanhadoras;
//    }
}
