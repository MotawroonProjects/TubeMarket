package com.app.tubemarket.services;

import com.app.tubemarket.models.AdCostDataModel;
import com.app.tubemarket.models.CampaignDataModel;
import com.app.tubemarket.models.CoinDataModel;
import com.app.tubemarket.models.CoinsDataModel;
import com.app.tubemarket.models.CostResultModel;
import com.app.tubemarket.models.GoldCostDataModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.SingleCampaign;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.SubscribeSecondsModel;
import com.app.tubemarket.models.VideoDataModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.models.ViewsSecondsModel;
import com.app.tubemarket.models.VipDataModel;
import com.app.tubemarket.models.WithdrawDataModel;

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
                              @Field("seconds") String seconds

    );

    @FormUrlEncoded
    @POST("api/like-one-video")
    Call<StatusResponse> like(@Header("Authorization") String token,
                              @Field("user_id") String user_id,
                              @Field("campaign_id") String campaign_id,
                              @Field("profit_coins") String profit_coins,
                              @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api/subscription-one-video")
    Call<StatusResponse> subscribe(@Header("Authorization") String token,
                                   @Field("user_id") String user_id,
                                   @Field("campaign_id") String campaign_id,
                                   @Field("profit_coins") String profit_coins
    );


    @GET("api/list-of-vip")
    Call<VipDataModel> getVipPay(@Header("Authorization") String token,
                                 @Query("orderBy") String orderBy
    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<StatusResponse> logout(@Header("Authorization") String bearer_token,
                                @Field("user_id") String user_id,
                                @Field("phone_token") String firebase_token
    );

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<StatusResponse> updateFirebaseToken(@Header("Authorization") String bearer_token,
                                             @Field("user_id") String user_id,
                                             @Field("phone_token") String firebase_token,
                                             @Field("software_type") String software_type
    );


    @GET("api/list-of-campaigns")
    Call<CampaignDataModel> getMyCampaign(@Header("Authorization") String token,
                                          @Query("user_id") String user_id,
                                          @Query("orderBy") String orderBy
    );

    @GET("api/one-campaign")
    Call<SingleCampaign> getSingleCampaign(@Header("Authorization") String token,
                                           @Query("user_id") String user_id,
                                           @Query("id") String id
    );

    @FormUrlEncoded
    @POST("api/delete-campaign")
    Call<StatusResponse> deleteCampaign(@Header("Authorization") String bearer_token,
                                        @Field("id") String user_id

    );

    @GET("api/advertisement-view-costs")
    Call<AdCostDataModel> getViewsCost(@Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("api/calculate-get-views")
    Call<CostResultModel> calculateViewCost(@Header("Authorization") String token,
                                            @Field("views_number") String views_number,
                                            @Field("watch_time") String watch_time
    );


    @FormUrlEncoded
    @POST("api/calculate-get-subscription-and-views")
    Call<CostResultModel> calculateSubViewCost(@Header("Authorization") String token,
                                               @Field("views_number") String views_number,
                                               @Field("watch_time") String watch_time,
                                               @Field("subscriptions_number") String subscriptions_number
    );

    @FormUrlEncoded
    @POST("api/calculate-get-likes")
    Call<CostResultModel> calculateLikeCost(@Header("Authorization") String token,
                                            @Field("likes_number") String likes_number
    );

    @GET("api/buy-coins")
    Call<CoinDataModel> getCoins(@Header("Authorization") String token,
                                 @Query("orderBy") String orderBy
    );

    @GET("api/exchange")
    Call<WithdrawDataModel> getWithdrawCoins(@Header("Authorization") String token,
                                             @Query("orderBy") String orderBy
    );

    @FormUrlEncoded
    @POST("api/calculate-get-subscriptions")
    Call<CostResultModel> calculateSubscribeCost(@Header("Authorization") String token,
                                                 @Field("subscriptions_number") String subscriptions_number
    );

    @FormUrlEncoded
    @POST("api/get-views")
    Call<StatusResponse> addViews(@Header("Authorization") String token,
                                  @Field("user_id") String user_id,
                                  @Field("views_number") String views_number,
                                  @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                                  @Field("total_cost") String total_cost,
                                  @Field("link") String link,
                                  @Field("watch_time") String watch_time


    );

    @FormUrlEncoded
    @POST("api/get-subscriptions")
    Call<StatusResponse> addSubscribes(@Header("Authorization") String token,
                                       @Field("user_id") String user_id,
                                       @Field("subscription_limit") String subscription_limit,
                                       @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                                       @Field("total_cost") String total_cost,
                                       @Field("link") String link

    );

    @FormUrlEncoded
    @POST("api/get-subscription-and-views")
    Call<StatusResponse> addSubscribesViews(@Header("Authorization") String token,
                                            @Field("user_id") String user_id,
                                            @Field("subscription_limit") String subscription_limit,
                                            @Field("views_number") String views_number,
                                            @Field("watch_time") String watch_time,
                                            @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                                            @Field("total_cost") String total_cost,
                                            @Field("link") String link

    );

    @FormUrlEncoded
    @POST("api/get-likes")
    Call<StatusResponse> addLikes(@Header("Authorization") String token,
                                  @Field("user_id") String user_id,
                                  @Field("likes_limit") String likes_limit,
                                  @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                                  @Field("total_cost") String total_cost,
                                  @Field("link") String link

    );

}

