package br.com.boa50.kbingo.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.boa50.kbingo.RealizaSorteioEspressoTest;
import br.com.boa50.kbingo.SorteioCartelaEspressoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RealizaSorteioEspressoTest.class,
        SorteioCartelaEspressoTest.class
})
public class EspressoTestSuite {}
