/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    ActivityUtils.java is part of Kbingo

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

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

import br.com.boa50.kbingo.R;

public final class ActivityUtils {
    public static void addFragmentToMainActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment){

        addFragmentToMainActivity(fragmentManager, fragment, false);
    }

    public static void addFragmentToMainActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment,
                                                 boolean backStack){

        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment);

        if (backStack) transaction.addToBackStack("");

        transaction.commit();
    }

    //TODO: trocar por um modelo usando WindowInsets
    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        hideSoftKeyboard(imm, view);
    }

    public static void hideSoftKeyboardFrom(Context context, View view) {
        hideSoftKeyboard((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE), view);
    }

    private static void hideSoftKeyboard(InputMethodManager imm, View view) {
        view.clearFocus();
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
