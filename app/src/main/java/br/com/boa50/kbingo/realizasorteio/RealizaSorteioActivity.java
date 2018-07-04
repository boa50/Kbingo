package br.com.boa50.kbingo.realizasorteio;

import android.os.Bundle;

import javax.inject.Inject;

import br.com.boa50.kbingo.BaseActivity;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.ActivityUtils;
import dagger.android.support.DaggerAppCompatActivity;

public class RealizaSorteioActivity extends BaseActivity {

    @Inject
    RealizaSorteioFragment mRealizaSorteioFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.realizar_sorteio_title);

        if (getSupportFragmentManager().findFragmentById(R.id.conteudoFrame) == null) {
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    mRealizaSorteioFragment,
                    R.id.conteudoFrame
            );
        }
    }
}
