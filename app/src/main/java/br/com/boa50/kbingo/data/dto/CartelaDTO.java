/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    CartelaDTO.java is part of Kbingo

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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import br.com.boa50.kbingo.data.entity.CartelaPedra;

public class CartelaDTO implements Parcelable{
    private int cartelaId;
    private List<CartelaPedra> cartelaPedras;
    private int qtdPedrasSorteadas;
    private boolean ganhadora;

    public CartelaDTO(int cartelaId, List<CartelaPedra> cartelaPedras) {
        this.cartelaId = cartelaId;
        this.cartelaPedras = cartelaPedras;
        this.qtdPedrasSorteadas = 0;
        this.ganhadora = false;
    }

    protected CartelaDTO(Parcel in) {
        cartelaId = in.readInt();
        qtdPedrasSorteadas = in.readInt();
        ganhadora = in.readByte() != 0;
    }

    public static final Creator<CartelaDTO> CREATOR = new Creator<CartelaDTO>() {
        @Override
        public CartelaDTO createFromParcel(Parcel in) {
            return new CartelaDTO(in);
        }

        @Override
        public CartelaDTO[] newArray(int size) {
            return new CartelaDTO[size];
        }
    };

    public int getCartelaId() {
        return cartelaId;
    }

    public void setCartelaId(int cartelaId) {
        this.cartelaId = cartelaId;
    }

    public List<CartelaPedra> getCartelaPedras() {
        return cartelaPedras;
    }

    public void setCartelaPedras(List<CartelaPedra> cartelaPedras) {
        this.cartelaPedras = cartelaPedras;
    }

    public int getQtdPedrasSorteadas() {
        return qtdPedrasSorteadas;
    }

    public void setQtdPedrasSorteadas(int qtdPedrasSorteadas) {
        this.qtdPedrasSorteadas = qtdPedrasSorteadas;
    }

    public boolean isGanhadora() {
        return ganhadora;
    }

    public void setGanhadora(boolean ganhadora) {
        this.ganhadora = ganhadora;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cartelaId);
        parcel.writeInt(qtdPedrasSorteadas);
        parcel.writeByte((byte) (ganhadora ? 1 : 0));
    }
}
