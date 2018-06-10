package br.com.boa50.kbingo.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Locale;

import static br.com.boa50.kbingo.Constant.FORMAT_CARTELA;

@Entity(primaryKeys = {"cartela_id", "pedra_id"},
        foreignKeys = {
        @ForeignKey(entity = Cartela.class,
                parentColumns = "id",
                childColumns = "cartela_id"),
        @ForeignKey(entity = Pedra.class,
                parentColumns = "id",
                childColumns = "pedra_id")
})
public class CartelaPedra {

    @NonNull
    @ColumnInfo(name = "cartela_id")
    private String cartelaId;

    @NonNull
    @ColumnInfo(name = "pedra_id")
    private String pedraId;

    private int linha;

    private int coluna;

    public CartelaPedra(@NonNull String cartelaId, @NonNull String pedraId, int linha, int coluna) {
        this.cartelaId = cartelaId;
        this.pedraId = pedraId;
        this.linha = linha;
        this.coluna = coluna;
    }

    @NonNull
    public String getCartelaId() {
        return cartelaId;
    }

    public void setCartelaId(@NonNull String cartelaId) {
        this.cartelaId = cartelaId;
    }

    @NonNull
    public String getPedraId() {
        return pedraId;
    }

    public void setPedraId(@NonNull String pedraId) {
        this.pedraId = pedraId;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public String getCartelaIdFormatado() {
        return String.format(Locale.ENGLISH, FORMAT_CARTELA, Integer.parseInt(cartelaId));
    }
}
