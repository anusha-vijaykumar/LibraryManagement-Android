package com.example.anusha.library;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.anusha.library.api.PlaceSearchAPI;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by anusha on 8/9/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private MarkerOptions[] places;
    private int userlocation,librarylocation;
    private Marker userMarker;
    LocationManager locationManager;
    Marker[] placeMarkers;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.librarymap, container, false);//inflate this xml file onto parent view
       // setContentView(R.layout.librarymap);
        userlocation = R.drawable.locationpinone;
        librarylocation = R.drawable.locationpintwo;
        placeMarkers =  new Marker[20];

        //find out if we already have it
        if (mMap == null) {
            //get the map
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            //check in case map/ Google Play services not available
            if (mMap != null) {
                //ok - proceed
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //create marker array
                placeMarkers = new Marker[20];
                //update location
                updateplaces();
            }

        }


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        try {
            mMap.setMyLocationEnabled(true);
        }catch (SecurityException e){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            if (checkPermission()) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMap != null) {
            if (checkPermission()) {

                locationManager.removeUpdates(this);
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }


    }




    @Override
    public void onLocationChanged(Location location) {
    updateplaces();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void updateplaces()
    {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try{
        Location lastlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double latitude = lastlocation.getLatitude();
            double longitude = lastlocation.getLongitude();

            LatLng lastlatlng = new LatLng(latitude,longitude);
            if(userMarker != null) userMarker.remove();
            userMarker = mMap.addMarker(new MarkerOptions().position(lastlatlng).title("here").icon(BitmapDescriptorFactory.fromResource(userlocation)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastlatlng));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.animateCamera(zoom);
           // CameraUpdate center = CameraUpdateFactory.newLatLng(lastlatlng);
            //CameraUpdate
            String placesearch;
            service(latitude+","+longitude);
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }

    }

    public void service(String lastlatlng)
    {
        String sensor = "true";
        String key = "AIzaSyCVHdD_q2L6yeGUs-yuLuLcUXbtT7yjE7I";
        String radius = "1000";
            String library = "library";
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com/maps/api/place/nearbysearch").setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("librarysearch")).build();
        PlaceSearchAPI search = restAdapter.create(PlaceSearchAPI.class);
        search.get(lastlatlng, radius, library, key, sensor, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {

                String output = BufferReaderOutput.BufferReaderOutput(response);
                //parse place data returned from Google Places
                //remove existing markers
                if (placeMarkers != null) {
                    for (int pm = 0; pm < placeMarkers.length; pm++) {
                        if (placeMarkers[pm] != null)
                            placeMarkers[pm].remove();
                    }
                }
                try {
                    //parse JSON

                    //create JSONObject, pass stinrg returned from doInBackground
                    Log.e("test",output);
                    JSONObject resultObject = new JSONObject(output);
                    //get "results" array
                    JSONArray placesArray = resultObject.getJSONArray("results");
                    //marker options for each place returned
                    places = new MarkerOptions[placesArray.length()];
                    //loop through places

                    for (int p = 0; p < placesArray.length(); p++) {
                        //parse each place
                        //if any values are missing we won't show the marker
                        boolean missingValue = false;
                        LatLng placeLL = null;
                        String placeName = "";
                        String vicinity = "";
                        int currIcon = userlocation;
                        try {
                            //attempt to retrieve place data values
                            missingValue = false;
                            //get place at this index
                            JSONObject placeObject = placesArray.getJSONObject(p);
                            //get location section
                            JSONObject loc = placeObject.getJSONObject("geometry")
                                    .getJSONObject("location");
                            //read lat lng
                            placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                    Double.valueOf(loc.getString("lng")));
                           // Toast.makeText(getActivity(), loc.getString("lat"), Toast.LENGTH_SHORT).show();
                            //get types
                            JSONArray types = placeObject.getJSONArray("types");
                            //loop through types
                            for (int t = 0; t < types.length(); t++) {
                                //what type is it
                                String thisType = types.get(t).toString();
                                //check for particular types - set icons
                                if (thisType.contains("library")) {
                                    currIcon = librarylocation;
                                    break;
                                }
                            }
                            //vicinity
                            vicinity = placeObject.getString("vicinity");
                            //name
                            placeName = placeObject.getString("name");
                        } catch (JSONException jse) {
                            Log.e("PLACES", "missing value");
                            missingValue = true;
                            jse.printStackTrace();
                        }
                        //if values missing we don't display
                        if (missingValue) {
                            places[p] = null;
                            Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // place if here
                            Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                            places[p] = new MarkerOptions()
                                    .position(placeLL)
                                    .title(placeName)
                                    .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                    .snippet(vicinity);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (places != null && placeMarkers != null) {
                    for (int p = 0; p < places.length && p < placeMarkers.length; p++) {
                        //will be null if a value was missing
                        if (places[p] != null)
                            placeMarkers[p] = mMap.addMarker(places[p]);
                    }
                }

            }


            @Override
            public void failure(RetrofitError error) {

            }
        });


    }
}
