package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Quiz2Activity extends AppCompatActivity {
    List<String> habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        habits = (List<String>) getIntent().getSerializableExtra("habitList");

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
}
