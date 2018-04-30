package br.com.boa50.kbingo.realizasorteio;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.Letra;
import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RealizaSorteioFragment extends DaggerFragment implements RealizaSorteioContract.View {
    private static final String ARGS_PEDRAS = "pedras";
    private static final String ARGS_PEDRA_ULTIMA = "ultimaPedra";
    private static final String ARGS_GRID_COLUNAS = "gridColunas";
    private static final String ARGS_LETRA_POSITION = "letraPosition";
//    private static final String STATE_SCROLL_ULTIMO = "ultimoScroll";

    final int QTDE_PEDRAS_LINHA_LANDSCAPE = 6;
    final int QTDE_PEDRAS_LINHA_PORTRAIT = 5;

    @Inject
    RealizaSorteioContract.Presenter mPresenter;

    @BindView(R.id.bt_sortear_pedra)
    Button btSortearPedra;

    @BindView(R.id.vp_pedras_sorteadas)
    ViewPager vpPedrasSorteadas;

    @BindView(R.id.tl_pedras_sorteadas)
    TabLayout tlPedrasSorteadas;

//    @BindView(R.id.rv_lista_pedras) RecyclerView rvListaPedras;

    @BindView(R.id.bt_novo_sorteio)
    Button btNovoSorteio;

//    @BindView(R.id.ll_botoes_lista_pedras) LinearLayout llBotoesListaPedras;

    private Unbinder unbinder;
    private int mGridColunas;
    PedrasSorteadasPageAdapter mPageAdapter;
//    private int mScrollPosition = 0;
//    private int mScrollChangeOrientation;
    private ArrayList<Pedra> mPedras;
    private List<Letra> mLetras;
    private String mUltimaPedraValor;
//    private ArrayList<Integer> mButtonIds;

    @Inject
    public RealizaSorteioFragment() {}

    public static RealizaSorteioFragment newInstance() {
        return new RealizaSorteioFragment();
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realizasorteio_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mPedras = savedInstanceState.getParcelableArrayList(ARGS_PEDRAS);
            mUltimaPedraValor = savedInstanceState.getString(ARGS_PEDRA_ULTIMA);
//            mScrollChangeOrientation = savedInstanceState.getInt(STATE_SCROLL_ULTIMO);
        } else {
            mUltimaPedraValor = "";
//            mScrollPosition = 0;
//            mScrollChangeOrientation = -1;
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARGS_PEDRAS, mPedras);
        outState.putString(ARGS_PEDRA_ULTIMA, mUltimaPedraValor);
//        outState.putInt(STATE_SCROLL_ULTIMO, ((GridLayoutManager)rvListaPedras.getLayoutManager()).findLastVisibleItemPosition());
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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridColunas = QTDE_PEDRAS_LINHA_LANDSCAPE;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridColunas = QTDE_PEDRAS_LINHA_PORTRAIT;
        }

        if (!mUltimaPedraValor.isEmpty())
            apresentarPedra(mUltimaPedraValor);

        mPresenter.setPedras(mPedras);
        mPresenter.subscribe(this);
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
        apresentarPedra(pedra.getValorPedra());
    }

    private void apresentarPedra(String pedraValor) {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto_sorteada)
        );
        btSortearPedra.setText(pedraValor);
        mUltimaPedraValor = pedraValor;
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
    public void iniciarPedras(ArrayList<Pedra> pedras) {
        mPedras = pedras;
//        List<Integer> headerPositions = PedraUtils.getHeadersPositions(mPedras);
//        List<String> headerLetras = new ArrayList<>();
//
//        for (int i : headerPositions) {
//            headerLetras.add(mPedras.get(i).getmLetra());
//        }
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), mGridColunas);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (mPedras.get(position).ismHeader()) {
//                    return mGridColunas;
//                } else
//                    return 1;
//            }
//        });
//        gridLayoutManager.setAutoMeasureEnabled(false);

//        rvListaPedras.setLayoutManager(gridLayoutManager);
//        rvListaPedras.setHasFixedSize(true);
//
//        rvListaPedras.addItemDecoration(
//                new GridSpacesItemDecoration(
//                        pedras.size(),
//                        headerPositions
//                )
//        );
//        rvListaPedras.setAdapter(new ApresentarPedrasAdapter(getContext(), mPedras));
//
//        if (mScrollChangeOrientation > 0)
//            rvListaPedras.scrollToPosition((mScrollChangeOrientation/16)*16);
//
//        rvListaPedras.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                mScrollPosition = ((GridLayoutManager)rvListaPedras.getLayoutManager()).findFirstVisibleItemPosition();
//
//                for (int i = 0; i < mButtonIds.size(); i++) {
//                    Button button = getActivity().findViewById(mButtonIds.get(i));
//                    if (i == mScrollPosition/16) {
//                        button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    } else {
//                        button.setTextColor(getResources().getColor(R.color.textDisabled));
//                    }
//                }
//            }
//        });

//        if (mButtonIds == null || mButtonIds.isEmpty())
//            iniciarBotoesHeader(headerLetras);
    }

    @Override
    public void iniciarLayout(List<Letra> letras) {
        mLetras = letras;

        mPageAdapter = new PedrasSorteadasPageAdapter(getActivity().getSupportFragmentManager());
        vpPedrasSorteadas.setAdapter(mPageAdapter);
        tlPedrasSorteadas.setupWithViewPager(vpPedrasSorteadas);
    }

