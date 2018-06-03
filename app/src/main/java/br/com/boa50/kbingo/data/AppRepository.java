package br.com.boa50.kbingo.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.StringUtils;
import io.reactivex.Flowable;

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
}
