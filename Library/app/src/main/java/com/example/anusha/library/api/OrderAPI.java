package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by anusha on 8/8/2016.
 */
public interface OrderAPI {

    @POST("/Library_Management/userLogin/index.php/order")
    public void post(
            Callback<Response> callback); // to get trigger once result s obtained from server

}
