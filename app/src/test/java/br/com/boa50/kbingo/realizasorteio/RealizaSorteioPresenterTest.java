package br.com.boa50.kbingo.realizasorteio;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.data.source.PedrasRepository;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Flowable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by boa50 on 3/11/18.
 */

public class RealizaSorteioPresenterTest {

    private static List<Pedra> PEDRAS;

    @Mock
    private RealizaSorteioContract.View mRealizaSorteioView;

    @Mock
    private PedrasRepository mPedrasRepository;

    private RealizaSorteioPresenter mRealizaSorteioPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mRealizaSorteioPresenter = new RealizaSorteioPresenter(mPedrasRepository, new ImmediateScheduleProvider());
        mRealizaSorteioPresenter.takeView(mRealizaSorteioView);

        PEDRAS = Lists.newArrayList(
                new Pedra("1","K","01"),
                new Pedra("2","K","02")
        );

        when(mPedrasRepository.getPedras()).thenReturn(Flowable.just(PEDRAS));
    }

    @Test
    public void sortearPedra_apresentarPedra() {
        mRealizaSorteioPresenter.sortearPedra();

        verify(mRealizaSorteioView).apresentarPedra(anyString());
    }

    @Test
    public void sortearPedra_atualizarPedraSorteada() {
        mRealizaSorteioPresenter.sortearPedra();

        verify(mRealizaSorteioView).apresentarPedras();
    }

    @Test
    public void sortearDuasPedras_apresentarPedrasDiferentes() {
        ArgumentCaptor<String> pedraValor = ArgumentCaptor.forClass(String.class);

        mRealizaSorteioPresenter.sortearPedra();
        verify(mRealizaSorteioView).apresentarPedra(pedraValor.capture());
        String pedra1 = pedraValor.getValue();

        mRealizaSorteioPresenter.sortearPedra();
        verify(mRealizaSorteioView, times(2)).apresentarPedra(pedraValor.capture());
        assertThat(pedra1, is(not(equalTo(pedraValor.getValue()))));
    }

    @Test
    public void sortearTodasPedras_EncerrarSorteio() {
        mRealizaSorteioPresenter.sortearPedra();
        mRealizaSorteioPresenter.sortearPedra();

        verify(mRealizaSorteioView).apresentarFimSorteio();
    }
}
