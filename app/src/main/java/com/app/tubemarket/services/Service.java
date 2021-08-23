package com.app.tubemarket.services;

import com.app.tubemarket.models.VideoModel;

import retrofit2.Call;
import retrofit2.http.GET;
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
}

