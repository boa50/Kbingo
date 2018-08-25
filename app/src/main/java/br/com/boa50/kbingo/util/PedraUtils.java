package br.com.boa50.kbingo.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.ContextThemeWrapper;

import java.util.List;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.Pedra;

public final class PedraUtils {
    public static Drawable getPedraDrawable(Context context, boolean enabled) {
        Resources.Theme theme;
        if (enabled)
            theme = new ContextThemeWrapper(context, R.style.PedraEnabled).getTheme();
        else
            theme = context.getTheme();

        return VectorDrawableCompat.create(
                context.getResources(),
                R.drawable.pedra,
                theme);
    }

    public static int getPedraPequenaLadoInPixels(int ladoLimitantePixels, int quantidadeLimitante, int marginInPixels) {
        return (ladoLimitantePixels - (quantidadeLimitante*2)*marginInPixels)/quantidadeLimitante;
    }

    public static boolean hasPedraSorteda(List<Pedra> pedras) {
        for (Pedra pedra : pedras) {
            if (pedra.isSorteada()) return true;
        }
        return false;
    }
}
