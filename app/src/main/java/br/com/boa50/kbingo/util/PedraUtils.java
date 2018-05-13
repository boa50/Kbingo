package br.com.boa50.kbingo.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.ContextThemeWrapper;

import br.com.boa50.kbingo.R;

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
}
