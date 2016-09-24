package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
// using retrofit to create API for server processing, dont need Asyntask

/**
 * Created by anusha on 7/16/2016.
 */
public interface LoginAPI {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/login")
    public void post(
            @Field("email") String name,
            @Field("password") String email,
            Callback<Response> callback); // to get trigger once result s obtained from server

}
