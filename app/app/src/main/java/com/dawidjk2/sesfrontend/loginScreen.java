package com.dawidjk2.sesfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class loginScreen extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.phoneButton);
        addOnClickListenerToSubmit();
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String numberAsString = email.getText().toString();
            String passwordAsString = password.getText().toString();
            submit.setEnabled(numberAsString.length() > 0 && passwordAsString.length() > 0);
        }
    };

    public void addOnClickListenerToSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginScreen.this, Quiz1Activity.class);
                startActivity(intent);
            }
        });
    }

    };



