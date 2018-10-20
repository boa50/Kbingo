package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.CartelaPedra;
import io.reactivex.Single;

@Dao
public interface CartelaPedraDao {
    @Query("SELECT * FROM CartelaPedra WHERE cartela_id = :cartelaId")
    Single<List<CartelaPedra>> loadCartelaPedras(int cartelaId);

    @Query("SELECT * FROM CartelaPedra WHERE cartela_id IN(:cartelasIds)")
    Single<List<CartelaPedra>> loadCartelaPedras(List<Integer> cartelasIds);

    @Query("SELECT MAX(cartela_id) FROM CartelaPedra")
    Single<Integer> loadCartelaMaxId();

    @Query("DELETE FROM CartelaPedra")
    void deleteAll();

    @Insert()
    void insertAll(List<CartelaPedra> cartelaPedras);
}
