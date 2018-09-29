package br.com.boa50.kbingo.realizasorteio;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Flowable;
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

        List<CartelaDTO> CARTELAS = new ArrayList<>();
        for (List<CartelaPedra> cartelaPedras : CARTELAS_PEDRAS) {
            CARTELAS.add(new CartelaDTO(cartelaPedras.get(0).getCartelaId(), cartelaPedras));
        }

        for (int i = 1; i <= 6; i++) {
            when(appDataSource.getPedra(i)).thenReturn(Single.just(PEDRAS.get(i - 1)));
        }
        when(appDataSource.getPedras()).thenReturn(Flowable.just(PEDRAS));
        when(appDataSource.getLetras()).thenReturn(Single.just(LETRAS));
        when(appDataSource.getCartelaUltimoId()).thenReturn(Single.just(CARTELAS_PEDRAS.size()));
        when(appDataSource.getCartelas()).thenReturn(Flowable.just(CARTELAS));
        when(appDataSource.getCartelasGanhadoras()).thenReturn(
                Flowable.fromIterable(CARTELAS).filter(CartelaDTO::isGanhadora).toList().toFlowable());
        when(appDataSource.getPedrasByCartelaId(1)).thenReturn(Single.just(CARTELAS_PEDRAS.get(0)));
        when(appDataSource.getPedrasByCartelaId(2)).thenReturn(Single.just(CARTELAS_PEDRAS.get(1)));
        when(appDataSource.getTipoSorteio()).thenReturn(TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CINCO_PEDRAS));

        QUANTIDADE_PEDRAS_SORTEAVEIS = PEDRAS.size();

        realizaSorteioPresenter.subscribe(realizaSorteioView);
    }

    @Test
    public void verificarCarregamentoPedrasCartelas() {
        verify(appDataSource).getLetras();
        verify(appDataSource, times(2)).getPedras();
        verify(appDataSource).getCartelas();
    }

    @Test
    public void sortearPedra_apresentarPedra() {
        realizaSorteioPresenter.sortearPedra();

        assertThat(realizaSorteioPresenter.getIdsSorteio().size(), equalTo(QUANTIDADE_PEDRAS_SORTEAVEIS - 1));
        verify(realizaSorteioView).apresentarPedra(any(Pedra.class));
    }

    @Test
    public void sortearPedra_atualizarPedraSorteada() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();

        verify(realizaSorteioView).apresentarPedra(pedra.capture());
        verify(appDataSource).updatePedraSorteada(pedra.getValue().getId());
        verify(realizaSorteioView).atualizarPedra(pedra.getValue().getId());
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

        assertThat(realizaSorteioPresenter.getIdsSorteio().isEmpty(), equalTo(true));
        verify(realizaSorteioView).apresentarFimSorteio();
    }

    @Test
    public void resetarPedras_reiniciarSorteio() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView).apresentarPedra(pedra.capture());

        realizaSorteioPresenter.resetarPedras();

        assertThat(realizaSorteioPresenter.getIdsSorteio().size(), equalTo(QUANTIDADE_PEDRAS_SORTEAVEIS));

        assertThat(pedra.getValue().isSorteada(), equalTo(false));
        verify(realizaSorteioView).reiniciarSorteio();
    }

    @Test
    public void resetarPedras_atualizarCartelasGanhadorasBadge() {
        realizaSorteioPresenter.resetarPedras();
        verify(realizaSorteioView).atualizarCartelasGanhadorasBadge(0);
    }

    @Test
    public void alterarTipoSorteio_apresentarTipoSorteio() {
        realizaSorteioPresenter.alterarTipoSorteio(1);

        verify(appDataSource).setTipoSorteioId(1);
        verify(realizaSorteioView).apresentarTipoSorteio(true);
    }

    @Test
    public void sortearPedra_atualizarCartelas() {
        ArgumentCaptor<Pedra> pedra = ArgumentCaptor.forClass(Pedra.class);

        realizaSorteioPresenter.sortearPedra();
        verify(realizaSorteioView).apresentarPedra(pedra.capture());

        verify(appDataSource).updateCartelas(pedra.getValue());
    }

    @Test
    public void sortearPedras_atualizarCartelasGanhadorasBadge() {
        for (int i = 0; i < QUANTIDADE_PEDRAS_SORTEAVEIS; i++) {
            realizaSorteioPresenter.sortearPedra();
        }

        verify(realizaSorteioView, times(QUANTIDADE_PEDRAS_SORTEAVEIS)).atualizarCartelasGanhadorasBadge(anyInt());
    }
}
