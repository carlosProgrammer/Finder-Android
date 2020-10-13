package com.neuronatech.finder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.time.LocalDateTime;


public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.textViewResult);
        Button locateBtn = findViewById(R.id.locateBtn);

        mQueue = Volley.newRequestQueue(this);

        locateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void jsonParse(){

        String url = "https://banplatform.com/api/get_devices_latest?"+
                "user_api_hash=$2y$10$WFrk2k4Hf48thRuvThSJj.rAfZ8uWLYg8iHgjQXFCHO8O//3Sz9.K" +
                "&time=" + LocalDateTime.now();

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
                                Number lat = item.getDouble("lat");
                                Number lng = item.getDouble("lng");

                                mTextViewResult.append(name  + ", " + String.valueOf(lat) + String.valueOf(lng) +  "\n\n");

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
}