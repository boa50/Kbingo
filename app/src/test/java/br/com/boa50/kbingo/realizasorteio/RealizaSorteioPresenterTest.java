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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by boa50 on 3/11/18.
 */

public class RealizaSorteioPresenterTest {

    List<Pedra> PEDRAS;

    @Mock
    private RealizaSorteioContract.View mRealizaSorteioView;

    @Mock
    private PedrasRepository mPedrasRepository;

    private RealizaSorteioPresenter mRealizaSorteioPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mRealizaSorteioPresenter = new RealizaSorteioPresenter(mPedrasRepository, new ImmediateScheduleProvider());

        PEDRAS = Lists.newArrayList(
                new Pedra("1", "K", "01"),
                new Pedra("2", "K", "02")
        );

        when(mPedrasRepository.getPedras()).thenReturn(Flowable.just(PEDRAS));

        mRealizaSorteioPresenter.subscribe(mRealizaSorteioView);
    }

    @Test
    public void sortearPedra_apresentarPedra() {
        mRealizaSorteioPresenter.sortearPedra();

        assertThat(mRealizaSorteioPresenter.getPosicoes().size(), equalTo(PEDRAS.size() - 1));
        verify(mRealizaSorteioView).apresentarPedra(any(Pedra.class));
    }

    @Test
    public void sortearPedra_atualizarPedraSorteada() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        mRealizaSorteioPresenter.sortearPedra();

        verify(mRealizaSorteioView).apresentarPedra(pedra.capture());

        assertThat(pedra.getValue().ismSorteada(), equalTo(true));
    }

    @Test
    public void sortearDuasPedras_apresentarPedrasDiferentes() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        mRealizaSorteioPresenter.sortearPedra();
        verify(mRealizaSorteioView).apresentarPedra(pedra.capture());
        String pedra1 = pedra.getValue().getValorPedra();

        mRealizaSorteioPresenter.sortearPedra();
        verify(mRealizaSorteioView, times(2)).apresentarPedra(pedra.capture());
        assertThat(pedra1, is(not(equalTo(pedra.getValue().getValorPedra()))));
    }

    @Test
    public void sortearTodasPedras_EncerrarSorteio() {
        mRealizaSorteioPresenter.sortearPedra();
        mRealizaSorteioPresenter.sortearPedra();
        mRealizaSorteioPresenter.sortearPedra();

        assertThat(mRealizaSorteioPresenter.getPosicoes().isEmpty(), equalTo(true));
        verify(mRealizaSorteioView).apresentarFimSorteio();
    }

    @Test
    public void resetarPedras_ReiniciarSorteio() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        mRealizaSorteioPresenter.sortearPedra();
        verify(mRealizaSorteioView).apresentarPedra(pedra.capture());

        mRealizaSorteioPresenter.resetarPedras();

        assertThat(mRealizaSorteioPresenter.getPosicoes().size(), equalTo(PEDRAS.size()));

        assertThat(pedra.getValue().ismSorteada(), equalTo(false));
        verify(mRealizaSorteioView).reiniciarSorteio();
    }
}
