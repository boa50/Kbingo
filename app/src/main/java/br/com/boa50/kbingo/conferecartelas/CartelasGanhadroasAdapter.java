package br.com.boa50.kbingo.conferecartelas;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.boa50.kbingo.Constant;
import br.com.boa50.kbingo.R;
import br.com.boa50.kbingo.util.ActivityUtils;

class CartelasGanhadorasAdapter extends ListAdapter<String, CartelasGanhadorasAdapter.ViewHolder> {
    private FragmentActivity mFragmentActivity;
    private Fragment mFragment;

    private class VIEW_TYPES {
        static final int Header = 1;
        static final int Normal = 2;
    }

    CartelasGanhadorasAdapter(FragmentActivity fragmentActivity, Fragment fragment) {
        super(DIFF_CALLBACK);
        mFragmentActivity = fragmentActivity;
        mFragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPES.Header;
        else return VIEW_TYPES.Normal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cartelas_ganhadoras, parent, false);
        if (viewType == VIEW_TYPES.Header) return new ViewHolder(view, true);
        else return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(getItem(position));
    }

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(String oldItem, @NonNull String newItem) {
                    return oldItem.equalsIgnoreCase(newItem);
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;

        ViewHolder(View v) {
            this(v, false);
        }

        ViewHolder(View v, boolean header) {
            super(v);
            mTextView = v.findViewById(R.id.tv_cartela_numero);
            if (header) mTextView.setTypeface(null, Typeface.BOLD);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();

            String textoCartela = mTextView.getText().toString();
            if (textoCartela.equalsIgnoreCase(
                    mFragmentActivity.getString(R.string.list_item_confere_outra_cartela))) {
                textoCartela = "";
            }
            bundle.putString(Constant.EXTRA_ULTIMA_CARTELA, textoCartela);
            bundle.putBoolean(Constant.EXTRA_CONFERE_CARTELA, true);
            mFragment.setArguments(bundle);

            mFragmentActivity.setTitle(R.string.conferir_cartelas_title);

            ActivityUtils.addFragmentToActivity(
                    mFragmentActivity.getSupportFragmentManager(),
                    mFragment,
                    R.id.conteudoFrame,
                    true
            );
        }
    }
}
