package br.com.boa50.kbingo.data;

import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.entity.Cartela;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.data.utils.PopularTabelas;
import io.reactivex.Single;

public class AppRepository implements AppDataSource {

    private AppDatabase db;

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
    public Single<List<Cartela>> getCartelas() {
        return db.cartelaDao().loadCartelas();
    }

    @Override
    public Single<Cartela> getCartelaById(String id) {
        return db.cartelaDao().loadCartela(id);
    }

    @Override
    public Single<String> getCartelaUltimoId() {
        return db.cartelaDao().loadCartelaMaxId();
    }

    @Override
    public Single<List<CartelaPedra>> getPedrasByCartelaId(String id) {
        return db.cartelaPedraDao().loadCartelaPedras(id);
    }

    @Override
    public void initializeDatabase() {
        PopularTabelas.preencherDadosIniciais(db);
    }
}
