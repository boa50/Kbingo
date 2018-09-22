package br.com.boa50.kbingo.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class DataModule {
    @Singleton
    @Provides
    static AppDatabase providerAppDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
    }

    @Singleton
    @Binds
    abstract AppDataSource appDataSource(FakeAppRepository fakeAppRepository);
}
