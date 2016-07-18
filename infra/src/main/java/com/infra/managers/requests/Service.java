package com.infra.managers.requests;

import com.infra.managers.home.models.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("stream/0/posts/stream/global")
    Call<Feed> getLatestNews();
}
