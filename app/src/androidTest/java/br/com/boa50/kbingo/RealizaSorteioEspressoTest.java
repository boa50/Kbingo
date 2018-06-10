package br.com.boa50.kbingo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.AppRepository;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.Constant.QTDE_LETRAS;
import static br.com.boa50.kbingo.CustomMatchers.indexChildOf;
import static br.com.boa50.kbingo.CustomMatchers.withPedraBackground;
import static br.com.boa50.kbingo.CustomMatchers.withTextColor;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RealizaSorteioEspressoTest {
    private AppDatabase db;

    @Rule
    public ActivityTestRule<RealizaSorteioActivity> mActivityRule =
            new ActivityTestRule<>(RealizaSorteioActivity.class);

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
        AppDataSource appDataSource = new AppRepository(db);
        appDataSource.initializeDatabase();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void realizarNovoSorteio_aparecerPedraInicial() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra)).substring(1);

        Drawable drawable = VectorDrawableCompat.create(
                mActivityRule.getActivity().getResources(),
                R.drawable.pedra,
                new ContextThemeWrapper(mActivityRule.getActivity(), R.style.PedraEnabled).getTheme());

        onView(withId(R.id.bt_novo_sorteio))
                .perform(click());

        onView(withText("01"))
                .check(matches(isDisplayed()));
        onView(withText(text))
                .check(matches(withTextColor(R.color.textDisabled)));
        onView(withText(text))
                .check(matches(not(withPedraBackground(drawable))));
    }

    @Test
    public void sortearPedra_scrollDistante_mantemCorMudada() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra)).substring(1);

        Drawable drawable = VectorDrawableCompat.create(
                mActivityRule.getActivity().getResources(),
                R.drawable.pedra,
                new ContextThemeWrapper(mActivityRule.getActivity(), R.style.PedraEnabled).getTheme());

        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .perform(click());
        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), 0))
                .perform(click());

        onView(withText(text))
                .check(matches(withTextColor(R.color.pedraTextoEnabled)));
        onView(withText(text))
                .check(matches(withPedraBackground(drawable)));

    }

    @Test
    public void sortearPedra_mudarOrientacao_materInformacoes() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra));

        Drawable drawable = VectorDrawableCompat.create(
                mActivityRule.getActivity().getResources(),
                R.drawable.pedra,
                new ContextThemeWrapper(mActivityRule.getActivity(), R.style.PedraEnabled).getTheme());

        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .perform(click());

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.bt_sortear_pedra))
                .check(matches(withText(text)));
        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .check(matches(isSelected()));
        onView(withText(text.substring(1)))
                .check(matches(withTextColor(R.color.pedraTextoEnabled)));
        onView(withText(text.substring(1)))
                .check(matches(withPedraBackground(drawable)));
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

    //TODO mudança de orientação no fim do sorteio
    //TODO pausar e restartar a aplicação
}
