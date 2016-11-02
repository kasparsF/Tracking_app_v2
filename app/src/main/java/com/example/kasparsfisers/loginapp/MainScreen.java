package com.example.kasparsfisers.loginapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.data.LocationContract.LocationEntry;

public class MainScreen extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,NavigationView.OnNavigationItemSelectedListener {

    // Identifier for coordinate data loader
    private static final int COORDINATE_LOADER = 0;
    private boolean permissionGranted = true;

    LocationCursorAdapter mCursorAdapter;
    DrawerLayout drawer;
    Button mTracking, mLogout;
    SharedPreferencesUtils preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mTracking = (Button) findViewById(R.id.btnLoc);
        mLogout = (Button) findViewById(R.id.btnLogout);
        preferences = new SharedPreferencesUtils(this);
        if (LocationService.isInstanceCreated()) {
            mTracking.setText(R.string.stop);
        } else {
            mTracking.setText(R.string.start);
        }

        mTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkPermissions()) {
                    serviceEnable();
                } else {
                    askPermissions();
                }


            }
        });


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.sessionSetLoggedIn(false);
                startActivity(new Intent(MainScreen.this, LoginActivity.class));
                finish();
            }
        });

        ListView coordinateListView = (ListView) findViewById(R.id.list);


        mCursorAdapter = new LocationCursorAdapter(this, null);
        coordinateListView.setAdapter(mCursorAdapter);


        coordinateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainScreen.this, GoogleMaps.class);

                Uri currentCoordinatesUri = ContentUris.withAppendedId(LocationEntry.CONTENT_URI, id);
                intent.setData(currentCoordinatesUri);
                startActivity(intent);
            }
        });


        getLoaderManager().initLoader(COORDINATE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCNAME};

        //  ContentProviders query method on a background thread
        return new CursorLoader(this,
                LocationEntry.CONTENT_URI,
                projection,
                null,
                null,
                LocationContract.LocationEntry._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update cursor containing updated coordinate data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    serviceEnable();

                } else {
                    Toast.makeText(this, R.string.no_permissions, Toast.LENGTH_SHORT).show();

                }
                return;
            }


        }
    }

    private boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        return true;

    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(MainScreen.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    private void serviceEnable() {

        if (!LocationService.isInstanceCreated()) {

            startService(new Intent(getBaseContext(), LocationService.class));
            mTracking.setText(R.string.stop);

        } else {
            stopService(new Intent(getBaseContext(), LocationService.class));
            mTracking.setText(R.string.start);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_share) {

           startActivity(new Intent(MainScreen.this, SliderActivity.class));
        } else if (id == R.id.nav_send) {

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
