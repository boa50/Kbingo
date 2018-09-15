package br.com.boa50.kbingo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

final class CustomGets {
    static Bitmap getBitmap(Drawable drawable){
        Bitmap bitmap;

        if (drawable.getIntrinsicWidth() > 0) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    static String getButtonText(final Matcher<View> matcher) {
        final String[] stringHolder = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a Button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                Button bt = (Button) view;
                stringHolder[0] = bt.getText().toString();
            }
        });
        return stringHolder[0];
    }

    static String getTextViewText(final Matcher<View> matcher) {
        final String[] stringHolder = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    static int getRecyclerViewSize(final Matcher<View> matcher) {
        final int[] intHolder = {0};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "getting size from a RecyclerView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView rv = (RecyclerView) view;
                intHolder[0] = rv.getChildCount();
            }
        });
        return intHolder[0];
    }

    static View getRecyclerViewChild(final Matcher<View> matcher, int position) {
        final View[] viewHolder = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "getting size from a RecyclerView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView rv = (RecyclerView) view;
                viewHolder[0] = rv.getChildAt(position);
            }
        });
        return viewHolder[0];
    }
}
