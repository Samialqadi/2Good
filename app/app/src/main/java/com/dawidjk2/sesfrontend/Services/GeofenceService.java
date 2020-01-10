package com.dawidjk2.sesfrontend.Services;

import com.dawidjk2.sesfrontend.Models.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;

public class GeofenceService {
    private GeofencingClient geofencingClient;
    private ArrayList<com.google.android.gms.location.Geofence> geofenceList;
    private int radius = 30;


    public GeofenceService(GeofencingClient geofencingClient) {
        this.geofencingClient = geofencingClient;
        geofenceList = new ArrayList<>();
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
                    .setExpirationDuration(geofence.exp)
                    .setTransitionTypes(com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER |
                            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofenceList);
        return builder.build();
    }
}
