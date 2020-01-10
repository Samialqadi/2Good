package com.dawidjk2.sesfrontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dawidjk2.sesfrontend.Models.Charity;
import com.dawidjk2.sesfrontend.Singletons.ApiSingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Quiz1Activity extends AppCompatActivity {
    public ArrayList<Charity> charityList = new ArrayList<>();

    public String url = "https://api.data.charitynavigator.org/v2/Organizations?app_id=a8597fc8&app_key=e2f022d55899528abddc3181808a6c94&rated=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

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
        addListenerOnSkip();
    }

    private Button btnSubmit;
    private Button btnSkip;

    public void addListenerOnButton() {
        btnSubmit = findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> habitsChecked = new ArrayList<>();
                JSONArray array = new JSONArray();

                LinearLayout habitList = findViewById(R.id.habitList);
                int count = habitList.getChildCount();
                for (int i = 0; i < count; i++) {
                    CheckBox habit = (CheckBox) habitList.getChildAt(i);
                    if (habit.isChecked()) {
                        habitsChecked.add((String) habit.getText());
                        array.put(habit.getText());
                    }
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("blacklisted", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String requestBody = object.toString();

                StringRequest jsonObjectRequest = new StringRequest
                        (Request.Method.POST, getString(R.string.backend_url) + "v0/charity/updateBlacklistPref", new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {
                                Log.d("Blacklist", response);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                /* TODO: Handle error */
                                Log.e("Volley Add Blacklist", error.toString());
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
                ApiSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

                // Send data to the next Quiz and start the activity
                Intent intent = new Intent(Quiz1Activity.this, Quiz2Activity.class);

                startActivity(intent);
            }
        });
    }

    public void addListenerOnSkip() {
        btnSkip = findViewById(R.id.skip);

        btnSkip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quiz1Activity.this, Quiz2Activity.class);
                startActivity(intent);
            }
        });
    }
}
