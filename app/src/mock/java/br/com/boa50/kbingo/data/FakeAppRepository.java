package br.com.boa50.kbingo.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;

public class FakeAppRepository extends AppRepository {

    private ArrayList<Integer> mIdsCartelasGanhadoras;
    private AppDatabase db;

    @Inject
    public FakeAppRepository(AppDatabase db) {
        super(db);
        this.db = db;
        mIdsCartelasGanhadoras = Lists.newArrayList(1, 2, 5);
    }

    @Override
    public Flowable<List<Pedra>> getPedras() {
        return super.getPedras()
                .flatMap(pedras -> Flowable.fromIterable(pedras)
                .map(pedra -> {
                    if (pedra.getNumero().equalsIgnoreCase("14")) {
                        pedra.setSorteada(true);
                    }
                    return pedra;
                })
                .toList()
                .toFlowable());
    }

    @Override
    public Flowable<List<CartelaDTO>> getCartelas() {
        return super.getCartelas()
                .flatMap(cartelas -> Flowable.fromIterable(cartelas)
                .map(cartela -> {
                    if (mIdsCartelasGanhadoras.contains(cartela.getCartelaId())) {
                        cartela.setGanhadora(true);
                    }
                    return cartela;
                }))
                .toList()
                .toFlowable();
    }

    public ArrayList<Integer> getIdsCartelasGanhadoras() {
        return mIdsCartelasGanhadoras;
    }

    public AppDatabase getDb() {
        return db;
    }
}
