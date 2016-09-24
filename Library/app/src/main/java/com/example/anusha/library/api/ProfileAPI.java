package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by anusha on 7/31/2016.
 */
public interface ProfileAPI  {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/profile")
    public void post(
            @Field("user_id") String user_id,
            Callback<Response> callback); // to get trigger once result s obtained from server
}

