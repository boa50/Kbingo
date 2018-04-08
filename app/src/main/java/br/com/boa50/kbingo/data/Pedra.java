package br.com.boa50.kbingo.data;

/**
 * Created by boa50 on 3/11/18.
 */

public class Pedra {

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
}
