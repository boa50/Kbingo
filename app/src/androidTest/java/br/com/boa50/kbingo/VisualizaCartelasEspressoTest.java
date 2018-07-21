package br.com.boa50.kbingo;

import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.data.AppDatabase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomGets.getTextViewText;
import static br.com.boa50.kbingo.CustomMatchers.indexChildOf;
import static br.com.boa50.kbingo.CustomMatchers.isFocused;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class VisualizaCartelasEspressoTest {
    private static AppDatabase db;

    @Rule
    public ActivityTestRule<BaseActivity> mActivityRule =
            new ActivityTestRule<>(BaseActivity.class);

    @BeforeClass
    public static void setup() {
        db = CustomProcedures.initializeDatabase(db);
    }

    @Before
    public void setupTest() {
        CustomProcedures.changeNavigation(R.id.item_visualizar_cartelas);
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @Test
    public void abrirTela_carregarCartelaInicial() {
        onView(withId(R.id.et_numero_cartela)).check(matches(withText("0001")));
        onView(withText("A")).check(matches(isDisplayed()));
        onView(indexChildOf(withId(R.id.gl_cartela),29)).check(matches(isDisplayed()));
    }

    @Test
    public void mudarCartela_mudarPedras() {
        onView(withId(R.id.et_numero_cartela)).perform(click());
        onView(withId(R.id.et_numero_cartela)).perform(replaceText("0003"));
        onView(withId(R.id.et_numero_cartela)).check(matches(isFocused()));
        String text = getTextViewText(indexChildOf(withId(R.id.gl_cartela),5));
        onView(withId(R.id.et_numero_cartela)).perform(pressImeActionButton());
        onView(withId(R.id.et_numero_cartela)).check(matches(not(isFocused())));
        onView(indexChildOf(withId(R.id.gl_cartela),5)).check(matches(not(withText(text))));
    }

    @Test
    public void mudarCartela_mudarOrientacao_manterInformacoes() {
        onView(withId(R.id.et_numero_cartela)).perform(replaceText("0002"));
        onView(withId(R.id.et_numero_cartela)).perform(pressImeActionButton());
        String text = getTextViewText(indexChildOf(withId(R.id.gl_cartela),5));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.et_numero_cartela)).check(matches(withText("0002")));
        onView(indexChildOf(withId(R.id.gl_cartela),5)).check(matches(withText(text)));
    }

    @Test
    public void trocarFragments_resetarInformacoes() {
        String text = getTextViewText(indexChildOf(withId(R.id.gl_cartela),5));
        onView(withId(R.id.et_numero_cartela)).perform(replaceText("0002"));
        onView(withId(R.id.et_numero_cartela)).perform(pressImeActionButton());

        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);
        CustomProcedures.changeNavigation(R.id.item_visualizar_cartelas);

        onView(withId(R.id.et_numero_cartela)).check(matches(withText("0001")));
        onView(indexChildOf(withId(R.id.gl_cartela),5)).check(matches(withText(text)));
    }
}
