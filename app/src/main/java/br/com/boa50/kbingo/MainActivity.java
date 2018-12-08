/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    MainActivity.java is part of Kbingo

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

public class MainActivity extends DaggerAppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    RealizaSorteioFragment realizaSorteioFragment;
    @Inject
    SorteioCartelaFragment sorteioCartelaFragment;
    @Inject
    VisualizaCartelasFragment visualizaCartelasFragment;

    private Unbinder viewsUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        viewsUnbinder = ButterKnife.bind(this);

        setupActionBar();

        if (savedInstanceState == null) {
            changeFragment(R.id.item_realizar_sorteio);
            navigationView.setCheckedItem(R.id.item_realizar_sorteio);
        }

        setupNavigation();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void setupNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            drawerLayout.closeDrawers();
            changeFragment(item.getItemId());
            return true;
        });
    }

    private void changeFragment(int menuItemId) {
        Fragment fragment;

        switch (menuItemId) {
            case R.id.item_realizar_sorteio:
                setTitle(R.string.realizar_sorteio_title);
                fragment = realizaSorteioFragment;
                break;
            case R.id.item_sorteio_cartela:
                setTitle(R.string.sorteio_cartela_title);
                fragment = sorteioCartelaFragment;
                break;
            case R.id.item_visualizar_cartelas:
                setTitle(R.string.visualizar_cartelas_title);
                fragment = visualizaCartelasFragment;
                break;
            default:
                fragment = realizaSorteioFragment;
        }

        ActivityUtils.addFragmentToMainActivity(
                getSupportFragmentManager(),
                fragment
        );
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
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        boolean fragmentInicial = false;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getId() == realizaSorteioFragment.getId()) {
                fragmentInicial = true;
                break;
            }
        }

        if (fragmentInicial) {
            abrirDialogSairApp();
        } else {
            changeFragment(R.id.item_realizar_sorteio);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewsUnbinder.unbind();
    }
}
