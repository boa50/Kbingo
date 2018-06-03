package br.com.boa50.kbingo.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by boa50 on 4/1/18.
 */

@Entity
public class Letra {

    @PrimaryKey
    @NonNull
    private String id;

    private String nome;

    private int posicao;

    public Letra(String id, String nome, int posicao) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
