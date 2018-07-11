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
    private int id;

    @NonNull
    private String nome;

    public Letra(int id, @NonNull String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }
}
