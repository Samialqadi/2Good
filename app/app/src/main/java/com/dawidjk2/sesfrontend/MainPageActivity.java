package com.dawidjk2.sesfrontend;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dawidjk2.sesfrontend.Adapters.CardAdapter;
import com.dawidjk2.sesfrontend.Models.Card;
import com.dawidjk2.sesfrontend.Models.Transaction;
import com.dawidjk2.sesfrontend.Singletons.ApiSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        getCards();
        // Handle the recycle view for all cards
        recyclerView = findViewById(R.id.mainPageCards);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardAdapter(myDataset, this);
        recyclerView.setAdapter(adapter);
    }
    public void getCards() {
        String endpoint = getString(R.string.backend_url) + "v0/charity/getAccounts";
        myDataset = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject object = array.getJSONObject(i);
                                String cardName = object.getString("nickname");
                                Log.d("cardnumber", cardName);
                                String cardNumber = object.getString("_id");
                                String prevActivity = object.getString("balance");
                                myDataset.add(new Card(cardName, cardNumber, prevActivity));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
    public void getTransactionAndSwitchActivity(final Card card) {
        String endpoint = getString(R.string.backend_url) + "v0/charity/getTransactions/?accountNumber=" + card.getCardNumber();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        ArrayList<Transaction> transactions = new ArrayList<>();
                        try {
                            for (int i = 0; i < array.length(); ++i) {
                                JSONObject object = array.getJSONObject(i);
                                String merchantId = object.getString("merchant_id");
                                String amount = object.getString("amount");
                                transactions.add(new Transaction(card, merchantId, amount));
                            }
                            // This is where you actually switch activites
                            Intent intent = new Intent(MainPageActivity.this, ExpandedCardActivity.class);
                            intent.putExtra("card", card);
                            intent.putExtra("transactions", transactions);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* TODO: Handle error */
                        Log.e("Volley", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        ApiSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
    @Override
    public void onItemClick(int position) {
        // Get the chosen card
        Card card = myDataset.get(position);
        getTransactionAndSwitchActivity(card);
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