package br.com.boa50.kbingo;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.view.Gravity;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.AppDatabase;
import br.com.boa50.kbingo.data.AppRepository;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

final class CustomProcedures {
    static AppDatabase initializeDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
        AppDataSource appDataSource = new AppRepository(db);
        appDataSource.initializeDatabase();
        return db;
    }

    static AppRepository initializeRepositoryDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Test.db").build();
        AppRepository repository = new AppRepository(db);
        repository.initializeDatabase();
        return repository;
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

    static void sortearPedras(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            onView(withId(R.id.bt_sortear_pedra))
                    .perform(click());

            try {
                if (i < (quantidade - 1))Thread.sleep(BuildConfig.DELAY_CLICK);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void alterarTipoSorteio(int tipoSorteioId) {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.item_alterar_tipo_sorteio))
                .perform(click());
        onView(withText(TipoSorteioDTO.getTipoSorteio(tipoSorteioId).getNome()))
                .perform(click());
        onView(withText(R.string.dialog_confirmative))
                .perform(click());
    }

    static void mudarOrientacaoTela(Activity activity) {
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
