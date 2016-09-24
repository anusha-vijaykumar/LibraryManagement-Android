package com.example.anusha.library.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.anusha.library.R;
import com.example.anusha.library.SplashScreen;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

public class GCMMessageHandler extends GcmListenerService {
    public static final int NOTIFICATION_ID = 1;
    public static int sNotificationCount = 0;

    String message;
    String mTitle;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        message = data.getString("message");
        mTitle = data.getString("title");
        Log.e("msg", "empty");

        //ServiceCall();

    /*    if(message != null){
            ServiceHandler serviceHandler = new_msg ServiceHandler();
            String jsonStr = serviceHandler.makeHttpRequest("http://www.samyadh-jain.com/p/g/services/pageContents.php","",new_msg LinkedList<NameValuePair>());
            Log.d("Response",jsonStr);
            //generateNotification(getApplicationContext(), message);
        } else {
            Log.d("Message","null");
        }*/

        Log.e("msg", "0");
        //   android.os.Debug.waitForDebugger();
        Log.e("msg", "1");
        // ServiceHandler serviceHandler = new_msg ServiceHandler();
        //String jsonStr = serviceHandler.makeHttpRequest("http://www.samyadh-jain.com/p/g/services/pageContents.php", "", new_msg LinkedList<NameValuePair>());
        // Log.d("Response", jsonStr);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
           // ServiceCall();
            e.printStackTrace();
        }
        sendNotification(mTitle);
    }

    // Creates notification based on title and body received


    private void sendNotification(String message) {
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        Bundle b = new Bundle();
        b.putString("isPushNotify", "true");
        intent.putExtras(b);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        // Notifications using both a large and a small icon (which yours should!) need the large
        // icon as a bitmap. So we need to create that here from the resource ID, and pass the
        // object along in our notification builder. Generally, you want to use the app icon as the
        // small icon, so that users understand what app is triggering this notification.
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("GTvMN")
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



}