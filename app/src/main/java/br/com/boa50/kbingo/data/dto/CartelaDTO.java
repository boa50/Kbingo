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
