package br.com.boa50.kbingo.data;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.data.utils.PopularTabelas;
import br.com.boa50.kbingo.util.CartelaUtils;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class AppRepository implements AppDataSource {

    private AppDatabase db;
    private List<Pedra> pedras;
    private Pedra ultimaPedraSorteada;
    private List<CartelaDTO> cartelas;
    private List<CartelaFiltroDTO> cartelasFiltro;
    private Integer tipoSorteioId;

    @Inject
    public AppRepository(AppDatabase db) {
        this.db = db;
    }

    @Override
    public boolean hasPedraSorteada() {
        if (pedras != null) {
            for (Pedra pedra : pedras) {
                if (pedra.isSorteada()) return true;
            }
        }
        return false;
    }

    @Override
    public Pedra getUltimaPedraSorteada() {
        return ultimaPedraSorteada;
    }

    @Override
    public TipoSorteioDTO getTipoSorteio() {
        return TipoSorteioDTO.getTipoSorteio(getTipoSorteioId());
    }

    @Override
    public int getTipoSorteioId() {
        if (tipoSorteioId == null)
            tipoSorteioId = TipoSorteioDTO.CARTELA_CHEIA;

        return tipoSorteioId;
    }

    @Override
    public void setTipoSorteioId(int tipoSorteioId) {
        this.tipoSorteioId = tipoSorteioId;
    }

    @Override
    public Single<Pedra> getPedra(int id) {
        return getPedras()
                .flatMap(pedras -> Flowable.fromIterable(pedras)
                .filter(pedra -> pedra.getId() == id))
                .firstOrError();
    }

    @Override
    public Single<List<Letra>> getLetras() {
        return db.letraDao().loadLetras();
    }

    @Override
    public Single<Integer> getCartelaUltimoId() {
        return db.cartelaPedraDao().loadCartelaMaxId();
    }

    @Override
    public Single<List<CartelaPedra>> getPedrasByCartelaId(int id) {
        return db.cartelaPedraDao().loadCartelaPedras(id);
    }

    @Override
    public Single<List<CartelaPedra>> getPedrasByCartelasIds(List<Integer> ids) {
        return db.cartelaPedraDao().loadCartelaPedras(ids);
    }

    @Override
    public Single<CartelaDTO> getCartela(int id) {
        return getCartelas()
                .flatMap(cartelas -> Flowable.fromIterable(cartelas)
                .filter(cartela -> cartela.getCartelaId() == id))
                .firstOrError();
    }

    @Override
    public Flowable<List<Pedra>> getPedras() {
        if (pedras != null) {
            return Flowable.fromIterable(pedras).toList().toFlowable();
        } else {
            pedras = new ArrayList<>();
        }

        return db.pedraDao().loadPedras()
                .flatMap(pedras -> Flowable.fromIterable(pedras)
                .doOnNext(pedra -> this.pedras.add(pedra))
                .toList()
                .toFlowable());
    }

    @Override
    public Flowable<List<CartelaDTO>> getCartelas() {
        if (cartelas != null) {
            return Flowable.fromIterable(cartelas).toList().toFlowable();
        } else {
            cartelas = new ArrayList<>();
        }

        return getCartelaUltimoId().toFlowable()
                .flatMap(maxId -> Flowable.range(1, maxId)
                        .flatMap(id -> getPedrasByCartelaId(id).toFlowable()
                                .flatMap(cartelaPedras -> Flowable.just(new CartelaDTO(id, cartelaPedras))
                                        .doOnNext(cartela -> cartelas.add(cartela)))))
                                        .toList()
                                        .toFlowable();
    }

    @Override
    public Flowable<List<CartelaDTO>> getCartelasGanhadoras() {
        return getCartelas()
                .flatMap(cartelas -> Flowable.fromIterable(cartelas)
                        .filter(CartelaDTO::isGanhadora)
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<List<CartelaFiltroDTO>> getCartelasFiltro() {
        if (cartelasFiltro != null) {
            return Flowable.fromIterable(cartelasFiltro).toList().toFlowable();
        } else {
            cartelasFiltro = new ArrayList<>();
        }

        return getCartelaUltimoId().toFlowable()
                .flatMap(maxId -> Flowable.range(1, maxId)
                        .flatMap(id -> getCartela(id).toFlowable()
                        .flatMap(cartela -> Flowable.just(new CartelaFiltroDTO(id,
                                        cartela.isGanhadora(),
                                        false))
                                .doOnNext(cartelaFiltroDTO -> cartelasFiltro.add(cartelaFiltroDTO)))))
                                .toList()
                                .toFlowable();
    }

    @Override
    public Flowable<List<Integer>> getCartelasSorteaveis() {
        return getCartelasFiltro()
                .flatMap(cartelasFiltro -> Flowable.fromIterable(cartelasFiltro)
                        .filter(CartelaFiltroDTO::isSelecionada)
                        .map(CartelaFiltroDTO::getCartelaId)
                        .toList()
                        .toFlowable());
    }

    @Override
    public void updatePedraSorteada(int id) {
        if (pedras != null) {
            pedras.get(id - 1).setSorteada(true);
            ultimaPedraSorteada = pedras.get(id - 1);
            updateCartelas(ultimaPedraSorteada);
        }
    }

    private void updateCartelas(Pedra ultimaPedraSorteada) {
        if (cartelas == null) return;
        int qtdePedrasSorteio = getTipoSorteio().getQuantidadePedras();

        for (CartelaDTO cartela : cartelas) {
            if (CartelaUtils.hasCartelaPedra(cartela.getCartelaPedras(), ultimaPedraSorteada)) {
                cartela.setQtdPedrasSorteadas(cartela.getQtdPedrasSorteadas() + 1);

                if (cartela.getQtdPedrasSorteadas() >= qtdePedrasSorteio) {
                    cartela.setGanhadora(true);
                    if (cartelasFiltro != null) {
                        cartelasFiltro.get(cartela.getCartelaId() - 1).setGanhadora(true);
                    }
                }
            }
        }
    }

    @Override
    public void updateCartelasFiltro(int id, boolean selecionada) {
        if (cartelasFiltro != null) {
            cartelasFiltro.get(id - 1).setSelecionada(selecionada);
        }
    }

    @Override
    public void cleanPedrasSorteadas() {
        if (pedras != null) {
            for (Pedra pedra : pedras) {
                pedra.setSorteada(false);
            }
        }
        ultimaPedraSorteada = null;
    }

    @Override
    public void cleanCartelasGanhadoras() {
        if (cartelas != null) {
            for (CartelaDTO cartela : cartelas) {
                cartela.setQtdPedrasSorteadas(0);
                cartela.setGanhadora(false);
                if (cartelasFiltro != null) {
                    cartelasFiltro.get(cartela.getCartelaId() - 1).setGanhadora(false);
                }
            }
        }
    }

    @Override
    public void cleanCartelasFiltro() {
        if (cartelasFiltro != null) {
            for (CartelaFiltroDTO cartelaFiltroDTO : cartelasFiltro) {
                updateCartelasFiltro(cartelaFiltroDTO.getCartelaId(), false);
            }
        }
    }

    @Override
    public void initializeDatabase() {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(db.pedraDao().countPedras()
                    .subscribe(
                            count -> {
                                if (count == 0)
                                    PopularTabelas.preencherDadosIniciais(db);
                            }
                    ));
        });
    }

    @VisibleForTesting
    public AppDatabase getDb() {
        return db;
    }
}
