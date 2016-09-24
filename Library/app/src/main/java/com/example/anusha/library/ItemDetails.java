package com.example.anusha.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anusha.library.api.BuyBookAPI;
import com.example.anusha.library.api.ItemDetailsAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link ItemDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //View view;
    String bookid;
    ImageView book_image;
    TextView title;
    TextView author;
    TextView bookgenre;
    TextView bookdescription;

    ImageView buyicon;
    // TODO: Rename and change types of parameters
    private String mbooktitle;
    private String mbookauthor;
    private View view;
    //private OnFragmentInteractionListener mListener;

    public ItemDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemDetails.
     */
    // TODO: Rename and change types and number of parameters
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
            serviceCall(mbooktitle,mbookauthor);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.itemdetails, container, false);
        title = (TextView)view.findViewById(R.id.booktitle);
        book_image = (ImageView)view.findViewById(R.id.list_image);
        author = (TextView)view.findViewById(R.id.author);
        bookgenre = (TextView)view.findViewById(R.id.genre);
        bookdescription = (TextView)view.findViewById(R.id.description);

        buyicon = (ImageView)view.findViewById(R.id.buyicon);
        buyicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String book_title = title.getText().toString();
                String book_author = author.getText().toString();
                servicecall_buybook(bookid);
            }
        });
        return view;

    }





    public void servicecall_buybook(String bookid){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        BuyBookAPI buybook = restAdapter.create(BuyBookAPI.class);
        buybook.post(Integer.parseInt(bookid),Sharedpreference.get_data_from_sharedpreference(getActivity(),"userid",""),new Callback<Response>() {
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
                    Log.e("responsecode","here="+responsecode);
                    String message = object.getString("MESSAGE");
                    if(responsecode.equals("200")){
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }
                    else if(responsecode.equals("201"))
                    {
                        Toast.makeText(getActivity(),"Sorry, could not place order due to invalid stock of books",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity(),"Connection error, please try again later!!",Toast.LENGTH_LONG).show();
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




    public void serviceCall(String booktitle, String bookauthor){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        ItemDetailsAPI detail = restAdapter.create(ItemDetailsAPI.class);
        detail.post(booktitle,bookauthor,new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                JSONObject object;
                JSONArray jsonarray;
                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    object = new JSONObject(str);
                    // String responsecode = object.getString("code");
                   // object = object.getJSONObject(0);
                    if(object.has("BOOK_IMAGE")){
                        bookid  = object.getString("BOOK_ID");
                        String bookimage = object.getString("BOOK_IMAGE");
                        String booktitle = object.getString("BOOK_TITLE");
                        String bookauthor = object.getString("BOOK_AUTHOR");
                        String description = object.getString("DESCRIPTION");
                        String genre = object.getString("GENRE");
                        Picasso.with(getActivity())
                                .load(bookimage)
                                .placeholder(R.drawable.abc_ab_share_pack_mtrl_alpha)
                                .error(R.drawable.errortriangle)
                                .into(book_image);
                        title.setText(booktitle);
                        author.setText(bookauthor);
                        bookgenre.setText(genre);
                        bookdescription.setText(description);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Connection Error! Please try again after some time.",Toast.LENGTH_LONG).show();
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



/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */


