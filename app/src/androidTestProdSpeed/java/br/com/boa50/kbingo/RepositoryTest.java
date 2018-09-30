package br.com.boa50.kbingo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import br.com.boa50.kbingo.data.AppRepository;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.disposables.CompositeDisposable;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {
    private static AppRepository repository;
    private static ImmediateScheduleProvider scheduleProvider;
    private static CompositeDisposable compositeDisposable;
    private AtomicBoolean updated;

    @BeforeClass
    public static void setup() {
        repository = CustomProcedures.initializeRepositoryDatabase();
        scheduleProvider = new ImmediateScheduleProvider();
        compositeDisposable = new CompositeDisposable();
    }

    @Before
    public void initialize() {
        updated = new AtomicBoolean();
    }

    @AfterClass
    public static void tearDown() {
        repository.getDb().close();
    }

    @Test
    public void carregarLetras_verificarQuantidadeCarregada() {
        AtomicInteger size = new AtomicInteger();
        compositeDisposable.add(repository
                .getLetras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(letras -> {
                    size.set(letras.size());
                    updated.set(true);
                }));

        esperarDados();
        assertThat(size.get(), is(5));
    }

    @Test
    public void carregarPedras_verificarQuantidadeCarregada() {
        AtomicInteger size = new AtomicInteger();
        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> {
                    size.set(pedras.size());
                    updated.set(true);
                }));

        esperarDados();
        assertThat(size.get(), is(75));
    }

    @Test
    public void carregarCartelas_verificarQuantidadeCarregada() {
        AtomicInteger size = new AtomicInteger();
        AtomicInteger sizeMax = new AtomicInteger();

        compositeDisposable.add(repository
                .getCartelas()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(cartelas -> {
                    size.set(cartelas.size());

                    compositeDisposable.add(repository
                            .getCartelaUltimoId()
                            .subscribeOn(scheduleProvider.io())
                            .observeOn(scheduleProvider.ui())
                            .subscribe(maxId -> {
                                sizeMax.set(maxId);
                                updated.set(true);
                            }));
                }));

        esperarDados();
        assertThat(size.get(), is(sizeMax.get()));
    }

    @Test
    public void sortearPedra_terPedraSorteada() {
        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> {
                    repository.updatePedraSorteada(1);
                    updated.set(true);
                }));

        esperarDados();
        assertThat(repository.hasPedraSorteada(), is(true));
    }

    @Test
    public void sortearPedras_atualizarQuantidadeSorteadas() {
        AtomicInteger qtdPedrasSorteadas = new AtomicInteger();
        repository.cleanPedrasSorteadas();

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> {
                    repository.updatePedraSorteada(1);
                    repository.updatePedraSorteada(2);

                    compositeDisposable.add(repository
                            .getPedras()
                            .subscribeOn(scheduleProvider.io())
                            .observeOn(scheduleProvider.ui())
                            .subscribe(pedras2 -> {
                                for (Pedra pedra : pedras2) {
                                    if (pedra.isSorteada()) {
                                        qtdPedrasSorteadas.set(qtdPedrasSorteadas.get() + 1);
                                    }
                                }
                                updated.set(true);
                            }));
                }));

        esperarDados();
        assertThat(qtdPedrasSorteadas.get(), is(2));
    }

    @Test
    public void sortearPedras_registrarPedrasSorteadasPorCartela() {
        AtomicInteger qtdPedrasSorteadasCartela = new AtomicInteger();
        AtomicInteger cartelaId = new AtomicInteger();
        cartelaId.set(1);

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> compositeDisposable.add(repository
                        .getCartelas()
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribe(cartelas -> compositeDisposable.add(repository
                                .getPedrasByCartelaId(cartelaId.get())
                                .subscribeOn(scheduleProvider.io())
                                .observeOn(scheduleProvider.ui())
                                .subscribe(cartelaPedras -> {
                                    repository.updatePedraSorteada(cartelaPedras.get(0).getPedraId());
                                    repository.updatePedraSorteada(cartelaPedras.get(1).getPedraId());

                                    compositeDisposable.add(repository
                                            .getCartela(cartelaId.get())
                                            .subscribeOn(scheduleProvider.io())
                                            .observeOn(scheduleProvider.ui())
                                            .subscribe(cartela -> {
                                                qtdPedrasSorteadasCartela.set(cartela.getQtdPedrasSorteadas());
                                                updated.set(true);
                                            }));
                                }))))));


        esperarDados();
        assertThat(qtdPedrasSorteadasCartela.get(), is(2));
    }

    @Test
    public void resetarPedras_zerarQuantidadeSorteadas() {
        AtomicInteger qtdPedrasSorteadas = new AtomicInteger();
        repository.cleanPedrasSorteadas();

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> {
                    repository.updatePedraSorteada(1);

                    compositeDisposable.add(repository
                            .getPedras()
                            .subscribeOn(scheduleProvider.io())
                            .observeOn(scheduleProvider.ui())
                            .subscribe(pedras2 -> {
                                for (Pedra pedra : pedras2) {
                                    if (pedra.isSorteada()) {
                                        qtdPedrasSorteadas.set(qtdPedrasSorteadas.get() + 1);
                                    }
                                }
                                updated.set(true);
                            }));
                }));

        esperarDados();
        assertThat(qtdPedrasSorteadas.get(), is(1));

        repository.cleanPedrasSorteadas();
        assertThat(repository.hasPedraSorteada(), is(false));
    }

    @Test
    public void atualizarPedra_obertUltimaPedraSorteadaCorreta() {
        AtomicInteger pedraSorteadaId = new AtomicInteger();

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> {
                    repository.updatePedraSorteada(50);
                    pedraSorteadaId.set(repository.getUltimaPedraSorteada().getId());
                    updated.set(true);
                }));

        esperarDados();
        assertThat(pedraSorteadaId.get(), is(50));
    }

    @Test
    public void alterarTipoSorteio_obterSorteioCorreto() {
        repository.setTipoSorteioId(TipoSorteioDTO.CARTELA_CHEIA);
        assertThat(repository.getTipoSorteio().getNome(),
                is(TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CARTELA_CHEIA).getNome()));

        repository.setTipoSorteioId(TipoSorteioDTO.CINCO_PEDRAS);
        assertThat(repository.getTipoSorteio().getNome(),
                is(TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CINCO_PEDRAS).getNome()));
    }

    @Test
    public void obterPedraCorreta() {
        AtomicInteger pedraId = new AtomicInteger();
        int pedraIdBusca = 50;

        compositeDisposable.add(repository
                .getPedra(pedraIdBusca)
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedra -> {
                    pedraId.set(pedra.getId());
                    updated.set(true);
                }));

        esperarDados();
        assertThat(pedraId.get(), is(pedraIdBusca));
    }

    @Test
    public void obterPedrasCorretasPorCartelaId() {
        AtomicIntegerArray pedrasCartelaId = new AtomicIntegerArray(24);
        int cartelaIdBusca = 50;

        compositeDisposable.add(repository
                .getPedrasByCartelaId(cartelaIdBusca)
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(cartelaPedras -> {
                    for (int i = 0; i < cartelaPedras.size(); i++) {
                        pedrasCartelaId.set(i, cartelaPedras.get(i).getCartelaId());
                    }
                    updated.set(true);
                }));

        esperarDados();
        for (int i = 0; i < pedrasCartelaId.length(); i++) {
            assertThat(pedrasCartelaId.get(i), is(cartelaIdBusca));
        }
    }

    @Test
    public void obterCartelaCorreta() {
        AtomicInteger cartelaId = new AtomicInteger();
        int cartelaIdBusca = 50;

        compositeDisposable.add(repository
                .getCartela(cartelaIdBusca)
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(cartela -> {
                    cartelaId.set(cartela.getCartelaId());
                    updated.set(true);
                }));

        esperarDados();
        assertThat(cartelaId.get(), is(cartelaIdBusca));
    }

    @Test
    public void sortearPedras_obterCartelasGanhadoras() {
        AtomicIntegerArray cartelasGanhadorasIds = new AtomicIntegerArray(200);
        AtomicInteger cartelaId = new AtomicInteger();
        cartelaId.set(1);

        repository.setTipoSorteioId(TipoSorteioDTO.CINCO_PEDRAS);

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> compositeDisposable.add(repository
                        .getCartelas()
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribe(cartelas -> compositeDisposable.add(repository
                                .getPedrasByCartelaId(cartelaId.get())
                                .subscribeOn(scheduleProvider.io())
                                .observeOn(scheduleProvider.ui())
                                .subscribe(cartelaPedras -> {
                                    for (int i = 0; i < 5; i++) {
                                        repository.updatePedraSorteada(cartelaPedras.get(i).getPedraId());
                                    }

                                    compositeDisposable.add(repository
                                            .getCartelasGanhadoras()
                                            .subscribeOn(scheduleProvider.io())
                                            .observeOn(scheduleProvider.ui())
                                            .subscribe(cartelasGanhadoras -> {
                                                for (int j = 0; j < cartelasGanhadoras.size(); j++) {
                                                    cartelasGanhadorasIds.set(j, cartelasGanhadoras.get(j).getCartelaId());
                                                }
                                                updated.set(true);
                                            }));
                                }))))));


        esperarDados();
        int tamanhoArrayPreenchido = 0;
        for (int i = 0; i < cartelasGanhadorasIds.length(); i++) {
            if (cartelasGanhadorasIds.get(i) > 0) tamanhoArrayPreenchido++;
        }

        assertThat(tamanhoArrayPreenchido, greaterThanOrEqualTo(1));
        assertThat(cartelasGanhadorasIds.get(0), is(cartelaId.get()));
    }

    @Test
    public void colocarCartelaGanhadora_obterCartelasFiltroCorretamente() {
        AtomicInteger cartelasFiltroSize = new AtomicInteger();
        AtomicInteger cartelasFiltroPrimeiraCartelaId = new AtomicInteger();
        AtomicBoolean cartelasFiltroPrimeiraCartelaGanhadora = new AtomicBoolean();
        AtomicInteger cartelaId = new AtomicInteger();
        cartelaId.set(1);

        repository.setTipoSorteioId(TipoSorteioDTO.CINCO_PEDRAS);

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> compositeDisposable.add(repository
                        .getCartelas()
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribe(cartelas -> compositeDisposable.add(repository
                                .getPedrasByCartelaId(cartelaId.get())
                                .subscribeOn(scheduleProvider.io())
                                .observeOn(scheduleProvider.ui())
                                .subscribe(cartelaPedras -> {
                                    for (int i = 0; i < 5; i++) {
                                        repository.updatePedraSorteada(cartelaPedras.get(i).getPedraId());
                                    }

                                    compositeDisposable.add(repository
                                            .getCartelasFiltro()
                                            .subscribeOn(scheduleProvider.io())
                                            .observeOn(scheduleProvider.ui())
                                            .subscribe(cartelasFiltro -> {
                                                cartelasFiltroSize.set(cartelasFiltro.size());
                                                cartelasFiltroPrimeiraCartelaId.set(cartelasFiltro.get(0).getCartelaId());
                                                cartelasFiltroPrimeiraCartelaGanhadora.set(cartelasFiltro.get(0).isGanhadora());
                                                updated.set(true);
                                            }));
                                }))))));


        esperarDados();
        assertThat(cartelasFiltroSize.get(), is(200));
        assertThat(cartelasFiltroPrimeiraCartelaId.get(), is(cartelaId.get()));
        assertThat(cartelasFiltroPrimeiraCartelaGanhadora.get(), is(true));
    }

    @Test
    public void adicionarCartelaFiltro_obterCartelasSorteaveis() {
        AtomicInteger cartelaFitroId = new AtomicInteger();
        AtomicInteger cartelaSorteavelId = new AtomicInteger();
        AtomicInteger cartelasSorteaveisSize = new AtomicInteger();
        cartelaFitroId.set(50);

        compositeDisposable.add(repository
                .getCartelasFiltro()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(cartelasFiltro -> {
                    repository.updateCartelasFiltro(cartelaFitroId.get(), true);
                    repository.updateCartelasFiltro(cartelaFitroId.get() + 1, true);

                    compositeDisposable.add(repository
                            .getCartelasSorteaveis()
                            .subscribeOn(scheduleProvider.io())
                            .observeOn(scheduleProvider.ui())
                            .subscribe(cartelasSorteaveisIds -> {
                                cartelasSorteaveisSize.set(cartelasSorteaveisIds.size());
                                cartelaSorteavelId.set(cartelasSorteaveisIds.get(0));
                                updated.set(true);
                            }));
                }));

        esperarDados();
        assertThat(cartelasSorteaveisSize.get(), is(2));
        assertThat(cartelaSorteavelId.get(), is(cartelaFitroId.get()));
    }

    @Test
    public void limparCartelasGanhadoras_trazerListaGanhadorasVazia() {
        AtomicIntegerArray cartelasGanhadorasIds = new AtomicIntegerArray(200);
        AtomicInteger cartelaId = new AtomicInteger();
        cartelaId.set(1);

        repository.setTipoSorteioId(TipoSorteioDTO.CINCO_PEDRAS);

        compositeDisposable.add(repository
                .getPedras()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(pedras -> compositeDisposable.add(repository
                        .getCartelas()
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribe(cartelas -> compositeDisposable.add(repository
                                .getPedrasByCartelaId(cartelaId.get())
                                .subscribeOn(scheduleProvider.io())
                                .observeOn(scheduleProvider.ui())
                                .subscribe(cartelaPedras -> {
                                    for (int i = 0; i < 5; i++) {
                                        repository.updatePedraSorteada(cartelaPedras.get(i).getPedraId());
                                    }

                                    repository.cleanCartelasGanhadoras();

                                    compositeDisposable.add(repository
                                            .getCartelasGanhadoras()
                                            .subscribeOn(scheduleProvider.io())
                                            .observeOn(scheduleProvider.ui())
                                            .subscribe(cartelasGanhadoras -> {
                                                for (int j = 0; j < cartelasGanhadoras.size(); j++) {
                                                    cartelasGanhadorasIds.set(j, cartelasGanhadoras.get(j).getCartelaId());
                                                }
                                                updated.set(true);
                                            }));
                                }))))));


        esperarDados();
        int tamanhoArrayPreenchido = 0;
        for (int i = 0; i < cartelasGanhadorasIds.length(); i++) {
            if (cartelasGanhadorasIds.get(i) > 0) tamanhoArrayPreenchido++;
        }

        assertThat(tamanhoArrayPreenchido, is(0));
    }

    @Test
    public void limparCartelasFiltro_trazerListaFiltroSemSelecao() {
        AtomicBoolean cartelasFiltroSelecionada = new AtomicBoolean();
        cartelasFiltroSelecionada.set(false);

        compositeDisposable.add(repository
                .getCartelasFiltro()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(cartelasFiltro -> {
                    repository.updateCartelasFiltro(50, true);
                    repository.updateCartelasFiltro(51, true);

                    repository.cleanCartelasFiltro();

                    compositeDisposable.add(repository
                            .getCartelasFiltro()
                            .subscribeOn(scheduleProvider.io())
                            .observeOn(scheduleProvider.ui())
                            .subscribe(cartelasFiltroLimpa -> {
                                for (CartelaFiltroDTO cartelaFiltro : cartelasFiltroLimpa) {
                                    if (cartelaFiltro.isSelecionada()) cartelasFiltroSelecionada.set(true);
                                }
                                updated.set(true);
                            }));
                }));

        esperarDados();
        assertThat(cartelasFiltroSelecionada.get(), is(false));
    }

    private void esperarDados() {
        while (!updated.get()){}
    }
}
