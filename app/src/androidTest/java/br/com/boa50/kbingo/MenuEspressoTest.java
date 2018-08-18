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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MenuEspressoTest {
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
        try {
            onView(withText(R.string.dialog_negative))
                    .perform(click());
        } catch (Exception ignored){}
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @Test
    public void trocarInterface_inicializarCorretamente() {
        onView(withText("02"))
                .check(matches(isDisplayed()));

        CustomProcedures.changeNavigation(R.id.item_visualizar_cartelas);

        onView(withId(R.id.et_numero_cartela))
                .check(matches(isDisplayed()));

        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);

        onView(withId(R.id.vp_pedras_sorteadas))
                .check(matches(isDisplayed()));
        onView(withText("02"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void mudarFragment_rotacionarTela_manterFragment() {
        CustomProcedures.changeNavigation(R.id.item_visualizar_cartelas);

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.et_numero_cartela))
                .check(matches(isDisplayed()));
    }
}
