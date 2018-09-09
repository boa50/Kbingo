package br.com.boa50.kbingo.data;

import java.util.List;

import javax.inject.Singleton;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public interface AppDataSource {
    Single<List<Letra>> getLetras();
    Single<List<Pedra>> getPedras();
    Single<Integer> getCartelaUltimoId();
    Single<List<CartelaPedra>> getPedrasByCartelaId(int id);

    Flowable<List<CartelaDTO>> getCartelas();
    Flowable<List<CartelaFiltroDTO>> getCartelasFiltro();
    Flowable<List<Integer>> getCartelasSorteaveis();

    void updateCartelasFiltro(int id, boolean selecionada);

    void initializeDatabase();
}
