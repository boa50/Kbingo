package br.com.boa50.kbingo;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.boa50.kbingo.CustomGets.getButtonText;
import static br.com.boa50.kbingo.CustomGets.getRecyclerViewChild;
import static br.com.boa50.kbingo.CustomGets.getRecyclerViewSize;
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
        try {
            onView(withText(R.string.dialog_negative))
                    .perform(click());
        } catch (Exception ignored){}
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

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .check(matches(not(isChecked())));
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
    public void filtrarCartela_trocarFragment_removerFiltro_aparecerTodasCartelasSorteaveis() {
        onView(withText(R.string.todas_cartelas_sorteaveis))
                .check(matches(isDisplayed()));

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        pressBack();

        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);
        CustomProcedures.changeNavigation(R.id.item_sorteio_cartela);

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        pressBack();

        onView(withText(R.string.todas_cartelas_sorteaveis))
                .check(matches(isDisplayed()));
    }

    @Test
    public void filtrarFiltroBusca_aparecerOpcoesCorretas() {
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

    @Test
    public void filtrarFiltroGanhadoras_aparecerApenasGanhadoras() {
        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);
        CustomProcedures.alterarTipoSorteio(TipoSorteioDTO.CINCO_PEDRAS);
        CustomProcedures.sortearPedras(7);

        onView(withId(R.id.item_confere_cartelas))
                .perform(click());

        //O -1 é para remover o item de "Conferir outra cartela"
        int rvSize = getRecyclerViewSize(withId(R.id.rv_cartelas_ganhadoras)) - 1;
        View vh = getRecyclerViewChild(withId(R.id.rv_cartelas_ganhadoras), 1);
        String vhText = ((TextView) vh.findViewById(R.id.tv_cartela_numero)).getText().toString();
        View vhFim = getRecyclerViewChild(withId(R.id.rv_cartelas_ganhadoras), rvSize);
        String vhFimText = ((TextView) vhFim.findViewById(R.id.tv_cartela_numero)).getText().toString();

        pressBack();

        CustomProcedures.changeNavigation(R.id.item_sorteio_cartela);

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .perform(click());

        int rv2Size = getRecyclerViewSize(withId(R.id.rv_sorteio_cartela_filtro));
        View vh2 = getRecyclerViewChild(withId(R.id.rv_sorteio_cartela_filtro), 0);
        String vh2Text = ((CheckBox) vh2.findViewById(R.id.cb_cartela_selecao)).getText().toString();
        View vh2Fim = getRecyclerViewChild(withId(R.id.rv_sorteio_cartela_filtro), rv2Size - 1);
        String vh2FimText = ((CheckBox) vh2Fim.findViewById(R.id.cb_cartela_selecao)).getText().toString();

        assertThat(rv2Size, equalTo(rvSize));
        assertThat(vh2Text, equalTo(vhText));
        assertThat(vh2FimText, equalTo(vhFimText));
    }

    @Test
    public void trocarFragments_manterInformacoesFiltro() {
        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);
        CustomProcedures.alterarTipoSorteio(TipoSorteioDTO.CINCO_PEDRAS);
        CustomProcedures.sortearPedras(7);

        CustomProcedures.changeNavigation(R.id.item_sorteio_cartela);

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .perform(click());

        int rvOldSize = getRecyclerViewSize(withId(R.id.rv_sorteio_cartela_filtro));
        View vhFiltro = getRecyclerViewChild(withId(R.id.rv_sorteio_cartela_filtro), 0);
        String vhFiltroText = ((CheckBox) vhFiltro.findViewById(R.id.cb_cartela_selecao)).getText().toString();

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.closeSoftKeyboard();
        pressBack();

        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);
        CustomProcedures.changeNavigation(R.id.item_sorteio_cartela);

        View vhSorteaveis = getRecyclerViewChild(withId(R.id.rv_sorteio_cartela_sorteaveis), 0);
        String vhSorteaveisText = ((TextView) vhSorteaveis.findViewById(R.id.tv_cartela_numero)).getText().toString();

        assertThat(vhSorteaveisText, equalTo(vhFiltroText));

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        int rvNewSize = getRecyclerViewSize(withId(R.id.rv_sorteio_cartela_filtro));

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .check(matches(isChecked()));
        assertThat(rvNewSize, equalTo(rvOldSize));
        onView(withText(vhFiltroText))
                .check(matches(isChecked()));
    }

    @Test
    public void filtrarCartelas_limparFiltro_zerarInformacoes() {
        CustomProcedures.changeNavigation(R.id.item_realizar_sorteio);
        CustomProcedures.alterarTipoSorteio(TipoSorteioDTO.CINCO_PEDRAS);
        CustomProcedures.sortearPedras(7);

        CustomProcedures.changeNavigation(R.id.item_sorteio_cartela);

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .perform(click());

        View vhFiltro = getRecyclerViewChild(withId(R.id.rv_sorteio_cartela_filtro), 0);
        String vhFiltroText = ((CheckBox) vhFiltro.findViewById(R.id.cb_cartela_selecao)).getText().toString();

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        pressBack();

        onView(withId(R.id.bt_sorteio_cartela))
                .perform(click());

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_limpar_filtro_cartelas))
                .perform(click());
        onView(withText(R.string.dialog_limpar_filtro_cartelas_title))
                .check(matches(isDisplayed()));
        onView(withText(R.string.dialog_limpar_filtro_cartelas_positive))
                .perform(click());

        onView(withText(R.string.todas_cartelas_sorteaveis))
                .check(matches(isDisplayed()));
        onView(withId(R.id.bt_sorteio_cartela))
                .check(matches(withText(R.string.bt_sorteio_cartela)));

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .check(matches(not(isChecked())));

        onView(withId(R.id.rv_sorteio_cartela_filtro))
                .perform(RecyclerViewActions.scrollToPosition(Integer.valueOf(vhFiltroText) - 1));
        onView(withText(vhFiltroText))
                .check(matches(not(isChecked())));
    }

    @Test
    public void mudarOrientacao_manterInformacoes() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_filtrar_cartelas_sorteaveis))
                .perform(click());

        for (int i = 0; i < 5; i++) {
            onView(withId(R.id.rv_sorteio_cartela_filtro))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
        }

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .perform(click());

        onView(withId(R.id.et_sorteio_cartela_numero))
                .perform(replaceText("0050"));

        CustomProcedures.mudarOrientacaoTela(mActivityRule);

        onView(withText(R.string.dialog_filtrar_cartelas_title))
                .check(matches(isDisplayed()));
        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .check(matches(isChecked()));

        onView(withId(R.id.cb_filtro_cartelas_ganhadoras))
                .perform(click());

        onView(withId(R.id.et_sorteio_cartela_numero))
                .check(matches(withText("0050")));

        int rvSize = getRecyclerViewSize(withId(R.id.rv_sorteio_cartela_filtro));
        assertThat(rvSize, equalTo(1));

        onView(withId(R.id.et_sorteio_cartela_numero))
                .perform(replaceText(""));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 5; i++) {
            onView(withText("000" + String.valueOf(i)))
                    .check(matches(isChecked()));
        }

        closeSoftKeyboard();
        pressBack();


        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_limpar_filtro_cartelas))
                .perform(click());

        CustomProcedures.mudarOrientacaoTela(mActivityRule);

        onView(withText(R.string.dialog_limpar_filtro_cartelas_title))
                .check(matches(isDisplayed()));
        onView(withText(R.string.dialog_negative))
                .perform(click());


        rvSize = getRecyclerViewSize(withId(R.id.rv_sorteio_cartela_sorteaveis));

        onView(withId(R.id.bt_sorteio_cartela))
                .perform(click());
        String buttonText = getButtonText(withId(R.id.bt_sorteio_cartela));

        CustomProcedures.mudarOrientacaoTela(mActivityRule);

        assertThat(getRecyclerViewSize(withId(R.id.rv_sorteio_cartela_sorteaveis)), equalTo(rvSize));
        assertThat(getButtonText(withId(R.id.bt_sorteio_cartela)), equalTo(buttonText));
    }
}
