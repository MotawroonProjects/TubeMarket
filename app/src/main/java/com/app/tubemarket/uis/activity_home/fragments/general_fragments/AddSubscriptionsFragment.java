package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.databinding.FragmentAddSubscriptionsBinding;
import com.app.tubemarket.databinding.FragmentNewAdditionBinding;
import com.app.tubemarket.models.CoinsDataModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.SubscribeSecondsModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.ViewsSecondsModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSubscriptionsFragment extends Fragment {

    private FragmentAddSubscriptionsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private SpinnerCountAdapter subscriptionsAdapter,secondsAdapter;
    private List<String> subscriptionsList,secondsList;
    private String subscribes="",seconds="";
    private CoinsDataModel.CoinsModel coinsModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_subscriptions,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        subscriptionsList =new ArrayList<>();
        secondsList = new ArrayList<>();
        binding.setModel(userModel.getChannelModel());

        subscriptionsAdapter = new SpinnerCountAdapter(subscriptionsList,activity);
        binding.spinnerSubscriptions.setAdapter(subscriptionsAdapter);
        secondsAdapter = new SpinnerCountAdapter(secondsList,activity);
        binding.spinnerSeconds.setAdapter(secondsAdapter);

        binding.spinnerSubscriptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subscribes = subscriptionsList.get(position);
                binding.setValue(subscriptionsList.get(position));
                calculateCoins();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerSeconds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seconds = secondsList.get(position);
                calculateCoins();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnCreateCampaign.setOnClickListener(v -> {
            if (coinsModel!=null){
                addVideo(v);
            }
        });
        getSubscribeSecond();
    }

    private void getSubscribeSecond(){

        Log.e("token",userModel.getToken());

        Api.getService(Tags.base_url)
                .getSubscribeSeconds("Bearer "+userModel.getToken())
                .enqueue(new Callback<SubscribeSecondsModel>() {
                    @Override
                    public void onResponse(Call<SubscribeSecondsModel> call, Response<SubscribeSecondsModel> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getData() != null ) {

                                activity.runOnUiThread(() -> {


                                    subscriptionsList.clear();
                                    subscriptionsList.addAll(response.body().getData().getList_of_subscriptions());
                                    subscriptionsAdapter.notifyDataSetChanged();
                                    secondsList.clear();
                                    secondsList.addAll(response.body().getData().getList_of_seconds());
                                    secondsAdapter.notifyDataSetChanged();



                                });
                            }
                        }else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string()+"_");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscribeSecondsModel> call, Throwable t) {
                        Log.e("error",t.getMessage()+"_");

                    }
                });
    }

    private void calculateCoins(){
        if (subscribes.isEmpty()||seconds.isEmpty()){
            return;
        }
        Api.getService(Tags.base_url)
                .calculateChannelCoin("Bearer "+userModel.getToken(),subscribes,seconds)
                .enqueue(new Callback<CoinsDataModel>() {
                    @Override
                    public void onResponse(Call<CoinsDataModel> call, Response<CoinsDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null ) {
                                coinsModel = response.body().getData();;
                                binding.setCoins(response.body().getData().getCampaign_coins());
                            }
                        }else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string()+"_");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinsDataModel> call, Throwable t) {
                        Log.e("error",t.getMessage()+"_");

                    }
                });
    }

    private void  addVideo(View view){
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addVideoSubscribes("Bearer "+userModel.getToken(),userModel.getId(),userModel.getVideoModel().getId(),seconds,subscribes,subscribes,coinsModel.getCampaign_coins(),coinsModel.getProfit_coins(),"no","0")
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200) {

                            Navigation.findNavController(view).popBackStack();
                            Common.CreateDialogAlert(activity,getString(R.string.admin_review));
                        }else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string()+"_");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        dialog.dismiss();

                    }
                });
    }

}