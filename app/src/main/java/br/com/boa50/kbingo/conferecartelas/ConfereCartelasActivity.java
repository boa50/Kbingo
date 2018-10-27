/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    ConfereCartelasActivity.java is part of Kbingo

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
package br.com.boa50.kbingo.conferecartelas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
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
    private boolean hasCartelasGanhadoras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_act_sem_navigation);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        hasCartelasGanhadoras = getIntent().getBooleanExtra(Constant.EXTRA_CARTELAS_GANHADORAS, false);

        if (savedInstanceState == null) direcionarFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confere_cartelas_menu, menu);
        return true;
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
        Fragment fragment;

        if (hasCartelasGanhadoras) {
            fragment = mConfereCartelasFragment;
        } else {
            Bundle bundle = new Bundle();
            fragment = mVisualizaCartelasFragment;
            bundle.putString(Constant.EXTRA_ULTIMA_CARTELA, "");
            bundle.putBoolean(Constant.EXTRA_CONFERE_CARTELA, true);
            setTitle(R.string.conferir_cartelas_title);
            Snackbar.make(findViewById(R.id.conteudoFrame), R.string.snack_sem_cartela_ganhadora,
                        Snackbar.LENGTH_LONG)
                    .show();
            fragment.setArguments(bundle);
        }

        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.conteudoFrame
        );
    }
}
