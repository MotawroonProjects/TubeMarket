package com.app.tubemarket.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.BuyMessageRowBinding;
import com.app.tubemarket.databinding.UserMessageRowBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.BuyMessageModel;
import com.app.tubemarket.models.UserMessageModel;

import java.util.List;

public class UserMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserMessageModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.UserMessageListener listener;
    public UserMessageAdapter(Context context, List<UserMessageModel> list, Listeners.UserMessageListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UserMessageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_message_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvLink.setPaintFlags(myHolder.binding.tvLink.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        myHolder.binding.flOpenLink.setOnClickListener(v -> {
            listener.onUserMessage(list.get(myHolder.getAdapterPosition()),myHolder.itemView);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private UserMessageRowBinding binding;

        public MyHolder(UserMessageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
