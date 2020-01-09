package com.dawidjk2.sesfrontend;

import com.dawidjk2.sesfrontend.Models.Location;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;

import java.util.ArrayList;

public class GeofenceService {
    private GeofencingClient geofencingClient;
    private ArrayList<Geofence .Builder> geofenceList;


    GeofenceService(GeofencingClient geofencingClient) {
        this.geofencingClient = geofencingClient;
    }

    public void addLocations(ArrayList<Location> locations) {
        for (Location location : locations) {
            geofenceList.add();
        }
    }
}
