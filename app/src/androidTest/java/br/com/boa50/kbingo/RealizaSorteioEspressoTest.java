package br.com.boa50.kbingo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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

        onView(withId(R.id.bt_novo_sorteio)).perform(click());

        onView(withText("01")).check(matches(isDisplayed()));
    }

    //TODO mudança na cor da pedra sorteada
    //TODO pausar e restartar a aplicação
    //TODO scroll do tab layout
    //TODO ver se a pedra continua sorteada quando mudar entre viewPagers distantes
}
