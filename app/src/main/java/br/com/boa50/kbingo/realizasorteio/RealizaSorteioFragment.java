package br.com.boa50.kbingo.realizasorteio;

import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.data.dto.TipoSorteioDTO;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.util.CartelaUtils;
import br.com.boa50.kbingo.util.PedraUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;
import static br.com.boa50.kbingo.util.StateUtils.readStateFromBundle;
import static br.com.boa50.kbingo.util.StateUtils.writeStateToBundle;

@ActivityScoped
public class RealizaSorteioFragment extends DaggerFragment implements RealizaSorteioContract.View {
    private static final String ARGS_TAB_LETRAS_SELECIONADA = "tabLetrasSelecionada";
    private static final String ARGS_GRID_COLUNAS = "gridColunas";
    private static final String ARGS_LETRA_POSITION = "letraPosition";
    private static final String ARGS_DIALOG_NOVO_SORTEIO = "dialogNovoSorteio";
    private static final String ARGS_DIALOG_TIPO_SORTEIO = "dialogTipoSorteio";
    private static final String ARGS_TIPO_SORTEIO_ALTERADO = "tipoSorteioAlterado";
    private static final String ARGS_TIPO_SORTEIO_SETADO = "tipoSorteioSetado";

    @Inject
    RealizaSorteioContract.Presenter mPresenter;

    @BindView(R.id.bt_sortear_pedra)
    Button btSortearPedra;

    @BindView(R.id.vp_pedras_sorteadas)
    ViewPager vpPedrasSorteadas;

    @BindView(R.id.tl_pedras_sorteadas)
    TabLayout tlPedrasSorteadas;

    private Unbinder unbinder;
    private int mGridColunas;
    PedrasSorteadasPageAdapter mPageAdapter;
    private List<Letra> mLetras;
    private long mLastClickTime;
    private Dialog mDialogNovoSorteio;
    private Dialog mDialogTipoSorteio;
    private int mTabLetrasSelecionada;
    private boolean mTipoSorteioSetado;
    private int mTipoSorteioAlterado;
    private RealizaSorteioContract.State mState;
    private TextView mTextoCartelasBadge;

