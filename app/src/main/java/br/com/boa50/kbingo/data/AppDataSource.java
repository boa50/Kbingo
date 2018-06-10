package br.com.boa50.kbingo.data;

import java.util.List;

import javax.inject.Singleton;

import br.com.boa50.kbingo.data.entity.Cartela;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Single;

@Singleton
public interface AppDataSource {
    Single<List<Letra>> getLetras();

    Single<List<Pedra>> getPedras();

    Single<List<Cartela>> getCartelas();
    Single<Cartela> getCartelaById(String id);
    Single<String> getCartelaUltimoId();
    Single<List<CartelaPedra>> getPedrasByCartelaId(String id);

    void initializeDatabase();
}
