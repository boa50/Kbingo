package br.com.boa50.kbingo.util;

/**
 * Created by boa50 on 4/8/18.
 */

public class StringUtils {
    public static String addZerosToNumberString(int numero) {
        return numero < 10 ? "0" + Integer.toString(numero) : Integer.toString(numero);
    }
}
