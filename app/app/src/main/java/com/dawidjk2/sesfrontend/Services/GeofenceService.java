package com.dawidjk2.sesfrontend.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dawidjk2.sesfrontend.Models.Geofence;
import com.dawidjk2.sesfrontend.R;
import com.dawidjk2.sesfrontend.Singletons.ApiSingleton;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeofenceService {
    private GeofencingClient geofencingClient;
    private ArrayList<com.google.android.gms.location.Geofence> geofenceList;
    private int radius = 50;


    public GeofenceService(GeofencingClient geofencingClient) {
        this.geofencingClient = geofencingClient;
        geofenceList = new ArrayList<>();
    }

    public void addFences(ArrayList<Geofence> fences, String url, Context context) {
        JSONArray jsonBody = new JSONArray();
        for (Geofence geofence : fences) {
            geofenceList.add(new com.google.android.gms.location.Geofence.Builder()
                    .setRequestId(geofence.key)
                    .setCircularRegion(
                            geofence.latitude,
                            geofence.longitude,
                            radius
                    )
                    .setExpirationDuration(geofence.exp)
                    .setTransitionTypes(com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER |
                            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

            try {
                JSONObject object = new JSONObject();
                object.put("latitude", geofence.latitude);
                object.put("longtitude", geofence.longitude);
                object.put("radius", radius);
                object.put("exp", geofence.exp);
                object.put("key", geofence.key);
                jsonBody.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final String requestBody = jsonBody.toString();

        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.POST, url + "v0/geofence/createGeofence", new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley Add Fences", error.toString());
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofenceList);
        return builder.build();
    }
}
