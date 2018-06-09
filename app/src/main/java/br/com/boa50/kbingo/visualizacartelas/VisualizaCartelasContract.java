package br.com.boa50.kbingo.visualizacartelas;

import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;

public interface VisualizaCartelasContract {

    interface View extends BaseView<Presenter> {
        void iniciarLayout(List<Letra> letras);
        void apresentarCartela(List<CartelaPedra> cartelaPedras);
    }

    interface Presenter extends BasePresenter {
        void carregarCartela(String id);
    }
}
