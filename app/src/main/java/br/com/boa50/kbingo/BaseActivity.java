package br.com.boa50.kbingo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioFragment;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity extends DaggerAppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    RealizaSorteioFragment mRealizaSorteioFragment;
    @Inject
    VisualizaCartelasFragment mVisualizaCartelasFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        setTitle(R.string.realizar_sorteio_title);
        modificarFragment(mRealizaSorteioFragment);
        mNavigationView.setCheckedItem(R.id.item_realizar_sorteio);

        mNavigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            mDrawerLayout.closeDrawers();

            switch (item.getItemId()){
                case R.id.item_realizar_sorteio:
                    setTitle(R.string.realizar_sorteio_title);
                    modificarFragment(mRealizaSorteioFragment);
                    break;
                case R.id.item_visualizar_cartelas:
                    setTitle(R.string.visualizar_cartelas_title);
                    modificarFragment(mVisualizaCartelasFragment);
                    break;
            }

            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void modificarFragment(Fragment fragment){
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.conteudoFrame
        );
    }
}
