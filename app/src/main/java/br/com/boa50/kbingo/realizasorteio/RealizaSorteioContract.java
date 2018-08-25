package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.BaseState;
import br.com.boa50.kbingo.BaseStatefulPresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

public interface RealizaSorteioContract {

    interface View extends BaseView {
        void apresentarPedra(Pedra pedra);
        void apresentarFimSorteio();
        void iniciarLayout(List<Letra> letras);
        void atualizarPedra(int position);
        void reiniciarSorteio();
        void apresentarTipoSorteio(boolean tipoAlterado);
        void atualizarCartelasGanhadorasBadge();
    }

    interface Presenter extends BaseStatefulPresenter<View, State> {
        void sortearPedra();
        void resetarPedras();
        void alterarTipoSorteio(int tipoSorteio);
        boolean hasPedraSorteada();
    }

    interface State extends BaseState {
        ArrayList<Pedra> getPedras();
        Pedra getUltimaPedraSorteada();
        int getTipoSorteio();
        ArrayList<CartelaDTO> getCartelas();
        int getQtdCartelasGanhadoras();
    }
}