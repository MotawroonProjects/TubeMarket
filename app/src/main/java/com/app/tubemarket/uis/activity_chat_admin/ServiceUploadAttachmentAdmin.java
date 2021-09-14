package com.app.tubemarket.uis.activity_chat_admin;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.app.tubemarket.R;
import com.app.tubemarket.models.AdminMessageModel;
import com.app.tubemarket.models.SingleAdminMessageDataModel;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceUploadAttachmentAdmin extends Service {
    private String file_uri;
    private String user_token;
    private int user_id;
    private int room_id;
    private int admin_id;
    private String attachment_type;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        file_uri = intent.getStringExtra("file_uri");
        user_token = intent.getStringExtra("user_token");
        user_id = intent.getIntExtra("user_id",0);
        room_id = intent.getIntExtra("room_id",0);
        admin_id = intent.getIntExtra("admin_id",0);
        attachment_type = intent.getStringExtra("attachment_type");
        uploadAttachment(attachment_type);

        return START_STICKY;
    }

    private void uploadAttachment(String attachment_type) {
        Calendar calendar = Calendar.getInstance();
        long date = calendar.getTimeInMillis();
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(user_id));

        RequestBody room_id_part = Common.getRequestBodyText(String.valueOf(room_id));
        RequestBody type_part = Common.getRequestBodyText(attachment_type);
        RequestBody message_part = Common.getRequestBodyText("");
        RequestBody date_part = Common.getRequestBodyText(String.valueOf(date));
        RequestBody admin_id_part = Common.getRequestBodyText(String.valueOf(admin_id));

        MultipartBody.Part file_part;
        file_part = Common.getMultiPartImage(this, Uri.parse(file_uri), "image");

        Api.getService(Tags.base_url).sendAdminChatAttachment("Bearer "+user_token,user_id_part, room_id_part,admin_id_part,type_part,file_part)
                .enqueue(new Callback<SingleAdminMessageDataModel>() {
                    @Override
                    public void onResponse(Call<SingleAdminMessageDataModel> call, Response<SingleAdminMessageDataModel> response) {
                        if (response.isSuccessful()) {

                            if (response.body()!=null&&response.body().getStatus()==200){
                                AdminMessageModel model = response.body().getData();
                                EventBus.getDefault().post(model);
                                stopSelf();
                            }else {
                                Toast.makeText(ServiceUploadAttachmentAdmin.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            if (response.code() == 500) {

                                Toast.makeText(ServiceUploadAttachmentAdmin.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceUploadAttachmentAdmin.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleAdminMessageDataModel> call, Throwable t) {

                        try {

                            stopSelf();

                            if (t.getMessage() != null) {
                                Log.e("msg_chat_error", t.getMessage() + "__");


                            }
                        } catch (Exception e) {

                        }
                    }
                });

        stopSelf();
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
