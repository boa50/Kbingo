package br.com.boa50.kbingo.sorteiocartela;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Single;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SorteioCartelaPresenterTest {

    @Mock
    private SorteioCartelaContract.View view;
    @Mock
    private AppDataSource appDataSource;

    private SorteioCartelaPresenter presenter;

    @Before
    public void setup() {
        initMocks(this);
        presenter = new SorteioCartelaPresenter(appDataSource, new ImmediateScheduleProvider());

        when(appDataSource.getCartelaUltimoId()).thenReturn(Single.just(200));
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

        assertThat(Integer.valueOf(numCartela.getValue()), greaterThanOrEqualTo(1));
        assertThat(Integer.valueOf(numCartela.getValue()), lessThanOrEqualTo(200));
    }
}
