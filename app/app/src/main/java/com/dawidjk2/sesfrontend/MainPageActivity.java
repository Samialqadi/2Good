package com.dawidjk2.sesfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements CardAdapter.OnItemListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Card> myDataset;

    private TextView hello;
    private TextView balance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Dummy data
        String name = "Sami";
        String accountBalance = "100.00";

        hello = findViewById(R.id.mainPageHello);
        balance = findViewById(R.id.mainPageBalance);

        hello.setText("Welcome back, " + name + "!");
        balance.setText("$" + accountBalance);


        // More dummy data
        myDataset = new ArrayList<>();
        myDataset.add(new Card("Card 1", "123456789", "100.00"));
        myDataset.add(new Card("Card 2", "24567890", "200.00"));
        myDataset.add(new Card("Card 3", "24567890", "200.00"));
        myDataset.add(new Card("Card 4", "24567890", "200.00"));
        myDataset.add(new Card("Card 5", "24567890", "200.00"));

        // Handle the recycle view for all cards
        recyclerView = (RecyclerView) findViewById(R.id.mainPageCards);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter(myDataset, this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(int position) {

        // Get the chosen card
        Card card = myDataset.get(position);

        // These are dummy transactions
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(card, "Chipotle", 10.00));
        transactions.add(new Transaction(card, "Macy's", 1000.00));

        // This is where you actually switch activites
        Intent intent = new Intent(MainPageActivity.this, ExpandedCardActivity.class);
        intent.putExtra("card", card);
        intent.putExtra("transactions", transactions);
        startActivity(intent);
    }
}
