package com.example.anusha.library;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anusha on 7/17/2016.
 */
 public class Sharedpreference {

    public static void insert_data_into_sharedpreference(Context context, String key, String value) {
        SharedPreferences shareddata = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareddata.edit();
        editor.putString(key, value).commit(); // adding userid value into sharedpreference
    }

    public static String get_data_from_sharedpreference(Context context, String key, String Default){
        SharedPreferences shareddata = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        return shareddata.getString(key,Default);// if key has no value sharedpreference returns default value
    }

    public static void clear_data(Activity context)
    {
        SharedPreferences shareddata = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareddata.edit();
        editor.clear().commit();// to remove jus a particular key use == remove(keyname)
    }
}
