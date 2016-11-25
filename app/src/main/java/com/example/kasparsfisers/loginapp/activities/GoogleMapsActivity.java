package com.example.kasparsfisers.loginapp.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.fragments.PlaceFragment;
import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.utils.Functions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_COORDINATES_LOADER = 0;
    private static final String LOCATION_SEPARATOR = ",";
    private Uri mCurrentCoordinatesUri;
    private GoogleMap mMap;
    private String mapLocation;
    boolean allTable = false;
    RelativeLayout picture;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        picture = (RelativeLayout) findViewById(R.id.fragment_container);
        picture.setVisibility(View.GONE);
        Intent intent = getIntent();
        mCurrentCoordinatesUri = intent.getData();
        if (mCurrentCoordinatesUri.equals(LocationContract.LocationEntry.CONTENT_URI)) {
            allTable = true;
        } else {
            allTable = false;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picture.setVisibility(View.GONE);
            }
        });
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
                LocationContract.LocationEntry.COLUMN_LOCNAME,
                LocationContract.LocationEntry.COLUMN_PICTURE_URI};

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


        if (!allTable) {
            locateCoordinates(cursor);
        } else {
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
            int PictureColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_PICTURE_URI);
            Double myLatitude = cursor.getDouble(LatColumnIndex);
            Double myLongitude = cursor.getDouble(LonColumnIndex);
            String myPlaceName = cursor.getString(NameColumnIndex);
            path = cursor.getString(PictureColumnIndex);

            if (myPlaceName.contains(LOCATION_SEPARATOR)) {
                String[] parts = myPlaceName.split(LOCATION_SEPARATOR);
                mapLocation = parts[0];
            } else {
                mapLocation = "Unknown";
            }

            LatLng target = new LatLng(myLatitude, myLongitude);
            mMap.addMarker(new MarkerOptions().position(target).title(mapLocation).snippet(path));
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
        int PictureColumnIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_PICTURE_URI);

        float distanceInMeters = 0;


        while (!cursor.isAfterLast()) {
            Double myLatitude = cursor.getDouble(LatColumnIndex);
            Double myLongitude = cursor.getDouble(LonColumnIndex);
            String myPlaceName = cursor.getString(NameColumnIndex);
            path = cursor.getString(PictureColumnIndex);
            if (myPlaceName.contains(LOCATION_SEPARATOR)) {
                String[] parts = myPlaceName.split(LOCATION_SEPARATOR);
                mapLocation = parts[0];
            } else {
                mapLocation = "Unknown";
            }

            if (i == 0) {
                loc1.setLatitude(myLatitude);
                loc1.setLongitude(myLongitude);
            }

            loc2.setLatitude(myLatitude);
            loc2.setLongitude(myLongitude);

            distanceInMeters += loc1.distanceTo(loc2);

            LatLng target = new LatLng(myLatitude, myLongitude);

            mMap.addMarker(new MarkerOptions().position(target).title(mapLocation).snippet(path));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 10));

            cursor.moveToNext();
            i = 1;
            loc1.setLatitude(myLatitude);
            loc1.setLongitude(myLongitude);
        }

        Toast.makeText(this, "meters: " + distanceInMeters, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        if (!Functions.isEmpty(marker.getSnippet())) {
            Fragment fragment = new PlaceFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("message", marker.getSnippet());
            fragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
            picture.setVisibility(View.VISIBLE);
            Toast.makeText(this, "" + marker.getTitle(), Toast.LENGTH_SHORT).show();
        }

        return true;
    }


}
