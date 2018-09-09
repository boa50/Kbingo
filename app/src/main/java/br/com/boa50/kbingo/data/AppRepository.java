package br.com.boa50.kbingo.data;

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
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class AppRepository implements AppDataSource {

    private AppDatabase db;
    private List<CartelaDTO> cartelas;
    private List<CartelaFiltroDTO> cartelasFiltro;
    private List<Integer> cartelasSorteaveis;
    private Integer tipoSorteioId;

    @Inject
    public AppRepository(AppDatabase db) {
        this.db = db;
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
    public Single<List<Letra>> getLetras() {
        return db.letraDao().loadLetras();
    }

    @Override
    public Single<List<Pedra>> getPedras() {
        return db.pedraDao().loadPedras();
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
                .doOnNext(cartela -> cartelas.add(cartela))
                .toList()
                .toFlowable())));
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
                        .flatMap(id -> Flowable.just(new CartelaFiltroDTO(id, false, false))
                                .doOnNext(cartelaFiltroDTO -> cartelasFiltro.add(cartelaFiltroDTO))
                                .toList()
                                .toFlowable()));
    }

    @Override
    public Flowable<List<Integer>> getCartelasSorteaveis() {
        if (cartelasSorteaveis == null) cartelasSorteaveis = new ArrayList<>();
        else cartelasSorteaveis.clear();

        Flowable<List<Integer>> cartelasSorteaveisFlowable = getCartelasFiltro()
                .flatMap(cartelasFiltro -> Flowable.fromIterable(cartelasFiltro)
                        .filter(CartelaFiltroDTO::isSelecionada)
                        .map(CartelaFiltroDTO::getCartelaId)
                        .doOnNext(cartelaId -> cartelasSorteaveis.add(cartelaId))
                        .toList()
                        .toFlowable());

        return Flowable.concat(cartelasSorteaveisFlowable, Flowable.just(cartelasSorteaveis));
    }

    @Override
    public void updateCartelasFiltro(int id, boolean selecionada) {
        cartelasFiltro.get(id - 1).setSelecionada(selecionada);
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
}
