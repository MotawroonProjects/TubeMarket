package com.app.tubemarket.services;

import com.app.tubemarket.models.CoinsDataModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.SubscribeSecondsModel;
import com.app.tubemarket.models.VideoDataModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.models.ViewsSecondsModel;

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
    Call<LoginRegisterModel> updateProfile(@Header("Authorization") String token,
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

    @GET("api/list-of-views-and-seconds")
    Call<ViewsSecondsModel> getViewSeconds(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/calculate-coins")
    Call<CoinsDataModel> calculateCoin(@Header("Authorization") String token,
                                       @Field("views_number") String views_number,
                                       @Field("second_number") String second_number
    );

    @FormUrlEncoded
    @POST("api/add-video")
    Call<StatusResponse> addVideo(@Header("Authorization") String token,
                                  @Field("user_id") String user_id,
                                  @Field("link") String link,
                                  @Field("timer_limit") String timer_limit,
                                  @Field("view_limit") String view_limit,
                                  @Field("campaign_coins") String campaign_coins,
                                  @Field("profit_coins") String profit_coins,
                                  @Field("have_discount") String have_discount,
                                  @Field("discount_coins") String discount_coins


    );

    @GET("api/list-of-subscriptions-and-seconds")
    Call<SubscribeSecondsModel> getSubscribeSeconds(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/calculate-channel-coins")
    Call<CoinsDataModel> calculateChannelCoin(@Header("Authorization") String token,
                                              @Field("subscriptions_number") String subscriptions_number,
                                              @Field("second_number") String second_number
    );

    @FormUrlEncoded
    @POST("api/add-channel")
    Call<StatusResponse> addVideoSubscribes(@Header("Authorization") String token,
                                            @Field("user_id") String user_id,
                                            @Field("link") String link,
                                            @Field("timer_limit") String timer_limit,
                                            @Field("view_limit") String view_limit,
                                            @Field("subscription_limit") String subscription_limit,
                                            @Field("campaign_coins") String campaign_coins,
                                            @Field("profit_coins") String profit_coins,
                                            @Field("have_discount") String have_discount,
                                            @Field("discount_coins") String discount_coins


    );

    @GET("api/list-of-videos")
    Call<VideoDataModel> getViewsVideo(@Header("Authorization") String token,
                                       @Query("user_id") String user_id,
                                       @Query("pagination_status") String pagination_status,
                                       @Query("per_link_") String per_link_,
                                       @Query("orderBy") String orderBy,
                                       @Query("page") int page
    );

    @GET("api/list-of-channels")
    Call<VideoDataModel> getChannel(@Header("Authorization") String token,
                                    @Query("user_id") String user_id,
                                    @Query("pagination_status") String pagination_status,
                                    @Query("per_link_") String per_link_,
                                    @Query("orderBy") String orderBy,
                                    @Query("page") int page
    );

    @GET("api/get-profile")
    Call<LoginRegisterModel> getProfile(@Header("Authorization") String token,
                                        @Query("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/view-one-video")
    Call<StatusResponse> view(@Header("Authorization") String token,
                              @Field("user_id") String user_id,
                              @Field("campaign_id") String campaign_id,
                              @Field("profit_coins") String profit_coins,
                              @Field("timer_limit") String timer_limit

    );

    @FormUrlEncoded
    @POST("api/like-one-video")
    Call<StatusResponse> like(@Header("Authorization") String token,
                              @Field("user_id") String user_id,
                              @Field("campaign_id") String campaign_id,
                              @Field("profit_coins") String profit_coins
    );

    @FormUrlEncoded
    @POST("api/subscription-one-video")
    Call<StatusResponse> subscribe(@Header("Authorization") String token,
                                   @Field("user_id") String user_id,
                                   @Field("campaign_id") String campaign_id,
                                   @Field("profit_coins") String profit_coins
    );


}

