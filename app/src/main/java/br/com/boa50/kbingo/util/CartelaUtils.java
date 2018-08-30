package br.com.boa50.kbingo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.com.boa50.kbingo.Constant;
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

    public static ArrayList<String> getCartelasGanhadoras(List<CartelaDTO> cartelas) {
        ArrayList<String> cartelasGanhadoras = new ArrayList<>();
        for (CartelaDTO cartela : cartelas) {
            if (cartela.isGanhadora()) {
                cartelasGanhadoras.add(formatarNumeroCartela(cartela.getCartelaId()));
            }
        }

        Collections.sort(cartelasGanhadoras, (o1, o2) -> new BigDecimal(o1).compareTo(new BigDecimal(o2)));
        return cartelasGanhadoras;
    }

    public static String formatarNumeroCartela(int cartelaId) {
        return String.format(
                Locale.ENGLISH,
                Constant.FORMAT_CARTELA,
                cartelaId);
    }
}
