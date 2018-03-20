package br.com.boa50.kbingo.realizasorteio;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import br.com.boa50.kbingo.pedras.domain.model.Pedra;

import static org.mockito.Mockito.verify;


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

        mRealizaSorteioPresenter.sortearPedra(arrayPedras);

        verify(mRealizaSorteioView).apresentarPedra();
    }
}
