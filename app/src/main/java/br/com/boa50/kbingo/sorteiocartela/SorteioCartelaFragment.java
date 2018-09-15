package br.com.boa50.kbingo.sorteiocartela;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.CartelaUtils;
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
    @Inject
    CartelasSorteaveisAdapter cartelasSorteaveisAdapter;
    @Inject
    CartelasFiltroAdapter cartelasFiltroAdapter;

    @BindView(R.id.bt_sorteio_cartela)
    Button btSorteioCartela;
    @BindView(R.id.rv_sorteio_cartela_sorteaveis)
    RecyclerView rvCartelasSorteaveis;
    RecyclerView rvCartelaFiltro;

    private Unbinder unbinder;
    private boolean mCbCartelasGanhadoras;

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
        menu.findItem(R.id.item_limpar_filtro_cartelas).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filtrar_cartelas_sorteaveis:
                abrirDialogFiltroCartelas();
                return true;
            case R.id.item_limpar_filtro_cartelas:
                abrirDialogLimparFiltro();
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
    public void apresentarCartela(int numeroCartela) {
        btSorteioCartela.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.sc_pedra_sorteada_texto)
        );
        btSorteioCartela.setText(CartelaUtils.formatarNumeroCartela(numeroCartela));
    }

    @Override
    public void preencherCartelasFiltro(List<CartelaFiltroDTO> cartelasFiltro) {
        if (rvCartelaFiltro.getAdapter() != null) {
            cartelasFiltroAdapter.submitList(cartelasFiltro);
        } else {
            estilizarRecyclerView(rvCartelaFiltro, 2);
            cartelasFiltroAdapter.submitList(cartelasFiltro);
            rvCartelaFiltro.setAdapter(cartelasFiltroAdapter);
        }
    }

    @Override
    public void preencherCartelasSorteaveis(List<Integer> cartelasSorteaveis) {
        if (cartelasSorteaveis.isEmpty()) cartelasSorteaveis.add(-1);

        if (rvCartelasSorteaveis.getAdapter() != null) {
            cartelasSorteaveisAdapter.submitList(cartelasSorteaveis);
        } else {
            estilizarRecyclerView(rvCartelasSorteaveis, 3);
            cartelasSorteaveisAdapter.submitList(cartelasSorteaveis);
            rvCartelasSorteaveis.setAdapter(cartelasSorteaveisAdapter);
        }
    }

    private void abrirDialogFiltroCartelas() {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.dialog_cartelas_filtro, null);
        rvCartelaFiltro = view.findViewById(R.id.rv_sorteio_cartela_filtro);
        EditText etFiltroCartelas = view.findViewById(R.id.et_sorteio_cartela_numero);
        CheckBox cbFiltroCartelasGanhadoras = view.findViewById(R.id.cb_filtro_cartelas_ganhadoras);

        etFiltroCartelas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.carregarFiltroCartelasSorteaveis(
                        charSequence.toString(), cbFiltroCartelasGanhadoras.isChecked());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        cbFiltroCartelasGanhadoras.setChecked(mCbCartelasGanhadoras);

        cbFiltroCartelasGanhadoras.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    mPresenter.carregarFiltroCartelasSorteaveis(
                            etFiltroCartelas.getText().toString(), isChecked);
                    mCbCartelasGanhadoras = isChecked;
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_filtrar_cartelas_title)
                .setView(view);
        if (mCbCartelasGanhadoras) mPresenter.carregarFiltroCartelasSorteaveis("", true);
        else mPresenter.carregarFiltroCartelasSorteaveis();
        builder.create().show();
    }

    private void estilizarRecyclerView(RecyclerView recyclerView, int colunasNumero) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, colunasNumero));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void abrirDialogLimparFiltro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_limpar_filtro_cartelas_title)
                .setNegativeButton(R.string.dialog_negative,
                        (dialog, which) -> {})
                .setPositiveButton(R.string.dialog_limpar_filtro_cartelas_positive,
                        (dialog, which) -> {
                            mPresenter.limparCartelasSorteaveis();
                            mCbCartelasGanhadoras = false;
                        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
