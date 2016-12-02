package com.example.kasparsfisers.loginapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.adapters.LocationCursorAdapter;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.data.LocationContract.LocationEntry;
import com.example.kasparsfisers.loginapp.services.LocationService;
import com.example.kasparsfisers.loginapp.utils.Functions;
import com.example.kasparsfisers.loginapp.utils.SharedPreferencesUtils;
import com.example.kasparsfisers.loginapp.views.ProgressView;

import java.io.File;

import static com.example.kasparsfisers.loginapp.R.id.takePhoto;

public class MainScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener {

    private static final int COORDINATE_LOADER = 0;
    private static final int PERMISSION_REQ_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int CONTENT_REQUEST = 1337;
    private File output = null;
    MenuItem itemTrack;
    float maxRows = 10;
    LocationCursorAdapter mCursorAdapter;
    DrawerLayout drawer;
    ProgressView progressView;
    SharedPreferencesUtils preferences;
    private float cursorRows;
    TextView headerUser;
    TextView headerEmail;
    private Uri mCurrentUri;
    private String mCurrentPicture;
    private long mCurrentId;

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


        //Custom view progress
        progressView = (ProgressView) findViewById(R.id.progView);


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
        preferences = SharedPreferencesUtils.getInstance(this);
        String value = preferences.sessionData();
        headerUser.setText(preferences.loginName(value));
        headerEmail.setText(preferences.loginEmail(value));
        maxRows = preferences.getMaxLoc();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (LocationService.isInstanceCreated()) {
            itemTrack.setTitle(R.string.stop);
        } else {
            itemTrack.setTitle(R.string.start);
        }
        // Setting up listView and adding adapter and contextMenu
        ListView coordinateListView = (ListView) findViewById(R.id.list);
//        View emptyView = findViewById(R.id.empty_view);
//        coordinateListView.setEmptyView(emptyView);
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
        verifyStoragePermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressView.refresh();
    }

    //Cursor loading
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String[] projection = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCNAME,
                LocationEntry.COLUMN_PICTURE_URI};

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
        cursorRows = data.getCount();
        mCursorAdapter.swapCursor(data);

        float progressCount = cursorRows / maxRows * 100;
        progressView.setProgress(progressCount);
        progressView.setTitle(String.format("%.1f", progressCount));


        if (cursorRows == maxRows) {
            itemTrack.setTitle(R.string.start);
        }


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
            case PERMISSION_REQ_CODE: {
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
                PERMISSION_REQ_CODE);
    }

    // Asking for Storage permissions
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(MainScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(MainScreenActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    // Location service method
    private void serviceEnable() {

        if (!LocationService.isInstanceCreated() && cursorRows < maxRows) {

            Intent i = new Intent(getBaseContext(), LocationService.class);
            i.putExtra(Functions.CURRENT_LOC, cursorRows);
            startService(i);

            itemTrack.setTitle(R.string.stop);

        } else {
            stopService(new Intent(getBaseContext(), LocationService.class));
            itemTrack.setTitle(R.string.start);
        }
        if (cursorRows >= maxRows) {
            Toast.makeText(this, "Memory Full", Toast.LENGTH_SHORT).show();
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

            showDeleteConfirmationDialog();

        } else if (id == R.id.nav_show_route) {

            Intent intent = new Intent(MainScreenActivity.this, GoogleMapsActivity.class);

            Uri allCoordUri = LocationEntry.CONTENT_URI;
            intent.setData(allCoordUri);
            startActivity(intent);


        } else if (id == R.id.nav_user_profile) {

            Intent i = new Intent(MainScreenActivity.this, ProfileActivity.class);
            i.putExtra(Functions.CURRENT_LOC, cursorRows);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainScreenActivity.this, SettingsActivity.class));

        } else if (id == R.id.nav_video) {
            startActivity(new Intent(MainScreenActivity.this, VideoActivity.class));
            Toast.makeText(this, "Playing video from assets", Toast.LENGTH_SHORT).show();
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
            mCurrentId = viewID;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    // getting popup menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.delete:
                deleteCoord();
                return true;
            case takePhoto:
                takePhoto();
                return true;
            case R.id.showPhoto:
                i = new Intent(this, DisplayPlaceActivity.class);
                i.putExtra(Functions.CURRENT_ID, mCurrentId);
                startActivity(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            if (resultCode == RESULT_OK && mCurrentPicture != null) {


                ContentValues values = new ContentValues();
                values.put(LocationEntry.COLUMN_PICTURE_URI, mCurrentPicture);


                int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
                mCurrentPicture = null;
                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, "Error with updating coords",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can activity_main_display a toast.

                }

            }
        }
    }

    private void takePhoto() {

        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), Functions.FOLDER_MAIN);
        if (!f.exists()) {
            f.mkdirs();
        }


        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);


        output = new File(dir, Functions.FOLDER_MAIN + "/" + mCurrentId + ".jpeg");
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        mCurrentPicture = output.getAbsolutePath();
        startActivityForResult(i, CONTENT_REQUEST);

    }

    // Method for deleting all data from DB
    private void deleteAllCoords() {
        int rowsDeleted = getContentResolver().delete(LocationEntry.CONTENT_URI, null, null);


        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), Functions.FOLDER_MAIN);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                new File(dir, aChildren).delete();
            }
            Toast.makeText(this, "deleted all", Toast.LENGTH_SHORT).show();
        }


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

                File dir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                output = new File(dir, Functions.FOLDER_MAIN + "/" + mCurrentId + ".jpeg");
                if (output.exists()) {
                    output.delete();
                }
            }

        }
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete all.
                deleteAllCoords();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
