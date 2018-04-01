package br.com.boa50.kbingo.data.source;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.boa50.kbingo.data.Letra;
import br.com.boa50.kbingo.data.Pedra;
import io.reactivex.Flowable;

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

        for (int i = 1; i <= 75; i++) {
            pedras.add(new Pedra(
                            Integer.toString(i),
                            letras.get((i-1)/15).getmNome(),
                            i < 10 ? "0" + Integer.toString(i) : Integer.toString(i)
                    ));
        }

        return Flowable.just(pedras);
    }
}
