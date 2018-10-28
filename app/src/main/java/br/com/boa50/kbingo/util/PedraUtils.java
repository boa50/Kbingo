/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    PedraUtils.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
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
            theme = new ContextThemeWrapper(context, R.style.PedraEnabledTheme).getTheme();
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
}
