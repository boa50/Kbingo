/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    PedrasSorteadasFragment.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package br.com.boa50.kbingo.realizasorteio;

import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.entity.Pedra;
import br.com.boa50.kbingo.util.ActivityUtils;
import br.com.boa50.kbingo.util.PedraUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.boa50.kbingo.Constant.QTDE_PEDRAS_LETRA;

public class PedrasSorteadasFragment extends Fragment {
    private static final String ARGS_PEDRAS = "pedras";
    private static final String ARGS_GRID_COLUNAS = "gridColunas";
    private static final String ARGS_LETRA_POSITION = "letraPosition";

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
