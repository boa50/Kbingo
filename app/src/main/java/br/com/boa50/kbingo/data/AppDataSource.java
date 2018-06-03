package br.com.boa50.kbingo.data;

import java.util.List;

import javax.inject.Singleton;

import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;

@Singleton
public interface AppDataSource {

    List<Letra> getLetras();

    Flowable<List<Pedra>> getPedras();
}
