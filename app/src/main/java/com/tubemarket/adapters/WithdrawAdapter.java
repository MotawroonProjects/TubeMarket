package com.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tubemarket.R;
import com.tubemarket.databinding.CoinRowBinding;
import com.tubemarket.databinding.WithdrawRowBinding;
import com.tubemarket.interfaces.Listeners;
import com.tubemarket.models.WithdrawModel;

import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WithdrawModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.WithdrawListener listener;
    public WithdrawAdapter(Context context, List<WithdrawModel> list, Listeners.WithdrawListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        WithdrawRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.withdraw_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            listener.onWithdrawData(list.get(myHolder.getAdapterPosition()),myHolder.itemView);



        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private WithdrawRowBinding binding;

        public MyHolder(WithdrawRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
