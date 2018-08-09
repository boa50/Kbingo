package br.com.boa50.kbingo.realizasorteio;

import java.util.ArrayList;

import br.com.boa50.kbingo.data.entity.Pedra;

public class RealizaSorteioState implements RealizaSorteioContract.State {
    private final ArrayList<Pedra> mPedras;
    private final Pedra mUltimaPedraSorteada;

    public RealizaSorteioState(ArrayList<Pedra> pedras, Pedra ultimaPedraSorteada) {
        mPedras = pedras;
        mUltimaPedraSorteada = ultimaPedraSorteada;
    }

    @Override
    public Pedra getUltimaPedraSorteada() {
        return mUltimaPedraSorteada;
    }

    @Override
    public ArrayList<Pedra> getPedras() {
        return mPedras;
    }
}
