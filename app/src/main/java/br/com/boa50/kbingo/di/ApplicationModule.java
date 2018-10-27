/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    ApplicationModule.java is part of Kbingo

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
package br.com.boa50.kbingo.di;

import android.content.Context;

import br.com.boa50.kbingo.KbingoApplication;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import br.com.boa50.kbingo.util.schedulers.ScheduleProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(KbingoApplication application);

    @Binds
    abstract BaseSchedulerProvider bindBaseSchedulerProvider(ScheduleProvider scheduleProvider);
}
