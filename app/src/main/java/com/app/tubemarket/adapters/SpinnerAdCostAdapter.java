package com.app.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.SpinnerRow2Binding;
import com.app.tubemarket.models.AdCostModel;

import java.util.List;

public class SpinnerAdCostAdapter extends BaseAdapter {
    private List<AdCostModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerAdCostAdapter(List<AdCostModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row2,parent,false);
        binding.setTitle(list.get(position).getSeconds());
        return binding.getRoot();
    }
}
