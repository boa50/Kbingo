/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    VisualizaCartelasContract.java is part of Kbingo

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
package br.com.boa50.kbingo.visualizacartelas;

import java.io.File;
import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

public interface VisualizaCartelasContract {

    interface View extends BaseView {
        void iniciarLayout(List<Letra> letras);
        void apresentarCartela(List<CartelaPedra> cartelaPedras, List<Pedra> pedras);
        void apresentarMaximoIdCartela(int id);
        void abrirDialogExportarCartelas(int idInicial, int idFinal);
        void mostrarMensagemInicioExportacao();
        void realizarDownload(File file);
    }

    interface Presenter extends BasePresenter<View> {
        void carregarCartela(int id, boolean confereCartela);
        void prepararDialogExportar(int idInicial, int idFinal);
        void exportarCartelas(int idInicial, int idFinal);
    }
}
