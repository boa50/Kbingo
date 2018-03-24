package br.com.boa50.kbingo.util.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by boa50 on 3/24/18.
 * Usado para criar uma comunicação sincrona
 */

public class ImmediateScheduleProvider implements BaseSchedulerProvider {
    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }
}
