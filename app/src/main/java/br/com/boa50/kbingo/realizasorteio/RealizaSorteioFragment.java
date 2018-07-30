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
import java.util.Objects;

import javax.inject.Inject;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.conferecartelas.ConfereCartelasActivity;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.di.ActivityScoped;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.util.PedraUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;

@ActivityScoped
public class RealizaSorteioFragment extends DaggerFragment implements RealizaSorteioContract.View {
    private static final String ARGS_PEDRAS = "pedras";
    private static final String ARGS_PEDRA_ULTIMA = "ultimaPedra";
    private static final String ARGS_TAB_LETRAS_SELECIONADA = "tabLetrasSelecionada";
    private static final String ARGS_GRID_COLUNAS = "gridColunas";
    private static final String ARGS_LETRA_POSITION = "letraPosition";
    private static final String ARGS_DIALOG_NOVO_SORTEIO = "dialogNovoSorteio";

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
    private ArrayList<Pedra> mPedras;
    private List<Letra> mLetras;
    private String mUltimaPedraValor;
    private long mLastClickTime;
    private Dialog mDialogNovoSorteio;
    private int mTabLetrasSelecionada;

    @Inject
    public RealizaSorteioFragment() {}

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realizasorteio_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        mLastClickTime = 0;
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mPedras = savedInstanceState.getParcelableArrayList(ARGS_PEDRAS);
            mUltimaPedraValor = savedInstanceState.getString(ARGS_PEDRA_ULTIMA);
            if (savedInstanceState.getBoolean(ARGS_DIALOG_NOVO_SORTEIO)) abrirDialogResetarPedras();
            mTabLetrasSelecionada = savedInstanceState.getInt(ARGS_TAB_LETRAS_SELECIONADA);
        } else {
            mUltimaPedraValor = "";
            mTabLetrasSelecionada = 0;
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.item_novo_sorteio).setVisible(true);
        menu.findItem(R.id.item_confere_cartelas).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_novo_sorteio:
                abrirDialogResetarPedras();
                return true;
            case R.id.item_confere_cartelas:
                Intent intent = new Intent(getActivity(), ConfereCartelasActivity.class);
                intent.putExtra("mPedras", mPedras);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirDialogResetarPedras() {
        if (mDialogNovoSorteio == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle(R.string.dialog_novo_sorteio_title)
                    .setPositiveButton(R.string.dialog_novo_sorteio_positive,
                            (dialog, which) -> resetarPedras())
                    .setNegativeButton(R.string.dialog_negative,
                            (dialog, which) -> {});

            mDialogNovoSorteio = builder.create();
        }

        mDialogNovoSorteio.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARGS_PEDRAS, mPedras);
        outState.putString(ARGS_PEDRA_ULTIMA, mUltimaPedraValor);
        outState.putBoolean(ARGS_DIALOG_NOVO_SORTEIO,
                mDialogNovoSorteio != null && mDialogNovoSorteio.isShowing());
        outState.putInt(ARGS_TAB_LETRAS_SELECIONADA, vpPedrasSorteadas.getCurrentItem());
    }

    @Override
    public void onPause() {
        super.onPause();
        mTabLetrasSelecionada = vpPedrasSorteadas.getCurrentItem();

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unsubscribe();
        mPedras = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridColunas = Constant.QTDE_PEDRAS_LINHA_PORTRAIT;
        } else {
            mGridColunas = Constant.QTDE_PEDRAS_LINHA_LANDSCAPE;
        }

        if (!mUltimaPedraValor.isEmpty()) {
            if (mUltimaPedraValor == getResources().getText(R.string.sorteio_fim))
                apresentarFimSorteio();
            else
                apresentarPedra(mUltimaPedraValor);
        }

        mPresenter.setPedras(mPedras);
        mPresenter.subscribe(this);
    }

    @OnClick(R.id.bt_sortear_pedra)
    void sortearPedra() {
        if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
            mPresenter.sortearPedra();
            mLastClickTime = SystemClock.elapsedRealtime();
        }
    }

    void resetarPedras() {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto)
        );

        mPresenter.resetarPedras();
        mUltimaPedraValor = "";
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
        mUltimaPedraValor = pedraValor;
    }

    @Override
    public void apresentarFimSorteio() {
        btSortearPedra.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.pedra_grande_texto)
        );
        btSortearPedra.setText(getResources().getText(R.string.sorteio_fim));
        mUltimaPedraValor = btSortearPedra.getText().toString();
    }

    @Override
    public void iniciarLayout(List<Letra> letras, ArrayList<Pedra> pedras) {
        mLetras = letras;
        mPedras = pedras;

        if (mPageAdapter == null) {
            mPageAdapter = new PedrasSorteadasPageAdapter(
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        }

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
        fragment.transitarTextViewPedra(mPedras.get(position).getId());
    }

    @Override
    public void reiniciarSorteio() {
        btSortearPedra.setText(getResources().getText(R.string.bt_sortear_pedra));

        for (int i = 0; i < mPageAdapter.getCount(); i++) {
            ((PedrasSorteadasFragment) mPageAdapter.getFragment(i)).reinicializarPedras();
        }
        mPageAdapter.notifyDataSetChanged();
        vpPedrasSorteadas.setCurrentItem(0);
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
            return PedrasSorteadasFragment.newInstance(mGridColunas, position, mPedras);
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
