package br.com.boa50.kbingo.realizasorteio;

import br.com.boa50.kbingo.data.entity.Pedra;

public class RealizaSorteioState implements RealizaSorteioContract.State {
//    private final ArrayList<Pedra> mPedras;
    private final Pedra mUltimaPedraSorteada;

    public RealizaSorteioState(/*ArrayList<Pedra> pedras,*/
                               Pedra ultimaPedraSorteada) {
//        mPedras = pedras;
        mUltimaPedraSorteada = ultimaPedraSorteada;
    }

//    @Override
//    public ArrayList<Pedra> getPedras() {
//        return mPedras;
//    }

    @Override
    public Pedra getUltimaPedraSorteada() {
        return mUltimaPedraSorteada;
    }
}
