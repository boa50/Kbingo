package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.Cartela;
import io.reactivex.Single;

@Dao
public interface CartelaDao {
    @Query("SELECT * FROM Cartela")
    Single<List<Cartela>> loadCartelas();

    @Query("SELECT * FROM Cartela WHERE id = :id")
    Single<Cartela> loadCartela(String id);

    @Query("SELECT MAX(id) FROM Cartela")
    Single<String> loadCartelaMaxId();

    @Query("DELETE FROM Cartela")
    void deleteAll();

    @Insert()
    void insertAll(Cartela...cartelas);
}
