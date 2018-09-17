package br.com.boa50.kbingo.sorteiocartela;

import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;

public interface SorteioCartelaContract {

    interface View extends BaseView {
        void apresentarCartela(int numeroCartela);
        void preencherCartelasFiltro(List<CartelaFiltroDTO> cartelasFiltro);
        void preencherCartelasSorteaveis(List<Integer> cartelasSorteaveis);
        void retornarPadraoTela();
    }

    interface Presenter extends BasePresenter<View> {
        void sortearCartela();
        void carregarFiltroCartelasSorteaveis();
        void carregarFiltroCartelasSorteaveis(String filtro, boolean apenasGanhadoras);
        void atualizarCartelasSorteaveis(int id, boolean selecionada);
        void limparCartelasSorteaveis();
    }
}