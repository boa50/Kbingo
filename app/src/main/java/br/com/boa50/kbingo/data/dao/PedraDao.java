package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Flowable;

@Dao
public interface PedraDao {
    @Query("SELECT * FROM Pedra")
    Flowable<List<Pedra>> loadPedras();
}
