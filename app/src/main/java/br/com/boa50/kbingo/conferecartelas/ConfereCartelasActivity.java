package br.com.boa50.kbingo.conferecartelas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

public class ConfereCartelasActivity extends DaggerAppCompatActivity {

    @Inject
    VisualizaCartelasFragment mVisualizaCartelasFragment;
    @Inject
    ConfereCartelasFragment mConfereCartelasFragment;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Unbinder unbinder;
    private ArrayList<Pedra> mPedras;
    private int[] mCartelasGanhadoras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act_sem_navigation);
        setTitle(R.string.conferir_cartelas_title);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mPedras = getIntent().getParcelableArrayListExtra(Constant.EXTRA_PEDRAS);
        mCartelasGanhadoras = getIntent().getIntArrayExtra(Constant.EXTRA_CARTELAS_GANHADORAS);

        direcionarFragment();
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

    private void direcionarFragment() {
        Bundle bundle = new Bundle();
        Fragment fragment;

        if (mCartelasGanhadoras != null && mCartelasGanhadoras.length > 0) {
            fragment = mConfereCartelasFragment;
            bundle.putIntArray(Constant.EXTRA_CARTELAS_GANHADORAS, mCartelasGanhadoras);
        } else {
            fragment = mVisualizaCartelasFragment;
            bundle.putString(Constant.EXTRA_ULTIMA_CARTELA, "");
            bundle.putParcelableArrayList(Constant.EXTRA_PEDRAS, mPedras);
        }

        fragment.setArguments(bundle);

        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.conteudoFrame
        );
    }
}
