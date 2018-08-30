package br.com.boa50.kbingo.sorteiocartela;

import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;

public interface SorteioCartelaContract {

    interface View extends BaseView {
        void apresentarCartela(String numeroCartela);
        void preencherCartelasFiltro(List<CartelaFiltroDTO> cartelasFiltro);
    }

    interface Presenter extends BasePresenter<View> {
        void sortearCartela();
    }
}
