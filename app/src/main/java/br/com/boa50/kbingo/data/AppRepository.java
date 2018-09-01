package br.com.boa50.kbingo.data;

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

//        final int[] maxId = new int[1];
//        Executors.newSingleThreadScheduledExecutor().execute(() ->
//                (new CompositeDisposable())
//                        .add(getCartelaUltimoId()
//                                .subscribe(id -> maxId[0] = id)));
//
//        while (maxId[0] <= 0) {}
//
//        for (int cartelaId = 1; cartelaId <= maxId[0]; cartelaId++){
//            cartelasFiltro.add(new CartelaFiltroDTO(cartelaId, false, false));
//        }

//        Executors.newSingleThreadScheduledExecutor().execute(() ->
//                (new CompositeDisposable())
//                        .add(getCartelaUltimoId()
//                                .subscribe(this::preencherCartelasFiltro)));

        Executors.newSingleThreadScheduledExecutor().execute(() ->
                (new CompositeDisposable())
                        .add(getCartelaUltimoId()
                                .subscribe(id -> {
                                    Thread.sleep(5000);
                                    preencherCartelasFiltro(id);
                                })));

//        return Flowable.fromIterable(cartelasFiltro).toList().toFlowable();

        return getCartelaUltimoId()
                .flatMap(maxId -> Flowable.range(1, maxId)
                .flatMap(id -> cartelasFiltro.add(retornarCartelaFiltro(id)))
                        .toList()
                        .toFlowable());
    }

    private void preencherCartelasFiltro(int cartelasMaxId) {
        for (int cartelaId = 1; cartelaId <= cartelasMaxId; cartelaId++){
            cartelasFiltro.add(new CartelaFiltroDTO(cartelaId, false, false));
        }
    }
    private CartelaFiltroDTO retornarCartelaFiltro(int id) {
        return new CartelaFiltroDTO(id, false, false);
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
