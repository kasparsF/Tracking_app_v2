package com.example.kasparsfisers.loginapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.activities.MainScreenActivity;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.utils.Functions;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static android.R.string.no;
import static com.google.android.gms.analytics.internal.zzy.e;
import static com.google.android.gms.analytics.internal.zzy.t;


public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int NOTIFICATION_ID = 1;
    private static LocationService instance = null;
    SharedPreferencesUtils preferences;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    int currLoc;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;


    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */

    public static boolean isInstanceCreated() {
        return instance != null;
    }


    Geocoder gcd = null;
    double lat, lon, acc;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        Toast.makeText(this, R.string.serviceStart, Toast.LENGTH_SHORT).show();

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Bundle extras = intent.getExtras();

        if (extras != null) {
            currLoc = (int) extras.getFloat("currLoc");
        }


        //Notification intent
        Intent resultIntent = new Intent(this, MainScreenActivity.class);


        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), resultIntent, 0);

        //Notification builder
        mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setContentTitle("TrackingApp")
                        .setContentText("Maximum number of locations have reached")
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 1000, 1000, 1000})
                        .setLights(Color.RED, 2000, 3000)
                        .setContentIntent(pIntent);






        preferences = SharedPreferencesUtils.getInstance(this);
        UPDATE_INTERVAL = Long.parseLong(preferences.timer()) * 1000;

        // Create the location client to start receiving updates
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }

        mGoogleApiClient.connect();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.serviceStop, Toast.LENGTH_SHORT).show();
        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        // only stop if it's connected
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        instance = null;
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        android.location.Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());

        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i("Activity", "error:"
                + result.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        acc = location.getAccuracy();

        Functions.getAddressFromLocation(lat, lon, acc, this, new GeocoderHandler());


    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            double lat = 0, lon = 0, acc = 0;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    lat = bundle.getDouble("lat");
                    lon = bundle.getDouble("lon");
                    acc = bundle.getDouble("acc");
                    break;
                default:
                    result = null;
            }
            ContentValues values = new ContentValues();
            values.put(LocationContract.LocationEntry.COLUMN_LATITUDE, lat);
            values.put(LocationContract.LocationEntry.COLUMN_LONGITUDE, lon);
            values.put(LocationContract.LocationEntry.COLUMN_ACCURACY, acc);
            values.put(LocationContract.LocationEntry.COLUMN_LOCNAME, result);

            if (currLoc < preferences.getMaxLoc()) {
                Uri newUri = getContentResolver().insert(LocationContract.LocationEntry.CONTENT_URI, values);
                currLoc++;
            } else {
                //Start notification
                mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
                stopSelf();
            }
        }
    }
}