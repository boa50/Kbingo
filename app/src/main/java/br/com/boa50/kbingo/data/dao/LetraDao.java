package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.Letra;
import io.reactivex.Single;

@Dao
public interface LetraDao {
    @Query("SELECT * FROM Letra")
    Single<List<Letra>> loadLetras();

    @Query("SELECT * FROM Letra WHERE id = :id")
    Single<Letra> loadLetra(String id);

    @Query("DELETE FROM Letra")
    void deleteAll();

    @Insert()
    void insertAll(Letra...letras);
}
