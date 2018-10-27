/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    AppDatabase.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package br.com.boa50.kbingo.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.boa50.kbingo.data.dao.CartelaPedraDao;
import br.com.boa50.kbingo.data.dao.LetraDao;
import br.com.boa50.kbingo.data.dao.PedraDao;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

@Database(entities = {
        Pedra.class,
        Letra.class,
        CartelaPedra.class
    }, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PedraDao pedraDao();
    public abstract LetraDao letraDao();
    public abstract CartelaPedraDao cartelaPedraDao();
}
