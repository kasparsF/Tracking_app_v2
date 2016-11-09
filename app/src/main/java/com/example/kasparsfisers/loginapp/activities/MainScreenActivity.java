package com.example.kasparsfisers.loginapp.activities;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasparsfisers.loginapp.adapters.LocationCursorAdapter;
import com.example.kasparsfisers.loginapp.services.LocationService;
import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.data.LocationContract.LocationEntry;

public class MainScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener {

    // Identifier for coordinate data loader
    private static final int COORDINATE_LOADER = 0;
    MenuItem itemTrack;
    LocationCursorAdapter mCursorAdapter;
    DrawerLayout drawer;
    SharedPreferencesUtils preferences;
    private int cursorRows=0;
    TextView headerUser;
    TextView headerEmail;
    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        // Getting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Home button inic
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        //Menu navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Changing navigation menu item views
        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        itemTrack = menu.findItem(R.id.nav_track);
        headerUser = (TextView) header.findViewById(R.id.textViewUserName);
        headerEmail = (TextView) header.findViewById(R.id.textViewEmail);

        //PreferencesUtil method getting data
        preferences = new SharedPreferencesUtils(this);
        String value = preferences.sessionData();
        headerUser.setText(preferences.loginName(value));
        headerEmail.setText(preferences.loginEmail(value));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (LocationService.isInstanceCreated()) {
            itemTrack.setTitle(R.string.stop);
        } else {
            itemTrack.setTitle(R.string.start);
        }
        // Setting up listView and adding adapter and contextMenu
        ListView coordinateListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        coordinateListView.setEmptyView(emptyView);
        registerForContextMenu(coordinateListView);
        mCursorAdapter = new LocationCursorAdapter(this, null);
        coordinateListView.setAdapter(mCursorAdapter);

        // List item view listener
        coordinateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainScreenActivity.this, GoogleMapsActivity.class);
                Uri currentCoordinatesUri = ContentUris.withAppendedId(LocationEntry.CONTENT_URI, id);
                intent.setData(currentCoordinatesUri);
                startActivity(intent);
            }
        });

        // start loader
        getLoaderManager().initLoader(COORDINATE_LOADER, null, this);
    }

    //Cursor loading
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

    // after Cursor loads
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update cursor containing updated coordinate data
       cursorRows =  data.getCount();
        mCursorAdapter.swapCursor(data);
    }

    //Cursor reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    // After permission granting or denying
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

    // Checking permissions status
    private boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        return true;

    }

    // Asking for location permissions
    private void askPermissions() {
        ActivityCompat.requestPermissions(MainScreenActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    // Location service method
    private void serviceEnable() {

        if (!LocationService.isInstanceCreated()) {

            startService(new Intent(getBaseContext(), LocationService.class));
            itemTrack.setTitle(R.string.stop);

        } else {
            stopService(new Intent(getBaseContext(), LocationService.class));
            itemTrack.setTitle(R.string.start);
        }

    }

    // Back button press
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Navigation menu item click methods
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_slider) {

            startActivity(new Intent(MainScreenActivity.this, SliderActivity.class));
        } else if (id == R.id.nav_logout) {

            preferences.sessionSetLoggedIn(false);
            preferences.sessionSetData("");
            startActivity(new Intent(MainScreenActivity.this, LoginActivity.class));
            finish();

        } else if (id == R.id.nav_track) {

            if (checkPermissions()) {
                serviceEnable();

            } else {
                askPermissions();
            }

        } else if (id == R.id.nav_delete_all) {

            deleteAllCoords();

        }else if (id == R.id.nav_show_route) {

            Toast.makeText(this, "Not Implemented", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_user_profile) {

            Intent i =new Intent(MainScreenActivity.this, ProfileActivity.class);
            i.putExtra("count",cursorRows);
            startActivity(i);

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Home button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // On item long click get id and pass it
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            long viewID = info.id;

            mCurrentUri = ContentUris.withAppendedId(LocationEntry.CONTENT_URI, viewID);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    // getting popup menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteCoord();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    // Method for deleting all data from DB
    private void deleteAllCoords() {
        int rowsDeleted = getContentResolver().delete(LocationEntry.CONTENT_URI, null, null);
        Log.v("MainScree: ", rowsDeleted + " rows deleted from database");
    }

    // Method for deleting specific placeName
    private void deleteCoord() {

        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Cant Delete",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Deleted",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
