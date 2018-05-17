package br.com.boa50.kbingo.data.source;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.boa50.kbingo.data.Letra;
import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.util.StringUtils;
import io.reactivex.Flowable;

import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;

/**
 * Created by boa50 on 3/24/18.
 */

@Singleton
public class PedrasRepository implements PedrasDataSource {

    @Inject
    LetrasRepository letrasRepository;

    @Inject
    PedrasRepository() {}

    @Override
    public Flowable<List<Pedra>> getPedras() {

        List<Letra> letras = letrasRepository.getLetras();
        List<Pedra> pedras = new ArrayList<>();

        for (int i = 1; i <= QTDE_PEDRAS_LETRA*letras.size(); i++) {
            String letra = letras.get((i-1)/ QTDE_PEDRAS_LETRA).getmNome();

            pedras.add(new Pedra(
                    Integer.toString(i),
                    letra,
                    StringUtils.addZerosToNumberString(i)
            ));
        }

        return Flowable.just(pedras);
    }
}
