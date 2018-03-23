package br.com.boa50.kbingo.realizasorteio;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import br.com.boa50.kbingo.pedras.domain.model.Pedra;

import static org.mockito.Mockito.*;


/**
 * Created by boa50 on 3/11/18.
 */

public class RealizaSorteioPresenterTest {

    @Mock
    private RealizaSorteioContract.View mRealizaSorteioView;

    private RealizaSorteioPresenter mRealizaSorteioPresenter;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void sortearPedra_apresentarPedra() {

        ArrayList<Pedra> arrayPedras = new ArrayList<>();
        arrayPedras.add(new Pedra("1","K",1));

        mRealizaSorteioPresenter = new RealizaSorteioPresenter(mRealizaSorteioView);

        mRealizaSorteioPresenter.sortearPedra(arrayPedras);

        verify(mRealizaSorteioView).apresentarPedra("K1");
    }
}
