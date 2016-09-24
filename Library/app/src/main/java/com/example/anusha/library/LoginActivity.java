package com.example.anusha.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anusha.library.api.LoginAPI;
import com.example.anusha.library.gcm.RegistrationIntentService;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by anusha on 7/27/2016.
 */
public class LoginActivity extends Activity{

    EditText email;
    EditText password;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if(!Sharedpreference.get_data_from_sharedpreference(LoginActivity.this,"userid","NULL").equals("NULL"))
        {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceCall();
            }
        });
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    public void serviceCall(){// have to pass view while implementing onclick()
        //dialog.show();
        // initialse retrofit adapter-- to tell we r using retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Library.url).setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Login")).build();
        LoginAPI login = restAdapter.create(LoginAPI.class);
        login.post(email.getText().toString(), password.getText().toString(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // BufferReaderOutput(response2)
                JSONObject object;
                try {
                    String str = BufferReaderOutput.BufferReaderOutput(response2);
                    object = new JSONObject(str);
                    String responsecode = object.getString("CODE");
                    String msg = object.getString("MESSAGE");
                    if(responsecode.equals("200"))
                    {
                        String userid = object.getString("USER_ID");
                        String firstname = object.getString("FIRSTNAME");
                        String lastname = object.getString("LASTNAME");
                        String photo = object.getString("PHOTO");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       Sharedpreference.insert_data_into_sharedpreference(LoginActivity.this,"userid",userid);
                        Sharedpreference.insert_data_into_sharedpreference(LoginActivity.this,"firstname",firstname);
                        Sharedpreference.insert_data_into_sharedpreference(LoginActivity.this,"lastname",lastname);
                        Sharedpreference.insert_data_into_sharedpreference(LoginActivity.this,"photo",photo);
                        startActivity(intent);// or getActivity().start.... wen in fragment

                        finish();// will destroy my login activity completely
                    }
                    else if(responsecode.equals("401"))
                    {
                        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                //dialog.dismiss();



            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(LoginActivity.this,"Connection Error, Please try again later",Toast.LENGTH_LONG).show();
                //dialog.dismiss();
            }
        });

    }



    public String create_jsonheader(String username,String password) {

        JSONObject user = new JSONObject();
        try {
            user.put("loginid", username);
            user.put("password", password);



        } catch (Exception e) {
            e.printStackTrace();
        }

        return user.toString();
    }

}
