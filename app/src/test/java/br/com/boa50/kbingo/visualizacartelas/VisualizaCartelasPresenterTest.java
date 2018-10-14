package br.com.boa50.kbingo.visualizacartelas;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.helper.EnviromentHelper;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VisualizaCartelasPresenterTest {

    private final int CARTELA_MAX_ID = 2;

    @Mock
    private VisualizaCartelasContract.View visualizarCartelasView;
    @Mock
    private AppDataSource appDataSource;
    @Mock
    private EnviromentHelper enviroment;

    private VisualizaCartelasPresenter visualizaCartelasPresenter;

    @Captor
    private ArgumentCaptor<List<CartelaPedra>> cartelaPedras;
    @Captor
    private ArgumentCaptor<List<Pedra>> pedras;

    private List<Pedra> PEDRAS;

    @Before
    public void setUp() {
        initMocks(this);

        visualizaCartelasPresenter = new VisualizaCartelasPresenter(
                appDataSource,
                new ImmediateScheduleProvider(),
                enviroment
        );

        List<Letra> letras = Lists.newArrayList(
                new Letra(1, "K")
        );
        PEDRAS = Lists.newArrayList(
                new Pedra(1, 1, "01")
        );
        List<CartelaPedra> cartelaPedras = Lists.newArrayList(
                new CartelaPedra(1, 1, 1, 1)
        );
        List<CartelaPedra> cartelaPedras2 = Lists.newArrayList(
                new CartelaPedra(2, 1, 1, 1)
        );
        List<Integer> cartelasIds = Lists.newArrayList(1, 2);
        List<CartelaPedra> cartelasPedras = Lists.newArrayList(
                cartelaPedras.get(0),
                cartelaPedras2.get(0)
        );

        when(appDataSource.getLetras()).thenReturn(Single.just(letras));
        when(appDataSource.getPedras()).thenReturn(Flowable.just(PEDRAS));
        when(appDataSource.getPedrasByCartelaId(1)).thenReturn(Single.just(cartelaPedras));
        when(appDataSource.getPedrasByCartelaId(2)).thenReturn(Single.just(cartelaPedras2));
        when(appDataSource.getPedrasByCartelaId(50)).thenReturn(Single.just(new ArrayList<>()));
        when(appDataSource.getPedrasByCartelasIds(cartelasIds)).thenReturn(Single.just(cartelasPedras));
        when(appDataSource.getCartelaUltimoId()).thenReturn(Single.just(CARTELA_MAX_ID));
        when(enviroment.getDowloadDirectory()).thenReturn(new File(""));

        visualizaCartelasPresenter.subscribe(visualizarCartelasView);
    }

    @Test
    public void carregarCartela_apresentarCartela() {
        visualizaCartelasPresenter.carregarCartela(1, false);

        verify(visualizarCartelasView).apresentarCartela(cartelaPedras.capture(), pedras.capture());
        assertThat(cartelaPedras.getValue().get(0).getCartelaId(), equalTo(1));
        assertThat(pedras.getValue(), equalTo(null));
    }

    @Test
    public void carregarCartelaMaiorQueMaximo_apresentarUltimaCartela() {
        visualizaCartelasPresenter.carregarCartela(50, false);

        verify(visualizarCartelasView).apresentarCartela(cartelaPedras.capture(), pedras.capture());
        assertThat(cartelaPedras.getValue().get(0).getCartelaId(), equalTo(CARTELA_MAX_ID));
        verify(visualizarCartelasView).apresentarMaximoIdCartela(CARTELA_MAX_ID);
        assertThat(pedras.getValue(), equalTo(null));
    }

    @Test
    public void carregarCartela_confereCartelas_apresentarPedras() {
        visualizaCartelasPresenter.carregarCartela(1, true);

        verify(appDataSource).getPedras();
        verify(visualizarCartelasView).apresentarCartela(cartelaPedras.capture(), pedras.capture());
        assertThat(cartelaPedras.getValue().get(0).getCartelaId(), equalTo(1));
        assertThat(pedras.getValue(), equalTo(PEDRAS));
    }

    @Test
    public void prepararDialogExportarSemIds_carregarIdsCorretos() {
        ArgumentCaptor<Integer> idInicial = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> idFinal = ArgumentCaptor.forClass(Integer.class);

        visualizaCartelasPresenter.prepararDialogExportar(0,0);

        verify(visualizarCartelasView).abrirDialogExportarCartelas(idInicial.capture(), idFinal.capture());
        assertThat(idInicial.getValue(), equalTo(1));
        assertThat(idFinal.getValue(), equalTo(CARTELA_MAX_ID));
    }

    @Test
    public void prepararDialogExportarComIds_carregarIdsCorretos() {
        ArgumentCaptor<Integer> idInicial = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> idFinal = ArgumentCaptor.forClass(Integer.class);

        visualizaCartelasPresenter.prepararDialogExportar(1,1);

        verify(visualizarCartelasView).abrirDialogExportarCartelas(idInicial.capture(), idFinal.capture());
        assertThat(idInicial.getValue(), equalTo(1));
        assertThat(idFinal.getValue(), equalTo(1));
    }

    @Test
    public void exportarCartelasIdsIncompativeis_mostrarMensagemCorreta() {
        visualizaCartelasPresenter.exportarCartelas(50, 1);

        verify(visualizarCartelasView).mostrarMensagensIdsIncompativeis();
        verify(visualizarCartelasView, times(0)).mostrarMensagemInicioExportacao();
        verify(visualizarCartelasView, times(0)).realizarDownload(Mockito.any(File.class));
    }

    @Test
    public void exportarCartelas_realizarDownload() {
        visualizaCartelasPresenter.exportarCartelas(1, 2);

        verify(visualizarCartelasView).mostrarMensagemInicioExportacao();
        verify(visualizarCartelasView).realizarDownload(Mockito.any(File.class));
        verify(visualizarCartelasView, times(0)).mostrarMensagensIdsIncompativeis();
    }
}
