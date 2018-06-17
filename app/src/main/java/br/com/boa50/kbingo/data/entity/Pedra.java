package br.com.boa50.kbingo.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Pedra.class,
                                    parentColumns = "id",
                                    childColumns = "letra_id"),
        indices = @Index("letra_id"))
public class Pedra implements Parcelable{

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "letra_id")
    private int letraId;

    @NonNull
    private String numero;

    private boolean sorteada;

    public Pedra(int id, int letraId, @NonNull String numero) {
        this.id = id;
        this.letraId = letraId;
        this.numero = numero;
        this.sorteada = false;
    }

    protected Pedra(Parcel in) {
        id = in.readInt();
        letraId = in.readInt();
        numero = in.readString();
        sorteada = in.readByte() != 0;
    }

    public static final Creator<Pedra> CREATOR = new Creator<Pedra>() {
        @Override
        public Pedra createFromParcel(Parcel in) {
            return new Pedra(in);
        }

        @Override
        public Pedra[] newArray(int size) {
            return new Pedra[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLetraId() {
        return letraId;
    }

    public void setLetraId(int letraId) {
        this.letraId = letraId;
    }

    @NonNull
    public String getNumero() {
        return numero;
    }

    public void setNumero(@NonNull String numero) {
        this.numero = numero;
    }

    public boolean isSorteada() {
        return sorteada;
    }

    public void setSorteada(boolean sorteada) {
        this.sorteada = sorteada;
    }

    public String getValorPedra(){
        return letraId + numero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(letraId);
        parcel.writeString(numero);
        parcel.writeByte((byte) (sorteada ? 1 : 0));
    }
}
