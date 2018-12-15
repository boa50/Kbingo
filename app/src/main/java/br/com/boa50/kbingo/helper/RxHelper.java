/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    EnviromentHelper.java is part of Kbingo

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
package br.com.boa50.kbingo.helper;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class RxHelper {
    private final BaseSchedulerProvider scheduleProvider;

    @Inject
    public RxHelper(@NonNull BaseSchedulerProvider scheduleProvider){
        this.scheduleProvider = scheduleProvider;
    }

    public <T> Single<T> getSubscribableSingle(Single<T> function) {
        return function.subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui());
    }

    public <T> Flowable<T> getSubscribableFlowable(Flowable<T> function) {
        return function.subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui());
    }

    public <T> Flowable<T> getSubscribableFlowableWithFilter(Flowable<T> function) {
        return function.subscribeOn(scheduleProvider.io())
                .observeOn(scheduleProvider.ui());
    }
}
