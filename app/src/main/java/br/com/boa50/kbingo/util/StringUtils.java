package br.com.boa50.kbingo.util;

import java.util.Locale;

import br.com.boa50.kbingo.Constant;

public class StringUtils {
    public static String formatarNumeroPedra(int pedraNumero) {
        return String.format(
                Locale.ENGLISH,
                Constant.FORMAT_PEDRA,
                pedraNumero);
    }
}
