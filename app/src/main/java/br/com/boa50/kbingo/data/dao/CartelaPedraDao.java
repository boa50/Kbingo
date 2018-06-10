package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.CartelaPedra;
import io.reactivex.Single;

@Dao
public interface CartelaPedraDao {
    @Query("SELECT * FROM CartelaPedra where cartela_id = :cartelaId")
    Single<List<CartelaPedra>> loadCartelaPedras(String cartelaId);

    @Query("DELETE FROM CartelaPedra")
    void deleteAll();

    @Insert()
    void insertAll(CartelaPedra...cartelaPedras);
}
