/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    AppDataSource.java is part of Kbingo

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
package br.com.boa50.kbingo.data;

import java.util.List;

import javax.inject.Singleton;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public interface AppDataSource {
    boolean hasPedraSorteada();
    Pedra getUltimaPedraSorteada();
    TipoSorteioDTO getTipoSorteio();
    int getTipoSorteioId();
    void setTipoSorteioId(int tipoSorteioId);

    Single<Pedra> getPedra(int id);
    Single<List<Letra>> getLetras();
    Single<Integer> getCartelaUltimoId();
    Single<List<CartelaPedra>> getPedrasByCartelaId(int id);
    Single<List<CartelaPedra>> getPedrasByCartelasIds(List<Integer> ids);
    Single<CartelaDTO> getCartela(int id);

    Flowable<List<Pedra>> getPedras();
    Flowable<List<CartelaDTO>> getCartelas();
    Flowable<List<CartelaDTO>> getCartelasGanhadoras();
    Flowable<List<CartelaFiltroDTO>> getCartelasFiltro();
    Flowable<List<Integer>> getCartelasSorteaveis();

    void updatePedraSorteada(int id);
    void updateCartelasFiltro(int id, boolean selecionada);

    void cleanPedrasSorteadas();
    void cleanCartelasGanhadoras();
    void cleanCartelasFiltro();

    void initializeDatabase();
}
