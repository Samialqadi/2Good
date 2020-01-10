package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class loginScreen extends AppCompatActivity {

    private EditText phoneNumber;

    private Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        phoneNumber = findViewById(R.id.usersPhoneNumber);
        submit = findViewById(R.id.phoneButton);
        addOnClickListenerToSubmit();
        phoneNumber.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String numberAsString = phoneNumber.getText().toString();
            submit.setEnabled(numberAsString.length() == 10);
        }
    };

    public void addOnClickListenerToSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginScreen.this, codeScreen.class);
                startActivity(intent);
            }
        });
    }
}


