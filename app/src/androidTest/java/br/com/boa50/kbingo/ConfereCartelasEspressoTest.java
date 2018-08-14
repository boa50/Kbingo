package br.com.boa50.kbingo;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Locale;

import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.Constant.FORMAT_PEDRA;
import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;
import static br.com.boa50.kbingo.CustomMatchers.withBackgroundDrawable;

@RunWith(AndroidJUnit4.class)
public class ConfereCartelasEspressoTest {
    private static AppDatabase db;
    private static ArrayList<Pedra> pedrasMock;
    private static ArrayList<String> cartelasGanhadorasMock;


    @Rule
    public ActivityTestRule<ConfereCartelasActivity> mActivityRule =
            new ActivityTestRule<>(ConfereCartelasActivity.class, false, false);

    @BeforeClass
    public static void setup() {
        db = CustomProcedures.initializeDatabase();
        Letra[] letras = new Letra[] {
                new Letra(1, "K"),
                new Letra(2, "I"),
                new Letra(3, "N"),
                new Letra(4, "K"),
                new Letra(5, "A")
        };

        pedrasMock = new ArrayList<>();
        for (int i = 1; i <= QTDE_PEDRAS_LETRA*letras.length; i++) {
            int letraId = letras[(i-1)/ QTDE_PEDRAS_LETRA].getId();

            pedrasMock.add(new Pedra(
                    i,
                    letraId,
                    String.format(Locale.ENGLISH, FORMAT_PEDRA, i)
            ));
        }

        pedrasMock.get(14 - 1).setSorteada(true);

        cartelasGanhadorasMock = Lists.newArrayList(
                "0001",
                "0002",
                "0005"
        );
    }

    @Before
    public void setupTest() {
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_PEDRAS, pedrasMock);
        intent.putExtra(Constant.EXTRA_CARTELAS_GANHADORAS, cartelasGanhadorasMock);
        mActivityRule.launchActivity(intent);
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @Test
    public void pedraSorteada_aparecerFundoVerde() {
        onView(withId(R.id.et_numero_cartela)).perform(replaceText("0001"));
        onView(withId(R.id.et_numero_cartela)).perform(pressImeActionButton());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("14"))
                .check(matches(withBackgroundDrawable(
                        mActivityRule.getActivity().getResources()
                                .getDrawable(R.drawable.pedrasorteada_customborder))));

        onView(withText("28"))
                .check(matches(withBackgroundDrawable(
                        mActivityRule.getActivity().getResources()
                                .getDrawable(R.drawable.customborder))));
    }

    @Test
    public void receberCartelaGanhadora_apresentarTodasGanhadoras() {
        for (int i = 0; i < cartelasGanhadorasMock.size(); i++) {
            onView(withText(cartelasGanhadorasMock.get(i)))
                    .check(matches(isDisplayed()));
        }
    }
}
