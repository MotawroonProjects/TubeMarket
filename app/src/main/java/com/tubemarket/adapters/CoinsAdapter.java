package com.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tubemarket.R;
import com.tubemarket.databinding.CoinRowBinding;
import com.tubemarket.databinding.VipPayRowBinding;
import com.tubemarket.interfaces.Listeners;
import com.tubemarket.models.CoinsModel;

import java.util.List;

public class CoinsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CoinsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.CoinsListener listener;
    public CoinsAdapter(Context context, List<CoinsModel> list, Listeners.CoinsListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CoinRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.coin_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            listener.onCoinsData(list.get(myHolder.getAdapterPosition()),myHolder.itemView);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CoinRowBinding binding;

        public MyHolder(CoinRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
