package br.com.boa50.kbingo.conferecartelas;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class ConfereCartelasFragment extends DaggerFragment implements ConfereCartelasContract.View {
    private static final String ARGS_TEXTO_BUSCA = "textoBusca";

    @Inject
    Context mContext;
    @Inject
    ConfereCartelasContract.Presenter mPresenter;
    @Inject
    VisualizaCartelasFragment mVisualizaCartelasFragment;

    @BindView(R.id.rv_cartelas_ganhadoras)
    RecyclerView rvCartelasGanhadoras;

    private Unbinder unbinder;
    private SearchView mSearchView;
    private String mTextoBusca;

    @Inject
    public ConfereCartelasFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conferecartelas_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        estilizarRecyclerView();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem busca = menu.findItem(R.id.item_busca);
        busca.setVisible(true);
        mSearchView = (SearchView) busca.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) mTextoBusca = newText;
                mPresenter.filtrarCartelas(newText);
                return true;
            }
        });

        if (!mTextoBusca.isEmpty()) {
            busca.expandActionView();
            mSearchView.setQuery(mTextoBusca, false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSearchView != null) mSearchView.setOnQueryTextListener(null);
        unbinder.unbind();
        mPresenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_TEXTO_BUSCA, mTextoBusca);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        ActivityUtils.hideSoftKeyboardFrom(mContext, Objects.requireNonNull(getView()));

        mPresenter.subscribe(this);

        if (savedInstanceState != null) {
            mTextoBusca = savedInstanceState.getString(ARGS_TEXTO_BUSCA);
        } else {
            mTextoBusca = "";
        }
    }

    private void estilizarRecyclerView() {
        rvCartelasGanhadoras.setHasFixedSize(true);

        int gridColunas;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridColunas = 2;
        } else {
            gridColunas = 3;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, gridColunas);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return gridColunas;
                else return 1;
            }
        });
        rvCartelasGanhadoras.setLayoutManager(gridLayoutManager);
        rvCartelasGanhadoras.addItemDecoration(new DividerItemDecoration(
                rvCartelasGanhadoras.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void apresentarCartelas(ArrayList<String> cartelas) {
        Objects.requireNonNull(getActivity())
                .setTitle(getString(R.string.cartelas_ganhadoras_title) + " - " +
                        cartelas.size() + " Cartelas");

        CartelasGanhadorasAdapter adapter =
                new CartelasGanhadorasAdapter(getActivity(),
                        mVisualizaCartelasFragment);
        adapter.submitList(tratarCartelas(cartelas));
        rvCartelasGanhadoras.setAdapter(adapter);
    }

    @Override
    public void apresentarCartelasFiltradas(ArrayList<String> cartelas) {
        ((CartelasGanhadorasAdapter) Objects.requireNonNull(rvCartelasGanhadoras.getAdapter()))
                .submitList(tratarCartelas(cartelas));
    }

    private ArrayList<String> tratarCartelas(ArrayList<String> cartelas) {
        String textoPadrao = getString(R.string.list_item_confere_outra_cartela);
        if (cartelas.isEmpty() || !cartelas.get(0).equalsIgnoreCase(textoPadrao)) {
            cartelas.add(0, textoPadrao);
        }
        return cartelas;
    }
}
