package br.com.boa50.kbingo.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.entity.Cartela;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.StringUtils;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static br.com.boa50.kbingo.Constant.QTDE_LETRAS;
import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;

public class AppRepository implements AppDataSource {

    @Inject
    AppRepository() {}

    @Override
    public List<Letra> getLetras() {
        ArrayList<Letra> array =  Lists.newArrayList(
                new Letra("1", "K", 0),
                new Letra("2", "I", 1),
                new Letra("3", "N", 2),
                new Letra("4", "K", 3),
                new Letra("5", "A", 4)
        );

        return array.subList(0, QTDE_LETRAS);
    }

    @Override
    public Flowable<List<Pedra>> getPedras() {
        List<Letra> letras = getLetras();
        List<Pedra> pedras = new ArrayList<>();

        for (int i = 1; i <= QTDE_PEDRAS_LETRA*letras.size(); i++) {
            String letra = letras.get((i-1)/ QTDE_PEDRAS_LETRA).getNome();

            pedras.add(new Pedra(
                    Integer.toString(i),
                    letra,
                    StringUtils.addZerosToNumberString(i)
            ));
        }

        return Flowable.just(pedras);
    }

    @Override
    public Single<List<Cartela>> getCartelas() {
        ArrayList<Cartela> cartelas = Lists.newArrayList(
                new Cartela("1")
        );
        return Single.just(cartelas);
    }

    @Override
    public Single<Cartela> getCartelaById(String id) {
        return Single.just(new Cartela("1"));
    }

    @Override
    public Single<List<CartelaPedra>> getPedrasByCartelaId(String id) {
        List<CartelaPedra> cartelaPedras = Lists.newArrayList(
                new CartelaPedra("1","1"),
                new CartelaPedra("1","2"),
                new CartelaPedra("1","3"),
                new CartelaPedra("1","4"),
                new CartelaPedra("1","5"),
                new CartelaPedra("1","16"),
                new CartelaPedra("1","17"),
                new CartelaPedra("1","18"),
                new CartelaPedra("1","19"),
                new CartelaPedra("1","20"),
                new CartelaPedra("1","31"),
                new CartelaPedra("1","32"),
                new CartelaPedra("1","33"),
                new CartelaPedra("1","34"),
                new CartelaPedra("1","46"),
                new CartelaPedra("1","47"),
                new CartelaPedra("1","48"),
                new CartelaPedra("1","49"),
                new CartelaPedra("1","50"),
                new CartelaPedra("1","61"),
                new CartelaPedra("1","62"),
                new CartelaPedra("1","63"),
                new CartelaPedra("1","64"),
                new CartelaPedra("1","65")
        );
        return Single.just(cartelaPedras);
    }
}
