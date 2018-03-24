package br.com.boa50.kbingo.realizasorteio;

import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.Pedra;

/**
 * Created by boa50 on 3/11/18.
 */

public interface RealizaSorteioContract {

    interface View extends BaseView<Presenter> {

        void apresentarPedra(String pedraValor);

        void apresentarFimSorteio();

        void apresentarPedras(List<Pedra> pedras);
    }

    interface Presenter extends BasePresenter {

        void sortearPedra();

        void takeView(RealizaSorteioContract.View view);

    }
}