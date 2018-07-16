package br.com.boa50.kbingo.conferecartelas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public class ConfereCartelasFragment extends DaggerFragment implements ConfereCartelasContract.View {

    @Inject
    ConfereCartelasContract.Presenter mPresenter;

    private Unbinder unbinder;

    @Inject
    public ConfereCartelasFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
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
}
