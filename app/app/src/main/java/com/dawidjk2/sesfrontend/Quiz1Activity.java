package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dawidjk2.sesfrontend.Models.Charity;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Quiz1Activity extends AppCompatActivity {
    public ArrayList<Charity> charityList = new ArrayList<>();
    TextView textView;
    private GeofencingClient geofencingClient;

    public String url = "https://api.data.charitynavigator.org/v2/Organizations?app_id=a8597fc8&app_key=e2f022d55899528abddc3181808a6c94&rated=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        geofencingClient = LocationServices.getGeofencingClient(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray array) {
                        ArrayList<String> stringArrayList = new ArrayList<>();
                        try {
                            for(int i = 0; i < array.length(); ++i) {
                                JSONObject object = array.getJSONObject(i);
                                Charity charity = new Charity();
                                charity.charityName = object.getString("charityName");//name
                                charity.websiteURL = object.getString("websiteURL");//website
                                JSONObject cause = object.getJSONObject("cause");//cause
                                charity.generalCause = cause.getString("causeName");
                                charity.mission = object.getString("mission");//mission
                                charity.tagline = object.getString("tagLine"); //tag line
                                JSONObject rating = object.getJSONObject("currentRating");//rating
                                charity.rating = rating.getInt("rating");//rating
                                charityList.add(charity);
                                stringArrayList.add(rating.getInt("rating") + "\n");
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

        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

        addListenerOnButton();
    }

    private Button btnSubmit;

    public void addListenerOnButton() {
        btnSubmit = findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> habitsChecked = new ArrayList<>();

                LinearLayout habitList = (LinearLayout) findViewById(R.id.habitList);
                int count = habitList.getChildCount();
                for (int i = 0; i < count; i++) {
                    CheckBox habit = (CheckBox) habitList.getChildAt(i);
                    if (habit.isChecked()) habitsChecked.add((String) habit.getText());
                }

                for (int i = 0; i < habitsChecked.size(); i++) {
                    System.out.println(habitsChecked.get(i));
                }
            }
        });
    }
}
