package br.com.boa50.kbingo.data.dto;

public class CartelaFiltroDTO {
    private int cartelaId;
    private boolean ganhadora;
    private boolean selecionada;

    public CartelaFiltroDTO(int cartelaId, boolean ganhadora, boolean selecionada) {
        this.cartelaId = cartelaId;
        this.ganhadora = ganhadora;
        this.selecionada = selecionada;
    }

    public int getCartelaId() {
        return cartelaId;
    }

    public void setCartelaId(int cartelaId) {
        this.cartelaId = cartelaId;
    }

    public boolean isGanhadora() {
        return ganhadora;
    }

    public void setGanhadora(boolean ganhadora) {
        this.ganhadora = ganhadora;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }
}
