package br.com.boa50.kbingo.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.boa50.kbingo.data.dao.LetraDao;
import br.com.boa50.kbingo.data.dao.PedraDao;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

@Database(entities = {Pedra.class, Letra.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PedraDao pedraDao();
    public abstract LetraDao letraDao();
}
