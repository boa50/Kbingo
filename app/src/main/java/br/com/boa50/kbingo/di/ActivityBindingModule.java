/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    ActivityBindingModule.java is part of Kbingo

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

import br.com.boa50.kbingo.MainActivity;
import br.com.boa50.kbingo.SplashScreenActivity;
import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.conferecartelas.ConfereCartelasModule;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioModule;
import br.com.boa50.kbingo.sorteiocartela.SorteioCartelaModule;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashScreenActivity splashScreenActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RealizaSorteioModule.class, VisualizaCartelasModule.class,
            SorteioCartelaModule.class})
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {VisualizaCartelasModule.class, ConfereCartelasModule.class})
    abstract ConfereCartelasActivity conferirCartelasActivity();
}
