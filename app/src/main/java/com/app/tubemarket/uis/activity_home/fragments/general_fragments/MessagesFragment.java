package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.BuyMessageAdapter;
import com.app.tubemarket.databinding.FragmentMessagesBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.AddMessageDataModel;
import com.app.tubemarket.models.BuyMessageDataModel;
import com.app.tubemarket.models.BuyMessageModel;
import com.app.tubemarket.models.MessageResponseModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_view.ViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessagesFragment extends Fragment implements Listeners.BuyMessageListener {

    private FragmentMessagesBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private List<BuyMessageModel> list;
    private BuyMessageAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private MessageResponseModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()== Activity.RESULT_OK){
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.buyMessageWithCouponFragment,bundle);
            }
        });
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new BuyMessageAdapter(activity, list, this);
        binding.recView.setAdapter(adapter);



        getMessages();
    }

    private void getMessages() {
        binding.progBar.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();
        Api.getService(Tags.base_url)
                .showMessages("Bearer " + userModel.getToken())
                .enqueue(new Callback<BuyMessageDataModel>() {
                    @Override
                    public void onResponse(Call<BuyMessageDataModel> call, Response<BuyMessageDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                if (response.body().getData().size()>0){
                                    list.clear();
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoMessage.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoMessage.setVisibility(View.VISIBLE);
                                }


                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BuyMessageDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        Log.e("fail", t.getMessage());
                    }
                });
    }


    @Override
    public void onBuyMessage(BuyMessageModel model, View view) {
        if (model.getType().equals("normal")){
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", model);
            Navigation.findNavController(view).navigate(R.id.buyMessageFragment,bundle);
        }else if (model.getType().equals("with_coupon")){
            createMessage(model);
        }
    }

    private void createMessage(BuyMessageModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .createMessageWithCoupon("Bearer "+userModel.getToken(),userModel.getId(), model.getId())
                .enqueue(new Callback<MessageResponseModel>() {
                    @Override
                    public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){

                            if (response.body()!=null&&response.body().getStatus()==200){
                                MessagesFragment.this.model = response.body();
                                Intent intent = new Intent(activity, ViewActivity.class);
                                intent.putExtra("url", response.body().getPay_link());
                                intent.putExtra("data",response.body().getData());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                launcher.launch(intent);

                            }else if (response.body()!=null&&response.body().getStatus()==201){
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", response.body());
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.buyMessageWithCouponFragment,bundle);


                            }

                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                        dialog.dismiss();

                        Log.e("failed", t.getMessage()+"__");
                    }
                });
    }



}