    @Inject
    public RealizaSorteioFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realizasorteio_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        mLastClickTime = 0;
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.item_novo_sorteio).setVisible(true);
        menu.findItem(R.id.item_alterar_tipo_sorteio).setVisible(true);

        MenuItem itemConfereCartelas = menu.findItem(R.id.item_confere_cartelas);
        itemConfereCartelas.setVisible(true);
        View actionView = itemConfereCartelas.getActionView();
        mTextoCartelasBadge = actionView.findViewById(R.id.item_confere_cartelas_badge);
        actionView.setOnClickListener(v -> onOptionsItemSelected(itemConfereCartelas));
        atualizarCartelasGanhadorasBadge();

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_novo_sorteio:
                abrirDialogNovoSorteio();
                return true;
            case R.id.item_alterar_tipo_sorteio:
                abrirDialogTipoSorteio();
                return true;
            case R.id.item_confere_cartelas:
                Intent intent = new Intent(getActivity(), ConfereCartelasActivity.class);
                intent.putExtra(Constant.EXTRA_PEDRAS, mPresenter.getState().getPedras());
                intent.putExtra(Constant.EXTRA_CARTELAS_GANHADORAS,
                        CartelaUtils.getCartelasGanhadoras(mPresenter.getState().getCartelas()));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirDialogNovoSorteio() {
        abrirDialogNovoSorteio(-1);
    }

    private void abrirDialogNovoSorteio(int tipoSorteioAlterado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_novo_sorteio_title)
                .setNegativeButton(R.string.dialog_negative,
                        (dialog, which) -> mTipoSorteioAlterado = -1);

        if (tipoSorteioAlterado >= 0) {
            mTipoSorteioAlterado = tipoSorteioAlterado;
            builder.setMessage(R.string.dialog_novo_sorteio_tipo_sorteio_message)
                    .setPositiveButton(R.string.dialog_novo_sorteio_positive,
                            (dialog, which) -> {
                        mPresenter.resetarPedras();
                        mPresenter.alterarTipoSorteio(tipoSorteioAlterado);
                        mTipoSorteioAlterado = -1;
                    });
        } else {
            builder.setPositiveButton(R.string.dialog_novo_sorteio_positive,
                            (dialog, which) -> mPresenter.resetarPedras());
        }

        mDialogNovoSorteio = builder.create();
        mDialogNovoSorteio.setCanceledOnTouchOutside(false);
        mDialogNovoSorteio.show();
    }

    private void abrirDialogTipoSorteio() {
        final int[] sorteioSelecionado = {mPresenter.getTipoSorteioId()};
        boolean hasPedraSorteada = mPresenter.hasPedraSorteada();

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_tipo_sorteio_title)
                .setSingleChoiceItems(TipoSorteioDTO.getTiposSorteioNome(), sorteioSelecionado[0],
                        (dialog, which) -> sorteioSelecionado[0] = which)
                .setPositiveButton(R.string.dialog_confirmative,
                        (dialog, which) -> {
                    if (hasPedraSorteada &&
                            mPresenter.getTipoSorteioId() != sorteioSelecionado[0]) {
                        abrirDialogNovoSorteio(sorteioSelecionado[0]);
                    } else {
                        mPresenter.alterarTipoSorteio(sorteioSelecionado[0]);
                    }
                })
                .setNegativeButton(R.string.dialog_negative,
                        (dialog, which) -> apresentarTipoSorteio(false));

        mDialogTipoSorteio = builder.create();
        mDialogTipoSorteio.setCanceledOnTouchOutside(false);
        mDialogTipoSorteio.show();
    }

    @Override
    public void apresentarTipoSorteio(boolean tipoAlterado) {
        if (!mTipoSorteioSetado) mTipoSorteioSetado = true;

        Objects.requireNonNull(getActivity())
                .setTitle(getString(R.string.realizar_sorteio_title) + " - " + mPresenter.getTipoSorteio().getNome());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARGS_DIALOG_NOVO_SORTEIO,
                mDialogNovoSorteio != null && mDialogNovoSorteio.isShowing());
        outState.putBoolean(ARGS_DIALOG_TIPO_SORTEIO,
                mDialogTipoSorteio != null && mDialogTipoSorteio.isShowing());
        outState.putInt(ARGS_TIPO_SORTEIO_ALTERADO, mTipoSorteioAlterado);
        outState.putBoolean(ARGS_TIPO_SORTEIO_SETADO, mTipoSorteioSetado);
        outState.putInt(ARGS_TAB_LETRAS_SELECIONADA, vpPedrasSorteadas.getCurrentItem());
        writeStateToBundle(outState, mPresenter.getState());
    }

    @Override
    public void onPause() {
        super.onPause();
        mTabLetrasSelecionada = vpPedrasSorteadas.getCurrentItem();
        mState = mPresenter.getState();

        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : manager.getFragments()) {
            if (fragment.getTag() != null) {
                transaction.remove(fragment);
            }
        }
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridColunas = Constant.QTDE_PEDRAS_LINHA_PORTRAIT;
        } else {
            mGridColunas = Constant.QTDE_PEDRAS_LINHA_LANDSCAPE;
        }

        mPresenter.subscribe(this, mState);
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

        if (savedInstanceState != null) {
            mState = readStateFromBundle(savedInstanceState);
            if (savedInstanceState.getBoolean(ARGS_DIALOG_NOVO_SORTEIO)) {
                abrirDialogNovoSorteio(savedInstanceState.getInt(ARGS_TIPO_SORTEIO_ALTERADO));
            }
            mTabLetrasSelecionada = savedInstanceState.getInt(ARGS_TAB_LETRAS_SELECIONADA);
            if (savedInstanceState.getBoolean(ARGS_DIALOG_TIPO_SORTEIO)) abrirDialogTipoSorteio();
            mTipoSorteioSetado = savedInstanceState.getBoolean(ARGS_TIPO_SORTEIO_SETADO);
        } else {
            mTipoSorteioAlterado = -1;
        }

        if (!mTipoSorteioSetado)  abrirDialogTipoSorteio();
        else apresentarTipoSorteio(false);
    }

    @OnClick(R.id.bt_sortear_pedra)
    void sortearPedra() {
        if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
            mPresenter.sortearPedra();
            mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    @Override
    public void apresentarPedra(Pedra pedra) {
        apresentarPedra(mLetras.get(pedra.getLetraId() - 1).getNome() + pedra.getNumero());
    }

    private void apresentarPedra(String pedraValor) {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto_sorteada)
        );
        btSortearPedra.setText(pedraValor);
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
    public void iniciarLayout(List<Letra> letras) {
        mLetras = letras;

        mPageAdapter = new PedrasSorteadasPageAdapter(
                Objects.requireNonNull(getActivity()).getSupportFragmentManager());

        vpPedrasSorteadas.setAdapter(mPageAdapter);
        vpPedrasSorteadas.setOffscreenPageLimit(4);
        vpPedrasSorteadas.setCurrentItem(mTabLetrasSelecionada);
        tlPedrasSorteadas.setupWithViewPager(vpPedrasSorteadas);
    }

    @Override
    public void atualizarPedra(int position) {
        vpPedrasSorteadas.setCurrentItem(position/QTDE_PEDRAS_LETRA);

        PedrasSorteadasFragment fragment =
                (PedrasSorteadasFragment) mPageAdapter.getFragment(position/QTDE_PEDRAS_LETRA);
        fragment.transitarTextViewPedra(mPresenter.getState().getPedras().get(position).getId());

    }

    @Override
    public void reiniciarSorteio() {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto)
        );
        btSortearPedra.setText(getResources().getText(R.string.bt_sortear_pedra));

        for (int i = 0; i < mPageAdapter.getCount(); i++) {
            ((PedrasSorteadasFragment) mPageAdapter.getFragment(i)).reinicializarPedras();
        }
        mPageAdapter.notifyDataSetChanged();
        vpPedrasSorteadas.setCurrentItem(0);
    }

    @Override
    public void atualizarCartelasGanhadorasBadge() {
        int qtdCartelasGanhadora = mPresenter.getState().getQtdCartelasGanhadoras();

        if (qtdCartelasGanhadora > 0) {
            if (qtdCartelasGanhadora <= 9) {
                mTextoCartelasBadge.setText(String.format(
                        Locale.ENGLISH,
                        Constant.FORMAT_PEDRA,
                        qtdCartelasGanhadora));
            } else {
                mTextoCartelasBadge.setText(R.string.item_confere_cartelas_badge_texto_plus);
            }
            mTextoCartelasBadge.setVisibility(View.VISIBLE);
        } else {
            mTextoCartelasBadge.setVisibility(View.GONE);
        }
    }

    public class PedrasSorteadasPageAdapter extends FragmentPagerAdapter {

        private SparseArray<String> mSparseFragment;
        private FragmentManager mFragmentManager;

        PedrasSorteadasPageAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mSparseFragment = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position) {
            return PedrasSorteadasFragment.newInstance(mGridColunas, position, mPresenter.getState().getPedras());
        }

        @Override
        public int getCount() {
            return mLetras.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mLetras.get(position).getNome();
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                mSparseFragment.append(position, ((Fragment) object).getTag());
            }
            return object;
        }

        Fragment getFragment(int position) {
            String tag = mSparseFragment.get(position);
            if (tag != null) {
                return mFragmentManager.findFragmentByTag(tag);
            }
            return null;
        }
    }

    public static class PedrasSorteadasFragment extends Fragment {
        private static final String ARGS_PEDRAS = "pedras";

        @BindView(R.id.gl_pedras_sorteadas)
        GridLayout glPedrasSorteadas;

        private Context mContext;
        private int mLetraPosition;
        private ArrayList<Pedra> mPedras;
        private Drawable mPedraDisabled;
        private Drawable mPedraEnabled;
        private int mPedrasTamanhoPx;
        private int mPedrasMarginPx;

        static PedrasSorteadasFragment newInstance(
                int gridColunas, int letraPosition, ArrayList<Pedra> pedras) {
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
        public View onCreateView(
                @NonNull LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.pedrassorteadas_frag, container, false);
            ButterKnife.bind(this, view);

            Bundle args = getArguments();
            if (args != null) {
                mLetraPosition = args.getInt(ARGS_LETRA_POSITION);
                mPedras = args.getParcelableArrayList(ARGS_PEDRAS);
                glPedrasSorteadas.setColumnCount(args.getInt(ARGS_GRID_COLUNAS));
                mContext = view.getContext();

                inicializarPedras();
            }

            return view;
        }

        public void inicializarPedras() {
            glPedrasSorteadas.removeAllViews();

            mPedraDisabled = PedraUtils.getPedraDrawable(mContext, false);
            mPedraEnabled = PedraUtils.getPedraDrawable(mContext, true);

            mPedrasMarginPx = Objects.requireNonNull(getContext())
                    .getResources().getDimensionPixelSize(R.dimen.pedra_pequena_margin);
            int ladoLimitantePixels;
            int quantidadeLimitante;
            int actionBarHeight = 0;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            TypedValue tv = new TypedValue();
            if (Objects.requireNonNull(getActivity()).getTheme()
                    .resolveAttribute(R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue
                        .complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                ladoLimitantePixels = displayMetrics.widthPixels;
                quantidadeLimitante = glPedrasSorteadas.getColumnCount();
            } else {
                ladoLimitantePixels = displayMetrics.heightPixels
                        - actionBarHeight
                        - getContext().getResources().getDimensionPixelSize(R.dimen.pedras_tab_height)
                        - ActivityUtils.getStatusBarHeight(getContext().getResources());
                quantidadeLimitante =
                        (int) Math.ceil(QTDE_PEDRAS_LETRA/(float) glPedrasSorteadas.getColumnCount());
            }

            mPedrasTamanhoPx = PedraUtils.getPedraPequenaLadoInPixels(ladoLimitantePixels,
                    quantidadeLimitante, mPedrasMarginPx);


            for (int i = 0; i < QTDE_PEDRAS_LETRA; i++) {
                TextView textView = new TextView(mContext);
                glPedrasSorteadas.addView(textView);
                estilizarTextViewPedra(textView, i);
            }
        }

        public void reinicializarPedras() {
            for (int i = 0; i < glPedrasSorteadas.getChildCount(); i++) {
                TextView tv = (TextView) glPedrasSorteadas.getChildAt(i);
                tv.setBackground(mPedraDisabled);
                tv.setTextColor(mContext.getResources()
                        .getColorStateList(R.color.pedra_pequena_text));
                tv.setEnabled(false);
            }
        }

        private void estilizarTextViewPedra(TextView textView, int position) {
            Resources resources = mContext.getResources();
            Pedra pedra = mPedras.get(position + (mLetraPosition * QTDE_PEDRAS_LETRA));

            GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
            params.width = mPedrasTamanhoPx;
            params.height = mPedrasTamanhoPx;

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                params.setMargins(
                        mPedrasMarginPx,
                        mPedrasMarginPx,
                        mPedrasMarginPx,
                        mPedrasMarginPx);
            else
                params.setMargins(
                        mPedrasMarginPx + mPedrasMarginPx/2,
                        mPedrasMarginPx,
                        mPedrasMarginPx + mPedrasMarginPx/2,
                        mPedrasMarginPx);

            textView.setLayoutParams(params);
            textView.setId(pedra.getId());
            textView.setText(pedra.getNumero());
            textView.setGravity(Gravity.CENTER);

            textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.pedra_pequena_texto)
            );

            textView.setTextColor(resources.getColorStateList(R.color.pedra_pequena_text));

            if (pedra.isSorteada()) {
                textView.setBackground(mPedraEnabled);
                textView.setEnabled(true);
            } else {
                textView.setBackground(mPedraDisabled);
                textView.setEnabled(false);
            }
        }

        public void transitarTextViewPedra(int id) {
            TextView textView =
                    Objects.requireNonNull(this.getView()).findViewById(id);

            AnimatedVectorDrawableCompat drawableAnimated = AnimatedVectorDrawableCompat.create(
                    mContext,
                    R.drawable.pedrapequena_anim);
            textView.setBackground(drawableAnimated);

            if (Build.VERSION.SDK_INT >= 21)
                textView.setStateListAnimator(
                        AnimatorInflater.loadStateListAnimator(
                                mContext,
                                R.animator.pedrapequenatext_animator));

            ((Animatable) textView.getBackground()).start();
            textView.setEnabled(true);
        }
    }
}
