package br.com.boa50.kbingo.visualizacartelas;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.util.CartelaUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static br.com.boa50.kbingo.Constant.FORMAT_PEDRA;
import static butterknife.internal.Utils.arrayOf;

public class VisualizaCartelasFragment extends DaggerFragment implements VisualizaCartelasContract.View {
    private static final String ARGS_CARTELA_ULTIMA = "ultimaCartela";
    private static final String ARGS_DIALOG_EXPORTAR_CARTELAS = "exportarCartelas";
    private static final String ARGS_EXPORTAR_CARTELAS_ID_INICIAL = "exportarCartelasIdInicial";
    private static final String ARGS_EXPORTAR_CARTELAS_ID_FINAL = "exportarCartelasIdFinal";
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 5050;

    @Inject
    VisualizaCartelasContract.Presenter mPresenter;

    @Inject
    Context mContext;

    @BindView(R.id.gl_cartela)
    GridLayout glCartela;

    @BindView(R.id.et_numero_cartela)
    EditText etNumeroCartela;

    private Unbinder unbinder;
    private String mUltimaCartelaNumero;
    private boolean mConfereCartela;
    private Dialog mDialogExportarCartelas;
    private int mIdInicial;
    private int mIdFinal;

    private boolean mPermissaoEscirta;

    @Inject
    public VisualizaCartelasFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visualizacartelas_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mPresenter.subscribe(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mUltimaCartelaNumero = bundle.getString(Constant.EXTRA_ULTIMA_CARTELA);
            mConfereCartela = bundle.getBoolean(Constant.EXTRA_CONFERE_CARTELA);
        }

        if (savedInstanceState == null) {
            if (mUltimaCartelaNumero == null) mUltimaCartelaNumero = "0001";
            mIdInicial = 0;
            mIdFinal = 0;
        } else {
            mUltimaCartelaNumero = savedInstanceState.getString(ARGS_CARTELA_ULTIMA);
            mIdInicial = savedInstanceState.getInt(ARGS_EXPORTAR_CARTELAS_ID_INICIAL);
            mIdFinal = savedInstanceState.getInt(ARGS_EXPORTAR_CARTELAS_ID_FINAL);
            if (savedInstanceState.getBoolean(ARGS_DIALOG_EXPORTAR_CARTELAS)) {
                mPresenter.prepararDialogExportar(mIdInicial, mIdFinal);
            }
        }

