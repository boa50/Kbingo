package br.com.boa50.kbingo.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.data.utils.PopularTabelas;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class AppRepository implements AppDataSource {

    private AppDatabase db;
    private List<CartelaFiltroDTO> cartelasFiltro;

    @Inject
    public AppRepository(AppDatabase db) {
        this.db = db;
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
    public Flowable<List<CartelaFiltroDTO>> getCartelasFiltro() {
        if (cartelasFiltro != null)
            return Flowable.fromIterable(cartelasFiltro).toList().toFlowable();
        else
            cartelasFiltro = new ArrayList<>();

        return getCartelaUltimoId().toFlowable()
                .flatMap(maxId -> Flowable.range(1, maxId)
                        .flatMap(id -> Flowable.just(new CartelaFiltroDTO(id, false, false))
                                .doOnNext(cartelaFiltroDTO -> cartelasFiltro.add(cartelaFiltroDTO))
                                .map(cartelaFiltroDTO -> cartelasFiltro)));
    }

    @Override
    public Flowable<List<Integer>> getCartelasSorteaveis() {
        List<Integer> cartelasSorteaveis = new ArrayList<>();

        Flowable<List<Integer>> cartelasSorteaveisFlowable = getCartelasFiltro()
                .flatMap(cartelasFiltro -> Flowable.fromIterable(cartelasFiltro)
                        .filter(CartelaFiltroDTO::isSelecionada)
                        .doOnNext(cartelaFiltroDTO -> cartelasSorteaveis
                                .add(cartelaFiltroDTO.getCartelaId()))
                        .map(cartelaFiltroDTO -> cartelasSorteaveis));

        return Flowable.concat(cartelasSorteaveisFlowable, Flowable.just(new ArrayList<>()));
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
