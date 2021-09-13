package com.app.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.BuyMessageRowBinding;
import com.app.tubemarket.databinding.VipPayRowBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.BuyMessageModel;
import com.app.tubemarket.models.VipModel;

import java.util.List;

public class BuyMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BuyMessageModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.BuyMessageListener listener;
    public BuyMessageAdapter(Context context, List<BuyMessageModel> list, Listeners.BuyMessageListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BuyMessageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.buy_message_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            listener.onBuyMessage(list.get(myHolder.getAdapterPosition()),myHolder.itemView);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private BuyMessageRowBinding binding;

        public MyHolder(BuyMessageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
