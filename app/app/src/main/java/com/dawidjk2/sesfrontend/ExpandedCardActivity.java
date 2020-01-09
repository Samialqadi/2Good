package com.dawidjk2.sesfrontend;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpandedCardActivity extends AppCompatActivity {

    private Card card;
    private ArrayList<Transaction> transactions;

    private TextView cardName;
    private TextView cardNumber;
    private TextView previousActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_card_page);

        card = (Card) getIntent().getSerializableExtra("card");
        transactions = (ArrayList<Transaction>) getIntent().getSerializableExtra("transactions");

        cardName = findViewById(R.id.expandedCardPageName);
        cardNumber = findViewById(R.id.expandedCardPageEndingIn);
        previousActivity = findViewById(R.id.expandedCardPagePreviousActivity);

        cardName.setText(card.getCardName());
        int cardNumLength = card.getCardNumber().length();
        cardNumber.setText("Ending In - " + card.getCardNumber().substring(cardNumLength - 5, cardNumLength - 1));
        previousActivity.setText("$" + card.getPreviousActivity());


    }
}
