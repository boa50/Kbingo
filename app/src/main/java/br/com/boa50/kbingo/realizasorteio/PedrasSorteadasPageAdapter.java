/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    PedrasSorteadasPageAdapter.java is part of Kbingo

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

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.data.entity.Pedra;

public class PedrasSorteadasPageAdapter extends FragmentPagerAdapter {

    private SparseArray<String> mSparseFragment;
    private FragmentManager mFragmentManager;
    private ArrayList<Pedra> mPedras;
    private int mGridColunas;
    private List<Letra> mLetras;

    PedrasSorteadasPageAdapter(FragmentManager fm, ArrayList<Pedra> pedras, int gridColunas,
                               List<Letra> letras) {
        super(fm);
        mFragmentManager = fm;
        mSparseFragment = new SparseArray<>();
        mPedras = pedras;
        mGridColunas = gridColunas;
        mLetras = letras;
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
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