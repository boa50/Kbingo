package br.com.boa50.kbingo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.util.CartelaUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomGets.getTextViewText;
import static br.com.boa50.kbingo.CustomMatchers.indexChildOf;
import static br.com.boa50.kbingo.CustomMatchers.isFocused;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class VisualizaCartelasEspressoTest {
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

        CustomProcedures.mudarOrientacaoTela(mActivityRule.getActivity());
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

    @Test
    public void abrirDialogExportarCartelas_carregarInformacoesIniciais() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_exportar_cartelas))
                .perform(click());

        onView(withText(R.string.dialog_exportar_cartelas_content_text)).check(matches(isDisplayed()));
        onView(withText(CartelaUtils.formatarNumeroCartela(1))).check(matches(isDisplayed()));
        onView(withText(CartelaUtils.formatarNumeroCartela(200))).check(matches(isDisplayed()));
    }

    @Test
    public void abrirDialogExportarCartelas_mudarOrientacao_manterInformacoes() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_exportar_cartelas))
                .perform(click());

        String idInicial = CartelaUtils.formatarNumeroCartela(7);
        String idFinal = CartelaUtils.formatarNumeroCartela(50);
        onView(withId(R.id.et_dialog_exportar_cartelas_inicial))
                .perform(replaceText(idInicial));
        onView(withId(R.id.et_dialog_exportar_cartelas_final))
                .perform(replaceText(idFinal));

        CustomProcedures.mudarOrientacaoTela(mActivityRule.getActivity());
        onView(withText(R.string.dialog_exportar_cartelas_content_text)).check(matches(isDisplayed()));
        onView(withText(idInicial)).check(matches(isDisplayed()));
        onView(withText(idFinal)).check(matches(isDisplayed()));
    }

    @Test
    public void abrirDialogEsportarCartelas_colocarCartelasInvalidas_validarCorretamente() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_exportar_cartelas))
                .perform(click());

        Context context = InstrumentationRegistry.getTargetContext();
        String erroCartelaValidacao = context.getResources().getString(R.string.validar_cartelas_erro);
        String idInicial = CartelaUtils.formatarNumeroCartela(7);
        String idFinal = CartelaUtils.formatarNumeroCartela(50);
        ViewInteraction viCartelaInicial = onView(withId(R.id.et_dialog_exportar_cartelas_inicial));
        ViewInteraction viCartelaFinal = onView(withId(R.id.et_dialog_exportar_cartelas_final));
        ViewInteraction viBotaoExportar =
                onView(allOf(withText(R.string.dialog_exportar_cartelas_positive),
                        instanceOf(Button.class)));


        viCartelaInicial.perform(replaceText(idFinal));
        viCartelaFinal.perform(replaceText(idInicial));

        viCartelaInicial.check(matches(hasErrorText(erroCartelaValidacao)));
        viCartelaFinal.check(matches(hasErrorText(erroCartelaValidacao)));
        viBotaoExportar.check(matches(not(isEnabled())));


        viCartelaInicial.perform(replaceText(idInicial));
        viCartelaFinal.perform(replaceText(idFinal));

        viCartelaInicial.check(matches(not(hasErrorText(erroCartelaValidacao))));
        viCartelaFinal.check(matches(not(hasErrorText(erroCartelaValidacao))));
        viBotaoExportar.check(matches(isEnabled()));
    }
}
