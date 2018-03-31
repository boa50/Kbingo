package br.com.boa50.kbingo.util.schedulers;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by boa50 on 3/24/18.
 */

@Singleton
public class ScheduleProvider implements BaseSchedulerProvider {
    private static ScheduleProvider INSTANCE;

    @Inject
    ScheduleProvider(){}

    public static synchronized ScheduleProvider getInstance(){
        if (INSTANCE == null)
            INSTANCE = new ScheduleProvider();

        return INSTANCE;
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
