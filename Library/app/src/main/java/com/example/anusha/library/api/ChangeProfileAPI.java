package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by anusha on 8/9/2016.
 */
public interface ChangeProfileAPI {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/changeprofile")
    public void post(

            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("age") String age,
            @Field("address") String address,
            @Field("user_id") String user_id,
            Callback<Response> callback); // to get trigger once result s obtained from server

}
