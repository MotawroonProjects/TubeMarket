package com.app.tubemarket.uis.activity_login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.ActivityLoginBinding;
import com.app.tubemarket.language.Language;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.BackOff;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private ActivityResultLauncher<Intent> launcher;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount account = task.getResult();
                login(account);

            } catch (Exception e) {
                Log.e("error", e.getMessage() + "__");
            }

        });


        binding.llLogin.setOnClickListener(v -> {
            account = GoogleSignIn.getLastSignedInAccount(this);

            if (account == null) {
                googleSignIn();
            } else {
                login(account);
            }
        });

    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        launcher.launch(signInIntent);

    }


    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(GoogleSignInAccount account) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString(), "0", "android")
                .enqueue(new Callback<LoginRegisterModel>() {
                    @Override
                    public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                LoginRegisterModel model = response.body();
                                UserModel.ChannelModel channelModel = null;
                                UserModel.VideoModel videoModel = null;
                                InterestsModel interestsModel = null;
                                if (model.getData().getChannel_id() != null) {
                                    channelModel = new UserModel.ChannelModel(model.getData().getChannel_id(), model.getData().getChannel_name(), model.getData().getChannel_description(), model.getData().getChannel_image());

                                }

                                if (model.getData().getChannel_video_link() != null) {
                                    videoModel = new UserModel.VideoModel(model.getData().getChannel_video_link(), model.getData().getChannel_video_name(), model.getData().getChannel_video_description(), model.getData().getChannel_video_image());

                                }

                                if (model.getData().getInterested() != 0) {
                                    interestsModel = new InterestsModel(model.getData().getInterested(), "");

                                }

                                UserModel userModel = new UserModel(model.getData().getId(), account.getId(), account.getEmail(), account.getDisplayName(), account.getPhotoUrl().toString(), model.getData().getCoins(), model.getData().getCode(), model.getData().getUser_type(), model.getData().getIs_vip(), model.getData().getToken(), channelModel, videoModel, interestsModel);
                                preferences.create_update_userdata(LoginActivity.this, userModel);
                                navigateToHomeActivity();

                            } else if (response.body().getStatus() == 409) {
                                Toast.makeText(LoginActivity.this, getString(R.string.user_blocked), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                        dialog.dismiss();

                    }
                });
    }


}