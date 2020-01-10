package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dawidjk2.sesfrontend.Classes.Charity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz2Activity extends AppCompatActivity {
    List<String> habits;
    public ArrayList<Charity> charityList = new ArrayList<>();
    public String url = "https://api.data.charitynavigator.org/v2/Organizations?app_id=a8597fc8&app_key=e2f022d55899528abddc3181808a6c94&rated=true";
    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    CheckBox cb5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        cb1 = findViewById(R.id.charity1);
        cb1.setVisibility(View.INVISIBLE);
        cb2 = findViewById(R.id.charity2);
        cb2.setVisibility(View.INVISIBLE);
        cb3 = findViewById(R.id.charity3);
        cb3.setVisibility(View.INVISIBLE);
        cb4 = findViewById(R.id.charity4);
        cb4.setVisibility(View.INVISIBLE);
        cb5 = findViewById(R.id.charity5);
        cb5.setVisibility(View.INVISIBLE);

        habits = (List<String>) getIntent().getSerializableExtra("habitList");

        getCharities();
        addListenerOnButton();
    }

    private Button btnSubmit;

    public void addListenerOnButton() {
        btnSubmit = findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> charityList = new ArrayList<>();

                LinearLayout charitylayout = findViewById(R.id.charityList);
                int count = charitylayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    CheckBox charity = (CheckBox) charitylayout.getChildAt(i);
                    if (charity.isChecked()) charityList.add((String) charity.getText());
                }

                Intent intent = new Intent(Quiz2Activity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getCharities() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray array) {
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

                                ProgressBar pb = findViewById(R.id.progressBarQuiz2);
                                pb.setVisibility(View.INVISIBLE);
                                cb1.setVisibility(View.VISIBLE);
                                cb2.setVisibility(View.VISIBLE);
                                cb3.setVisibility(View.VISIBLE);
                                cb4.setVisibility(View.VISIBLE);
                                cb5.setVisibility(View.VISIBLE);

                                LinearLayout charityLayout = findViewById(R.id.charityList);
                                CheckBox charityBox = (CheckBox) charityLayout.getChildAt(i);
                                if (charityBox != null) charityBox.setText(charity.charityName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //sort by rating
                        Collections.sort(charityList);
                        for (Charity charity : charityList) {
                            Log.d("Charity rating", String.valueOf(charity.rating));
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
}
