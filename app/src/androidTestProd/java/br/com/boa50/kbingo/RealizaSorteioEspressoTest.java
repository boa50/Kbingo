package br.com.boa50.kbingo;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomGets.getButtonText;

@RunWith(AndroidJUnit4.class)
public class RealizaSorteioEspressoTest {
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
    public void clickarDuasVezesRapidamente_sortearUmaPedra() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra));

        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        onView(withId(R.id.bt_sortear_pedra))
                .check(matches(withText(text)));
    }
}
