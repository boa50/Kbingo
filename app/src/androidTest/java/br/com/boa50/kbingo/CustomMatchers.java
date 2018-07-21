package br.com.boa50.kbingo;

import android.graphics.drawable.Drawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static br.com.boa50.kbingo.CustomGets.getBitmap;

final class CustomMatchers {

    static Matcher<View> withTextColor(int color) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: " + color);
            }

            @Override
            protected boolean matchesSafely(TextView item) {
                return ContextCompat.getColor(item.getContext(), color) == item.getCurrentTextColor();
            }
        };
    }

    static Matcher<View> withBackgroundDrawable(Drawable drawable) {
        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with background drawable: " + drawable.toString());
            }

            @Override
            protected boolean matchesSafely(TextView item) {
                return getBitmap(drawable)
                        .sameAs(getBitmap(item.getBackground()));
            }
        };
    }

    static Matcher<View> indexChildOf(Matcher<View> parentMatcher, int index) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + index + " child view: ");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(index).equals(view);
            }
        };
    }

    static Matcher<View> isFocused() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with focusable view: ");
            }

            @Override
            protected boolean matchesSafely(View view) {
                return view.isFocused();
            }
        };
    }
}
