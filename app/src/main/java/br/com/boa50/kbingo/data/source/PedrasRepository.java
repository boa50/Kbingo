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
                new Pedra("10","K","10"),
                new Pedra("11","K","11"),
                new Pedra("12","K","12"),
                new Pedra("13","K","13"),
                new Pedra("14","K","14"),
                new Pedra("15","K","15"),
                new Pedra("1","I","01"),
                new Pedra("2","I","02"),
                new Pedra("3","I","03"),
                new Pedra("4","I","04"),
                new Pedra("5","I","05"),
                new Pedra("6","I","06"),
                new Pedra("7","I","07"),
                new Pedra("8","I","08"),
                new Pedra("9","I","09"),
                new Pedra("10","I","10"),
                new Pedra("11","I","11"),
                new Pedra("12","I","12"),
                new Pedra("13","I","13"),
                new Pedra("14","I","14"),
                new Pedra("15","I","15")
                )
        );
    }
}
