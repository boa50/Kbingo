/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    Constants.java is part of Kbingo

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
package br.com.boa50.kbingo;

public interface Constants {
    int QTDE_PEDRAS_LINHA_PORTRAIT = 5;
    int QTDE_PEDRAS_LINHA_LANDSCAPE = 5;
    int QTDE_PEDRAS_LETRA = 15;

    String FORMAT_CARTELA = "%04d";
    String FORMAT_PEDRA = "%02d";

    int MESSAGE_READ = 0;
    int MESSAGE_WRITE = 1;
    int MESSAGE_TOAST = 2;

    int STATE_NONE = 0;
    int STATE_LISTEN = 1;
    int STATE_CONNECTED = 2;

    String EXTRA_ULTIMA_CARTELA = "extraUltimaCartela";
    String EXTRA_CARTELAS_GANHADORAS = "extraCartelasGanhadoras";
    String EXTRA_CONFERE_CARTELA = "extraConfereCartela";
    String EXTRA_TOAST = "extraToast";
}
