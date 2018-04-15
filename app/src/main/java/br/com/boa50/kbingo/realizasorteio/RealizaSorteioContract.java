package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.Pedra;

/**
 * Created by boa50 on 3/11/18.
 */

public interface RealizaSorteioContract {

    interface View extends BaseView<Presenter> {

        void apresentarPedra(Pedra pedra);

        void apresentarFimSorteio();

        void iniciarPedras(ArrayList<Pedra> pedras);

        void atualizarPedra(int position);

        void reiniciarSorteio();
    }

    interface Presenter extends BasePresenter {

        void sortearPedra();

        void resetarPedras();

        void setPedras(ArrayList<Pedra> pedras);
    }
}