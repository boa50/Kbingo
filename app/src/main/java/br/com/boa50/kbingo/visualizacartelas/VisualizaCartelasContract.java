package br.com.boa50.kbingo.visualizacartelas;

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
    }

    interface Presenter extends BasePresenter<View> {
        void carregarCartela(int id, boolean confereCartela);
    }
}
