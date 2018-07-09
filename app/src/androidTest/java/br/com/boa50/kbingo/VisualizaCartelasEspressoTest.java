package br.com.boa50.kbingo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.AppRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomGets.getTextViewText;
import static br.com.boa50.kbingo.CustomMatchers.indexChildOf;
import static br.com.boa50.kbingo.CustomMatchers.isFocused;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class VisualizaCartelasEspressoTest {
    private AppDatabase db;

    @Rule
    public ActivityTestRule<BaseActivity> mActivityRule =
            new ActivityTestRule<>(BaseActivity.class);

    @Before
    //TODO fazer alterações para o @BeforeClass para agilizar os testes
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
        AppDataSource appDataSource = new AppRepository(db);
        appDataSource.initializeDatabase();

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.navigation_view))
                .perform(navigateTo(R.id.item_visualizar_cartelas));
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());
    }

    @After
    public void tearDown() {
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
        onView(indexChildOf(withId(R.id.gl_cartela),5)).check(matches(withText(text)));
    }
}
