package br.com.boa50.kbingo.data;

import java.util.List;

import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;

public interface AppDataSource {

    List<Letra> getLetras();

    Flowable<List<Pedra>> getPedras();
}
