package br.com.boa50.kbingo.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

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

    public CartelaPedra(@NonNull String cartelaId, @NonNull String pedraId) {
        this.cartelaId = cartelaId;
        this.pedraId = pedraId;
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
}
