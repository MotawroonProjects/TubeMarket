package com.tubemarket.uis.activity_chat_admin;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.tubemarket.R;
import com.tubemarket.adapters.ChatAdminAdapter;
import com.tubemarket.databinding.ActivityAdminChatBinding;
import com.tubemarket.language.Language;
import com.tubemarket.models.AdminMessageDataModel;
import com.tubemarket.models.AdminMessageModel;
import com.tubemarket.models.AdminRoomModel;
import com.tubemarket.models.SingleAdminMessageDataModel;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdminActivity extends AppCompatActivity {
    private ActivityAdminChatBinding binding;
    private String lang;
    private final int IMG_REQ = 1;
    private final int CAMERA_REQ = 2;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String CAMERA_PERM = Manifest.permission.CAMERA;
    private UserModel userModel;
    private Preferences preferences;
    private ChatAdminAdapter adapter;
    private List<AdminMessageModel> messageModelList;
    private boolean isDataChanged = false;
    private AdminRoomModel roomModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_chat);
        initView();

    }



    private void initView() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(Tags.not_tag, Tags.not_id);

        }
        messageModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new ChatAdminAdapter(messageModelList, this,Integer.parseInt( userModel.getId()));
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(adapter);

        binding.llBack.setOnClickListener(v -> {
            back();
        });


        binding.imageChooser.setOnClickListener(v -> {
            checkGalleryPermission();

        });

        binding.imageCamera.setOnClickListener(v -> {
            checkCameraPermission();
        });


        binding.imageSend.setOnClickListener(v -> {
            String message = binding.edtMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                binding.edtMessage.setText("");
                sendChatText(message);
            }
        });

        EventBus.getDefault().register(this);
        getAllMessages();

    }


    public void getAllMessages() {

        Api.getService(Tags.base_url)
                .getAdminChatMessage("Bearer " + userModel.getToken(), userModel.getId())
                .enqueue(new Callback<AdminMessageDataModel>() {
                    @Override
                    public void onResponse(Call<AdminMessageDataModel> call, Response<AdminMessageDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {

                                if (response.body().room!=null){
                                    roomModel = response.body().room;
                                    preferences.create_room_id(ChatAdminActivity.this,response.body().room.getId());
                                }
                                if (response.body().getData().size() > 0) {
                                    messageModelList.clear();
                                    messageModelList.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    binding.recView.postDelayed(() -> binding.recView.smoothScrollToPosition(messageModelList.size() - 1), 200);

                                }
                            } else {
                                Toast.makeText(ChatAdminActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(ChatAdminActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChatAdminActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AdminMessageDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("Error", t.getMessage());

                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void sendAttachment(String file_uri, String attachment_type) {

        Intent intent = new Intent(this, ServiceUploadAttachmentAdmin.class);
        intent.putExtra("file_uri", file_uri);
        intent.putExtra("user_token", userModel.getToken());
        intent.putExtra("user_id", Integer.parseInt(userModel.getId()));
        intent.putExtra("room_id", Integer.parseInt(roomModel.getId()));
        intent.putExtra("admin_id", Integer.parseInt(roomModel.getAdmin_id()));
        intent.putExtra("attachment_type", attachment_type);
        startService(intent);


    }

    private void sendChatText(String message) {
        long date = Calendar.getInstance().getTimeInMillis();
        Api.getService(Tags.base_url)
                .sendAdminChatMessage("Bearer " + userModel.getToken(), userModel.getId(),roomModel.getId(), roomModel.getAdmin_id(), "message", message)
                .enqueue(new Callback<SingleAdminMessageDataModel>() {
                    @Override
                    public void onResponse(Call<SingleAdminMessageDataModel> call, Response<SingleAdminMessageDataModel> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                isDataChanged = true;
                                AdminMessageModel model = response.body().getData();
                                messageModelList.add(model);
                                adapter.notifyItemInserted(messageModelList.size());
                                binding.recView.postDelayed(() -> binding.recView.smoothScrollToPosition(messageModelList.size() - 1), 200);
                            } else {
                                Toast.makeText(ChatAdminActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            try {
                                Log.e("error", response.code() + "___" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(ChatAdminActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<SingleAdminMessageDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("Error", t.getMessage());


                            }
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, CAMERA_PERM) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, WRITE_PERM) == PackageManager.PERMISSION_GRANTED) {
            selectImage(CAMERA_REQ);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERM, WRITE_PERM}, CAMERA_REQ);

        }

    }

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) == PackageManager.PERMISSION_GRANTED) {
            selectImage(IMG_REQ);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ);

        }

    }

    private void selectImage(int req) {

        Intent intent = new Intent();
        if (req == IMG_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            }
            intent.setType("image/*");


        } else if (req == CAMERA_REQ) {

            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        }

        startActivityForResult(intent, req);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage(requestCode);
            } else {
                Toast.makeText(this, "Access image denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                selectImage(requestCode);
            } else {
                Toast.makeText(this, "Access camera denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQ && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            sendAttachment(uri.toString(), "image");

        } else if (requestCode == CAMERA_REQ && resultCode == RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Uri uri = getUriFromBitmap(bitmap);
            sendAttachment(uri.toString(), "image");

        }

    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        return Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", ""));

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttachmentSuccess(AdminMessageModel messageModel) {
        isDataChanged = true;
        messageModelList.add(messageModel);
        adapter.notifyItemChanged(messageModelList.size());
        binding.recView.postDelayed(() -> binding.recView.smoothScrollToPosition(messageModelList.size() - 1), 200);


    }


    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        preferences.create_room_id(this, "");
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

}