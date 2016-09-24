package com.example.anusha.library.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by anusha on 8/12/2016.
 */
public interface ItemDetailsAPI {

    @FormUrlEncoded
    @POST("/Library_Management/userLogin/index.php/itemdetails")
    public void post(
            @Field("book_title") String book_title,
            @Field("book_author") String book_author,
           Callback<Response> callback); // to get trigger once result s obtained from server

}
