package br.com.boa50.kbingo.realizasorteio;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Single;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RealizaSorteioPresenterTest {

    private int QUANTIDADE_PEDRAS_SORTEAVEIS;
    private int QUANTIDADE_CARTELAS;

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
                new Pedra(2, 1, "02"),
                new Pedra(3, 1, "03"),
                new Pedra(4, 1, "04"),
                new Pedra(5, 1, "05"),
                new Pedra(6, 1, "06")
        );

        List<Letra> LETRAS = Lists.newArrayList(
                new Letra(1, "K")
        );

        @SuppressWarnings("unchecked")
        List<List<CartelaPedra>> CARTELAS_PEDRAS = Lists.newArrayList(
                Lists.newArrayList(
                        new CartelaPedra(1, 1, 1, 1),
                        new CartelaPedra(1, 2, 1, 1),
                        new CartelaPedra(1, 3, 1, 1),
                        new CartelaPedra(1, 4, 1, 1),
                        new CartelaPedra(1, 5, 1, 1)
                ),
                Lists.newArrayList(
                        new CartelaPedra(2, 2, 1, 1),
                        new CartelaPedra(2, 3, 1, 1),
                        new CartelaPedra(2, 4, 1, 1),
                        new CartelaPedra(2, 5, 1, 1),
                        new CartelaPedra(2, 6, 1, 1)
                )
        );

        when(appDataSource.getPedras()).thenReturn(Single.just(PEDRAS));
        when(appDataSource.getLetras()).thenReturn(Single.just(LETRAS));
        when(appDataSource.getCartelaUltimoId()).thenReturn(Single.just(CARTELAS_PEDRAS.size()));
        when(appDataSource.getPedrasByCartelaId(1)).thenReturn(Single.just(CARTELAS_PEDRAS.get(0)));
        when(appDataSource.getPedrasByCartelaId(2)).thenReturn(Single.just(CARTELAS_PEDRAS.get(1)));

        QUANTIDADE_PEDRAS_SORTEAVEIS = PEDRAS.size();
        QUANTIDADE_CARTELAS = CARTELAS_PEDRAS.size();

        realizaSorteioPresenter.subscribe(realizaSorteioView,
                new RealizaSorteioState(null, null, TipoSorteioDTO.CINCO_PEDRAS, null, 0));
    }

    @Test
    public void sortearPedra_apresentarPedra() {
        realizaSorteioPresenter.sortearPedra();

        assertThat(realizaSorteioPresenter.getmPosicoes().size(), equalTo(QUANTIDADE_PEDRAS_SORTEAVEIS - 1));
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
        String pedra1 = pedra.getValue().getNumero();

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView, times(2)).apresentarPedra(pedra.capture());
        assertThat(pedra1, is(not(equalTo(pedra.getValue().getNumero()))));
    }

    @Test
    public void sortearTodasPedras_encerrarSorteio() {
        for (int i = 0; i <= QUANTIDADE_PEDRAS_SORTEAVEIS; i++) {
            realizaSorteioPresenter.sortearPedra();
        }

        assertThat(realizaSorteioPresenter.getmPosicoes().isEmpty(), equalTo(true));
        verify(realizaSorteioView).apresentarFimSorteio();
    }

    @Test
    public void resetarPedras_reiniciarSorteio() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView).apresentarPedra(pedra.capture());

        realizaSorteioPresenter.resetarPedras();

        assertThat(realizaSorteioPresenter.getmPosicoes().size(), equalTo(QUANTIDADE_PEDRAS_SORTEAVEIS));

        assertThat(pedra.getValue().isSorteada(), equalTo(false));
        verify(realizaSorteioView).reiniciarSorteio();
    }

    @Test
    public void resetarPedras_atualizarCartelasGanhadorasBadge() {
        realizaSorteioPresenter.resetarPedras();
        verify(realizaSorteioView).atualizarCartelasGanhadorasBadge();
    }

    @Test
    public void alterarTipoSorteio_apresentarTipoSorteio() {
        realizaSorteioPresenter.alterarTipoSorteio(anyInt());
        verify(realizaSorteioView).apresentarTipoSorteio(true);
    }

    @Test
    public void carregarCartelas_verificarQuantidadeCarregada() {
        assertThat(realizaSorteioPresenter.getState().getCartelas().size(),
                equalTo(QUANTIDADE_CARTELAS));
    }

    @Test
    public void sortearPedras_atualizarQuantidadePedrasSorteadas() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView).apresentarPedra(pedra.capture());
        List<CartelaDTO> cartelas = realizaSorteioPresenter.getState().getCartelas();

        switch (pedra.getValue().getId()) {
            case 1:
                assertThat(cartelas.get(0).getQtdPedrasSorteadas(), equalTo(1));
                break;
            case 2:
                assertThat(cartelas.get(0).getQtdPedrasSorteadas(), equalTo(1));
                assertThat(cartelas.get(1).getQtdPedrasSorteadas(), equalTo(1));
                break;
            case 3:
                assertThat(cartelas.get(1).getQtdPedrasSorteadas(), equalTo(1));
                break;
        }
    }

    @Test
    public void sortearPedras_verificarCartelaGanhadora_atualizarCartelasGanhadorasBadge() {
        for (int i = 0; i < QUANTIDADE_PEDRAS_SORTEAVEIS; i++) {
            realizaSorteioPresenter.sortearPedra();
        }
        List<CartelaDTO> cartelas = realizaSorteioPresenter.getState().getCartelas();

        for (int i = 0; i < cartelas.size(); i++) {
            assertThat(cartelas.get(i).isGanhadora(), equalTo(true));
        }

        assertThat(realizaSorteioPresenter.getState().getQtdCartelasGanhadoras(), greaterThan(0));
        verify(realizaSorteioView, times(2)).atualizarCartelasGanhadorasBadge();
    }

    @Test
    public void sortearPedras_registrarMaxPedrasSorteadasPorCartela() {
        for (int i = 0; i < QUANTIDADE_PEDRAS_SORTEAVEIS; i++) {
            realizaSorteioPresenter.sortearPedra();
        }
        List<CartelaDTO> cartelas = realizaSorteioPresenter.getState().getCartelas();

        for (int i = 0; i < cartelas.size(); i++) {
            assertThat(cartelas.get(i).getQtdPedrasSorteadas(),
                    equalTo(cartelas.get(i).getCartelaPedras().size()));
        }
    }

    @Test
    public void sortearPedras_verificarCartelaGanhadora_resetarPedras_zerarGanhadoras_zerarPedrasSorteadas() {
        for (int i = 0; i < QUANTIDADE_PEDRAS_SORTEAVEIS; i++) {
            realizaSorteioPresenter.sortearPedra();
        }
        List<CartelaDTO> cartelas = realizaSorteioPresenter.getState().getCartelas();

        for (int i = 0; i < cartelas.size(); i++) {
            assertThat(cartelas.get(i).isGanhadora(), equalTo(true));
        }

        realizaSorteioPresenter.resetarPedras();
        for (int i = 0; i < cartelas.size(); i++) {
            assertThat(cartelas.get(i).isGanhadora(), equalTo(false));
            assertThat(cartelas.get(i).getQtdPedrasSorteadas(), equalTo(0));
        }
    }
}
