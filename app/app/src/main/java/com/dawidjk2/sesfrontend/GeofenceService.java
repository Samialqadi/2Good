package com.dawidjk2.sesfrontend;

import com.dawidjk2.sesfrontend.Models.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;

public class GeofenceService {
    private GeofencingClient geofencingClient;
    private ArrayList<com.google.android.gms.location.Geofence> geofenceList;
    private int radius = 100;


    GeofenceService(GeofencingClient geofencingClient) {
        this.geofencingClient = geofencingClient;
    }

    public void addFences(ArrayList<Geofence> fences) {
        for (Geofence geofence : fences) {
            geofenceList.add(new com.google.android.gms.location.Geofence.Builder()
                    .setRequestId(geofence.key)
                    .setCircularRegion(
                            geofence.latitude,
                            geofence.longitude,
                            radius
                    )
                    .setTransitionTypes(com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_DWELL |
                            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }
}
