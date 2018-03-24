package br.com.boa50.kbingo.util.schedulers;

import io.reactivex.Scheduler;

/**
 * Created by boa50 on 3/24/18.
 */

public interface BaseSchedulerProvider {

    Scheduler computation();

    Scheduler io();

    Scheduler ui();
}
