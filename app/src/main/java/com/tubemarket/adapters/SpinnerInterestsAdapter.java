package com.tubemarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.tubemarket.R;
import com.tubemarket.databinding.SpinnerRowBinding;
import com.tubemarket.models.InterestsModel;

import java.util.List;

public class SpinnerInterestsAdapter extends BaseAdapter {
    private List<InterestsModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerInterestsAdapter(List<InterestsModel> list, Context context) {
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
        SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row,parent,false);
        binding.setTitle(list.get(position).getName());
        return binding.getRoot();
    }
}
