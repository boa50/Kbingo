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
import android.widget.TextView;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
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

    private Unbinder unbinder;

    @Inject
    public VisualizaCartelasFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visualizacartelas_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        for (int i = 1; i <= 30; i++) {
            TextView tv = new TextView(mContext);
            glCartela.addView(tv);

            switch (i) {
                case 1:
                    tv.setText("K");
                    break;
                case 2:
                    tv.setText("I");
                    break;
                case 3:
                    tv.setText("N");
                    break;
                case 4:
                    tv.setText("K");
                    break;
                case 5:
                    tv.setText("A");
                    break;
                case 18:
                    tv.setText("*");
                    break;
                default:
                    tv.setText("00");
            }

            if (i <= 5) {
                tv.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            } else {
                tv.setTextColor(mContext.getResources().getColor(android.R.color.black));
            }

            tv.setTextSize(50);

            tv.setPadding(40,20, 40, 20);
            tv.setBackground(mContext.getResources().getDrawable(R.drawable.customborder));
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) tv.getLayoutParams();
//            params.setGravity(Gravity.CENTER);
            params.width = 200;
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
        }

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
}
