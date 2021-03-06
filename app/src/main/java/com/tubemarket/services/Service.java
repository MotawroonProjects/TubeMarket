package com.tubemarket.services;

import com.tubemarket.models.AdCostDataModel;
import com.tubemarket.models.AdPayModel;
import com.tubemarket.models.AddMessageDataModel;
import com.tubemarket.models.AdminMessageDataModel;
import com.tubemarket.models.AdsViewDataModel;
import com.tubemarket.models.BuyMessageDataModel;
import com.tubemarket.models.CampaignDataModel;
import com.tubemarket.models.ChannelUrlModel;
import com.tubemarket.models.CoinDataModel;
import com.tubemarket.models.CoinsDataModel;
import com.tubemarket.models.CostResultModel;
import com.tubemarket.models.LoginRegisterModel;
import com.tubemarket.models.MessageResponseModel;
import com.tubemarket.models.MyAdsDataModel;
import com.tubemarket.models.PayModel;
import com.tubemarket.models.SingleAdminMessageDataModel;
import com.tubemarket.models.SingleCampaign;
import com.tubemarket.models.StatusResponse;
import com.tubemarket.models.SubscribeSecondsModel;
import com.tubemarket.models.UserMessagesDataModel;
import com.tubemarket.models.VideoDataModel;
import com.tubemarket.models.VideoModel;
import com.tubemarket.models.ViewsSecondsModel;
import com.tubemarket.models.VipDataModel;
import com.tubemarket.models.WithdrawDataModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<AdPayModel> addViews(@Header("Authorization") String token,
                              @Field("user_id") String user_id,
                              @Field("views_number") String views_number,
                              @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                              @Field("total_cost") String total_cost,
                              @Field("link") String link,
                              @Field("watch_time") String watch_time,
                              @Field("channel_name") String channel_name,
                              @Field("channel_image") String channel_image


    );

    @FormUrlEncoded
    @POST("api/get-subscriptions")
    Call<AdPayModel> addSubscribes(@Header("Authorization") String token,
                                   @Field("user_id") String user_id,
                                   @Field("subscription_limit") String subscription_limit,
                                   @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                                   @Field("total_cost") String total_cost,
                                   @Field("link") String link,
                                   @Field("channel_name") String channel_name,
                                   @Field("channel_image") String channel_image,
                                   @Field("timer_limit") String timer_limit

    );

    @FormUrlEncoded
    @POST("api/get-subscription-and-views")
    Call<AdPayModel> addSubscribesViews(@Header("Authorization") String token,
                                        @Field("user_id") String user_id,
                                        @Field("subscription_limit") String subscription_limit,
                                        @Field("views_number") String views_number,
                                        @Field("watch_time") String watch_time,
                                        @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                                        @Field("total_cost") String total_cost,
                                        @Field("link") String link,
                                        @Field("channel_name") String channel_name,
                                        @Field("channel_image") String channel_image

    );

    @FormUrlEncoded
    @POST("api/get-likes")
    Call<AdPayModel> addLikes(@Header("Authorization") String token,
                              @Field("user_id") String user_id,
                              @Field("likes_limit") String likes_limit,
                              @Field("estimated_arrive_time_per_day") String estimated_arrive_time_per_day,
                              @Field("total_cost") String total_cost,
                              @Field("link") String link,
                              @Field("watch_time") String watch_time,
                              @Field("channel_name") String channel_name,
                              @Field("channel_image") String channel_image


    );

    @GET("api/show-buy-messages")
    Call<BuyMessageDataModel> showMessages(@Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("api/create-normal-message")
    Call<AddMessageDataModel> addMessage(@Header("Authorization") String token,
                                         @Field("user_id") String user_id,
                                         @Field("buy_message_id") String buy_message_id,
                                         @Field("link") String link,
                                         @Field("contents") String contents

    );

    @FormUrlEncoded
    @POST("api/create-with-coupon-message")
    Call<MessageResponseModel> createMessageWithCoupon(@Header("Authorization") String token,
                                                       @Field("user_id") String user_id,
                                                       @Field("buy_message_id") String buy_message_id

    );


    @FormUrlEncoded
    @POST("api/update-with-coupon-message")
    Call<MessageResponseModel> updateMessageWithCoupon(@Header("Authorization") String token,
                                                       @Field("user_id") String user_id,
                                                       @Field("id") String id,
                                                       @Field("buy_message_id") String buy_message_id,
                                                       @Field("link") String link,
                                                       @Field("contents") String contents,
                                                       @Field("coupon_code") String coupon_code


    );

    @GET("api/get-user-messages")
    Call<UserMessagesDataModel> getUserMessages(@Header("Authorization") String token,
                                                @Query("user_id") String user_id
    );

    @GET("api/show-videos-advertisements")
    Call<AdsViewDataModel> getAdsView(@Header("Authorization") String token,
                                      @Query("user_id") String user_id,
                                      @Query("orderBy") String orderBy
    );

    @FormUrlEncoded
    @POST("api/view-advertisement")
    Call<StatusResponse> viewAds(@Header("Authorization") String token,
                                 @Field("user_id") String user_id,
                                 @Field("advertisement_user_id") String advertisement_user_id,
                                 @Field("seconds") String seconds


    );

    @GET("api/get-all-subscriptions-advertisements")
    Call<AdsViewDataModel> getAllSubscriptions(@Header("Authorization") String token,
                                               @Query("user_id") String user_id,
                                               @Query("orderBy") String orderBy
    );


    @FormUrlEncoded
    @POST("api/like-advertisement")
    Call<StatusResponse> likeAdVideo(@Header("Authorization") String token,
                                     @Field("user_id") String user_id,
                                     @Field("advertisement_user_id") String advertisement_user_id,
                                     @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api/subscription-advertisement")
    Call<StatusResponse> subscribeAdChannel(@Header("Authorization") String token,
                                            @Field("user_id") String user_id,
                                            @Field("advertisement_user_id") String advertisement_user_id
    );


    @Multipart
    @POST("api/send-admin-chat-message")
    Call<SingleAdminMessageDataModel> sendAdminChatAttachment(@Header("Authorization") String bearer_token,
                                                              @Part("user_id") RequestBody user_id,
                                                              @Part("admin_room_id") RequestBody admin_room_id,
                                                              @Part("admin_id") RequestBody admin_id,
                                                              @Part("type") RequestBody type,
                                                              @Part MultipartBody.Part attachment
    );

    @GET("api/admin-room-messages")
    Call<AdminMessageDataModel> getAdminChatMessage(@Header("Authorization") String user_token,
                                                    @Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/send-admin-chat-message")
    Call<SingleAdminMessageDataModel> sendAdminChatMessage(@Header("Authorization") String bearer_token,
                                                           @Field("user_id") String user_id,
                                                           @Field("admin_room_id") String admin_room_id,
                                                           @Field("admin_id") String admin_id,
                                                           @Field("type") String type,
                                                           @Field("message") String message


    );

    @FormUrlEncoded
    @POST("api/pay-subscribe-to-golden-account")
    Call<PayModel> payGoldAccount(@Header("Authorization") String token,
                                  @Field("user_id") String user_id,
                                  @Field("vip_coin_id") String vip_coin_id
    );

    @FormUrlEncoded
    @POST("api/pay-buy-coins")
    Call<PayModel> buyPayCoin(@Header("Authorization") String token,
                              @Field("user_id") String user_id,
                              @Field("buy_coin_id") String buy_coin_id
    );

    @GET("api/my-advertisement")
    Call<MyAdsDataModel> getMyAds(@Header("Authorization") String user_token,
                                  @Query("user_id") String user_id);


    @GET("youtube/v3/search")
    Call<ChannelUrlModel> getChannelIdFromUrl(@Query("part") String part,
                                              @Query("q") String videoUrl,
                                              @Query("key") String key
    );

    @GET("youtube/v3/playlists")
    Call<VideoModel> getPlaylistByChannelId(@Query("part") String part,
                                            @Query("channelId") String channelId,
                                            @Query("key") String key
    );

    @GET("youtube/v3/playlistItems")
    Call<VideoModel> getPlaylistItemByPlaylistId(@Query("part") String part,
                                                 @Query("playlistId") String playlistId,
                                                 @Query("key") String key
    );

    @FormUrlEncoded
    @POST("api/delete-advertisement")
    Call<StatusResponse> deleteMyAds(@Header("Authorization") String token,
                                     @Field("user_id") String user_id,
                                     @Field("advertisement_id") String advertisement_id
    );

    @FormUrlEncoded
    @POST("api/activate-channel-gain")
    Call<AdPayModel> addProfitChannel(@Header("Authorization") String token,
                                      @Field("user_id") String user_id,
                                      @Field("contact_number") String contact_number,
                                      @Field("channel_link") String channel_link,
                                      @Field("channel_name") String channel_name,
                                      @Field("channel_image") String channel_image

    );

    @FormUrlEncoded
    @POST("api/exchange-coins")
    Call<StatusResponse> exchangeCoins(@Header("Authorization") String token,
                                       @Field("user_id") String user_id,
                                       @Field("exchange_id") String exchange_id,
                                       @Field("cost") String cost,
                                       @Field("contact_data") String contact_data
    );

    @FormUrlEncoded
    @POST("api/activate-coupon")
    Call<StatusResponse> activateCoupon(@Header("Authorization") String token,
                                       @Field("user_id") String user_id,
                                       @Field("coupon_code") String coupon_code
    );

}


