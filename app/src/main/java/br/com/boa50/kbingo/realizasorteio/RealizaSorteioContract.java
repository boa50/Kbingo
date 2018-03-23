package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.pedras.domain.model.Pedra;

/**
 * Created by boa50 on 3/11/18.
 */

public interface RealizaSorteioContract {

    interface View extends BaseView<Presenter> {

        void apresentarPedra(String pedraValor);

    }

    interface Presenter extends BasePresenter {

        void sortearPedra(ArrayList<Pedra> pedrasDisponiveis);

    }
}