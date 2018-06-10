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
                new Cartela("1"),
                new Cartela("2")
        };
        CartelaPedra[] cartelaPedras = new CartelaPedra[] {
                new CartelaPedra("1","1", 1, 0),
                new CartelaPedra("1","2", 2, 0),
                new CartelaPedra("1","3", 3, 0),
                new CartelaPedra("1","4", 4, 0),
                new CartelaPedra("1","5", 5, 0),
                new CartelaPedra("1","16", 1, 1),
                new CartelaPedra("1","17", 2, 1),
                new CartelaPedra("1","18", 3, 1),
                new CartelaPedra("1","19", 4, 1),
                new CartelaPedra("1","20", 5, 1),
                new CartelaPedra("1","31", 1, 2),
                new CartelaPedra("1","32", 2, 2),
                new CartelaPedra("1","33", 4, 2),
                new CartelaPedra("1","34", 5, 2),
                new CartelaPedra("1","46", 1, 3),
                new CartelaPedra("1","47", 2, 3),
                new CartelaPedra("1","48", 3, 3),
                new CartelaPedra("1","49", 4, 3),
                new CartelaPedra("1","50", 5, 3),
                new CartelaPedra("1","61", 1, 4),
                new CartelaPedra("1","62", 2, 4),
                new CartelaPedra("1","63", 3, 4),
                new CartelaPedra("1","64", 4, 4),
                new CartelaPedra("1","65", 5, 4),
                new CartelaPedra("2","6", 1, 0),
                new CartelaPedra("2","7", 2, 0),
                new CartelaPedra("2","8", 3, 0),
                new CartelaPedra("2","9", 4, 0),
                new CartelaPedra("2","10", 5, 0),
                new CartelaPedra("2","21", 1, 1),
                new CartelaPedra("2","22", 2, 1),
                new CartelaPedra("2","23", 3, 1),
                new CartelaPedra("2","24", 4, 1),
                new CartelaPedra("2","25", 5, 1),
                new CartelaPedra("2","36", 1, 2),
                new CartelaPedra("2","37", 2, 2),
                new CartelaPedra("2","38", 4, 2),
                new CartelaPedra("2","39", 5, 2),
                new CartelaPedra("2","51", 1, 3),
                new CartelaPedra("2","52", 2, 3),
                new CartelaPedra("2","53", 3, 3),
                new CartelaPedra("2","54", 4, 3),
                new CartelaPedra("2","55", 5, 3),
                new CartelaPedra("2","66", 1, 4),
                new CartelaPedra("2","67", 2, 4),
                new CartelaPedra("2","68", 3, 4),
                new CartelaPedra("2","69", 4, 4),
                new CartelaPedra("2","70", 5, 4)
        };
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.letraDao().deleteAll();
                db.letraDao().insertAll(letras);
                db.pedraDao().deleteAll();
                db.pedraDao().insertAll(pedras);
                db.cartelaDao().deleteAll();
                db.cartelaDao().insertAll(cartelas);
                db.cartelaPedraDao().deleteAll();
                db.cartelaPedraDao().insertAll(cartelaPedras);
            }
        });
    }
}
