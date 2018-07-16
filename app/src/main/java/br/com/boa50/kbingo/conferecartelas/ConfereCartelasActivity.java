package br.com.boa50.kbingo.conferecartelas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.ActivityUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

public class ConfereCartelasActivity extends DaggerAppCompatActivity {

    @Inject
    ConfereCartelasFragment mConfereCartelasFragment;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act_sem_navigation);
        setTitle(R.string.conferir_cartelas_title);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                mConfereCartelasFragment,
                R.id.conteudoFrame
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
