package br.com.boa50.kbingo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.view.Gravity;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.AppRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

final class CustomProcedures {
    static AppDatabase initializeDatabase(AppDatabase db) {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
        AppDataSource appDataSource = new AppRepository(db);
        appDataSource.initializeDatabase();
        return db;
    }

    static void changeNavigation(int viewId) {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START)))
                .perform(DrawerActions.open());
        onView(withId(R.id.navigation_view))
                .perform(navigateTo(viewId));
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());
    }
}
