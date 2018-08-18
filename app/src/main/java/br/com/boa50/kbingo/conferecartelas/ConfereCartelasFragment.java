package br.com.boa50.kbingo.conferecartelas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasFragment;
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
    @Inject
    VisualizaCartelasFragment mVisualizaCartelasFragment;

    @BindView(R.id.lv_cartelas_ganhadoras)
    ListView lvCartelasGanhadoras;

    private Unbinder unbinder;
    private ArrayList<String> mCartelasGanhadoras;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.subscribe(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mCartelasGanhadoras = bundle.getStringArrayList(Constant.EXTRA_CARTELAS_GANHADORAS);

            Objects.requireNonNull(getActivity())
                    .setTitle(getString(R.string.cartelas_ganhadoras_title) + " - " +
                    mCartelasGanhadoras.size() + " Cartelas");

            ArrayAdapter adapter = new ArrayAdapter<>(
                    Objects.requireNonNull(getContext()),
                    android.R.layout.simple_list_item_1,
                    mCartelasGanhadoras);
            lvCartelasGanhadoras.setAdapter(adapter);

            lvCartelasGanhadoras.setOnItemClickListener((parent, view, position, id) -> {
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.EXTRA_ULTIMA_CARTELA, mCartelasGanhadoras.get(position));
                bundle1.putParcelableArrayList(Constant.EXTRA_PEDRAS,
                        getArguments().getParcelableArrayList(Constant.EXTRA_PEDRAS));
                mVisualizaCartelasFragment.setArguments(bundle1);

                Objects.requireNonNull(getActivity()).setTitle(R.string.conferir_cartelas_title);

                ActivityUtils.addFragmentToActivity(
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                        mVisualizaCartelasFragment,
                        R.id.conteudoFrame,
                        true
                );
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unsubscribe();
    }
}
