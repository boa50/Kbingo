package br.com.boa50.kbingo.util;

public final class StringUtils {
    public static String removeZeros(String s) {
        return s.replaceFirst("^0+(?!$)", "");
    }
}
