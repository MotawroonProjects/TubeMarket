package com.app.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.UserRowBinding;
import com.app.tubemarket.databinding.VipPayRowBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.OperationModel;
import com.app.tubemarket.models.VipModel;

import java.util.List;

public class ViewUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OperationModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ViewUsersAdapter(Context context, List<OperationModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UserRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private UserRowBinding binding;

        public MyHolder(UserRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
