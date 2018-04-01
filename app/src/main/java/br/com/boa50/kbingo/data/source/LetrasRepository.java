package br.com.boa50.kbingo.data.source;

import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.boa50.kbingo.data.Letra;

/**
 * Created by boa50 on 4/1/18.
 */

@Singleton
public class LetrasRepository implements LetrasDataSource {

    @Inject
    LetrasRepository() {}

    @Override
    public List<Letra> getLetras() {
        return Lists.newArrayList(
                new Letra("1", "K", 0),
                new Letra("2", "I", 1),
                new Letra("3", "N", 2),
                new Letra("4", "K", 3),
                new Letra("5", "A", 4)
        );


    }
}
