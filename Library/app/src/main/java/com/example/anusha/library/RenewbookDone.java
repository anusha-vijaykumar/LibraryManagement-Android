package com.example.anusha.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.anusha.library.api.BuyBookAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by anusha on 8/17/2016.
 */
public class RenewbookDone extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mbooktitle;
    private String mbookauthor;

    public RenewbookDone() {
        // Required empty public constructor
    }

    public static ItemDetails newInstance(String param1, String param2) {
        ItemDetails fragment = new ItemDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mbooktitle = getArguments().getString(ARG_PARAM1);
            mbookauthor = getArguments().getString(ARG_PARAM2);
            serviceCall_renewbook(mbooktitle,mbookauthor);
        }

    }



   public void serviceCall_renewbook(String title, String author){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        RenewBookAPI renewbook = restAdapter.create(RenewBookAPI.class);
        renewbook.post(title,author,new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                JSONObject object;
                JSONArray jsonarray;
                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    object = new JSONObject(str);
                    // String responsecode = object.getString("code");
                    object = new JSONObject(str);
                    String responsecode = object.getString("CODE");
                    String message = object.getString("MESSAGE");
                    if(responsecode.equals(200)){
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }


                }catch(JSONException e){
                    e.printStackTrace();
                }
                //dialog.dismiss();



            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(),"Connection Error, Please try again later",Toast.LENGTH_LONG).show();
                //dialog.dismiss();
            }
        });

    }


}
