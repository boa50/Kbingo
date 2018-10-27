/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    CartelasFiltroAdapter.java is part of Kbingo

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

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;
import br.com.boa50.kbingo.util.StringUtils;

public class CartelasFiltroAdapter extends ListAdapter<CartelaFiltroDTO, CartelasFiltroAdapter.ViewHolder> {
    private SorteioCartelaContract.Presenter mPresenter;

    @Inject
    CartelasFiltroAdapter(@NonNull SorteioCartelaContract.Presenter presenter) {
        super(DIFF_CALLBACK);
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cartelas_filtro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCheckBox.setText(StringUtils.formatarNumeroCartela(getItem(position).getCartelaId()));
        holder.mCheckBox.setChecked(getItem(position).isSelecionada());
    }

    private static final DiffUtil.ItemCallback<CartelaFiltroDTO> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CartelaFiltroDTO>() {
                @Override
                public boolean areItemsTheSame(CartelaFiltroDTO oldItem, @NonNull CartelaFiltroDTO newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(CartelaFiltroDTO oldItem, CartelaFiltroDTO newItem) {
                    return oldItem.getCartelaId() == newItem.getCartelaId()
                            && oldItem.isGanhadora() == newItem.isGanhadora();
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatCheckBox mCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.cb_cartela_selecao);
            mCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mPresenter.atualizarCartelasSorteaveis(
                    Integer.valueOf(mCheckBox.getText().toString()), mCheckBox.isChecked());
        }
    }
}
