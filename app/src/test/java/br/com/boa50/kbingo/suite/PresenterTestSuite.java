package br.com.boa50.kbingo.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.boa50.kbingo.conferecartelas.ConfereCartelasPresenterTest;
import br.com.boa50.kbingo.realizasorteio.RealizaSorteioPresenterTest;
import br.com.boa50.kbingo.sorteiocartela.SorteioCartelaPresenterTest;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasPresenterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RealizaSorteioPresenterTest.class,
        VisualizaCartelasPresenterTest.class,
        ConfereCartelasPresenterTest.class,
        SorteioCartelaPresenterTest.class
})
public class PresenterTestSuite {}
