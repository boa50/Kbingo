package br.com.boa50.kbingo.realizasorteio;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.boa50.kbingo.pedras.domain.model.Pedra;

/**
 * Created by boa50 on 3/11/18.
 */

public class RealizaSorteioPresenter implements RealizaSorteioContract.Presenter {

    private final RealizaSorteioContract.View mRealizaSorteioView;


    public RealizaSorteioPresenter(@NonNull RealizaSorteioContract.View mRealizaSorteioView) {
        this.mRealizaSorteioView = mRealizaSorteioView;
    }

    public void start() {

    }

    @Override
    public void sortearPedra(ArrayList<Pedra> pedrasDisponiveis) {
//        mRealizaSorteioView.apresentarPedra();
    }
}