        etNumeroCartela.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etNumeroCartela.clearFocus();
                InputMethodManager imm = (InputMethodManager)
                        v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);

                mPresenter.carregarCartela(Integer.parseInt(etNumeroCartela.getText().toString()),
                        mConfereCartela);
            }
            return false;
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissaoEscirta = true;
                } else {
                    mPermissaoEscirta = false;
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.item_exportar_cartelas).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_exportar_cartelas:
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_WRITE_EXTERNAL_STORAGE);
                } else {
                    mPermissaoEscirta = true;
                }

                if (mPermissaoEscirta) {
                    mPresenter.prepararDialogExportar(0,0);
                } else {
                    String texto = mContext.getResources()
                            .getText(R.string.toast_permissao_escrita_nao_concedida).toString();
                    ActivityUtils.showToastEstilizado(mContext, texto, Toast.LENGTH_SHORT);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_CARTELA_ULTIMA, etNumeroCartela.getText().toString());
        outState.putBoolean(ARGS_DIALOG_EXPORTAR_CARTELAS,
                mDialogExportarCartelas != null && mDialogExportarCartelas.isShowing());
        outState.putInt(ARGS_EXPORTAR_CARTELAS_ID_INICIAL, mIdInicial);
        outState.putInt(ARGS_EXPORTAR_CARTELAS_ID_FINAL, mIdFinal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        etNumeroCartela.setOnEditorActionListener(null);
        unbinder.unbind();
        mPresenter.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void iniciarLayout(List<Letra> letras) {
        etNumeroCartela.setText(mUltimaCartelaNumero);

        if (!"".equals(mUltimaCartelaNumero)) {
            mPresenter.carregarCartela(Integer.parseInt(mUltimaCartelaNumero), mConfereCartela);
        }

        for (int i = 0; i <= letras.size() - 1; i++) {
            TextView textView = new TextView(mContext);
            glCartela.addView(textView, new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1f),
                    GridLayout.spec(i, 1f)));

            textView.setText(letras.get(i).getNome());

            estilizarCelulaCartela(textView, true, false);
        }
    }

    @Override
    public void apresentarCartela(List<CartelaPedra> cartelaPedras, List<Pedra> pedras) {
        etNumeroCartela.setText(cartelaPedras.get(0).getCartelaIdFormatado());

        if (glCartela.getChildCount() > 5) {
            glCartela.removeViews(5, 25);
        }

        for (CartelaPedra cartelaPedra : cartelaPedras) {
            TextView textView = new TextView(mContext);
            glCartela.addView(textView, new GridLayout.LayoutParams(
                                        GridLayout.spec(cartelaPedra.getLinha(), 1f),
                                        GridLayout.spec(cartelaPedra.getColuna(), 1f)));

            textView.setText(String.format(
                    Locale.ENGLISH,
                    FORMAT_PEDRA,
                    cartelaPedra.getPedraId()));

            if (pedras != null) {
                estilizarCelulaCartela(textView, false,
                        pedras.get(cartelaPedra.getPedraId()-1).isSorteada());
            } else {
                estilizarCelulaCartela(textView, false, false);
            }
        }

        ImageView imageView = new ImageView(mContext);
        glCartela.addView(imageView, new GridLayout.LayoutParams(
                GridLayout.spec(3, 1f),
                GridLayout.spec(2, 1f)));

        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.lens_black));
        imageView.setBackground(mContext.getResources().getDrawable(R.drawable.customborder));
        int dotPadding = (int) mContext.getResources().getDimension(R.dimen.dot_padding);
        imageView.setPadding(dotPadding, dotPadding, dotPadding, dotPadding);
    }

    @Override
    public void apresentarMaximoIdCartela(int id) {
        String texto = mContext.getResources().getText(R.string.toast_cartela_maxima).toString() + " " + id;
        ActivityUtils.showToastEstilizado(mContext, texto, Toast.LENGTH_SHORT);
    }

    private void estilizarCelulaCartela(TextView textView, boolean header, boolean sorteada) {
        if (header) {
            textView.setTextColor(mContext.getResources().getColor(R.color.headerCartelaTexto));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.textoPadrao));

            if (sorteada){
                textView.setBackground(
                        mContext.getResources().getDrawable(R.drawable.pedrasorteada_customborder));
            } else {
                textView.setBackground(
                        mContext.getResources().getDrawable(R.drawable.customborder));
            }
        }

        textView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.pedra_text));

        textView.setPadding(
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_left_right),
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_top_bottom),
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_left_right),
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_top_bottom));
        textView.setGravity(Gravity.CENTER);
    }

    @Override
    public void abrirDialogExportarCartelas(int idInicial, int idFinal) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater()
                .inflate(R.layout.dialog_exportar_cartelas, null);
        TextInputEditText etInicial = view.findViewById(R.id.et_dialog_exportar_cartelas_inicial);
        TextInputEditText etFinal = view.findViewById(R.id.et_dialog_exportar_cartelas_final);

        mIdInicial = idInicial;
        mIdFinal = idFinal;
        etInicial.setText(CartelaUtils.formatarNumeroCartela(idInicial));
        etFinal.setText(CartelaUtils.formatarNumeroCartela(idFinal));

        etInicial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIdInicial = Integer.parseInt(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etFinal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIdFinal = Integer.parseInt(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_exportar_cartelas_title)
                .setView(view)
                .setNegativeButton(R.string.dialog_negative, (dialogInterface, i) -> {})
                .setPositiveButton(R.string.dialog_exportar_cartelas_positive, (dialogInterface, i) ->
                        mPresenter.exportarCartelas(
                                Integer.parseInt(etInicial.getText().toString()),
                                Integer.parseInt(etFinal.getText().toString())
                        ));

        mDialogExportarCartelas = builder.create();
        mDialogExportarCartelas.setCanceledOnTouchOutside(false);
        mDialogExportarCartelas.show();
    }

    @Override
    public void mostrarMensagensIdsIncompativeis() {
        String texto = mContext.getResources().getText(R.string.toast_ids_incompativeis).toString();
        ActivityUtils.showToastEstilizado(mContext, texto, Toast.LENGTH_SHORT);
    }

    @Override
    public void realizarDownload(File file) {
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);

            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
