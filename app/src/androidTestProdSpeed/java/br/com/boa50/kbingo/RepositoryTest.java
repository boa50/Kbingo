package br.com.boa50.kbingo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.boa50.kbingo.data.AppRepository;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.schedulers.ImmediateScheduleProvider;
import io.reactivex.disposables.CompositeDisposable;

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
        compositeDisposable.add(repository
                .getCartelas()
                .subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui())
                .subscribe(cartelas -> {
                    size.set(cartelas.size());
                    updated.set(true);
                }));

        esperarDados();
        assertThat(size.get(), is(200));
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

    private void esperarDados() {
        while (!updated.get()){}
    }
}
