package br.com.boa50.kbingo.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioContract;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioState;

public final class StateUtils {
    private static final String ARGS_PEDRAS = "pedras";
    private static final String ARGS_PEDRA_ULTIMA = "ultimaPedra";
    private static final String ARGS_TIPO_SORTEIO = "tipoSorteio";

    public static void writeStateToBundle(@NonNull Bundle outState, RealizaSorteioContract.State state) {
        outState.putParcelableArrayList(ARGS_PEDRAS, state.getPedras());
        outState.putParcelable(ARGS_PEDRA_ULTIMA, state.getUltimaPedraSorteada());
        outState.putInt(ARGS_TIPO_SORTEIO, state.getTipoSorteio());
    }

    public static RealizaSorteioContract.State readStateFromBundle(@NonNull Bundle outState) {
        return new RealizaSorteioState(
                outState.getParcelableArrayList(ARGS_PEDRAS),
                outState.getParcelable(ARGS_PEDRA_ULTIMA),
                outState.getInt(ARGS_TIPO_SORTEIO));
    }
}
