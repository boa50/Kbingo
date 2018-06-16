package br.com.boa50.kbingo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.entity.Cartela;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Single;

import static br.com.boa50.kbingo.Constant.FORMAT_PEDRA;
import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;

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
        Letra[] letras = new Letra[] {
                new Letra("1", "K", 0),
                new Letra("2", "I", 1),
                new Letra("3", "N", 2),
                new Letra("4", "K", 3),
                new Letra("5", "A", 4)
        };

        List<Pedra> pedras = new ArrayList<>();
        for (int i = 1; i <= QTDE_PEDRAS_LETRA*letras.length; i++) {
            String letraId = letras[(i-1)/ QTDE_PEDRAS_LETRA].getId();

            pedras.add(new Pedra(
                    Integer.toString(i),
                    letraId,
                    String.format(Locale.ENGLISH, FORMAT_PEDRA, i)
            ));
        }

        Cartela[] cartelas = new Cartela[] {
                new Cartela("1")
        };
        CartelaPedra[] cartelaPedras = new CartelaPedra[] {
                new CartelaPedra("1","13",1,0),new CartelaPedra("1","28",1,1),new CartelaPedra("1","43",1,2),new CartelaPedra("1","46",1,3),new CartelaPedra("1","72",1,4),new CartelaPedra("1","5",2,0),new CartelaPedra("1","16",2,1),new CartelaPedra("1","38",2,2),new CartelaPedra("1","57",2,3),new CartelaPedra("1","66",2,4),new CartelaPedra("1","3",3,0),new CartelaPedra("1","25",3,1),new CartelaPedra("1","53",3,3),new CartelaPedra("1","75",3,4),new CartelaPedra("1","9",4,0),new CartelaPedra("1","22",4,1),new CartelaPedra("1","40",4,2),new CartelaPedra("1","52",4,3),new CartelaPedra("1","74",4,4),new CartelaPedra("1","15",5,0),new CartelaPedra("1","19",5,1),new CartelaPedra("1","37",5,2),new CartelaPedra("1","48",5,3),new CartelaPedra("1","67",5,4)
        };
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.cartelaPedraDao().deleteAll();
                db.cartelaDao().deleteAll();
                db.pedraDao().deleteAll();
                db.letraDao().deleteAll();

                db.letraDao().insertAll(letras);
                db.pedraDao().insertAll(pedras);
                db.cartelaDao().insertAll(cartelas);
                db.cartelaPedraDao().insertAll(cartelaPedras);
            }
        });
    }
}
