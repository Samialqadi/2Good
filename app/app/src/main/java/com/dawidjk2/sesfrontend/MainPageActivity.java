package com.dawidjk2.sesfrontend;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dawidjk2.sesfrontend.Adapters.CardAdapter;
import com.dawidjk2.sesfrontend.Models.Card;
import com.dawidjk2.sesfrontend.Models.Geofence;
import com.dawidjk2.sesfrontend.Models.Transaction;
import com.dawidjk2.sesfrontend.Singletons.ApiSingleton;
import com.dawidjk2.sesfrontend.Services.GeofenceBroadcastReceiver;
import com.dawidjk2.sesfrontend.Services.GeofenceService;
import com.dawidjk2.sesfrontend.Singletons.ApiSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainPageActivity extends AppCompatActivity implements CardAdapter.OnItemListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Card> myDataset;
    private TextView hello;
    private TextView balance;
    private TextView cardStatus;

    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private GeofenceService geofenceService;
    private LocationManager locationManager;
    public static String lastKnownLocation = "";
    private String type = "";
    private HashMap<String, String> convert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);

        convert = new HashMap<>();
        convert.put("Alcohol", "liquor_store, bar, night_club" );
        convert.put("Coffee Shops", "cafe");
        convert.put("Gambling", "casino");
        convert.put("Restaurants", "restaurant");
        convert.put("Clothes", "shoe_store, shopping_mall");

        geofencingClient = LocationServices.getGeofencingClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geofenceService = new GeofenceService(geofencingClient);
        cardStatus = findViewById(R.id.cardDisabled);
        checkCardStatus();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getString(R.string.backend_url) + "v0/charity/getPref", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {

                        try {
                            JSONArray array = object.getJSONArray("blacklisted");

                            for (int i = 0; i < array.length(); ++i) {
                                type += convert.get(array.getString(i));

                                if (i < array.length() - 1) {
                                    type += ",";
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, getString(R.string.backend_url) + "v0/geofence/getPlaces", null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject object) {
                                        try {
                                            JSONArray locationArray = object.getJSONArray("locations");
                                            ArrayList<Geofence> geofenceArrayList = new ArrayList<>();
                                            Log.d("locationArray length", String.valueOf(locationArray.length()));
                                            for(int i = 0; i < locationArray.length(); ++i) {
                                                JSONObject location = locationArray.getJSONObject(i);
                                                Geofence geofence = new Geofence();
                                                geofence.latitude = location.getDouble("lat");
                                                geofence.longitude = location.getDouble("lng");
                                                try {
                                                    geofence.key = location.getString("name");
                                                } catch (Exception e) {
                                                    geofence.key = "sdfsdgfdg";
                                                }
                                                geofence.exp = 999999999999999999L;
                                                geofenceArrayList.add(geofence);
                                            }
                                            geofenceService.addFences(geofenceArrayList,getString(R.string.backend_url), getApplicationContext());
                                            intializeGeofence();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        /* TODO: Handle error */
                                        Log.e("Volley Places", error.toString());
                                    }
                                }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String>  params = new HashMap<>();
                                @SuppressLint("MissingPermission")
                                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    lastKnownLocation = location.getLatitude() + "," + location.getLongitude();
                                }
                                Log.d("Local", lastKnownLocation);
                                params.put("location", lastKnownLocation);
                                params.put("type", type);
                                return params;
                            }
                        };

                        // Access the RequestQueue through your singleton class.
                        ApiSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley Places", error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        // Dummy data
        String name = "Sami";
        String accountBalance = "100.00";
        hello = findViewById(R.id.mainPageHello);
        balance = findViewById(R.id.mainPageBalance);
        findViewById(R.id.nav_bar_button).setOnClickListener(this);
        hello.setText("Welcome back, " + name + "!");
        balance.setText("$" + accountBalance);
        getCards();
        // Handle the recycle view for all cards
        recyclerView = findViewById(R.id.mainPageCards);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardAdapter(myDataset, this);
        recyclerView.setAdapter(adapter);
    }
    public void getCards() {
        String endpoint = getString(R.string.backend_url) + "v0/charity/getAccounts";
        myDataset = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject object = array.getJSONObject(i);
                                String cardName = object.getString("nickname");
                                Log.d("cardnumber", cardName);
                                String cardNumber = object.getString("_id");
                                String prevActivity = object.getString("balance");
                                myDataset.add(new Card(cardName, cardNumber, prevActivity));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
    public void getTransactionAndSwitchActivity(final Card card) {
        String endpoint = getString(R.string.backend_url) + "v0/charity/getTransactions/?accountNumber=" + card.getCardNumber();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        ArrayList<Transaction> transactions = new ArrayList<>();
                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject object = array.getJSONObject(i);
                                String merchantId = object.getString("merchant_id");
                                String amount = object.getString("amount");
                                transactions.add(new Transaction(card, merchantId, amount));
                            }
                            // This is where you actually switch activites
                            Intent intent = new Intent(MainPageActivity.this, ExpandedCardActivity.class);
                            intent.putExtra("card", card);
                            intent.putExtra("transactions", transactions);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
    @Override
    public void onItemClick(int position) {
        // Get the chosen card
        Card card = myDataset.get(position);
        getTransactionAndSwitchActivity(card);
    }
    public void onClick(View v) {
        DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
        if (!navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.openDrawer(GravityCompat.START);
        } else {
            navDrawer.closeDrawer(GravityCompat.END);
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private void intializeGeofence() {
        geofencingClient.addGeofences(geofenceService.getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                    }
                });
    }

    private void checkCardStatus() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getString(R.string.backend_url) + "v0/geofence/getCardStatus", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            if (object.getBoolean("status")) {
                                cardStatus.setVisibility(View.VISIBLE);
                            } else {
                                cardStatus.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley Places", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                @SuppressLint("MissingPermission")
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    lastKnownLocation = location.getLatitude() + "," + location.getLongitude();
                }
                Log.d("Local", lastKnownLocation);
                params.put("location", lastKnownLocation);
                params.put("type", "cafe");

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCardStatus();
    }
}
