/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    CartelasSorteaveisAdapter.java is part of Kbingo

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
package br.com.boa50.kbingo.sorteiocartela;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.StringUtils;

public class CartelasSorteaveisAdapter extends ListAdapter<Integer, CartelasSorteaveisAdapter.ViewHolder> {
    private Context mContext;

    @Inject
    CartelasSorteaveisAdapter(@NonNull Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cartelas_sorteaveis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItem(position) >= 0) {
            holder.mTextView.setText(StringUtils.formatarNumeroCartela(getItem(position)));
        } else {
            holder.mTextView.setText(mContext.getText(R.string.todas_cartelas_sorteaveis));
        }
    }

    private static final DiffUtil.ItemCallback<Integer> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Integer>() {
                @Override
                public boolean areItemsTheSame(Integer oldItem, @NonNull Integer newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(Integer oldItem, Integer newItem) {
                    return oldItem.intValue() == newItem.intValue();
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_cartela_numero);
        }
    }
}
