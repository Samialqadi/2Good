package com.dawidjk2.sesfrontend;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Card[] myDataset = new Card[1];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);

        Card card = new Card("card 1", "123456789", "100.00");
        myDataset[0] = card;

        recyclerView = (RecyclerView) findViewById(R.id.mainPageCards);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter(myDataset);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.nav_bar_button).setOnClickListener(this);
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
