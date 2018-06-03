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

    @NonNull
    private String nome;

    private int posicao;

    public Letra(@NonNull String id, @NonNull String nome, int posicao) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
