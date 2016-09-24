package com.example.anusha.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anusha.library.adapter.Adapter;
import com.example.anusha.library.adapter.RenewAdapter;
import com.example.anusha.library.api.RenewAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by anusha on 8/11/2016.
 */
public class bookRenew extends Fragment {


    ImageView image;
    TextView title;
    TextView author;
    TextView genre;
    View view;
    ArrayList<Modelitem> arrayitems = new ArrayList<Modelitem>();
    public String value;
    RenewAdapter customadapter;
    private ListView listView;

    public static bookRenew newIntsance(String h) {
        bookRenew fragment = new bookRenew();
        Bundle bundle = new Bundle();
        bundle.putString("key", h);
        fragment.setArguments(bundle);
        return fragment;
    }

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.renew_listview, container, false);
        listView = (ListView)v.findViewById(R.id.listView);
        return v;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value = getArguments().getString("key");
        setHasOptionsMenu(true);// will tell about extra menu present and display that,,, will call oncreateoptionsmenu

    }

    @Override
    public void onResume() {
        super.onResume();
        serviceCall();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bookrenew, menu);// display fragmentone menu here-- new menu called fragmentone created in menu folder with extra option

    }


    public void serviceCall(){// have to pass view while implementing onclick()
        //dialog.show();

        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        RenewAPI renew = restAdapter.create(RenewAPI.class);
        renew.post(Sharedpreference.get_data_from_sharedpreference(getActivity(),"userid",""),new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                JSONArray jsonarray;
                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    JSONArray object = new JSONArray(str);
                    JSONObject jsonobject ;
                    for(int i=0;i<object.length();i++){
                        Modelitem modelitem = new Modelitem();
                        jsonobject = object.getJSONObject(i);
                        modelitem.setBookimage(jsonobject.getString("BOOK_IMAGE"));
                        modelitem.setGenre(jsonobject.getString("GENRE"));
                        modelitem.setBooktitle(jsonobject.getString("BOOK_TITLE"));
                        modelitem.setAuthor(jsonobject.getString("AUTHOR"));
                        arrayitems.add(modelitem);
                    }
                    customadapter = new RenewAdapter(getActivity(), arrayitems);
                    listView.setAdapter(customadapter);// link adapter and list view object for display
                    }catch(JSONException e){
                    e.printStackTrace();
                }
                //dialog.dismiss();



            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(),"Connection Error! Please try again after some time.",Toast.LENGTH_LONG).show();
                //dialog.dismiss();
            }
        });

    }
}
