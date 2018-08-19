package br.com.boa50.kbingo.conferecartelas;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.Pedra;
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

    @BindView(R.id.rv_cartelas_ganhadoras)
    RecyclerView rvCartelasGanhadoras;

    private Unbinder unbinder;

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
        rvCartelasGanhadoras.setHasFixedSize(true);
        rvCartelasGanhadoras.setLayoutManager(new LinearLayoutManager(mContext));
        rvCartelasGanhadoras.addItemDecoration(new DividerItemDecoration(
                rvCartelasGanhadoras.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unsubscribe();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mPresenter.subscribe(this);
        ActivityUtils.hideSoftKeyboardFrom(mContext, Objects.requireNonNull(getView()));

        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<String> cartelasGanhadoras =
                    bundle.getStringArrayList(Constant.EXTRA_CARTELAS_GANHADORAS);

            assert cartelasGanhadoras != null;
            if (!cartelasGanhadoras.get(0).equalsIgnoreCase(
                    getString(R.string.list_item_confere_outra_cartela))) {
                cartelasGanhadoras.add(0, getString(R.string.list_item_confere_outra_cartela));
            }

            Objects.requireNonNull(getActivity())
                    .setTitle(getString(R.string.cartelas_ganhadoras_title) + " - " +
                            (cartelasGanhadoras.size() - 1) + " Cartelas");

            CartelasGanhadorasAdapter adapter =
                    new CartelasGanhadorasAdapter(getActivity(),
                            mVisualizaCartelasFragment,
                            getArguments().getParcelableArrayList(Constant.EXTRA_PEDRAS));
            adapter.submitList(cartelasGanhadoras);
            rvCartelasGanhadoras.setAdapter(adapter);
        }
    }

}

class CartelasGanhadorasAdapter extends ListAdapter<String, CartelasGanhadorasAdapter.ViewHolder> {
    private FragmentActivity mFragmentActivity;
    private Fragment mFragment;
    private ArrayList<Pedra> mPedras;

    private class VIEW_TYPES {
        static final int Header = 1;
        static final int Normal = 2;
    }

    CartelasGanhadorasAdapter(FragmentActivity fragmentActivity, Fragment fragment, ArrayList<Pedra> pedras) {
        super(DIFF_CALLBACK);
        mFragmentActivity = fragmentActivity;
        mFragment = fragment;
        mPedras = pedras;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPES.Header;
        else return VIEW_TYPES.Normal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cartelas_ganhadoras, parent, false);
        if (viewType == VIEW_TYPES.Header) return new ViewHolder(view, true);
        else return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(getItem(position));
    }

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(String oldItem, String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(String oldItem, String newItem) {
                    return oldItem.equalsIgnoreCase(newItem);
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;

        ViewHolder(View v) {
            this(v, false);
        }

        ViewHolder(View v, boolean header) {
            super(v);
            mTextView = v.findViewById(R.id.tv_cartela_numero);
            if (header) mTextView.setTypeface(null, Typeface.BOLD);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();

            String textoCartela = mTextView.getText().toString();
            if (textoCartela.equalsIgnoreCase(
                    mFragmentActivity.getString(R.string.list_item_confere_outra_cartela))) {
                textoCartela = "";
            }
            bundle.putString(Constant.EXTRA_ULTIMA_CARTELA, textoCartela);

            bundle.putParcelableArrayList(Constant.EXTRA_PEDRAS, mPedras);
            mFragment.setArguments(bundle);

            mFragmentActivity.setTitle(R.string.conferir_cartelas_title);

            ActivityUtils.addFragmentToActivity(
                    mFragmentActivity.getSupportFragmentManager(),
                    mFragment,
                    R.id.conteudoFrame,
                    true
            );
        }
    }
}
