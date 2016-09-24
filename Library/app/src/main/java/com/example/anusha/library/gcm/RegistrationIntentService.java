package com.example.anusha.library.gcm;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.anusha.library.BufferReaderOutput;
import com.example.anusha.library.Library;
import com.example.anusha.library.R;
import com.example.anusha.library.Sharedpreference;
import com.example.anusha.library.api.ServiceRegistrationAPI;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;


public class RegistrationIntentService extends IntentService {

    public static final String GCM_TOKEN = "gcmToken";
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private String deviceId;


    public RegistrationIntentService() {
        super(TAG);
    }

    // [END subscribe_topics]
    public static JsonObject createJSONHeader(String gcm_id, String device_id) {
        JsonObject json = new JsonObject();
        try {
            json.addProperty("appId", device_id);
            json.addProperty("deviceId", gcm_id);
            json.addProperty("devicetype", "ANDROID");

        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        return json;

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String senderId = getResources().getString(R.string.gcm_defaultSenderId);
            String token = instanceID.getToken(senderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.e(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);
            //sendRegistrationToMyServer(token);

            // Subscribe to topic channels
            //subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            Sharedpreference.insert_data_into_sharedpreference(this,QuickstartPreferences.SENT_TOKEN_TO_SERVER, "true");
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new_msg token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            Sharedpreference.insert_data_into_sharedpreference(this,QuickstartPreferences.SENT_TOKEN_TO_SERVER, "false");
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new_msg token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        //   Utilities.putSharedString(GCM_TOKEN, token, this);
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Library.url)
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("gcm in services"))
                .build();
        ServiceRegistrationAPI api = adapter.create(ServiceRegistrationAPI.class);
        JsonObject jsonObj = createJSONHeader(token, deviceId);
        Log.d("JSON", jsonObj.toString());
        api.post(Sharedpreference.get_data_from_sharedpreference(this,"userid",""),token, new Callback<Response>() {

            @Override
            public void success(Response object, Response response) {
                String output = BufferReaderOutput.BufferReaderOutput(response);
                try {
                    JSONObject root = new JSONObject(output);
                    String ResponseCode = root.getString("CODE");

                    if (ResponseCode.equals("2000")) {
                        Log.d(TAG, "GCM Successfully uploaded");
                    } else  {
                        Log.d(TAG, "GCM failed to uploaded");
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (retrofitError.getKind().equals(RetrofitError.Kind.NETWORK)) {
                }

            }
        });

    }


}