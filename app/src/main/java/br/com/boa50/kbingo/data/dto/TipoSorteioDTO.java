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
