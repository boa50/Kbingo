package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.Pedra;
import io.reactivex.Single;

@Dao
public interface PedraDao {
    @Query("SELECT * FROM Pedra")
    Single<List<Pedra>> loadPedras();

    @Query("SELECT COUNT(*) FROM Pedra")
    Single<Integer> countPedras();

    @Query("DELETE FROM Pedra")
    void deleteAll();

    @Insert()
    void insertAll(List<Pedra> pedras);
}
