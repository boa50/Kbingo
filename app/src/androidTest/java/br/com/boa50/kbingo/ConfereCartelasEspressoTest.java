package br.com.boa50.kbingo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.data.AppDatabase;

@RunWith(AndroidJUnit4.class)
public class ConfereCartelasEspressoTest {
    private static AppDatabase db;

    @Rule
    public ActivityTestRule<ConfereCartelasActivity> mActivityRule =
            new ActivityTestRule<>(ConfereCartelasActivity.class);

    @BeforeClass
    public static void setup() {
        db = CustomProcedures.initializeDatabase(db);
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }
}
