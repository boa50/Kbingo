package br.com.boa50.kbingo.data;

import java.util.List;

import javax.inject.Singleton;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public interface AppDataSource {
    boolean hasPedraSorteada();
    Pedra getUltimaPedraSorteada();
    TipoSorteioDTO getTipoSorteio();
    int getTipoSorteioId();
    void setTipoSorteioId(int tipoSorteioId);

    Single<Pedra> getPedra(int id);
    Single<List<Letra>> getLetras();
    Single<Integer> getCartelaUltimoId();
    Single<List<CartelaPedra>> getPedrasByCartelaId(int id);
    Single<CartelaDTO> getCartela(int id);

    Flowable<List<Pedra>> getPedras();
    Flowable<List<CartelaDTO>> getCartelas();
    Flowable<List<CartelaDTO>> getCartelasGanhadoras();
    Flowable<List<CartelaFiltroDTO>> getCartelasFiltro();
    Flowable<List<Integer>> getCartelasSorteaveis();

    void updatePedraSorteada(int id);
    void updateCartelas(Pedra ultimaPedraSorteada);
    void updateCartelasFiltro(int id, boolean selecionada);

    void cleanPedrasSorteadas();
    void cleanCartelasGanhadoras();
    void cleanCartelasFiltro();

    void initializeDatabase();
}
