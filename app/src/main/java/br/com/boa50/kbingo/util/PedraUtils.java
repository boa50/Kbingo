package br.com.boa50.kbingo.util;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.data.Pedra;

public class PedraUtils {
    public static List<Integer> getHeadersPositions(List<Pedra> pedras) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < pedras.size(); i++) {
            if (pedras.get(i).ismHeader())
                positions.add(i);
        }
        return positions;
    }
}
