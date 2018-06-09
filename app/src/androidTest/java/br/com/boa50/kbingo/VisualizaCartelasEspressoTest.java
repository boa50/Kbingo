package br.com.boa50.kbingo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasActivity;

@RunWith(AndroidJUnit4.class)
public class VisualizaCartelasEspressoTest {

    @Rule
    public ActivityTestRule<VisualizaCartelasActivity> mActivityRule =
            new ActivityTestRule<>(VisualizaCartelasActivity.class);
}
