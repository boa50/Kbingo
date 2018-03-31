package br.com.boa50.kbingo.data.source;

import com.google.common.collect.Lists;

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

        return Flowable.just(Lists.newArrayList(
                new Pedra("1","K","01"),
                new Pedra("2","K","02"),
                new Pedra("3","K","03"),
                new Pedra("4","K","04"),
                new Pedra("5","K","05"),
                new Pedra("6","K","06"),
                new Pedra("7","K","07"),
                new Pedra("8","K","08"),
                new Pedra("9","K","09"),
                new Pedra("10","K","10")
                )
        );
    }
}
