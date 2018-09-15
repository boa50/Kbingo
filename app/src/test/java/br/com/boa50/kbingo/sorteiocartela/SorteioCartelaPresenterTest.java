package br.com.boa50.kbingo.sorteiocartela;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SorteioCartelaPresenterTest {

    @Mock
    private SorteioCartelaContract.View view;
    @Mock
    private AppDataSource appDataSource;

    @Captor
    private ArgumentCaptor<List<CartelaFiltroDTO>> cartelasFiltroCaptor;
    @Captor
    private ArgumentCaptor<List<Integer>> cartelasSorteaveisCaptor;

    private SorteioCartelaPresenter presenter;
    private List<CartelaFiltroDTO> cartelasFiltro;
    private List<Integer> cartelasSorteaveis;
    private int cartelaMaxId = 200;
    private int cartelaMinId = 1;

    @Before
    public void setup() {
        initMocks(this);
        presenter = new SorteioCartelaPresenter(appDataSource, new ImmediateScheduleProvider());

        cartelasFiltro = Lists.newArrayList(
                new CartelaFiltroDTO(1, false, false),
                new CartelaFiltroDTO(2, true, false),
                new CartelaFiltroDTO(3, true, false)
        );

        cartelasSorteaveis = Lists.newArrayList();

        when(appDataSource.getCartelaUltimoId()).thenReturn(Single.just(cartelaMaxId));
        when(appDataSource.getCartelasFiltro()).thenReturn(Flowable.just(cartelasFiltro));
        when(appDataSource.getCartelasSorteaveis()).thenReturn(Flowable.just(cartelasSorteaveis));
        presenter.subscribe(view);
    }

    @Test
    public void sortearCartela_apresentarCartela() {
        presenter.sortearCartela();

        verify(view).apresentarCartela(anyString());
    }

    @Test
    public void sortearCartela_retornarDentroDoRange() {
        ArgumentCaptor<String> numCartela = ArgumentCaptor.forClass(String.class);
        presenter.sortearCartela();

        verify(view).apresentarCartela(numCartela.capture());

        assertThat(Integer.valueOf(numCartela.getValue()), greaterThanOrEqualTo(cartelaMinId));
        assertThat(Integer.valueOf(numCartela.getValue()), lessThanOrEqualTo(cartelaMaxId));
    }

    @Test
    public void verificarCarregarFiltro_verificarCarregarSorteaveis() {
        presenter.carregarFiltroCartelasSorteaveis();
        verify(view).preencherCartelasFiltro(cartelasFiltroCaptor.capture());
        assertThat(cartelasFiltroCaptor.getValue(), equalTo(cartelasFiltro));
        assertThat(cartelasFiltroCaptor.getValue().size(), equalTo(cartelasFiltro.size()));
    }

    @Test
    public void verificarCarregarSorteaveis() {
        verify(view).preencherCartelasSorteaveis(cartelasSorteaveisCaptor.capture());
        assertThat(cartelasSorteaveisCaptor.getValue(), equalTo(cartelasSorteaveis));
        assertThat(cartelasSorteaveisCaptor.getValue().size(), equalTo(0));
    }

    @Test
    public void atualizarCartelasSorteaveis_preencherCartelasSorteaveis() {
        int id = cartelasFiltro.get(0).getCartelaId();
        List<Integer> cartelasSorteaveis = Lists.newArrayList(id);
        when(appDataSource.getCartelasSorteaveis()).thenReturn(Flowable.just(cartelasSorteaveis));

        presenter.atualizarCartelasSorteaveis(id, true);

        verify(view, times(2)).preencherCartelasSorteaveis(cartelasSorteaveisCaptor.capture());
        assertThat(cartelasSorteaveisCaptor.getValue().get(0), equalTo(1));
        assertThat(cartelasSorteaveisCaptor.getValue().size(), equalTo(1));
    }

    @Test
    public void carregarCartelasFiltroBusca_retornarDadosCorretos() {
        presenter.carregarFiltroCartelasSorteaveis("2", false);
        verify(view).preencherCartelasFiltro(cartelasFiltroCaptor.capture());
        assertThat(cartelasFiltroCaptor.getValue().get(0), equalTo(cartelasFiltro.get(1)));
        assertThat(cartelasFiltroCaptor.getValue().size(), equalTo(1));
    }

    @Test
    public void carregarCartelasFiltroGanhadoras_retornarDadosCorretos() {
        presenter.carregarFiltroCartelasSorteaveis("", true);
        verify(view).preencherCartelasFiltro(cartelasFiltroCaptor.capture());
        assertThat(cartelasFiltroCaptor.getValue().get(0), equalTo(cartelasFiltro.get(1)));
        assertThat(cartelasFiltroCaptor.getValue().get(1), equalTo(cartelasFiltro.get(2)));
        assertThat(cartelasFiltroCaptor.getValue().size(), equalTo(2));
    }

    @Test
    public void carregarCartelasFiltroGanhadorasBusca_retornarDadosCorretos() {
        presenter.carregarFiltroCartelasSorteaveis("2", true);
        verify(view).preencherCartelasFiltro(cartelasFiltroCaptor.capture());
        assertThat(cartelasFiltroCaptor.getValue().get(0), equalTo(cartelasFiltro.get(1)));
        assertThat(cartelasFiltroCaptor.getValue().size(), equalTo(1));
    }

    @Test
    public void atualizarCartelasGanhadoras_retornarDadosCorretos() {
        presenter.carregarFiltroCartelasSorteaveis("", true);
        verify(view).preencherCartelasFiltro(cartelasFiltroCaptor.capture());
        assertThat(cartelasFiltroCaptor.getValue().size(), equalTo(2));

        cartelasFiltro.get(0).setGanhadora(true);

        presenter.carregarFiltroCartelasSorteaveis("", true);
        verify(view, times(2)).preencherCartelasFiltro(cartelasFiltroCaptor.capture());
        assertThat(cartelasFiltroCaptor.getValue().size(), equalTo(3));
    }
}
