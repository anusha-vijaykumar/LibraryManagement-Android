package com.example.anusha.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anusha.library.adapter.Adapter;
import com.example.anusha.library.api.OrderAPI;

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
 * Created by anusha on 7/19/2016.
 */
public class Fragmentfirst extends Fragment {

    ArrayList<Modelitem> arrayitems = new ArrayList<Modelitem>();
    public String value;
    Adapter customadapter;
    private ListView listView;
    EditText searchtext;
    ImageView searchicon;
    ImageView moreinfo;


    public static Fragmentfirst newIntsance(String h) {
        Fragmentfirst fragment = new Fragmentfirst();
        Bundle bundle = new Bundle();
        bundle.putString("key", h);
        fragment.setArguments(bundle);
        return fragment;
    }

    View v;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragmentfirst, container, false);//inflate this xml file onto parent view
        listView = (ListView) v.findViewById(R.id.listView);
        searchicon = (ImageView) v.findViewById(R.id.searchicon);//get id's for searchicon and searchtext to create API call
        searchtext = (EditText) v.findViewById(R.id.searchtext);

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("test","ff");

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customadapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //init();
        // ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,a);// adapter for refresh purpoes and arryadapter to display array parameters
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Modelitem mitem = (Modelitem) parent.getItemAtPosition(position);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                // ft.replace(R.id.container,ItemDetails.newInstance( mitem.getDescription(),mitem.getTitle())).addToBackStack("Fragment1").commit();

            }

        });
       /* moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modelitem modelitem =
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container,ItemDetails.newInstance( modelitem.getBooktitle(),modelitem.getGenre())).addToBackStack("Fragment1").commit();

            }
        });*/


        return v;
    }

   /* private void init() {
        arrayitems.add(new Modelitem("Red Riding Hood", "http://pngimg.com/upload/book_PNG2111.png", 500.90, "bed time"));
        arrayitems.add(new Modelitem("dark Nights", "http://www.thefutureorganization.com/wp-content/uploads/2014/03/book-image-crop.jpg", 900.788, "horror"));
        arrayitems.add(new Modelitem("small world of fun", "http://lytherus.com/wp-content/uploads/2012/02/jk-rowling-new-book-mock-cover.png", 90.7, "romance"));

    }*/

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
        inflater.inflate(R.menu.fragmentfirst, menu);// display fragmentone menu here-- new menu called fragmentone created in menu folder with extra option

    }


    public void serviceCall(){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        OrderAPI order = restAdapter.create(OrderAPI.class);
        order.post(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                arrayitems.clear();

                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    JSONArray object = new JSONArray(str);
                    JSONObject jsonobject ;
                    for(int i=0;i<object.length();i++){
                        Modelitem modelitem = new Modelitem();
                        jsonobject = object.getJSONObject(i);
                        modelitem.setBookimage(jsonobject.getString("BOOK_IMAGE"));
                        modelitem.setGenre(jsonobject.getString("GENRE"));
                        modelitem.setBooktitle(jsonobject.getString("TITLE"));
                        modelitem.setAuthor(jsonobject.getString("AUTHOR"));
                        arrayitems.add(modelitem);
                    }
                    customadapter = new Adapter(getActivity(), arrayitems);
                    listView.setAdapter(customadapter);// link adapter and list view object for display

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


