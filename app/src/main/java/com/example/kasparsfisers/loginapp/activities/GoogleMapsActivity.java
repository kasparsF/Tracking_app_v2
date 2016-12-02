package com.example.kasparsfisers.loginapp.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.fragments.EditFragment;
import com.example.kasparsfisers.loginapp.fragments.PlaceFragment;
import com.example.kasparsfisers.loginapp.utils.Functions;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import static android.R.attr.name;
import static com.example.kasparsfisers.loginapp.R.id.placeName;
import static com.example.kasparsfisers.loginapp.R.id.placePicture;
import static com.google.android.gms.analytics.internal.zzy.v;

public class GoogleMapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, GoogleMap.InfoWindowAdapter {
    private static final int EXISTING_COORDINATES_LOADER = 0;
    private static final String LOCATION_SEPARATOR= ",";

    public static final String CURRENT_URI = "current_uri";
    public static final String CURRENT_PLACE_NAME = "current_name";
    private Uri mCurrentCoordinatesUri;
    private GoogleMap mMap;
    private String mapLocation;
    boolean allTable = false;
    RelativeLayout picture;
    private String path;
    SharedPreferencesUtils preferences;
    FragmentManager fm = getSupportFragmentManager();
    FloatingActionButton fabEdit;
    FloatingActionButton fab;


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

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picture.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }
        });




     fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
    fabEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!allTable) {
            EditFragment editFragment = new EditFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CURRENT_URI, mCurrentCoordinatesUri.toString());

            editFragment.setArguments(bundle);
            editFragment.show(fm, "");
            }else{
                Toast.makeText(GoogleMapsActivity.this, "Cant edit in Route mode", Toast.LENGTH_SHORT).show();
            }
        }
    });
}



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLoaderManager().initLoader(EXISTING_COORDINATES_LOADER, null, this);

        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(this);

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
            } else if(Functions.isEmpty(myPlaceName)) {
                mapLocation = "Unknown";
            } else {
                mapLocation = myPlaceName;
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
            } else if(Functions.isEmpty(myPlaceName)) {
                mapLocation = "Unknown";
            } else {
                mapLocation = myPlaceName;
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
        preferences = SharedPreferencesUtils.getInstance(this);
        preferences.setDistance(distanceInMeters);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(this).inflate(R.layout.info_window, null, false);

        ImageView img = (ImageView)view.findViewById(R.id.imageForMap);
        TextView place = (TextView) view.findViewById(R.id.textViewForMap);
        String pictureStr = marker.getSnippet();


        place.setText(marker.getTitle());

        if (!Functions.isEmpty(pictureStr)) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(pictureStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] imageData = exif.getThumbnail();
            Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            img.setImageBitmap(thumbnail);

        } else {
            img.setVisibility(View.GONE);
        }




        return view;
    }
}
