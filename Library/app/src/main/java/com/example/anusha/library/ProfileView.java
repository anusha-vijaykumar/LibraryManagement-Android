package com.example.anusha.library;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anusha.library.api.ChangeProfileAPI;
import com.example.anusha.library.api.ProfileAPI;
import com.example.anusha.library.api.StoreImageAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileView extends Fragment implements View.OnClickListener{
    ImageView image;
    TextView firstname_tv;
    TextView lastname_tv;
    TextView address_tv;
    TextView age_tv;
    View view;
    boolean flag = true;
    ImageView addressicon;
    TextView uploadimage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
       public ProfileView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProfileView.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileView newInstance() {
        ProfileView fragment = new ProfileView();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        serviceCall();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_view, container, false);
        addressicon = (ImageView)view.findViewById(R.id.editbutton);
        addressicon.setOnClickListener(this);
        uploadimage = (TextView)view.findViewById(R.id.uploadimage);
        uploadimage.setOnClickListener(this);
        firstname_tv = (TextView) view.findViewById(R.id.firstname);
        lastname_tv = (TextView)view.findViewById(R.id.lastname);
        address_tv = (TextView)view.findViewById(R.id.address);
        age_tv = (TextView)view.findViewById(R.id.age);
        image = (ImageView)view.findViewById(R.id.profilepic);
        return view;
    }

   /* public void newaddress(View view)
    {

        String address = address_tv.getText().toString();
        String firstname = firstname_tv.getText().toString();
        String lastname = lastname_tv.getText().toString();
        String age = age_tv.getText().toString();
        servicecall_changeprofile(firstname, lastname, age, address);

    }
*/

    public void servicecall_changeprofile(String firstname, String lastname, String age, String address)
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        ChangeProfileAPI changeprofile = restAdapter.create(ChangeProfileAPI.class);
        changeprofile.post(firstname, lastname, age, address, Sharedpreference.get_data_from_sharedpreference(getActivity(),"userid",""),new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                JSONObject object;
                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    object = new JSONObject(str);
                    String responsecode = object.getString("CODE");
                    if(responsecode.equals("200"))
                    {
                        Toast.makeText(getActivity(),"Successfully updated!! ",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Connection Error! Please try again after some time.",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
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
    public void serviceCall(){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        ProfileAPI login = restAdapter.create(ProfileAPI.class);
        login.post( Sharedpreference.get_data_from_sharedpreference(getActivity(),"userid",""),new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                JSONObject object;
                JSONArray jsonarray;
                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    jsonarray = new JSONArray(str);
                   // String responsecode = object.getString("code");
                    object = jsonarray.getJSONObject(0);
                    if(object.has("FIRSTNAME"))
                    {
                        String firstname = object.getString("FIRSTNAME");
                        String imageurl = object.getString("PHOTO");
                        Bitmap bitmap_image = StringToBitMap(imageurl);
                        String lastname = object.getString("LASTNAME");
                        String age = object.getString("AGE");
                        String address = object.getString("ADDRESS");


                        firstname_tv.setText(firstname);

                        lastname_tv.setText(lastname);

                        age_tv.setText(age);

                        address_tv.setText(address);

                        image.setImageBitmap(bitmap_image);
                       /* Picasso.with(getActivity())
                                .load(imageurl)
                                .placeholder(R.drawable.abc_ab_share_pack_mtrl_alpha)
                                .error(R.drawable.errortriangle)
                                .into(image);
                       */ // intent.putExtra("userid",userid);
                        //Sharedpreference.insert_data_into_sharedpreference(LoginActivity.this,"userid",userid);
                       // getActivity().finish();// will destroy my login activity completely
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Connection Error! Please try again after some time.",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
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

    @Override
    public void onResume() {
        super.onResume();
        }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editbutton:
                uploadimage.setVisibility(View.VISIBLE);
                address_tv.setEnabled(true);
                firstname_tv.setEnabled(true);
                lastname_tv.setEnabled(true);
                age_tv.setEnabled(true);
                image.setEnabled(true);
                if(flag == true) {

                    addressicon.setImageResource(R.drawable.arrow);
                    flag = false;
                }
                else if (flag == false){
                    String address = address_tv.getText().toString();
                    String firstname = firstname_tv.getText().toString();
                    String lastname = lastname_tv.getText().toString();
                    String age = age_tv.getText().toString();
                    servicecall_changeprofile(firstname,lastname,age,address);
                    addressicon.setImageResource(R.drawable.edit);
                    address_tv.setEnabled(false);
                    firstname_tv.setEnabled(false);
                    lastname_tv.setEnabled(false);
                    age_tv.setEnabled(false);
                    uploadimage.setVisibility(View.INVISIBLE);
                    flag = true;
                }
                break;
            case R.id.uploadimage:
                Log.e("uploadimageclick", "onClick: ");
            uploadimagedialog();
                break;

        }
    }

    private void uploadimagedialog()
    {
        final Dialog customdialog = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.uploadimage,null);
        customdialog.setContentView(view);
        RelativeLayout gallery = (RelativeLayout)view.findViewById(R.id.gallery);
        RelativeLayout camera = (RelativeLayout)view.findViewById(R.id.camera);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 44);//one can be replaced with any action code
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 88);//zero can be replaced with any action code
            }
        });
        customdialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result","onActivityResult: "+resultCode );
        switch(requestCode) {
            case 88:
                if(resultCode == getActivity().RESULT_OK){
                 //   Uri selectedImage = data.getData();
                    Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                    image.setImageBitmap(bitmap);
                   String imagestring =  BitMapToString(bitmap);
                    Log.e("imagetsring","is"+imagestring);
                    servicecall_storeimage(imagestring);
                   // String image =
                }
                break;
            case 44:
                if(resultCode == getActivity().RESULT_OK && data != null){
                    Uri selectedImage = data.getData();
                    try{
                        Log.e("galleryuri","here"+selectedImage);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImage);
                        image.setImageBitmap(bitmap);
                        String imagestring =  BitMapToString(bitmap);
                        //Log.e("imagetsring","is"+imagestring);
                        servicecall_storeimage(imagestring);
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                   // String imagestring =  BitMapToString(bitmap);
                    //Log.e("imagetsring","is"+imagestring);
                    //servicecall_storeimage(imagestring);

                }
                break;
        }
    }



    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }



    public void servicecall_storeimage(String imagestring){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        StoreImageAPI storeimage = restAdapter.create(StoreImageAPI.class);
        storeimage.post( Sharedpreference.get_data_from_sharedpreference(getActivity(),"userid",""),imagestring,new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)

                try {
                    String output = BufferReaderOutput.BufferReaderOutput(response2);
                    JSONObject root = new JSONObject(output);
                    String ResponseCode = root.getString("CODE");
                    String message = root.getString("MESSAGE");

                    if(ResponseCode.equals("200"))
                    {
                           Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Connection Error! Please try again after some time.",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
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


    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}


