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

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.helper.RxHelper;
import br.com.boa50.kbingo.util.StringUtils;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

@ActivityScoped
public class SorteioCartelaPresenter implements SorteioCartelaContract.Presenter {

    private SorteioCartelaContract.View view;
    private final AppDataSource appDataSource;
    private final RxHelper rxHelper;
    private CompositeDisposable compositeDisposable;

    private ArrayList<Integer> cartelasSorteaveis;
    private String numeroCartelaFiltro;
    private boolean apenasGanhadorasCartelaFiltro;

    @Inject
    SorteioCartelaPresenter(@NonNull AppDataSource appDataSource,
                            @NonNull RxHelper rxHelper) {
        this.appDataSource = appDataSource;
        this.rxHelper = rxHelper;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(@NonNull SorteioCartelaContract.View view) {
        this.view = view;
        obterCartelasSorteaveis();
    }

    private void obterCartelasSorteaveis() {
        compositeDisposable.add(
            rxHelper.getSubscribableFlowable(appDataSource.getCartelasSorteaveis())
            .subscribe(this::tratarCartelasSorteaveis)
        );
    }

    private void tratarCartelasSorteaveis(List<Integer> cartelasSorteaveis) {
        view.preencherCartelasSorteaveis(cartelasSorteaveis);
        zerarCartelasSorteaveis();
        popularCartelasSorteaveis(cartelasSorteaveis);
    }

    private void zerarCartelasSorteaveis() {
        if (cartelasSorteaveis == null) {
            cartelasSorteaveis = new ArrayList<>();
        } else {
            cartelasSorteaveis.clear();
        }
    }

    private void popularCartelasSorteaveis(List<Integer> cartelasSorteaveis) {
        if (cartelasSorteaveis.isEmpty() || cartelasSorteaveis.get(0) == -1) {
            compositeDisposable.add(
                    rxHelper.getSubscribableSingle(appDataSource.getCartelaUltimoId())
                    .subscribe(this::popularCartelasSorteaveis)
            );
        } else {
            this.cartelasSorteaveis.addAll(cartelasSorteaveis);
        }
    }

    private void popularCartelasSorteaveis(int maxId) {
        for (int i = 1; i <= maxId; i++) {
            cartelasSorteaveis.add(i);
        }
    }

    @Override
    public void sortearCartela() {
        view.apresentarCartela(cartelasSorteaveis.get(
                new Random().nextInt(cartelasSorteaveis.size())));
    }

    @Override
    public void carregarFiltroCartelasSorteaveis() {
        carregarFiltroCartelasSorteaveis("", false);
    }

    @Override
    public void carregarFiltroCartelasSorteaveis(String filtro, boolean apenasGanhadoras) {
        numeroCartelaFiltro = filtro;
        apenasGanhadorasCartelaFiltro = apenasGanhadoras;

        compositeDisposable.add(
            rxHelper.getSubscribableFlowable(appDataSource.getCartelasFiltro())
            .flatMap(cartelasFiltro -> Flowable.fromIterable(cartelasFiltro)
                .filter(this::isCartelaFiltro).toList().toFlowable())
            .subscribe(cartelasFiltro -> view.preencherCartelasFiltro(cartelasFiltro))
        );
    }

    private boolean isCartelaFiltro(CartelaFiltroDTO cartela) {
        boolean compareFilter = StringUtils.formatarNumeroCartela(cartela.getCartelaId())
                .contains(numeroCartelaFiltro);

        if (apenasGanhadorasCartelaFiltro) {
            return compareFilter && cartela.isGanhadora();
        } else {
            return compareFilter;
        }
    }

    @Override
    public void atualizarCartelasSorteaveis(int id, boolean selecionada) {
        appDataSource.updateCartelasFiltro(id, selecionada);
        atualizarInformacoesCartelas();
    }

    @Override
    public void limparCartelasSorteaveis() {
        appDataSource.cleanCartelasFiltro();
        atualizarInformacoesCartelas();
    }

    private void atualizarInformacoesCartelas() {
        obterCartelasSorteaveis();
        view.retornarPadraoTela();
    }

    @Override
    public void unsubscribe() {
        view = null;
        compositeDisposable.clear();
    }
}
