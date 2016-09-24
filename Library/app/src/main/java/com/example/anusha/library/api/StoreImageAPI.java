package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by anusha on 8/18/2016.
 */
public interface StoreImageAPI {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/storeimage")
    public void post(
            @Field("user_id") String user_id,
            @Field("image") String image,
            Callback<Response> callback); // to get trigger once result s obtained from server

}
