package br.com.boa50.kbingo.visualizacartelas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visualizacartelas_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

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
            mPresenter.carregarCartela(
                    Integer.toString(Integer.parseInt(etNumeroCartela.getText().toString()))
            );
        }

        for (int i = 0; i <= 4; i++) {
            TextView textView = new TextView(mContext);
            glCartela.addView(textView);

            textView.setText(letras.get(i).getNome());

            estilizarCelulaCartela(textView, true);
        }
    }

    @Override
    public void apresentarCartela(List<CartelaPedra> cartelaPedras) {

        etNumeroCartela.setText(cartelaPedras.get(0).getCartelaIdFormatado());

        for (CartelaPedra cartelaPedra : cartelaPedras) {
            TextView textView = new TextView(mContext);
            glCartela.addView(textView, new GridLayout.LayoutParams(
                                        GridLayout.spec(cartelaPedra.getLinha()),
                                        GridLayout.spec(cartelaPedra.getColuna())));

            textView.setText(cartelaPedra.getPedraId());

            estilizarCelulaCartela(textView, false);
        }

        TextView textView = new TextView(mContext);
        glCartela.addView(textView, new GridLayout.LayoutParams(
                GridLayout.spec(3),
                GridLayout.spec(2)));

        textView.setText("*");

        estilizarCelulaCartela(textView, false);
    }

    private void estilizarCelulaCartela(TextView textView, boolean header) {
        if (header) {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        } else {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }

        textView.setTextSize(50);

        textView.setPadding(40,20, 40, 20);
        textView.setBackground(mContext.getResources().getDrawable(R.drawable.customborder));
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
//            params.setGravity(Gravity.CENTER);
        params.width = 200;
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
    }
}
