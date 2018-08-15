package br.com.boa50.kbingo.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public final class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int layoutId){

        addFragmentToActivity(fragmentManager, fragment, layoutId, false);
    }

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,
                                             int layoutId,
                                             boolean backStack){

        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(layoutId, fragment);

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
}
