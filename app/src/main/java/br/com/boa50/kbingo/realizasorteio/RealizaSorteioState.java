package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.entity.Pedra;

public class RealizaSorteioState implements RealizaSorteioContract.State {
    private final ArrayList<Pedra> mPedras;
    private final Pedra mUltimaPedraSorteada;
    private final int mTipoSorteio;
    private final ArrayList<CartelaDTO> mCartelas;

    public RealizaSorteioState(ArrayList<Pedra> pedras, Pedra ultimaPedraSorteada, int tipoSorteio, ArrayList<CartelaDTO> cartelas) {
        mPedras = pedras;
        mUltimaPedraSorteada = ultimaPedraSorteada;
        mTipoSorteio = tipoSorteio;
        mCartelas = cartelas;
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
    public int getTipoSorteio() {
        return mTipoSorteio;
    }

    @Override
    public ArrayList<CartelaDTO> getCartelas() {
        return mCartelas;
    }
}
