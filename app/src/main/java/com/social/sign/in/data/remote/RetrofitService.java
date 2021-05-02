package com.social.sign.in.data.remote;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface RetrofitService {

    @GET
    Call<JsonObject> getAccessToken(@Url String url);

    @GET
    Call<JsonObject> getUser(@Url String url, @Header("Authorization") String token);
}
