package br.com.boa50.kbingo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.FakeAppRepository;

final class CustomProceduresMock {
    static FakeAppRepository initializeFakeDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
        FakeAppRepository repository = new FakeAppRepository(db);
        repository.initializeDatabase();
        return repository;
    }
}
