package br.com.boa50.kbingo;

import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RealizaSorteioEspressoTest {

    @Rule
    public ActivityTestRule<RealizaSorteioActivity> mActivityRule =
            new ActivityTestRule<>(RealizaSorteioActivity.class);

    @Test
    public void realizarNovoSorteio_aparecerPedraInicial() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        onView(withId(R.id.bt_novo_sorteio))
                .perform(click());

        onView(withText("01"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void sortearPedra_mudarCorPedra() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra)).substring(1);

        Drawable drawable = VectorDrawableCompat.create(
                mActivityRule.getActivity().getResources(),
                R.drawable.pedra,
                new ContextThemeWrapper(mActivityRule.getActivity(), R.style.PedraEnabled).getTheme());

        onView(withText(text))
                .check(matches(CustomMatchers.withTextColor(R.color.pedraTextoEnabled)));

        onView(withText(text))
                .check(matches(CustomMatchers.withPedraBackground(drawable)));
    }

    private String getButtonText(final Matcher<View> matcher) {
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
                Button bt = (Button)view;
                stringHolder[0] = bt.getText().toString();
            }
        });
        return stringHolder[0];
    }


    //TODO pausar e restartar a aplicação
    //TODO scroll do tab layout
    //TODO ver se a pedra continua sorteada quando mudar entre viewPagers distantes
    //TODO mudança da orientação e manutenção das pedras sorteadas
    //TODO mudança da orientação e mudança no layout?
}
