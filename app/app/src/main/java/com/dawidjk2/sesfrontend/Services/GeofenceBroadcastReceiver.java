package com.dawidjk2.sesfrontend.Services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.dawidjk2.sesfrontend.MainPageActivity;
import com.dawidjk2.sesfrontend.R;
import com.dawidjk2.sesfrontend.Singletons.ApiSingleton;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static int notificationId = 0;
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "Error with geofence";
            Log.e(TAG, errorMessage);
            return;
        }

        Log.d("GeofenceTriggered", "Triggered");

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent launchIntent = new Intent(context, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("status", "true");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = jsonBody.toString();

            StringRequest jsonObjectRequest = new StringRequest
                    (Request.Method.POST, context.getString(R.string.backend_url) + "v0/geofence/setCardStatus", new Response.Listener<String>() {


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

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Geofence zone entered, cards are disabled")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
            notificationId++;
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("status", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = jsonBody.toString();

            StringRequest jsonObjectRequest = new StringRequest
                    (Request.Method.POST, context.getString(R.string.backend_url) + "v0/geofence/setCardStatus", new Response.Listener<String>() {


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

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Geofence zone exited, cards are enabled")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
            notificationId++;
        }
    }
}