package br.com.boa50.kbingo;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.Constant.QTDE_LETRAS;
import static br.com.boa50.kbingo.CustomGets.getButtonText;
import static br.com.boa50.kbingo.CustomMatchers.indexChildOf;
import static br.com.boa50.kbingo.CustomMatchers.withBackgroundDrawable;
import static br.com.boa50.kbingo.CustomMatchers.withTextColor;
import static org.hamcrest.Matchers.not;

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
        } catch (Exception e){}
    }

    @AfterClass
    public static void tearDown() {
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

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_novo_sorteio))
                .perform(click());

        onView(withText(R.string.dialog_novo_sorteio_positive))
                .perform(click());

        onView(withText("01"))
                .check(matches(isDisplayed()));
        onView(withText(text))
                .check(matches(withTextColor(R.color.textDisabled)));
        onView(withText(text))
                .check(matches(not(withBackgroundDrawable(drawable))));
    }

    @Test
    public void clickarDuasVezesSimultaneamente_sortearUmaPedra() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra));

        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        onView(withId(R.id.bt_sortear_pedra))
                .check(matches(withText(text)));
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
                .check(matches(withBackgroundDrawable(drawable)));

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

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_novo_sorteio))
                .perform(click());

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withText(R.string.item_novo_sorteio))
                .check(matches(isDisplayed()));

        onView(withText(R.string.dialog_negative))
                .perform(click());

        onView(withId(R.id.bt_sortear_pedra))
                .check(matches(withText(text)));
        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .check(matches(isSelected()));
        onView(withText(text.substring(1)))
                .check(matches(withTextColor(R.color.pedraTextoEnabled)));
        onView(withText(text.substring(1)))
                .check(matches(withBackgroundDrawable(drawable)));
    }

    @Test
    public void trocarFragments_resetarInformacoes() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        String text = getButtonText(withId(R.id.bt_sortear_pedra));

        Drawable drawable = VectorDrawableCompat.create(
                mActivityRule.getActivity().getResources(),
                R.drawable.pedra,
                new ContextThemeWrapper(mActivityRule.getActivity(), R.style.PedraEnabled).getTheme());

        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .perform(click());

        CustomProcedures.changeNavigation(R.id.item_visualizar_cartelas);
        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);

        onView(withId(R.id.bt_sortear_pedra))
                .check(matches(withText(R.string.bt_sortear_pedra)));
        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), 0))
                .check(matches(isSelected()));
        onView(withText(text.substring(1)))
                .check(matches(not(withTextColor(R.color.pedraTextoEnabled))));
        onView(withText(text.substring(1)))
                .check(matches(not(withBackgroundDrawable(drawable))));
    }

    @Test
    public void mudarTab_confereCartela_manterInformacoes() {
        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .perform(click());

        onView(withId(R.id.item_confere_cartelas))
                .perform(click());

        pressBack();

        onView(indexChildOf(withParent(withId(R.id.tl_pedras_sorteadas)), QTDE_LETRAS - 1))
                .check(matches(isSelected()));
    }

    @Test
    public void sortearPedra_NovoSorteio_ConferirCartela_NovoSorteioPedra() {
        onView(withId(R.id.bt_sortear_pedra))
                .perform(click());

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_novo_sorteio))
                .perform(click());

        onView(withText(R.string.dialog_novo_sorteio_positive))
                .perform(click());

        onView(withId(R.id.item_confere_cartelas))
                .perform(click());

        pressBack();

        onView(withId(R.id.bt_sortear_pedra))
                .check(matches(withText(R.string.bt_sortear_pedra)));
    }

    @Test
    public void cancelarAlterarTipoSorteio_ManterTipoAnterior() {
        setTipoSorteioDefault();
        String tituloTipoSorteio =
                mActivityRule.getActivity().getString(R.string.realizar_sorteio_title) + " - " +
                        TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CARTELA_CHEIA).getNome();

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(tituloTipoSorteio)));

        CustomProcedures.changeNavigation(R.id.item_visualizar_cartelas);
        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(tituloTipoSorteio)));

        onView(withId(R.id.item_confere_cartelas))
                .perform(click());

        pressBack();

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(tituloTipoSorteio)));
    }

    @Test
    public void alterarTipoSorteio_ModificarTitulo() {
        setTipoSorteioDefault();
        String textoTipoAlterado =
                TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CINCO_PEDRAS).getNome();

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(
                        mActivityRule.getActivity().getString(R.string.realizar_sorteio_title)
                                + " - " +
                                TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CARTELA_CHEIA).getNome())));

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_alterar_tipo_sorteio))
                .perform(click());
        onView(withText(textoTipoAlterado))
                .perform(click());
        onView(withText(R.string.dialog_confirmative))
                .perform(click());

        onView(withText(mActivityRule.getActivity().getTitle().toString()))
                .check(matches(withText(
                        mActivityRule.getActivity().getString(R.string.realizar_sorteio_title)
                                + " - " + textoTipoAlterado)));
    }

    private void setTipoSorteioDefault() {
        String textoTipoSorteioDefault =
                TipoSorteioDTO.getTipoSorteio(TipoSorteioDTO.CARTELA_CHEIA).getNome();

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_alterar_tipo_sorteio))
                .perform(click());
        onView(withText(textoTipoSorteioDefault))
                .perform(click());
        onView(withText(R.string.dialog_confirmative))
                .perform(click());
    }

    //TODO mudança de orientação no fim do sorteio
    //TODO pausar e restartar a aplicação
}
