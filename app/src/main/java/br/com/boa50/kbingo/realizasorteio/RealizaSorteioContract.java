/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    RealizaSorteioContract.java is part of Kbingo

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
package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

public interface RealizaSorteioContract {

    interface View extends BaseView {
        void apresentarPedra(Pedra pedra);
        void apresentarFimSorteio();
        void iniciarLayout(List<Letra> letras, ArrayList<Pedra> pedras);
        void atualizarPedra(int pedraId);
        void reiniciarSorteio();
        void apresentarTipoSorteio(boolean tipoAlterado);
        void atualizarCartelasGanhadorasBadge(int qtdCartelasGanhadoras);
    }

    interface Presenter extends BasePresenter<View> {
        void sortearPedra();
        void atualizarCartelasGanhadoras();
        void resetarPedras();
        TipoSorteioDTO getTipoSorteio();
        int getTipoSorteioId();
        void alterarTipoSorteio(int tipoSorteio);
        boolean hasPedraSorteada();
    }
}