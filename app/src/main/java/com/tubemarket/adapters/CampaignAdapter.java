package com.tubemarket.adapters;

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

import com.tubemarket.R;
import com.tubemarket.databinding.CampaignRowBinding;
import com.tubemarket.databinding.VipPayRowBinding;
import com.tubemarket.interfaces.Listeners;
import com.tubemarket.models.CampaignModel;

import java.util.List;

public class CampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CampaignModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Listeners.CampaignListener listener;
    public CampaignAdapter(Context context, List<CampaignModel> list, Listeners.CampaignListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CampaignRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.campaign_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CampaignModel campaignModel = list.get(position);
        try {
            int progress =Math.round ((Float.parseFloat(campaignModel.getView_count())/Float.parseFloat(campaignModel.getView_limit()))*100);
            myHolder.binding.progBar.setProgress(progress);
        }catch (Exception e){}

        myHolder.binding.setModel(campaignModel);
        myHolder.itemView.setOnClickListener(v -> {
            listener.onCampaignData(list.get(myHolder.getAdapterPosition()),1,myHolder.itemView);
        });

        myHolder.binding.imageMenu.setOnClickListener(v -> {
            createMenu(list.get(myHolder.getAdapterPosition()),myHolder.binding.imageMenu,myHolder.itemView);
        });

    }

    private void createMenu(CampaignModel campaignModel, View view,View root) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.PopupMenu);

        PopupMenu popupMenu = new PopupMenu(ctw,view);
        popupMenu.getMenu().add(Menu.NONE,1,1, R.string.delete);
        popupMenu.getMenu().add(Menu.NONE,2,2, R.string.details);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id){
                case 1:
                    listener.onCampaignData(campaignModel,2,root);

                    break;

                case 2:
                    listener.onCampaignData(campaignModel,1,root);

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
        private CampaignRowBinding binding;

        public MyHolder(CampaignRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
