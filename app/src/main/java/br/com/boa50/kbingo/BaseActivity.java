/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    BaseActivity.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package br.com.boa50.kbingo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioFragment;
import br.com.boa50.kbingo.sorteiocartela.SorteioCartelaFragment;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
    SorteioCartelaFragment mSorteioCartelaFragment;
    @Inject
    VisualizaCartelasFragment mVisualizaCartelasFragment;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        if (savedInstanceState == null) {
            modificarFragment(R.id.item_realizar_sorteio);
            mNavigationView.setCheckedItem(R.id.item_realizar_sorteio);
        }

        mNavigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            mDrawerLayout.closeDrawers();
            modificarFragment(item.getItemId());
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityUtils.hideSoftKeyboard(this);
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        boolean fragmentInicial = false;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getId() == mRealizaSorteioFragment.getId()) fragmentInicial = true;
        }

        if (fragmentInicial) {
            abrirDialogSairApp();
        } else {
            modificarFragment(R.id.item_realizar_sorteio);
        }
    }

    private void abrirDialogSairApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(this));
        builder.setTitle(R.string.dialog_sair_app_title)
                .setPositiveButton(R.string.dialog_sair_app_positive,
                        (dialog, which) -> this.finishAffinity())
                .setNegativeButton(R.string.dialog_negative,
                        (dialog, which) -> {})
                .create()
                .show();
    }

    private void modificarFragment(int menuItemId) {
        Fragment fragment;

        switch (menuItemId) {
            case R.id.item_realizar_sorteio:
                setTitle(R.string.realizar_sorteio_title);
                fragment = mRealizaSorteioFragment;
                break;
            case R.id.item_sorteio_cartela:
                setTitle(R.string.sorteio_cartela_title);
                fragment = mSorteioCartelaFragment;
                break;
            case R.id.item_visualizar_cartelas:
                setTitle(R.string.visualizar_cartelas_title);
                fragment = mVisualizaCartelasFragment;
                break;
            default:
                fragment = mRealizaSorteioFragment;
        }

        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.conteudoFrame
        );
    }
}
