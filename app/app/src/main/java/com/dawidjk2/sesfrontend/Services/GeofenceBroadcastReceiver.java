package com.dawidjk2.sesfrontend.Services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dawidjk2.sesfrontend.MainPageActivity;
import com.dawidjk2.sesfrontend.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.content.ContentValues.TAG;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static int notificationId = 0;
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "Error with geofence";
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent launchIntent = new Intent(context, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Geofence zone entered, cards are disabled")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
            notificationId++;
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Geofence zone exited, cards are enabled")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
            notificationId++;
        }
    }
}