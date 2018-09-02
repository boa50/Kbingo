package br.com.boa50.kbingo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.CartelaUtils;

public class CartelasSorteaveisAdapter extends ListAdapter<Integer, CartelasSorteaveisAdapter.ViewHolder> {

    public CartelasSorteaveisAdapter() {
        super(DIFF_CALLBACK);
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
            holder.mTextView.setText(CartelaUtils.formatarNumeroCartela(getItem(position)));
        } else {
            holder.mTextView.setText("Todas as cartelas");
        }

    }

    private static final DiffUtil.ItemCallback<Integer> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Integer>() {
                @Override
                public boolean areItemsTheSame(Integer oldItem, Integer newItem) {
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
