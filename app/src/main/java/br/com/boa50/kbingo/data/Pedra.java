package br.com.boa50.kbingo.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boa50 on 3/11/18.
 */

public class Pedra implements Parcelable{

    private String mId;

    private String mLetra;

    private String mNumero;

    private boolean mHeader;

    private boolean mSorteada;

    public Pedra(String mId, String mLetra, String mNumero) {
        this.mId = mId;
        this.mLetra = mLetra;
        this.mNumero = mNumero;
        this.mSorteada = false;
    }

    public Pedra(String mId, String mLetra, String mNumero, boolean mHeader) {
        this.mId = mId;
        this.mLetra = mLetra;
        this.mNumero = mNumero;
        this.mHeader = mHeader;
        this.mSorteada = false;
    }

    protected Pedra(Parcel in) {
        mId = in.readString();
        mLetra = in.readString();
        mNumero = in.readString();
        mHeader = in.readByte() != 0;
        mSorteada = in.readByte() != 0;
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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmLetra() {
        return mLetra;
    }

    public void setmLetra(String mLetra) {
        this.mLetra = mLetra;
    }

    public String getmNumero() {
        return mNumero;
    }

    public void setmNumero(String mNumero) {
        this.mNumero = mNumero;
    }

    public boolean ismSorteada() {
        return mSorteada;
    }

    public void setmSorteada(boolean mSorteada) {
        this.mSorteada = mSorteada;
    }

    public boolean ismHeader() {
        return mHeader;
    }

    public void setmHeader(boolean mHeader) {
        this.mHeader = mHeader;
    }

    public String getValorPedra(){
        return mLetra + mNumero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mLetra);
        parcel.writeString(mNumero);
        parcel.writeByte((byte) (mHeader ? 1 : 0));
        parcel.writeByte((byte) (mSorteada ? 1 : 0));
    }
}
