package br.com.boa50.kbingo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.data.dto.CartelaFiltroDTO;

public class CartelasFiltroAdapter extends ListAdapter<CartelaFiltroDTO, CartelasFiltroAdapter.ViewHolder> {

    public CartelasFiltroAdapter() {
        super(DIFF_CALLBACK);
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
        holder.mTextView.setText(String.valueOf(getItem(position).getCartelaId()));
        holder.mCheckBox.setEnabled(getItem(position).isSelecionada());
    }

    private static final DiffUtil.ItemCallback<CartelaFiltroDTO> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CartelaFiltroDTO>() {
                @Override
                public boolean areItemsTheSame(CartelaFiltroDTO oldItem, CartelaFiltroDTO newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(CartelaFiltroDTO oldItem, CartelaFiltroDTO newItem) {
                    return oldItem.getCartelaId() == newItem.getCartelaId()
                            && oldItem.isGanhadora() == newItem.isGanhadora();
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;
        AppCompatCheckBox mCheckBox;


        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_cartela_numero);
            mCheckBox = itemView.findViewById(R.id.cb_cartela_selecao);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
