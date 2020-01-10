package com.dawidjk2.sesfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        createNotificationChannel();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            MainPageActivity.lastKnownLocation = location.getLatitude() + "," + location.getLongitude();
                        }
                    }
                });

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("appState", MODE_PRIVATE);
                String prefString = sharedPrefs.getString("state", "");
                System.out.println(prefString);
                if (prefString.equals("LoggedIn")) {
                    System.out.println("We ARE INSIDE LOGGED IN!");
                    Intent homeIntent = new Intent(SplashActivity.this, MainPageActivity.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    System.out.println("WE ARE NOT INSIDE THE LOGGED IN!!");
                    Intent homeIntent = new Intent(SplashActivity.this, loginScreen.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
