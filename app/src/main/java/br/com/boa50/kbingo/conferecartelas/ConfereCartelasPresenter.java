/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    ConfereCartelasPresenter.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package br.com.boa50.kbingo.conferecartelas;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.StringUtils;
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

    @Inject
    ConfereCartelasPresenter(
            @NonNull AppDataSource appDataSource,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(@NonNull ConfereCartelasContract.View view) {
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
        Disposable disposable = mAppDataSource
                .getCartelasGanhadoras()
                .flatMap(cartelas -> Flowable.fromIterable(cartelas)
                .filter(cartela ->
                        StringUtils.formatarNumeroCartela(cartela.getCartelaId()).contains(filtro)))
                .toList()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(cartelas -> mView.apresentarCartelasFiltradas(obterIdsCartelas(cartelas)));

        mCompositeDisposable.add(disposable);
    }

    private void apresentarCartelas() {
        Disposable disposable = mAppDataSource
                .getCartelasGanhadoras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(cartelas -> mView.apresentarCartelas(obterIdsCartelas(cartelas)));

        mCompositeDisposable.add(disposable);
    }

    private ArrayList<String> obterIdsCartelas(List<CartelaDTO> cartelas) {
        ArrayList<String> idsCartelas = new ArrayList<>();
        for (CartelaDTO cartela : cartelas) {
            idsCartelas.add(StringUtils.formatarNumeroCartela(cartela.getCartelaId()));
        }
        return idsCartelas;
    }
}
