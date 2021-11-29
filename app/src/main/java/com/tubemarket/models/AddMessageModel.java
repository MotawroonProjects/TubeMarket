package com.tubemarket.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.tubemarket.BR;
import com.tubemarket.R;

import java.io.Serializable;

public class AddMessageModel extends BaseObservable implements Serializable {
    private String link;
    private String content;
    public ObservableField<String> error_link = new ObservableField<>();
    public ObservableField<String> error_content = new ObservableField<>();

    public boolean isDataValid(Context context)
    {
        if (!link.isEmpty()&&!content.isEmpty()){
            error_link.set(null);
            error_content.set(null);
            return true;
        }else {
            if (link.isEmpty()){
                error_link.set(context.getString(R.string.field_required));

            }else {
                error_link.set(null);

            }

            if (content.isEmpty()){
                error_content.set(context.getString(R.string.field_required));

            }else {
                error_content.set(null);

            }
            return false;
        }
    }

    public AddMessageModel() {
        link ="";
        content="";
    }

    @Bindable
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        notifyPropertyChanged(BR.link);
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);

    }
}
