package br.com.boa50.kbingo;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomGets.getButtonText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SorteioCartelaEspressoTest {
    private static AppDatabase db;

    @Rule
    public ActivityTestRule<BaseActivity> mActivityRule =
            new ActivityTestRule<>(BaseActivity.class);

    @BeforeClass
    public static void setup() {
        db = CustomProcedures.initializeDatabase();
    }

    @Before
    public void setupTest() {
        CustomProcedures.changeNavigation(R.id.item_sorteio_cartela);
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @Test
    public void realizarSorteio_mostrarNumeroCartela() {
        onView(withId(R.id.bt_sorteio_cartela))
                .check(matches(withText(R.string.bt_sorteio_cartela)));

        onView(withId(R.id.bt_sorteio_cartela))
                .perform(click());

        onView(withId(R.id.bt_sorteio_cartela))
                .check(matches(not(withText(R.string.bt_sorteio_cartela))));

        assertThat(getButtonText(withId(R.id.bt_sorteio_cartela)).length(), equalTo(4));
    }

    @Test
    public void abrirFiltro_configuracoesIniciais() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.et_sorteio_cartela_numero))
                .check(matches(not(hasFocus())));

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .check(matches(isDisplayed()));
    }

    @Test
    public void filtraCartela_apareceCartelaSorteavel() {
        onView(withText(R.string.todas_cartelas_sorteaveis))
                .check(matches(isDisplayed()));

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.closeSoftKeyboard();
        pressBack();

        onView(withText("0001"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void removerFiltro_aparecerTodasCartelasSorteaveis() {
        onView(withText(R.string.todas_cartelas_sorteaveis))
                .check(matches(isDisplayed()));

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.closeSoftKeyboard();
        pressBack();

        onView(withText(R.string.todas_cartelas_sorteaveis))
                .check(matches(isDisplayed()));
    }

    @Test
    public void filtrarFiltro_aparecerOpcoesCorretas() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.et_sorteio_cartela_numero))
                .perform(replaceText("1"));

        onView(withText("0001"))
                .check(matches(isDisplayed()));
        onView(withText("0010"))
                .check(matches(isDisplayed()));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("0005"))
                .check(doesNotExist());
    }
}
