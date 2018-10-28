/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    SorteioCartelaModule.java is part of Kbingo

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
package br.com.boa50.kbingo.sorteiocartela;

import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SorteioCartelaModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SorteioCartelaFragment sorteioCartelaFragment();

    @ActivityScoped
    @Binds
    abstract SorteioCartelaContract.Presenter sorteioCartelaPresenter(SorteioCartelaPresenter presenter);
}
