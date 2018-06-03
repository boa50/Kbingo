package br.com.boa50.kbingo.realizasorteio;

import android.os.Bundle;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.ActivityUtils;
import dagger.android.support.DaggerAppCompatActivity;

public class RealizaSorteioActivity extends DaggerAppCompatActivity {

    @Inject
    RealizaSorteioFragment mRealizaSorteioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act);

        if (getSupportFragmentManager().findFragmentById(R.id.conteudoFrame) == null) {
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    mRealizaSorteioFragment,
                    R.id.conteudoFrame
            );
        }
    }
}
