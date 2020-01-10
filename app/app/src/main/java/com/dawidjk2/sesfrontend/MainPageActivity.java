package com.dawidjk2.sesfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dawidjk2.sesfrontend.Adapters.CardAdapter;
import com.dawidjk2.sesfrontend.Models.Card;
import com.dawidjk2.sesfrontend.Models.Transaction;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements CardAdapter.OnItemListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Card> myDataset;

    private TextView hello;
    private TextView balance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);

        // Dummy data
        String name = "Sami";
        String accountBalance = "100.00";

        hello = findViewById(R.id.mainPageHello);
        balance = findViewById(R.id.mainPageBalance);
        findViewById(R.id.nav_bar_button).setOnClickListener(this);

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
        recyclerView = findViewById(R.id.mainPageCards);
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

    public void onClick(View v) {
        DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
        if (!navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.openDrawer(GravityCompat.START);
        } else {
            navDrawer.closeDrawer(GravityCompat.END);
        }
    }
}
