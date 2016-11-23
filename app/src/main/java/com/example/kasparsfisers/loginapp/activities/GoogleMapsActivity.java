package com.example.kasparsfisers.loginapp.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.tag;
import static com.example.kasparsfisers.loginapp.R.drawable.target;
import static com.google.android.gms.analytics.internal.zzy.i;

public class GoogleMapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_COORDINATES_LOADER = 0;
    private static final String LOCATION_SEPARATOR = ",";
    private Uri mCurrentCoordinatesUri;
    private GoogleMap mMap;
    private String mapLocation;
    boolean allTable = false;

    private Uri mCurrentPetUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        mCurrentCoordinatesUri = intent.getData();
        if (mCurrentCoordinatesUri.equals(LocationContract.LocationEntry.CONTENT_URI)){
            allTable = true;
        }   else {
            allTable = false;
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLoaderManager().initLoader(EXISTING_COORDINATES_LOADER, null, this);

        mMap.setOnMarkerClickListener(this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                LocationContract.LocationEntry._ID,
                LocationContract.LocationEntry.COLUMN_LATITUDE,
                LocationContract.LocationEntry.COLUMN_LONGITUDE,
                LocationContract.LocationEntry.COLUMN_LOCNAME};

        return new CursorLoader(this,
                mCurrentCoordinatesUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1 || mMap == null) {
            return;
        }


        if(!allTable){
            locateCoordinates(cursor);
        }
        else{
            locateAllCoordinates(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void locateCoordinates(Cursor cursor) {

        if (cursor.moveToFirst()) {

            int LatColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LATITUDE);
            int LonColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LONGITUDE);
            int NameColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LOCNAME);
            Double myLatitude = cursor.getDouble(LatColumnIndex);
            Double myLongitude = cursor.getDouble(LonColumnIndex);
            String myPlaceName = cursor.getString(NameColumnIndex);

            if (myPlaceName.contains(LOCATION_SEPARATOR)) {
                String[] parts = myPlaceName.split(LOCATION_SEPARATOR);
                mapLocation = parts[0];
            } else {
                mapLocation = "Unknown";
            }

            LatLng target = new LatLng(myLatitude, myLongitude);
            mMap.addMarker(new MarkerOptions().position(target).title(mapLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 13));

        }

    }


    private void locateAllCoordinates(Cursor cursor) {
        Location loc1 = new Location("");
        Location loc2 = new Location("");
        cursor.moveToFirst();
        int i = 0;
        int LatColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LATITUDE);
        int LonColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LONGITUDE);
        int NameColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LOCNAME);
        float distanceInMeters = 0;


        while (!cursor.isAfterLast()) {
            Double myLatitude = cursor.getDouble(LatColumnIndex);
            Double myLongitude = cursor.getDouble(LonColumnIndex);
            String myPlaceName = cursor.getString(NameColumnIndex);

            if (myPlaceName.contains(LOCATION_SEPARATOR)) {
                String[] parts = myPlaceName.split(LOCATION_SEPARATOR);
                mapLocation = parts[0];
            } else {
                mapLocation = "Unknown";
            }

            if(i == 0){
                loc1.setLatitude(myLatitude);
                loc1.setLongitude(myLongitude);
            }

            loc2.setLatitude(myLatitude);
            loc2.setLongitude(myLongitude);

            distanceInMeters += loc1.distanceTo(loc2);

            LatLng target = new LatLng(myLatitude, myLongitude);

            mMap.addMarker(new MarkerOptions().position(target).title(mapLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 10));

            cursor.moveToNext();
            i=1;
            loc1.setLatitude(myLatitude);
            loc1.setLongitude(myLongitude);
        }

        Toast.makeText(this, "meters: "+distanceInMeters, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "Hello!!!", Toast.LENGTH_SHORT).show();
        return false;
    }
}
