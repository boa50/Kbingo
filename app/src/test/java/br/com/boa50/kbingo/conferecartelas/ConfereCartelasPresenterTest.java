package br.com.boa50.kbingo.conferecartelas;


import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConfereCartelasPresenterTest {
    private int tamanhoLista;
    private String textoPadrao;

    @Mock
    private ConfereCartelasContract.View view;

    private ConfereCartelasContract.Presenter presenter;

    @Before
    public void setUp() {
        initMocks(this);
        presenter = new ConfereCartelasPresenter();

        ArrayList<String> CARTELAS_GANHADORAS = Lists.newArrayList(
                "0001", "0002", "0007", "0050", "0100", "0123"
        );
        tamanhoLista = CARTELAS_GANHADORAS.size();
        textoPadrao = "oitava";

        presenter.subscribe(view, CARTELAS_GANHADORAS, textoPadrao);
    }

    @Test
    public void verificarTextoPadrao() {
        assertThat(presenter.getCartelasGanhadoras().get(0),
                equalTo(textoPadrao));
    }

    @Test
    public void verificarTamanhoLista() {
        assertThat(presenter.getCartelasGanhadorasSize(),
                equalTo(tamanhoLista));
    }

    @Test
    public void filtrarCartelas_apresentarCartelasFiltradas() {
        presenter.filtrarCartelas("7");

        verify(view).apresentarCartelasFiltradas();
    }

    @Test
    public void filtrarCartelas_apresentarCartelasCorretas() {
        presenter.filtrarCartelas("1");

        assertThat(presenter.getCartelasGanhadoras(),
                contains(textoPadrao,"0001","0100","0123"));
    }
}
