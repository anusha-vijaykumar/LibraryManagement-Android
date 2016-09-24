package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by anusha on 8/16/2016.
 */
public interface BuyBookAPI {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/orderBook")
    public void post(
            @Field ("book_id") int book_id,
            @Field("user_id") String user_id,
            Callback<Response> callback); // to get trigger once result s obtained from server


}
