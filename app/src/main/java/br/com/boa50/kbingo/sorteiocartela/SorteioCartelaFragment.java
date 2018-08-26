package br.com.boa50.kbingo.sorteiocartela;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.di.ActivityScoped;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SorteioCartelaFragment extends DaggerFragment implements SorteioCartelaContract.View {

    @Inject
    SorteioCartelaContract.Presenter mPresenter;

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

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.subscribe(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mPresenter.unsubscribe();
    }
}
