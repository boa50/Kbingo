/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    TipoSorteioDTO.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package br.com.boa50.kbingo.data.dto;

import java.util.ArrayList;

public class TipoSorteioDTO {
    public static final int CINCO_PEDRAS = 0;
    public static final int DEZ_PEDRAS = 1;
    public static final int CARTELA_CHEIA = 2;

    private String nome;
    private int quantidadePedras;

    private TipoSorteioDTO(String nome, int quantidadePedras) {
        this.nome = nome;
        this.quantidadePedras = quantidadePedras;
    }

    public static TipoSorteioDTO getTipoSorteio(int tipoSorteio) {
        switch (tipoSorteio) {
            case CARTELA_CHEIA:
                return new TipoSorteioDTO("Cartela Cheia", 24);
            case CINCO_PEDRAS:
                return new TipoSorteioDTO("05 Pedras", 5);
            case DEZ_PEDRAS:
                return new TipoSorteioDTO("10 Pedras", 10);
            default:
                return new TipoSorteioDTO("Cartela Cheia", 24);
        }
    }

    public static String[] getTiposSorteioNome() {
        ArrayList<String> tiposSorteioNome = new ArrayList<>();
        tiposSorteioNome.add(CINCO_PEDRAS, getTipoSorteio(CINCO_PEDRAS).nome);
        tiposSorteioNome.add(DEZ_PEDRAS, getTipoSorteio(DEZ_PEDRAS).nome);
        tiposSorteioNome.add(CARTELA_CHEIA, getTipoSorteio(CARTELA_CHEIA).nome);

        return tiposSorteioNome.toArray(new String[tiposSorteioNome.size()]);
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadePedras() {
        return quantidadePedras;
    }
}
