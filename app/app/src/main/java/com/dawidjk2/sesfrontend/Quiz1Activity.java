package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
