package com.app.tubemarket.services;

import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.VideoModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @GET("youtube/v3/videos")
    Call<VideoModel> getYouTubeVideoById(@Query("part") String part,
                                         @Query("id") String id,
                                         @Query("key") String key
    );

    @GET("youtube/v3/channels")
    Call<VideoModel> getYouTubeChannelById(@Query("part") String part,
                                           @Query("id") String id,
                                           @Query("key") String key
    );

    @FormUrlEncoded
    @POST("api/login-or-register")
    Call<LoginRegisterModel> login(@Field("google_id") String google_id,
                                   @Field("name") String name,
                                   @Field("email") String email,
                                   @Field("image") String image,
                                   @Field("interested") String interested,
                                   @Field("software_type") String software_type
    );

    @FormUrlEncoded
    @POST("api/update-profile")
    Call<LoginRegisterModel> updateProfile(@Header ("Authorization") String token,
                                           @Field("user_id") String user_id,
                                           @Field("google_id") String google_id,
                                           @Field("channel_video_name") String channel_video_name,
                                           @Field("channel_video_image") String channel_video_image,
                                           @Field("channel_video_description") String channel_video_description,
                                           @Field("channel_video_link") String channel_video_link,
                                           @Field("channel_id") String channel_id,
                                           @Field("channel_code") String channel_code,
                                           @Field("channel_name") String channel_name,
                                           @Field("channel_image") String channel_image,
                                           @Field("channel_description") String channel_description,
                                           @Field("interested") String interested




    );


}

