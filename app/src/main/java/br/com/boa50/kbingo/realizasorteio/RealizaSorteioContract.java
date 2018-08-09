package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.BaseState;
import br.com.boa50.kbingo.BaseStatefulPresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

public interface RealizaSorteioContract {

    interface View extends BaseView {
        void apresentarPedra(Pedra pedra);
        void apresentarFimSorteio();
        void iniciarLayout(List<Letra> letras, ArrayList<Pedra> pedras);
        void atualizarPedra(int position);
        void reiniciarSorteio();
    }

    interface Presenter extends BaseStatefulPresenter<View, State> {
        void sortearPedra();
        void resetarPedras();
        void setPedras(ArrayList<Pedra> pedras);
    }

    interface State extends BaseState {
        Pedra getUltimaPedraSorteada();
    }
}