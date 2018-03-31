package br.com.boa50.kbingo.data.source;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.boa50.kbingo.data.Pedra;
import io.reactivex.Flowable;

/**
 * Created by boa50 on 3/24/18.
 */

@Singleton
public class PedrasRepository implements PedrasDataSource {

    @Inject
    PedrasRepository() {}

    @Override
    public Flowable<List<Pedra>> getPedras() {

        List<Pedra> pedras = new ArrayList<>();

        String letra = "K";
        for (int i = 1; i <= 75; i++) {
            if (i == 16)
                letra = "I";
            else if (i == 31)
                letra = "N";
            else if (i == 46)
                letra = "K";
            else if (i == 61)
                letra = "A";

            String valorNumero = i < 10 ? "0" + Integer.toString(i) : Integer.toString(i);

            pedras.add(new Pedra(Integer.toString(i),letra,valorNumero));
        }

        return Flowable.just(pedras);
    }
}
