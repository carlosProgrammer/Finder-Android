package com.neuronatech.finder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.neuronatech.finder.LoginActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDateTime;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue mQueue;
    static Double lat, lng;
    FloatingActionButton callBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Button locateBtn = findViewById(R.id.locateBtn);

        mQueue = Volley.newRequestQueue(this);

        locateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                jsonParseAndGet();
            }
        });

        callBtn = (FloatingActionButton) findViewById(R.id.callBtn);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:8095407555"));
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void jsonParseAndGet() {

        String url = "https://banplatform.com/api/get_devices_latest?"+
                "user_api_hash=$2y$10$WFrk2k4Hf48thRuvThSJj.rAfZ8uWLYg8iHgjQXFCHO8O//3Sz9.K"
                +  "&time=" + LocalDateTime.now();

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);

                                String name = item.getString("name");
                                //Integer id = item.getInt("id");
                                lat = item.getDouble("lat");
                                lng = item.getDouble("lng");

                                LatLng coordinate = new LatLng(lat, lng);

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lng)).title(name));

                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

                                marker.setIcon(icon);

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(coordinate)
                                        .zoom(17)
                                        .build();

                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));





                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        jsonParseAndGet();

    }


}