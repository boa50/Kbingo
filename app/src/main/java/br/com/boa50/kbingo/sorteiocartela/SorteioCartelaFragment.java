package br.com.boa50.kbingo.sorteiocartela;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
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

import br.com.boa50.kbingo.BuildConfig;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SorteioCartelaFragment extends DaggerFragment implements SorteioCartelaContract.View {
    private static final String ARGS_DIALOG_FILTRO_SORTEIO = "dialogFiltroSorteio";
    private static final String ARGS_DIALOG_LIMPA_FILTRO = "dialogLimpaFiltro";
    private static final String ARGS_FILTRO_CB_CARTELAS_GANHADORAS = "filtroCartelasGanhadoras";
    private static final String ARGS_FILTRO_TEXTO = "filtroTexto";
    private static final String ARGS_ULTIMA_SORTEADA = "ultimaSorteada";

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
    private long mLastClickTime;
    private boolean mCbCartelasGanhadoras;
    private int mGridCartelasSorteaveisColunas;
    private Dialog mDialogFiltroSorteio;
    private Dialog mDialogLimpaFiltro;
    private String mFiltroCartelasTexto;
    private int mUltimaSorteada;

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
        mLastClickTime = 0;

        return view;
    }

    @OnClick(R.id.bt_sorteio_cartela)
    void sortearCartela() {
        if (SystemClock.elapsedRealtime() - mLastClickTime > BuildConfig.DELAY_CLICK) {
            mPresenter.sortearCartela();
            mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_ULTIMA_SORTEADA, mUltimaSorteada);
        outState.putBoolean(ARGS_FILTRO_CB_CARTELAS_GANHADORAS, mCbCartelasGanhadoras);
        outState.putString(ARGS_FILTRO_TEXTO, mFiltroCartelasTexto);
        outState.putBoolean(ARGS_DIALOG_FILTRO_SORTEIO,
                mDialogFiltroSorteio != null && mDialogFiltroSorteio.isShowing());
        outState.putBoolean(ARGS_DIALOG_LIMPA_FILTRO,
                mDialogLimpaFiltro != null && mDialogLimpaFiltro.isShowing());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.subscribe(this);

        if (savedInstanceState != null) {
            mUltimaSorteada = savedInstanceState.getInt(ARGS_ULTIMA_SORTEADA);
            mCbCartelasGanhadoras = savedInstanceState.getBoolean(ARGS_FILTRO_CB_CARTELAS_GANHADORAS);
            mFiltroCartelasTexto = savedInstanceState.getString(ARGS_FILTRO_TEXTO);
            if (savedInstanceState.getBoolean(ARGS_DIALOG_FILTRO_SORTEIO)) {
                abrirDialogFiltroCartelas();
            }
            if (savedInstanceState.getBoolean(ARGS_DIALOG_LIMPA_FILTRO)) {
                abrirDialogLimparFiltro();
            }
        } else {
            mFiltroCartelasTexto = "";
        }
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
    public void onResume() {
        super.onResume();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridCartelasSorteaveisColunas = 3;
        } else {
            mGridCartelasSorteaveisColunas = 2;
        }

        if (mUltimaSorteada > 0) apresentarCartela(mUltimaSorteada);
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
        btSorteioCartela.setText(StringUtils.formatarNumeroCartela(numeroCartela));
        mUltimaSorteada = numeroCartela;
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
        rvCartelaFiltro.postDelayed(() -> rvCartelaFiltro.scrollToPosition(0), 50);
    }

    @Override
    public void preencherCartelasSorteaveis(List<Integer> cartelasSorteaveis) {
        if (cartelasSorteaveis.isEmpty()) cartelasSorteaveis.add(-1);

        if (rvCartelasSorteaveis.getAdapter() != null) {
            cartelasSorteaveisAdapter.submitList(cartelasSorteaveis);
        } else {
            estilizarRecyclerView(rvCartelasSorteaveis, mGridCartelasSorteaveisColunas);
            cartelasSorteaveisAdapter.submitList(cartelasSorteaveis);
            rvCartelasSorteaveis.setAdapter(cartelasSorteaveisAdapter);
        }
    }

    @Override
    public void retornarPadraoTela() {
        btSorteioCartela.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.sc_pedra_texto)
        );
        btSorteioCartela.setText(R.string.bt_sorteio_cartela);
    }

    private void abrirDialogFiltroCartelas() {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.dialog_cartelas_filtro, null);
        rvCartelaFiltro = view.findViewById(R.id.rv_sorteio_cartela_filtro);
        EditText etFiltroCartelas = view.findViewById(R.id.et_sorteio_cartela_numero);
        CheckBox cbFiltroCartelasGanhadoras = view.findViewById(R.id.cb_filtro_cartelas_ganhadoras);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_filtrar_cartelas_title)
                .setView(view);

        etFiltroCartelas.setText(mFiltroCartelasTexto);
        etFiltroCartelas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.carregarFiltroCartelasSorteaveis(
                        charSequence.toString(), cbFiltroCartelasGanhadoras.isChecked());
                mFiltroCartelasTexto = charSequence.toString();
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

        mPresenter.carregarFiltroCartelasSorteaveis(
                etFiltroCartelas.getText().toString(), mCbCartelasGanhadoras);
        mDialogFiltroSorteio = builder.create();
        mDialogFiltroSorteio.show();
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

        mDialogLimpaFiltro = builder.create();
        mDialogLimpaFiltro.setCanceledOnTouchOutside(false);
        mDialogLimpaFiltro.show();
    }
}
