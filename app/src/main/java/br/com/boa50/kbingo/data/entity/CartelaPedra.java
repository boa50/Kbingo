package br.com.boa50.kbingo.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import java.util.Locale;

import static br.com.boa50.kbingo.Constant.FORMAT_CARTELA;

@Entity(primaryKeys = {"cartela_id", "pedra_id"},
        foreignKeys = {
        @ForeignKey(entity = Pedra.class,
                parentColumns = "id",
                childColumns = "pedra_id")},
        indices = {@Index(name = "cartela_pedra", value = {"pedra_id", "cartela_id"})}
)
public class CartelaPedra {

    @ColumnInfo(name = "cartela_id")
    private int cartelaId;

    @ColumnInfo(name = "pedra_id")
    private int pedraId;

    private int linha;

    private int coluna;

    public CartelaPedra(int cartelaId, int pedraId, int linha, int coluna) {
        this.cartelaId = cartelaId;
        this.pedraId = pedraId;
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getCartelaId() {
        return cartelaId;
    }

    public void setCartelaId(int cartelaId) {
        this.cartelaId = cartelaId;
    }

    public int getPedraId() {
        return pedraId;
    }

    public void setPedraId(int pedraId) {
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
        return String.format(Locale.ENGLISH, FORMAT_CARTELA, cartelaId);
    }
}
