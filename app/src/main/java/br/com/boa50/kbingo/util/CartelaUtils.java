package br.com.boa50.kbingo.util;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.data.dto.CartelaDTO;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Pedra;

public final class CartelaUtils {
    public static boolean hasCartelaPedra(List<CartelaPedra> cartelaPedras, Pedra pedra) {
        for (CartelaPedra cartelaPedra : cartelaPedras) {
            if (cartelaPedra.getPedraId() == pedra.getId()) return true;
        }
        return false;
    }

    public static ArrayList<Integer> getCartelasGanhadoras(List<CartelaDTO> cartelas) {
        ArrayList<Integer> cartelasGanhadoras = new ArrayList<>();
        for (CartelaDTO cartela : cartelas) {
            if (cartela.isGanhadora()) cartelasGanhadoras.add(cartela.getCartelaId());
        }
        return cartelasGanhadoras;
    }
}
