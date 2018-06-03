package br.com.boa50.kbingo.visualizacartelas;

import android.os.Bundle;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.ActivityUtils;
import dagger.android.support.DaggerAppCompatActivity;

public class VisualizaCartelasActivity extends DaggerAppCompatActivity {

    @Inject
    VisualizaCartelasFragment mVisualizaCartelasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act);

        if (getSupportFragmentManager().findFragmentById(R.id.conteudoFrame) == null)
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    mVisualizaCartelasFragment,
                    R.id.conteudoFrame
            );
    }
}
