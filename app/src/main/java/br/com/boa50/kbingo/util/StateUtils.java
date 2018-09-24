package br.com.boa50.kbingo.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioContract;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioState;

public final class StateUtils {
    private static final String ARGS_PEDRAS = "pedras";
    private static final String ARGS_PEDRA_ULTIMA = "ultimaPedra";

    public static void writeStateToBundle(@NonNull Bundle outState, RealizaSorteioContract.State state) {
        outState.putParcelableArrayList(ARGS_PEDRAS, state.getPedras());
        outState.putParcelable(ARGS_PEDRA_ULTIMA, state.getUltimaPedraSorteada());
    }

    public static RealizaSorteioContract.State readStateFromBundle(@NonNull Bundle outState) {
        return new RealizaSorteioState(
                outState.getParcelableArrayList(ARGS_PEDRAS),
                outState.getParcelable(ARGS_PEDRA_ULTIMA));
    }
}
