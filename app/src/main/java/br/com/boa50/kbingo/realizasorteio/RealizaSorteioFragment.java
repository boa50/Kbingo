package br.com.boa50.kbingo.realizasorteio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.Pedra;


public class RealizaSorteioFragment extends Fragment implements RealizaSorteioContract.View {

    public RealizaSorteioFragment() {}

    public static RealizaSorteioFragment newInstance() {
        return new RealizaSorteioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.realizasorteio_frag, container, false);
    }

    @Override
    public void apresentarPedra(String pedraValor) {

    }

    @Override
    public void apresentarFimSorteio() {

    }

    @Override
    public void apresentarPedras(List<Pedra> pedras) {

    }
}
