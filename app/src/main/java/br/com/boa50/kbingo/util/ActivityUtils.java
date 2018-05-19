package br.com.boa50.kbingo.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by boa50 on 25/03/18.
 */

public final class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int layoutId){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(layoutId, fragment);
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
}
