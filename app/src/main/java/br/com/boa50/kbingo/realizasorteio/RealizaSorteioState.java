package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.entity.Pedra;

public class RealizaSorteioState implements RealizaSorteioContract.State {
    private final ArrayList<Pedra> mPedras;
    private final Pedra mUltimaPedraSorteada;
    private final ArrayList<CartelaDTO> mCartelas;
    private final int mQtdCartelasGanhadoras;

    public RealizaSorteioState(ArrayList<Pedra> pedras,
                               Pedra ultimaPedraSorteada,
                               ArrayList<CartelaDTO> cartelas,
                               int qtdCartelasGanhadoras) {
        mPedras = pedras;
        mUltimaPedraSorteada = ultimaPedraSorteada;
        mCartelas = cartelas;
        mQtdCartelasGanhadoras = qtdCartelasGanhadoras;
    }

    @Override
    public ArrayList<Pedra> getPedras() {
        return mPedras;
    }

    @Override
    public Pedra getUltimaPedraSorteada() {
        return mUltimaPedraSorteada;
    }

    @Override
    public ArrayList<CartelaDTO> getCartelas() {
        return mCartelas;
    }

    @Override
    public int getQtdCartelasGanhadoras() {
        return mQtdCartelasGanhadoras;
    }
}
