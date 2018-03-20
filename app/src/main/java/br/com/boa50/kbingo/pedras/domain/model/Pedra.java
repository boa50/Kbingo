package br.com.boa50.kbingo.pedras.domain.model;

/**
 * Created by boa50 on 3/11/18.
 */

public class Pedra {

    private String mId;

    private String mLetra;

    private int mNumero;

    public Pedra(String mId, String mLetra, int mNumero) {
        this.mId = mId;
        this.mLetra = mLetra;
        this.mNumero = mNumero;
    }

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

    public int getmNumero() {
        return mNumero;
    }

    public void setmNumero(int mNumero) {
        this.mNumero = mNumero;
    }
}
