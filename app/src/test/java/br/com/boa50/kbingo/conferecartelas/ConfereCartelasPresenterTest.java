package br.com.boa50.kbingo.conferecartelas;


import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Flowable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConfereCartelasPresenterTest {
    private int QUANTIDADE_CARTELAS_GANHADORAS;

    @Mock
    private ConfereCartelasContract.View view;
    @Mock
    private AppDataSource appDataSource;

    @Captor
    private ArgumentCaptor<ArrayList<String>> cartelas;

    private ConfereCartelasContract.Presenter presenter;

    @Before
    public void setUp() {
        initMocks(this);
        presenter = new ConfereCartelasPresenter(appDataSource, new ImmediateScheduleProvider());

        ArrayList<CartelaDTO> CARTELAS_GANHADORAS = Lists.newArrayList(
                new CartelaDTO(1, new ArrayList<>()),
                new CartelaDTO(2, new ArrayList<>()),
                new CartelaDTO(7, new ArrayList<>()),
                new CartelaDTO(50, new ArrayList<>()),
                new CartelaDTO(100, new ArrayList<>()),
                new CartelaDTO(123, new ArrayList<>())
        );
        QUANTIDADE_CARTELAS_GANHADORAS = CARTELAS_GANHADORAS.size();

        when(appDataSource.getCartelasGanhadoras()).thenReturn(Flowable.just(CARTELAS_GANHADORAS));
        presenter.subscribe(view);
    }

    @Test
    public void verificarTamanhoLista() {
        verify(view).apresentarCartelas(cartelas.capture());
        assertThat(cartelas.getValue().size(), equalTo(QUANTIDADE_CARTELAS_GANHADORAS));
    }

    @Test
    public void filtrarCartelas_apresentarCartelasFiltradas() {
        presenter.filtrarCartelas("8");

        verify(view).apresentarCartelasFiltradas(any());
    }

    @Test
    public void filtrarCartelas_apresentarCartelasCorretas() {
        presenter.filtrarCartelas("1");

        verify(view).apresentarCartelasFiltradas(cartelas.capture());
        assertThat(cartelas.getValue(), contains("0001","0100","0123"));
    }
}
