package br.com.boa50.kbingo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.internal.util.Checks.checkNotNull;

public final class CustomMatchers {

    public static Matcher<View> withTextColor(int color) {
        checkNotNull(color);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: " + color);
            }

            @Override
            protected boolean matchesSafely(TextView item) {
                return item.getContext().getColor(color) == item.getCurrentTextColor();
            }
        };
    }

    public static Matcher<View> withPedraBackground(Drawable drawable) {
        checkNotNull(drawable);
        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with pedra background drawable: " + drawable.toString());
            }

            @Override
            protected boolean matchesSafely(TextView item) {
                return CustomMatchers.getBitmap(drawable)
                        .sameAs(CustomMatchers.getBitmap(item.getBackground()));
            }
        };
    }

    private static Bitmap getBitmap(Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
