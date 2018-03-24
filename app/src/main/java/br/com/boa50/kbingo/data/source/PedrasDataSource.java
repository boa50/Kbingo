package br.com.boa50.kbingo.data.source;

import java.util.List;

import br.com.boa50.kbingo.data.Pedra;
import io.reactivex.Flowable;

/**
 * Created by boa50 on 3/24/18.
 */

public interface PedrasDataSource {

    Flowable<List<Pedra>> getPedras();
}
