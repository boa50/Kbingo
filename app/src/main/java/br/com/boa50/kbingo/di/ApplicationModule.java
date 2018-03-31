package br.com.boa50.kbingo.di;

import android.app.Application;
import android.content.Context;

import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import br.com.boa50.kbingo.util.schedulers.ScheduleProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);

    @Binds
    abstract BaseSchedulerProvider bindBaseSchedulerProvider(ScheduleProvider scheduleProvider);
}
