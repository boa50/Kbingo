package br.com.boa50.kbingo.util;

import java.util.ArrayList;

public class ArrayUtils {
    public static ArrayList<String> filtrar(ArrayList<String> arrayList, String texto) {
        ArrayList<String> retorno = new ArrayList<>();
        for (String str : arrayList) {
            if (str.contains(texto)) retorno.add(str);
        }
        return retorno;
    }
}
