package com.example.cityguideapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card1, card2, card3, card4, card5, card6;

    private String apiKey = "AIzaSyCBcgvFEshhaQ8MNpKjze9pp6JkQjVQzS4";

    public static MainActivity Instance;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String latitude;
    String longitude;
    FirebaseAuth auth;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }

        TextView nameView = findViewById(R.id.t2);
        nameView.setText(user.getEmail());
        card1 = (CardView) findViewById(R.id.c1);
        card2 = (CardView) findViewById(R.id.c2);
        card3 = (CardView) findViewById(R.id.c3);
        card4 = (CardView) findViewById(R.id.c4);
        card5 = (CardView) findViewById(R.id.c5);
        card6 = (CardView) findViewById(R.id.c6);


        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
         card3.setOnClickListener(this);        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);

        Instance = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                getLocation();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        Log.d("nirav", "college");
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = getLastKnownLocation();
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Log.d("nirav", latitude);
                Log.d("nirav", longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public List<CityPlace> GetPlaces(String type) {
        String output = "";
        String mapUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=30000&type=" + type + "&key=" + apiKey;
        HttpGetRequest getRequest = new HttpGetRequest();
        try {


            output = getRequest.execute(mapUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<CityPlace> places = new LinkedList<CityPlace>();


        if (output.isEmpty()) {
            //Log.d("nirav","181");
            return places;

        }
        try {
            JSONObject responseJson = new JSONObject(output);
            //Log.d("nirav","188");
            JSONArray jsonArray = responseJson.getJSONArray("results");

            for (int objIndex = 0; objIndex < jsonArray.length(); objIndex++) {
                JSONObject placeJson = (JSONObject) jsonArray.get(objIndex);

                String name = placeJson.getString("name");
                String address = placeJson.getString("vicinity");
                if(!placeJson.has("photos")) continue;
                JSONArray photos = placeJson.getJSONArray("photos");
                //Log.d("nirav","195");
                if (photos==null)continue;
                JSONObject firstPhoto = photos.getJSONObject(0);

                String photoReference = firstPhoto.getString("photo_reference");
                String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?photoreference="+photoReference+"&maxwidth=600&key=" + apiKey;
                String lat = placeJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = placeJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

                String directionUrl = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+lat+","+lng;

                places.add(new CityPlace(name, address, imageUrl, directionUrl));
            }
        }
        catch (Exception e) {
            Log.d("nirav", e.getMessage());
        }

        return places;
    }

    @Override
    public void onClick(View v)
    {

        Intent i;

        switch (v.getId())
        {
            case R.id.c1 :
                i = new Intent(this, Restaurant.class);
                startActivity(i);
                break;

            case R.id.c2 :
                i = new Intent(this, ATM.class);
                startActivity(i);
                break;

            case R.id.c3 :
                i = new Intent(this,Education.class);
                startActivity(i);
                break;

            case R.id.c4 :
                i = new Intent(this, BusStation.class);
                startActivity(i);
                break;

            case R.id.c5 :
                i = new Intent(this,Temple.class);
                startActivity(i);
                break;

            case R.id.c6 :
                i = new Intent(this,Health.class);
                startActivity(i);
                break;
        }

    }
}