package br.com.boa50.kbingo.data.entity;

/**
 * Created by boa50 on 4/1/18.
 */

public class Letra {
    private String mId;

    private String mNome;

    private int mPosicao;

    public Letra(String mId, String mNome, int mPosicao) {
        this.mId = mId;
        this.mNome = mNome;
        this.mPosicao = mPosicao;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmNome() {
        return mNome;
    }

    public void setmNome(String mNome) {
        this.mNome = mNome;
    }

    public int getmPosicao() {
        return mPosicao;
    }

    public void setmPosicao(int mPosicao) {
        this.mPosicao = mPosicao;
    }
}
