package br.com.boa50.kbingo.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.boa50.kbingo.ConfereCartelasEspressoTest;
import br.com.boa50.kbingo.MenuEspressoTest;
import br.com.boa50.kbingo.VisualizaCartelasEspressoTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        MenuEspressoTest.class,
        VisualizaCartelasEspressoTest.class,
        ConfereCartelasEspressoTest.class
})
public class EspressoTestSuite {}
