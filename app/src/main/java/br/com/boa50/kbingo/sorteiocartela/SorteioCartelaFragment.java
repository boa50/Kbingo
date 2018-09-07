package br.com.boa50.kbingo.sorteiocartela;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.adapter.CartelasFiltroAdapter;
import br.com.boa50.kbingo.adapter.CartelasSorteaveisAdapter;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.di.ActivityScoped;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SorteioCartelaFragment extends DaggerFragment implements SorteioCartelaContract.View {

    @Inject
    Context mContext;
    @Inject
    SorteioCartelaContract.Presenter mPresenter;

    @BindView(R.id.bt_sorteio_cartela)
    Button btSorteioCartela;
//    @BindView(R.id.rv_sorteio_cartela_filtro)
    RecyclerView rvCartelaFiltro;
    @BindView(R.id.rv_sorteio_cartela_sorteaveis)
    RecyclerView rvCartelasSorteaveis;

    private Unbinder unbinder;

    @Inject
    public SorteioCartelaFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sorteiocartela_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }

    @OnClick(R.id.bt_sorteio_cartela)
    void sortearCartela() {
        mPresenter.sortearCartela();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.subscribe(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.item_filtrar_cartelas_sorteaveis).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filtrar_cartelas_sorteaveis:
                abrirDialogFiltroCartelas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mPresenter.unsubscribe();
    }

    @Override
    public void apresentarCartela(String numeroCartela) {
        btSorteioCartela.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.sc_pedra_sorteada_texto)
        );
        btSorteioCartela.setText(numeroCartela);
    }

    @Override
    public void preencherCartelasFiltro(List<CartelaFiltroDTO> cartelasFiltro) {
        if (rvCartelaFiltro.getAdapter() != null) {
            rvCartelaFiltro.getAdapter().notifyDataSetChanged();
        } else {
            rvCartelaFiltro.setHasFixedSize(true);
            rvCartelaFiltro.setLayoutManager(new LinearLayoutManager(mContext));
            rvCartelaFiltro.addItemDecoration(new DividerItemDecoration(
                    rvCartelaFiltro.getContext(), DividerItemDecoration.VERTICAL));

            CartelasFiltroAdapter adapter = new CartelasFiltroAdapter(mPresenter);
            adapter.submitList(cartelasFiltro);
            rvCartelaFiltro.setAdapter(adapter);
        }
    }

    @Override
    public void preencherCartelasSorteaveis(List<Integer> cartelasSorteaveis) {
        if (cartelasSorteaveis.isEmpty()) cartelasSorteaveis.add(-1);

        if (rvCartelasSorteaveis.getAdapter() != null) {
            rvCartelasSorteaveis.getAdapter().notifyDataSetChanged();
        } else {
            rvCartelasSorteaveis.setHasFixedSize(true);
            rvCartelasSorteaveis.setLayoutManager(new LinearLayoutManager(mContext));
            rvCartelasSorteaveis.addItemDecoration(new DividerItemDecoration(
                    rvCartelasSorteaveis.getContext(), DividerItemDecoration.VERTICAL));

            CartelasSorteaveisAdapter adapter = new CartelasSorteaveisAdapter(mContext);
            adapter.submitList(cartelasSorteaveis);
            rvCartelasSorteaveis.setAdapter(adapter);
        }
    }

    private void abrirDialogFiltroCartelas() {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.dialog_cartelas_filtro, null);
        rvCartelaFiltro = view.findViewById(R.id.rv_sorteio_cartela_filtro);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_filtrar_cartelas_title)
                .setView(view)
                .setNeutralButton(R.string.dialog_confirmative, null);
        mPresenter.carregarFiltroCartelasSorteaveis();

        builder.create().show();
    }
}
