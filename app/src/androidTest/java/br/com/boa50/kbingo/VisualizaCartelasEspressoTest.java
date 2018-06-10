package br.com.boa50.kbingo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
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
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomMatchers.indexChildOf;
import static br.com.boa50.kbingo.CustomMatchers.isFocused;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class VisualizaCartelasEspressoTest {
    private AppDatabase db;

    @Rule
    public ActivityTestRule<VisualizaCartelasActivity> mActivityRule =
            new ActivityTestRule<>(VisualizaCartelasActivity.class);

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
    public void abrirTela_carregarCartelaInicial() {
        onView(withId(R.id.et_numero_cartela)).check(matches(withText("0001")));
        onView(withText("A")).check(matches(isDisplayed()));
        onView(indexChildOf(withId(R.id.gl_cartela),29)).check(matches(isDisplayed()));
    }

    @Test
    public void mudarCartela_mudarPedras() {
        onView(withId(R.id.et_numero_cartela)).perform(click());
        onView(withId(R.id.et_numero_cartela)).perform(replaceText("0002"));
        onView(withId(R.id.et_numero_cartela)).check(matches(isFocused()));
        String text = getTextViewText(indexChildOf(withId(R.id.gl_cartela),5));
        onView(withId(R.id.et_numero_cartela)).perform(pressImeActionButton());
        onView(withId(R.id.et_numero_cartela)).check(matches(not(isFocused())));
        onView(indexChildOf(withId(R.id.gl_cartela),5)).check(matches(not(withText(text))));
    }

    private String getTextViewText(final Matcher<View> matcher) {
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
}
