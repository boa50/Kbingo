package br.com.boa50.kbingo.realizasorteio;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Single;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Created by boa50 on 3/11/18.
 */

public class RealizaSorteioPresenterTest {

    private int QUANTIDADE_PEDRAS_SORTEAVEIS;

    @Mock
    private RealizaSorteioContract.View realizaSorteioView;

    @Mock
    private AppDataSource appDataSource;

    private RealizaSorteioPresenter realizaSorteioPresenter;

    @Before
    public void setUp() {
        initMocks(this);

        realizaSorteioPresenter = new RealizaSorteioPresenter(appDataSource, new ImmediateScheduleProvider());

        List<Pedra> PEDRAS = Lists.newArrayList(
                new Pedra(1, 1, "01"),
                new Pedra(2, 1, "02")
        );

        List<Letra> LETRAS = Lists.newArrayList(
                new Letra(1, "K")
        );

        when(appDataSource.getPedras()).thenReturn(Single.just(PEDRAS));
        when(appDataSource.getLetras()).thenReturn(Single.just(LETRAS));

        QUANTIDADE_PEDRAS_SORTEAVEIS = PEDRAS.size();

        realizaSorteioPresenter.subscribe(realizaSorteioView);
    }

    @Test
    public void sortearPedra_apresentarPedra() {
        realizaSorteioPresenter.sortearPedra();

        assertThat(realizaSorteioPresenter.getPosicoes().size(), equalTo(QUANTIDADE_PEDRAS_SORTEAVEIS - 1));
        verify(realizaSorteioView).apresentarPedra(any(Pedra.class));
    }

    @Test
    public void sortearPedra_atualizarPedraSorteada() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();

        verify(realizaSorteioView).apresentarPedra(pedra.capture());

        assertThat(pedra.getValue().isSorteada(), equalTo(true));
        verify(realizaSorteioView).atualizarPedra(anyInt());
    }

    @Test
    public void sortearDuasPedras_apresentarPedrasDiferentes() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView).apresentarPedra(pedra.capture());
        String pedra1 = pedra.getValue().getValorPedra();

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView, times(2)).apresentarPedra(pedra.capture());
        assertThat(pedra1, is(not(equalTo(pedra.getValue().getValorPedra()))));
    }

    @Test
    public void sortearTodasPedras_EncerrarSorteio() {
        realizaSorteioPresenter.sortearPedra();
        realizaSorteioPresenter.sortearPedra();
        realizaSorteioPresenter.sortearPedra();

        assertThat(realizaSorteioPresenter.getPosicoes().isEmpty(), equalTo(true));
        verify(realizaSorteioView).apresentarFimSorteio();
    }

    @Test
    public void resetarPedras_ReiniciarSorteio() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView).apresentarPedra(pedra.capture());

        realizaSorteioPresenter.resetarPedras();

        assertThat(realizaSorteioPresenter.getPosicoes().size(), equalTo(QUANTIDADE_PEDRAS_SORTEAVEIS));

        assertThat(pedra.getValue().isSorteada(), equalTo(false));
        verify(realizaSorteioView).reiniciarSorteio();
    }
}
