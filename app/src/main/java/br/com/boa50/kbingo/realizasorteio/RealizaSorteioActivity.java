package br.com.boa50.kbingo.realizasorteio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.ActivityUtils;
import dagger.android.support.DaggerAppCompatActivity;

public class RealizaSorteioActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realizasorteio_act);

        RealizaSorteioFragment realizaSorteioFragment =
                (RealizaSorteioFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.conteudoFrame);
        if (realizaSorteioFragment == null){
            realizaSorteioFragment = RealizaSorteioFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    realizaSorteioFragment,
                    R.id.conteudoFrame
            );
        }
    }
}
