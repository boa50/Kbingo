package br.com.boa50.kbingo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.data.FakeAppRepository;
import br.com.boa50.kbingo.util.StringUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomMatchers.isFocused;
import static br.com.boa50.kbingo.CustomMatchers.withBackgroundDrawable;

@RunWith(AndroidJUnit4.class)
public class ConfereCartelasEspressoTest {
    private static FakeAppRepository repository;
    private static ArrayList<String> cartelasGanhadorasMock;


    @Rule
    public ActivityTestRule<ConfereCartelasActivity> mActivityRule =
            new ActivityTestRule<>(ConfereCartelasActivity.class, false, false);

    @BeforeClass
    public static void setup() {
        repository = CustomProceduresMock.initializeFakeDatabase();

        cartelasGanhadorasMock = new ArrayList<>();
        for (int i : repository.getIdsCartelasGanhadoras()) {
            cartelasGanhadorasMock.add(StringUtils.formatarNumeroCartela(i));
        }
    }

    @Before
    public void setupTest() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_CARTELAS_GANHADORAS, true);
        mActivityRule.launchActivity(intent);
    }

    @AfterClass
    public static void tearDown() {
        repository.getDb().close();
    }

    @Test
    public void pedraSorteada_aparecerFundoVerde() {
        onView(withId(R.id.rv_cartelas_ganhadoras))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.et_numero_cartela))
                .perform(replaceText("0001"));
        onView(withId(R.id.et_numero_cartela))
                .perform(pressImeActionButton());

        try {
            Thread.sleep(50);
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
        onView(withText(R.string.list_item_confere_outra_cartela))
                .check(matches(isDisplayed()));

        for (int i = 0; i < cartelasGanhadorasMock.size(); i++) {
            onView(withText(cartelasGanhadorasMock.get(i)))
                    .check(matches(isDisplayed()));
        }

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(
                        mActivityRule.getActivity().getString(R.string.cartelas_ganhadoras_title) +
                                " - " + cartelasGanhadorasMock.size() + " Cartelas"
                )));
    }

    @Test
    public void apertarCartelaGanhadora_apresentarCartelaCorreta() {
        int i = 1;
        onView(withText(cartelasGanhadorasMock.get(i)))
                .perform(click());

        onView(withId(R.id.et_numero_cartela))
                .check(matches(withText(cartelasGanhadorasMock.get(i))));
    }

    @Test
    public void apertarCartelaGanhadora_voltarTelaCartelas() {
        onView(withText(cartelasGanhadorasMock.get(0)))
                .perform(click());

        pressBack();

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(
                        mActivityRule.getActivity().getString(R.string.cartelas_ganhadoras_title) +
                                " - " + cartelasGanhadorasMock.size() + " Cartelas"
                )));
        onView(withText(cartelasGanhadorasMock.get(cartelasGanhadorasMock.size() - 1)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void verificarMenuDisponivel() {
        onView(withId(R.id.item_busca))
                .check(matches(isDisplayed()));

        onView(withId(R.id.rv_cartelas_ganhadoras))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.item_busca))
                .check(doesNotExist());
    }

    @Test
    public void filtrarCartelas_selecionarCartela_conferirCartela() {
        String cartelaFiltrada = "0001";

        filtrarCartelas(cartelaFiltrada);

        onView(withId(R.id.rv_cartelas_ganhadoras))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.et_numero_cartela))
                .check(matches(isDisplayed()));
        onView(withText(cartelaFiltrada))
                .check(matches(isDisplayed()));
        onView(withText("14"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void filtrarCartelas_selecionarCartela_voltar_todasCartelasApresentadas() {
        filtrarCartelas("1");

        onView(withId(R.id.rv_cartelas_ganhadoras))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        pressBack();

        onView(withText(cartelasGanhadorasMock.get(cartelasGanhadorasMock.size()-1)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void filtrarCartelas_mudarOrientacao_manterInformacoes() {
        String textoFiltro = "0050";
        filtrarCartelas(textoFiltro);

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withText(textoFiltro))
                .check(matches(isDisplayed()));
        onView(withText(textoFiltro))
                .check(matches(isFocused()));
    }

    private void filtrarCartelas(String textoFiltro) {
        onView(withId(R.id.item_busca))
                .perform(click());
        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText(textoFiltro));
    }
}
