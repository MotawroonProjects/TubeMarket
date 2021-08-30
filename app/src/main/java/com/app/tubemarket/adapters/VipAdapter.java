package com.app.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.VipPayRowBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.VipModel;

import java.util.List;

public class VipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VipModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.VipListener listener;
    public VipAdapter(Context context, List<VipModel> list,Listeners.VipListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        VipPayRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.vip_pay_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            listener.onVipPay(list.get(myHolder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private VipPayRowBinding binding;

        public MyHolder(VipPayRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
