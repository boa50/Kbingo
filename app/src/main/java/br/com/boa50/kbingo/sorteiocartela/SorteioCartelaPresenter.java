/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    SorteioCartelaPresenter.java is part of Kbingo

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
package br.com.boa50.kbingo.sorteiocartela;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import br.com.boa50.kbingo.bluetooth.Maruagem;
import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.StringUtils;
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
        Disposable disposable = mAppDataSource
                .getCartelaUltimoId()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(maxId -> {
                    if (mCartelasSorteaveis.size() == maxId
                            && Maruagem.getInstance().getIdCartelaSorteada() > 0) {
                        mView.apresentarCartela(Maruagem.getInstance().getIdCartelaSorteada());
                        Maruagem.getInstance().setIdCartelaSorteada(-1);
                    } else {
                        mView.apresentarCartela(mCartelasSorteaveis.get(
                                new Random().nextInt(mCartelasSorteaveis.size())));
                    }
                });
        mCompositeDisposable.add(disposable);
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
                               return  StringUtils.formatarNumeroCartela(cartelaFiltroDTO.getCartelaId())
                                       .contains(filtro) &&
                                       cartelaFiltroDTO.isGanhadora();
                            } else {
                                return StringUtils.formatarNumeroCartela(cartelaFiltroDTO.getCartelaId())
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
