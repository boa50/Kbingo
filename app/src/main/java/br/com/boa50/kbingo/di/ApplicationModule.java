package br.com.boa50.kbingo.di;

import android.content.Context;

import br.com.boa50.kbingo.KbingoApplication;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import br.com.boa50.kbingo.util.schedulers.ScheduleProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(KbingoApplication application);

    @Binds
    abstract BaseSchedulerProvider bindBaseSchedulerProvider(ScheduleProvider scheduleProvider);
}
