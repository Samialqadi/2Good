package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dawidjk2.sesfrontend.Classes.Charity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        addListenerOnButton();
    }

    private Button btnSubmit;

    public void addListenerOnButton() {
        btnSubmit = findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> habitsChecked = new ArrayList<>();

                LinearLayout habitList = findViewById(R.id.habitList);
                int count = habitList.getChildCount();
                for (int i = 0; i < count; i++) {
                    CheckBox habit = (CheckBox) habitList.getChildAt(i);
                    if (habit.isChecked()) habitsChecked.add((String) habit.getText());
                }

                // Send data to the next Quiz and start the activity
                Intent intent = new Intent(Quiz1Activity.this, Quiz2Activity.class);
                intent.putExtra("habitList", habitsChecked);
                startActivity(intent);
            }
        });
    }
}
