package com.dawidjk2.sesfrontend;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dawidjk2.sesfrontend.Adapters.PreferencesAdapter;
import com.dawidjk2.sesfrontend.Adapters.TransactionAdapter;
import com.dawidjk2.sesfrontend.Models.Charity;
import com.dawidjk2.sesfrontend.Models.PreferenceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdatePreferenceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<PreferenceItem> preferences;
    private ArrayList<Charity> charities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_preferences);

        // Get preferences
        preferences = new ArrayList<>();
        preferences.add(new PreferenceItem("Toggle Alcohol"));
        preferences.add(new PreferenceItem("Toggle Coffee Shops"));
        preferences.add(new PreferenceItem("Toggle Gambling"));
        preferences.add(new PreferenceItem("Toggle Restaurants"));
        preferences.add(new PreferenceItem("Toggle Clothes"));

        getChosenCharities();



        // Handle the recycle view for all transactions
        recyclerView = findViewById(R.id.preferences);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PreferencesAdapter(preferences);
        recyclerView.setAdapter(adapter);
    }

    public void getChosenCharities() {

        String allCharitiesEndpoint = getString(R.string.backend_url) + "v0/charity/getCharities";
        charities = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, allCharitiesEndpoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {

                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject object = array.getJSONObject(i);
                                String charityName = object.getString("charityName");
                                Charity charity = new Charity();
                                charity.charityName = charityName;
                                charities.add(charity);
                                System.out.println(charities);
                            }
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

        String chosenCharitiesEndpoint = getString(R.string.backend_url) + "v0/charity/getPref";

    }
}
