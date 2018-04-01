package br.com.boa50.kbingo.realizasorteio;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RealizaSorteioFragment extends DaggerFragment implements RealizaSorteioContract.View {

    private final int GRID_COLUNAS = 15;

    @Inject
    RealizaSorteioContract.Presenter mPresenter;

    @BindView(R.id.bt_sortear_pedra) Button btSortearPedra;

    @BindView(R.id.tv_pedra_sorteada) TextView tvPedraSorteada;

    @BindView(R.id.rv_lista_pedras) RecyclerView rvListaPedras;

    @BindView(R.id.bt_novo_sorteio) Button btNovoSorteio;

    private Unbinder unbinder;

    @Inject
    public RealizaSorteioFragment() {}

    public static RealizaSorteioFragment newInstance() {
        return new RealizaSorteioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realizasorteio_frag, container, false);
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

    @OnClick(R.id.bt_sortear_pedra)
    void sortearPedra() {
        mPresenter.sortearPedra();
    }

    @OnClick(R.id.bt_novo_sorteio)
    void resetarPedras() {
        mPresenter.resetarPedras();
    }

    @Override
    public void apresentarPedra(Pedra pedra) {
        tvPedraSorteada.setText(pedra.getValorPedra());
    }

    @Override
    public void apresentarFimSorteio() {
        Toast.makeText(getActivity(),"Sorteio Finalizado",Toast.LENGTH_LONG).show();
    }

    @Override
    public void apresentarPedras(List<Pedra> pedras) {
        if (rvListaPedras.getAdapter() == null) {
            rvListaPedras.setLayoutManager(new GridLayoutManager(getActivity(), GRID_COLUNAS));
            rvListaPedras.setAdapter(new ApresentarPedrasAdapter(getContext(),pedras));
        } else {
            rvListaPedras.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void reiniciarSorteio() {
        tvPedraSorteada.setText("K99");

        rvListaPedras.getAdapter().notifyDataSetChanged();
    }

    private static class ApresentarPedrasAdapter extends RecyclerView.Adapter<ApresentarPedrasAdapter.ViewHolder> {
        private List<Pedra> mPedras;
        private Context mContext;

        ApresentarPedrasAdapter(Context context, List<Pedra> pedras) {
            mContext = context;
            mPedras = pedras;
        }

        @Override
        public ApresentarPedrasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.apresentarpedrasadapter_item,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ApresentarPedrasAdapter.ViewHolder holder, int position) {
            holder.mTextView.setText(mPedras.get(position).getValorPedra());
            if (mPedras.get(position).ismSorteada())
                holder.mTextView.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        }

        @Override
        public int getItemCount() {
            return mPedras.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            ViewHolder(View view) {
                super(view);
                mTextView = view.findViewById(R.id.textView);
            }
        }
    }
}
