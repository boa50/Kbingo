/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    CartelaFiltroDTO.java is part of Kbingo

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
package br.com.boa50.kbingo.data.dto;

public class CartelaFiltroDTO {
    private int cartelaId;
    private boolean ganhadora;
    private boolean selecionada;

    public CartelaFiltroDTO(int cartelaId, boolean ganhadora, boolean selecionada) {
        this.cartelaId = cartelaId;
        this.ganhadora = ganhadora;
        this.selecionada = selecionada;
    }

    public int getCartelaId() {
        return cartelaId;
    }

    public void setCartelaId(int cartelaId) {
        this.cartelaId = cartelaId;
    }

    public boolean isGanhadora() {
        return ganhadora;
    }

    public void setGanhadora(boolean ganhadora) {
        this.ganhadora = ganhadora;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }
}