//    private void iniciarBotoesHeader(List<String> headers) {
//
//        mButtonIds = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Button button = new Button(new ContextThemeWrapper(getContext(), R.style.AppTheme_ButtonToBar));
//            button.setText(headers.get(i));
//
//            if (Build.VERSION.SDK_INT < 17)
//                button.setId(i);
//            else
//                button.setId(View.generateViewId());
//
//            mButtonIds.add(button.getId());
//
//            llBotoesListaPedras.addView(button);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    1.0f
//            );
//            button.setLayoutParams(params);
//
//            final int position = i;
////            button.setOnClickListener((View v) -> controlarScroll(position * 16 + 1, false));
//        }
//    }

    @Override
    public void atualizarPedra(int position) {
        vpPedrasSorteadas.setCurrentItem(position/15);

        PedrasSorteadasFragment fragment =  (PedrasSorteadasFragment) mPageAdapter.getFragment(position/15);
        fragment.transitarTextViewPedra(mPedras.get(position).getmId());
    }

    @Override
    public void reiniciarSorteio() {
        btSortearPedra.setText(getResources().getText(R.string.bt_sortear_pedra));

        for (int i = 0; i < mPageAdapter.getCount(); i++) {
            ((PedrasSorteadasFragment) mPageAdapter.getFragment(i)).inicializarPedras();
        }
        mPageAdapter.notifyDataSetChanged();
        vpPedrasSorteadas.setCurrentItem(0);
    }

    public class PedrasSorteadasPageAdapter extends FragmentPagerAdapter {

        private SparseArray<String> mSparseFragment;
        private FragmentManager mFragmentManager;

        public PedrasSorteadasPageAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mSparseFragment = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position) {
            return PedrasSorteadasFragment.newInstance(mGridColunas, position, mPedras);
        }

        @Override
        public int getCount() {
            return mLetras.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mLetras.get(position).getmNome();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                mSparseFragment.append(position, ((Fragment) object).getTag());
            }
            return object;
        }

        public Fragment getFragment(int position) {
            String tag = mSparseFragment.get(position);
            if (tag != null) {
                return mFragmentManager.findFragmentByTag(tag);
            }
            return null;
        }
    }

    public static class PedrasSorteadasFragment extends Fragment {

        @BindView(R.id.gl_pedras_sorteadas)
        GridLayout glPedrasSorteadas;

        private Context mContext;
        private int mLetraPosition;
        private ArrayList<Pedra> mPedras;

        static PedrasSorteadasFragment newInstance(int gridColunas, int letraPosition, ArrayList<Pedra> pedras) {
            PedrasSorteadasFragment fragment = new PedrasSorteadasFragment();

            Bundle args = new Bundle();
            args.putInt(ARGS_GRID_COLUNAS, gridColunas);
            args.putInt(ARGS_LETRA_POSITION, letraPosition);
            args.putParcelableArrayList(ARGS_PEDRAS, pedras);
            fragment.setArguments(args);

            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.pedrassorteadas_frag, container, false);
            ButterKnife.bind(this, view);

            Bundle args = getArguments();
            mLetraPosition = args.getInt(ARGS_LETRA_POSITION);
            mPedras = args.getParcelableArrayList(ARGS_PEDRAS);
            mContext = view.getContext();

            glPedrasSorteadas.setColumnCount(args.getInt(ARGS_GRID_COLUNAS));

            inicializarPedras();

            return view;
        }

        public void inicializarPedras() {
            glPedrasSorteadas.removeAllViews();

            for (int i = 0; i < 15; i++) {
                TextView textView = new TextView(mContext);
                glPedrasSorteadas.addView(textView);
                estilizarTextViewPedra(textView, i);
            }
        }

        private void estilizarTextViewPedra(TextView textView, int position) {
            Resources resources = mContext.getResources();
            Pedra pedra = mPedras.get(position + (mLetraPosition * 15));

            GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
            params.width = resources.getDimensionPixelSize(R.dimen.pedra_pequena_lado);;
            params.height = resources.getDimensionPixelSize(R.dimen.pedra_pequena_lado);
            int marginPx = resources.getDimensionPixelSize(R.dimen.pedra_pequena_margin);
            params.setMargins(marginPx, marginPx, marginPx, marginPx);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            textView.setLayoutParams(params);

            textView.setId(Integer.parseInt(pedra.getmId()));
            textView.setText(pedra.getmNumero());
            textView.setGravity(Gravity.CENTER);

            textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.pedra_pequena_texto)
            );

            textView.setTextColor(resources.getColorStateList(R.color.pedra_pequena_text));
            textView.setEnabled(false);

            ContextThemeWrapper wrapper = new ContextThemeWrapper(mContext, R.style.PedraDisabled);
            AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(mContext, R.drawable.pedrapequena_anim);
            Objects.requireNonNull(drawable).applyTheme(wrapper.getTheme());
            textView.setBackground(drawable);
        }

        public void transitarTextViewPedra(String id) {
            TextView textView = Objects.requireNonNull(this.getView()).findViewById(Integer.parseInt(id));

            if (Build.VERSION.SDK_INT >= 21)
                textView.setStateListAnimator(AnimatorInflater.loadStateListAnimator(mContext, R.animator.pedrapequenatext_animator));

            ((Animatable) textView.getBackground()).start();
            textView.setEnabled(true);
        }
    }

