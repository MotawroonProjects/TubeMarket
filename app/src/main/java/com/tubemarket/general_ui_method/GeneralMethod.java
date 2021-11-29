package com.tubemarket.general_ui_method;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.tubemarket.R;
import com.tubemarket.models.MyAdsModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }





    @BindingAdapter("image")
    public static void image(View view, String url) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (url != null) {

                Picasso.get().load(Uri.parse(url)).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (url != null) {

                Picasso.get().load(Uri.parse(url)).into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (url != null) {

                Picasso.get().load(Uri.parse(url)).into(imageView);
            }
        }

    }



    @BindingAdapter("channel_image")
    public static void channelImage(View view, String url) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (url != null) {

                Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_youtube).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (url != null) {

                Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_youtube).into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (url != null) {

                Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_youtube).into(imageView);
            }
        }

    }

    @BindingAdapter("user_image")
    public static void user_image(View view, String url) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;

            if (url != null) {
                Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_avatar).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;
            if (url != null) {
                Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_avatar).into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (url != null) {
                Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_avatar).into(imageView);
            }
        }

    }


    @BindingAdapter("ad_type")
    public static void ad_type_count(TextView textView, MyAdsModel model) {
        String count ="0";
        String limit ="0";
        String type ="";
        Context context = textView.getContext();

        if (model.getType().equals("get_subscription")){
            count = model.getSubscriptions_count()+"/";
            limit = model.getSubscription_limit()+" ";
            type = context.getString(R.string.subsc);
        }else if (model.getType().equals("get_views")){
            count = model.getViews_count()+"/";
            limit = model.getViews_number()+" ";
            type = context.getString(R.string.views);
        }else if (model.getType().equals("get_subscription_and_views")){
            count = model.getSubscriptions_count()+"/";
            limit = model.getSubscription_limit()+" ";
            type = context.getString(R.string.subsc)+" - ";

            count += model.getViews_count()+"/";
            limit += model.getViews_number()+" ";
            type += context.getString(R.string.views);

        }else if (model.getType().equals("get_likes")){
            count = model.getLikes_count();
            limit = model.getLikes_limit()+" ";
            type = context.getString(R.string.likes);
        }
        String data = count + limit + type;
        textView.setText(data);
    }



}










