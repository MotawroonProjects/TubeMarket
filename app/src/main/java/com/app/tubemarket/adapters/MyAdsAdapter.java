package com.app.tubemarket.adapters;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.CampaignRowBinding;
import com.app.tubemarket.databinding.MyAdsRowBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.AdsViewModel;
import com.app.tubemarket.models.CampaignModel;
import com.app.tubemarket.models.MyAdsModel;

import java.util.List;

public class MyAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyAdsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.MyAdsListener listener;

    public MyAdsAdapter(Context context, List<MyAdsModel> list, Listeners.MyAdsListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MyAdsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.my_ads_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        MyAdsModel myAdsModel = list.get(position);
        try {
            int progress = 0;

            if (myAdsModel.getType().equals("get_subscription")) {
                progress = Math.round((Float.parseFloat(myAdsModel.getSubscriptions_count()) / Float.parseFloat(myAdsModel.getSubscription_limit())) * 100);

            } else if (myAdsModel.getType().equals("get_views")) {
                progress = Math.round((Float.parseFloat(myAdsModel.getViews_count()) / Float.parseFloat(myAdsModel.getViews_number())) * 100);

            } else if (myAdsModel.getType().equals("get_subscription_and_views")) {
                float count = Float.parseFloat(myAdsModel.getViews_count())+Float.parseFloat(myAdsModel.getSubscriptions_count());
                float limit = Float.parseFloat(myAdsModel.getViews_number())+Float.parseFloat(myAdsModel.getSubscription_limit());

                progress = Math.round((count / limit) * 100);


            } else if (myAdsModel.getType().equals("get_likes")) {
                progress = Math.round((Float.parseFloat(myAdsModel.getLikes_count()) / Float.parseFloat(myAdsModel.getLikes_limit())) * 100);

            }
            myHolder.binding.progBar.setProgress(progress);
        } catch (Exception e) {
        }

        myHolder.binding.setModel(myAdsModel);
        myHolder.itemView.setOnClickListener(v -> {
            listener.onMyAdsData(list.get(myHolder.getAdapterPosition()), 1, myHolder.itemView);
        });

        myHolder.binding.imageMenu.setOnClickListener(v -> {
            createMenu(list.get(myHolder.getAdapterPosition()), myHolder.binding.imageMenu, myHolder.itemView);
        });

    }

    private void createMenu(MyAdsModel myAdsModel, View view, View root) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.PopupMenu);

        PopupMenu popupMenu = new PopupMenu(ctw, view);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, R.string.delete);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case 1:
                    listener.onMyAdsData(myAdsModel, 2, root);

                    break;


            }
            popupMenu.dismiss();
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private MyAdsRowBinding binding;

        public MyHolder(MyAdsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
