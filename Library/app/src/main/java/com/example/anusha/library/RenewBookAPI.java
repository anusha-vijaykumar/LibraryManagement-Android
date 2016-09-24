package com.example.anusha.library;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by anusha on 8/17/2016.
 */
public interface RenewBookAPI {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/renewBook")
    public void post(
            @Field("title") String title,
            @Field("author") String author,
            Callback<Response> callback); // to get trigger once result s obtained from server
}
