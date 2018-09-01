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
    public Single<List<CartelaFiltroDTO>> getCartelasFiltro() {
        if (cartelasFiltro != null)
            return Single.just(cartelasFiltro);
        else
            cartelasFiltro = new ArrayList<>();

        final int[] maxId = new int[1];
        Executors.newSingleThreadScheduledExecutor().execute(() ->
                (new CompositeDisposable())
                        .add(getCartelaUltimoId()
                                .subscribe(id -> maxId[0] = id)));

        while (maxId[0] <= 0) {}

        for (int cartelaId = 1; cartelaId <= maxId[0]; cartelaId++){
            cartelasFiltro.add(new CartelaFiltroDTO(cartelaId, false, false));
        }

        return Single.just(cartelasFiltro);
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
