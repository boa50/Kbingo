package br.com.boa50.kbingo.bluetooth;

public class Maruagem {
    private int idCartelaSorteada;
    private static Maruagem instance = null;
    private Maruagem() {
        idCartelaSorteada = -1;
    }

    public static Maruagem getInstance() {
        if (instance == null) {
            instance = new Maruagem();
        }
        return instance;
    }

    public int getIdCartelaSorteada() {
        return idCartelaSorteada;
    }

    public void setIdCartelaSorteada(int idCartelaSorteada) {
        this.idCartelaSorteada = idCartelaSorteada;
    }
}
