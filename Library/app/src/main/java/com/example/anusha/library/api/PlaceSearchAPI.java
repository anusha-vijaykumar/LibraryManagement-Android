package com.example.anusha.library.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by anusha on 8/18/2016.
 */
public interface PlaceSearchAPI {

    @GET("/json")
    void get(@Query("location") String location,
             @Query("radius") String radius,
             @Query("type") String type,
             @Query("key") String key,
             @Query("sensor") String sensor,
             Callback<JsonObject> callback);



}
