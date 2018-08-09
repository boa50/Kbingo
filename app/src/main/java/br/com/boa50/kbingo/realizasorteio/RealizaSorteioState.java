package br.com.boa50.kbingo.realizasorteio;

import br.com.boa50.kbingo.data.entity.Pedra;

public class RealizaSorteioState implements RealizaSorteioContract.State {
    private final Pedra mUltimaPedraSorteada;

    public RealizaSorteioState(Pedra ultimaPedraSorteada) {
        mUltimaPedraSorteada = ultimaPedraSorteada;
    }

    @Override
    public Pedra getUltimaPedraSorteada() {
        return mUltimaPedraSorteada;
    }
}
