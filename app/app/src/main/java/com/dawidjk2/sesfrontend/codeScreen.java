package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class codeScreen extends AppCompatActivity {

    private EditText userCode;

    private Button codeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_screen);
        userCode = findViewById(R.id.userCode);
        codeButton = findViewById(R.id.codeButton);
        addOnClickListenerToSubmit();
        userCode.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String numberAsString = userCode.getText().toString();
            String max = numberAsString;
            codeButton.setEnabled(numberAsString.length() == 6);
        }
    };

    public void addOnClickListenerToSubmit() {
        codeButton = findViewById(R.id.codeButton);

        codeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(codeScreen.this, Quiz1Activity.class);
                startActivity(intent);
            }
        });
    }
}