//    static class ApresentarPedrasAdapter extends RecyclerView.Adapter<ApresentarPedrasAdapter.ViewHolder> {
//        private List<Pedra> mPedras;
//        private Context mContext;
//
//        ApresentarPedrasAdapter(Context context, List<Pedra> pedras) {
//            mContext = context;
//            mPedras = pedras;
//        }
//
//        @Override
//        public ApresentarPedrasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.apresentarpedrasadapter_item, parent,false);
//
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ApresentarPedrasAdapter.ViewHolder holder, int position) {
//            holder.bindData(mPedras.get(position));
//
//            LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) holder.mTextView.getLayoutParams();
//
//            if (position == 0)
//                params.topMargin = 0;
//
//            holder.mTextView.setLayoutParams(params);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mPedras.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            @BindView(R.id.textView) TextView mTextView;
//
//            ViewHolder(View view) {
//                super(view);
//                ButterKnife.bind(this, view);
//            }
//
//            void bindData(Pedra pedra){
//                LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) mTextView.getLayoutParams();
//                int dimenTextSize;
//                int typeface;
//                int dimenHeight;
//                int dimenTopMargin;
//
//                if (pedra.ismHeader()) {
//                    mTextView.setText(pedra.getmLetra());
//                    mTextView.setBackgroundResource(R.color.headerBackground);
//                    mTextView.setTextColor(mContext.getResources().getColor(R.color.headerTexto));
//
//                    dimenTextSize = R.dimen.header_texto;
//                    typeface = Typeface.BOLD;
//
//                    dimenHeight = R.dimen.header_hight;
//                    dimenTopMargin = R.dimen.header_margin_top;
//
//                    params.width = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
//                } else {
//                    mTextView.setText(pedra.getmNumero());
//                    mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.pedra_pequena_text));
//
//                    ContextThemeWrapper wrapper;
//                    if (pedra.ismSorteada())
//                        wrapper = new ContextThemeWrapper(mContext, R.style.Pedra);
//                    else
//                        wrapper = new ContextThemeWrapper(mContext, R.style.PedraDisabled);
//                    Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.pedra, wrapper.getTheme());
//                    mTextView.setBackground(drawable);
//
//                    dimenTextSize = R.dimen.pedra_pequena_texto;
//                    typeface = Typeface.NORMAL;
//
//                    dimenHeight = R.dimen.pedra_pequena_lado;
//                    dimenTopMargin = R.dimen.pedra_pequena_margin_top;
//
//                    params.width = mContext.getResources().getDimensionPixelSize(R.dimen.pedra_pequena_lado);
//                }
//
//                mTextView.setTextSize(
//                        TypedValue.COMPLEX_UNIT_PX,
//                        mContext.getResources().getDimension(dimenTextSize)
//                );
//                mTextView.setTypeface(null, typeface);
//                mTextView.setEnabled(pedra.ismSorteada());
//
//                params.height = mContext.getResources().getDimensionPixelSize(dimenHeight);
//                params.topMargin = mContext.getResources().getDimensionPixelSize(dimenTopMargin);
//
//                mTextView.setLayoutParams(params);
//            }
//        }
//    }
}
