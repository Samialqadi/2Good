package com.dawidjk2.sesfrontend;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dawidjk2.sesfrontend.Adapters.PreferencesAdapter;
import com.dawidjk2.sesfrontend.Adapters.TransactionAdapter;
import com.dawidjk2.sesfrontend.Models.Charity;
import com.dawidjk2.sesfrontend.Models.PreferenceItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class UpdatePreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_preferences);
        addListenerOnButton();
        // Get preferences
//        preferences = new ArrayList<>();
//        preferences.add(new PreferenceItem("Toggle Alcohol"));
//        preferences.add(new PreferenceItem("Toggle Coffee Shops"));
//        preferences.add(new PreferenceItem("Toggle Gambling"));
//        preferences.add(new PreferenceItem("Toggle Restaurants"));
//        preferences.add(new PreferenceItem("Toggle Clothes"));
    }
    private Button btnSubmit;
    private Button btnSkip;
    public void addListenerOnButton() {
        btnSubmit = findViewById(R.id.submitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdatePreferenceActivity.this, MainPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}