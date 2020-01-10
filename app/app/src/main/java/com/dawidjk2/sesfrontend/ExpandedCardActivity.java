package com.dawidjk2.sesfrontend;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dawidjk2.sesfrontend.Adapters.TransactionAdapter;
import com.dawidjk2.sesfrontend.Models.Card;
import com.dawidjk2.sesfrontend.Models.Transaction;

import java.util.ArrayList;

public class ExpandedCardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Card card;
    private ArrayList<Transaction> transactions;

    private TextView cardName;
    private TextView cardNumber;
    private TextView previousActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_card_page);

        // Get data from bundle
        card = (Card) getIntent().getSerializableExtra("card");
        transactions = (ArrayList<Transaction>) getIntent().getSerializableExtra("transactions");

        // Fill in the card information into TextViews
        cardName = findViewById(R.id.expandedCardPageName);
        cardNumber = findViewById(R.id.expandedCardPageEndingIn);
        previousActivity = findViewById(R.id.expandedCardPagePreviousActivity);

        cardName.setText(card.getCardName());
        int cardNumLength = card.getCardNumber().length();
        cardNumber.setText("Ending In - " + card.getCardNumber().substring(cardNumLength - 5, cardNumLength - 1));
        previousActivity.setText("$" + card.getPreviousActivity());

        // Handle the recycle view for all transactions
        recyclerView = findViewById(R.id.allTransactions);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TransactionAdapter(transactions);
        recyclerView.setAdapter(adapter);

    }
}
