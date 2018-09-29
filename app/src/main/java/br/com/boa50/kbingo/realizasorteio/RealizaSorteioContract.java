package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

public interface RealizaSorteioContract {

    interface View extends BaseView {
        void apresentarPedra(Pedra pedra);
        void apresentarFimSorteio();
        void iniciarLayout(List<Letra> letras, ArrayList<Pedra> pedras);
        void atualizarPedra(int pedraId);
        void reiniciarSorteio();
        void apresentarTipoSorteio(boolean tipoAlterado);
        void atualizarCartelasGanhadorasBadge(int qtdCartelasGanhadoras);
    }

    interface Presenter extends BasePresenter<View> {
        void sortearPedra();
        void atualizarCartelasGanhadoras();
        void resetarPedras();
        TipoSorteioDTO getTipoSorteio();
        int getTipoSorteioId();
        void alterarTipoSorteio(int tipoSorteio);
        boolean hasPedraSorteada();
    }
}