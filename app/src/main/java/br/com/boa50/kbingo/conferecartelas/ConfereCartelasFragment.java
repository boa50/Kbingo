package br.com.boa50.kbingo.conferecartelas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.di.ActivityScoped;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class ConfereCartelasFragment extends DaggerFragment implements ConfereCartelasContract.View {

    @Inject
    Context mContext;
    @Inject
    ConfereCartelasContract.Presenter mPresenter;

    @BindView(R.id.lv_cartelas_ganhadoras)
    ListView lvCartelasGanhadoras;

    private Unbinder unbinder;
    private int[] mCartelasGanhadoras;

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

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.subscribe(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mCartelasGanhadoras = bundle.getIntArray(Constant.EXTRA_CARTELAS_GANHADORAS);
        }

        for (int i = 0; i < Objects.requireNonNull(mCartelasGanhadoras).length; i ++){
            TextView tv = new TextView(mContext);
            tv.setText(String.format(
                    Locale.ENGLISH,
                    Constant.FORMAT_CARTELA,
                    mCartelasGanhadoras[i]));
            lvCartelasGanhadoras.addView(tv);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unsubscribe();
    }
}
