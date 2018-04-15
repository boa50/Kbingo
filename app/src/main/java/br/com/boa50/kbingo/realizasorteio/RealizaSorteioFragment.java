package br.com.boa50.kbingo.realizasorteio;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.GridSpacesItemDecoration;
import br.com.boa50.kbingo.util.PedraUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RealizaSorteioFragment extends DaggerFragment implements RealizaSorteioContract.View {

    final int QTDE_PEDRAS_LINHA_LANDSCAPE = 8;
    final int QTDE_PEDRAS_LINHA_PORTRAIT = 5;

    private int gridColunas;

    private int scrollPosition = 0;

    @Inject
    RealizaSorteioContract.Presenter mPresenter;

    @BindView(R.id.bt_sortear_pedra) Button btSortearPedra;

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridColunas = QTDE_PEDRAS_LINHA_LANDSCAPE;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridColunas = QTDE_PEDRAS_LINHA_PORTRAIT;
        }
    }

    @OnClick(R.id.bt_sortear_pedra)
    void sortearPedra() {
        mPresenter.sortearPedra();
    }

    @OnClick(R.id.bt_novo_sorteio)
    void resetarPedras() {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto)
        );
        mPresenter.resetarPedras();
    }

    @Override
    public void apresentarPedra(Pedra pedra) {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto_sorteada)
        );
        btSortearPedra.setText(pedra.getValorPedra());
    }

    @Override
    public void apresentarFimSorteio() {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto)
        );
        btSortearPedra.setText(getResources().getText(R.string.sorteio_fim));
    }


    @Override
    public void iniciarPedras(List<Pedra> pedras) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), gridColunas);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (pedras.get(position).ismHeader())
                    return gridColunas;
                else
                    return 1;
            }
        });
        gridLayoutManager.setAutoMeasureEnabled(false);
        rvListaPedras.setLayoutManager(gridLayoutManager);
        rvListaPedras.setHasFixedSize(true);

        if ((rvListaPedras.getAdapter() == null) && (pedras != null)) {
            rvListaPedras.addItemDecoration(
                    new GridSpacesItemDecoration(
                            pedras.size(),
                            PedraUtils.getHeadersPositions(pedras)
                    )
            );
            rvListaPedras.setAdapter(new ApresentarPedrasAdapter(getContext(), pedras));
        }
    }

    @Override
    public void atualizarPedra(int position) {
        controlarScroll(position);
        rvListaPedras.getAdapter().notifyItemChanged(position);
    }

    @Override
    public void reiniciarSorteio() {
        btSortearPedra.setText(getResources().getText(R.string.bt_sortear_pedra));
        controlarScroll(0);
        rvListaPedras.getAdapter().notifyDataSetChanged();
    }

    private void controlarScroll(int position){
        if (position > 64) {
            if (position > scrollPosition)
                rvListaPedras.smoothScrollToPosition(80);
            else
                rvListaPedras.smoothScrollToPosition(64);
        } else if (position > 48) {
            if (position > scrollPosition)
                rvListaPedras.smoothScrollToPosition(63);
            else
                rvListaPedras.smoothScrollToPosition(48);
        } else if (position > 32) {
            if (position > scrollPosition)
                rvListaPedras.smoothScrollToPosition(47);
            else
                rvListaPedras.smoothScrollToPosition(32);
        } else if (position > 16) {
            if (position > scrollPosition)
                rvListaPedras.smoothScrollToPosition(31);
            else
                rvListaPedras.smoothScrollToPosition(16);
        } else {
            if (position > scrollPosition)
                rvListaPedras.smoothScrollToPosition(15);
            else
                rvListaPedras.smoothScrollToPosition(0);
        }
        scrollPosition = position;
    }

    static class ApresentarPedrasAdapter extends RecyclerView.Adapter<ApresentarPedrasAdapter.ViewHolder> {
        private List<Pedra> mPedras;
        private Context mContext;

        ApresentarPedrasAdapter(Context context, List<Pedra> pedras) {
            mContext = context;
            mPedras = pedras;
        }

        @Override
        public ApresentarPedrasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.apresentarpedrasadapter_item, parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ApresentarPedrasAdapter.ViewHolder holder, int position) {
            holder.bindData(mPedras.get(position));

            LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) holder.mTextView.getLayoutParams();

            if (position == 0)
                params.topMargin = 0;

            holder.mTextView.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return mPedras.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView) TextView mTextView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            void bindData(Pedra pedra){
                LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) mTextView.getLayoutParams();
                int dimenTextSize;
                int typeface;
                int dimenHeight;
                int dimenTopMargin;

                if (pedra.ismHeader()) {
                    mTextView.setText(pedra.getmLetra());
                    mTextView.setBackgroundResource(R.color.headerBackground);
                    mTextView.setTextColor(mContext.getResources().getColor(R.color.headerTexto));

                    dimenTextSize = R.dimen.header_texto;
                    typeface = Typeface.BOLD;

                    dimenHeight = R.dimen.header_hight;
                    dimenTopMargin = R.dimen.header_margin_top;

                    params.width = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
                } else {
                    mTextView.setText(pedra.getmNumero());
                    mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.pedra_pequena_text));

                    ContextThemeWrapper wrapper;
                    if (pedra.ismSorteada())
                        wrapper = new ContextThemeWrapper(mContext, R.style.Pedra);
                    else
                        wrapper = new ContextThemeWrapper(mContext, R.style.PedraDisabled);
                    Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.pedra, wrapper.getTheme());
                    mTextView.setBackground(drawable);

                    dimenTextSize = R.dimen.pedra_pequena_texto;
                    typeface = Typeface.NORMAL;

                    dimenHeight = R.dimen.pedra_pequena_lado;
                    dimenTopMargin = R.dimen.pedra_pequena_margin_top;

                    params.width = mContext.getResources().getDimensionPixelSize(R.dimen.pedra_pequena_lado);
                }

                mTextView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        mContext.getResources().getDimension(dimenTextSize)
                );
                mTextView.setTypeface(null, typeface);
                mTextView.setEnabled(pedra.ismSorteada());

                params.height = mContext.getResources().getDimensionPixelSize(dimenHeight);
                params.topMargin = mContext.getResources().getDimensionPixelSize(dimenTopMargin);

                mTextView.setLayoutParams(params);
            }
        }
    }
}
