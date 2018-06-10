package br.com.boa50.kbingo.visualizacartelas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static br.com.boa50.kbingo.Constant.FORMAT_PEDRA;
import static br.com.boa50.kbingo.util.StringUtils.removeZeros;

public class VisualizaCartelasFragment extends DaggerFragment implements VisualizaCartelasContract.View {

    @Inject
    VisualizaCartelasContract.Presenter mPresenter;

    @Inject
    Context mContext;

    @BindView(R.id.gl_cartela)
    GridLayout glCartela;

    @BindView(R.id.et_numero_cartela)
    EditText etNumeroCartela;

    private Unbinder unbinder;

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

        etNumeroCartela.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etNumeroCartela.clearFocus();
                InputMethodManager imm = (InputMethodManager)
                        v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);

                mPresenter.carregarCartela(removeZeros(etNumeroCartela.getText().toString()));
            }
            return false;
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
    }

    @Override
    public void iniciarLayout(List<Letra> letras) {

        if ("".equals(etNumeroCartela.getText().toString())) {
            mPresenter.carregarCartela("1");
        } else {
            mPresenter.carregarCartela(removeZeros(etNumeroCartela.getText().toString()));
        }

        for (int i = 0; i <= letras.size() - 1; i++) {
            TextView textView = new TextView(mContext);
            glCartela.addView(textView, new GridLayout.LayoutParams(
                    GridLayout.spec(0, 1f),
                    GridLayout.spec(i, 1f)));

            textView.setText(letras.get(i).getNome());

            estilizarCelulaCartela(textView, true);
        }
    }

    @Override
    public void apresentarCartela(List<CartelaPedra> cartelaPedras) {

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
                    Integer.parseInt(cartelaPedra.getPedraId())));

            estilizarCelulaCartela(textView, false);
        }

        TextView textView = new TextView(mContext);
        glCartela.addView(textView, new GridLayout.LayoutParams(
                GridLayout.spec(3, 1f),
                GridLayout.spec(2, 1f)));

        textView.setText("*");

        estilizarCelulaCartela(textView, false);
    }

    private void estilizarCelulaCartela(TextView textView, boolean header) {
        if (header) {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        } else {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }

        textView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimension(R.dimen.pedra_text));

        textView.setPadding(
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_left_right),
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_top_bottom),
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_left_right),
                mContext.getResources().getDimensionPixelSize(R.dimen.pedra_padding_top_bottom));
        textView.setBackground(mContext.getResources().getDrawable(R.drawable.customborder));
        textView.setGravity(Gravity.CENTER);
    }
}
