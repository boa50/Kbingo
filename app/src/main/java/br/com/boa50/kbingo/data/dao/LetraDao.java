package br.com.boa50.kbingo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.boa50.kbingo.data.entity.Letra;
import io.reactivex.Flowable;

@Dao
public interface LetraDao {
    @Query("SELECT * FROM Letra")
    Flowable<List<Letra>> loadLetras();

    @Query("SELECT * FROM Letra WHERE id = :id")
    Flowable<Letra> loadLetra(String id);
}
