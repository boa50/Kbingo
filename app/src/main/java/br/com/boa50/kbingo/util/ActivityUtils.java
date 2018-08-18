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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        hideSoftKeyboard(imm, view);

//        view.clearFocus();
//        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboardFrom(Context context, View view) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        view.clearFocus();
//        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        hideSoftKeyboard((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE), view);
    }

    private static void hideSoftKeyboard(InputMethodManager imm, View v) {
        v.clearFocus();
        Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
