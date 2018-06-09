package br.com.boa50.kbingo.visualizacartelas;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Single;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VisualizaCartelasPresenterTest {

    @Mock
    private VisualizaCartelasContract.View visualizarCartelasView;

    @Mock
    private AppDataSource appDataSource;

    private VisualizaCartelasPresenter visualizaCartelasPresenter;

    @Captor
    private ArgumentCaptor<List<CartelaPedra>> cartelaPedras;

    @Before
    public void setUp() {
        initMocks(this);

        visualizaCartelasPresenter = new VisualizaCartelasPresenter(
                appDataSource,
                new ImmediateScheduleProvider()
        );

        List<CartelaPedra> cartelaPedras = Lists.newArrayList(
                new CartelaPedra("1", "1", 1, 1)
        );

        when(appDataSource.getPedrasByCartelaId("1")).thenReturn(Single.just(cartelaPedras));

        visualizaCartelasPresenter.subscribe(visualizarCartelasView);
    }

    @Test
    public void carregarCartela_apresentarCartela() {
        visualizaCartelasPresenter.carregarCartela("1");

        verify(visualizarCartelasView).apresentarCartela(cartelaPedras.capture());
        assertThat(cartelaPedras.getValue().get(0).getCartelaId(), equalTo("1"));
    }
}
