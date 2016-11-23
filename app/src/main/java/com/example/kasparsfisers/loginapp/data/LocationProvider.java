package com.example.kasparsfisers.loginapp.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.kasparsfisers.loginapp.data.LocationContract.LocationEntry;

public class LocationProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = LocationProvider.class.getSimpleName();

    // code for the coordinates table */
    private static final int COORDINATES = 100;

    // code for a single coordinate in the coordinates table */
    private static final int COORDINATE_ID = 101;

    // UriMatcher object to match a content URI to a corresponding code.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(LocationContract.CONTENT_AUTHORITY, LocationContract.PATH_COORDINATES, COORDINATES);

        sUriMatcher.addURI(LocationContract.CONTENT_AUTHORITY, LocationContract.PATH_COORDINATES + "/#", COORDINATE_ID);
    }

    private LocationDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new LocationDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case COORDINATES:
                cursor = database.query(LocationEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case COORDINATE_ID:

                selection = LocationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(LocationEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // If the data at this URI changes, then update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COORDINATES:
                return insertCoordinates(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertCoordinates(Uri uri, ContentValues values) {


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(LocationEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the coordinates content URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COORDINATES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COORDINATE_ID:
                // Delete a single row given by the ID in the URI
                selection = LocationEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COORDINATES:
                return updateCoords(uri, values, selection, selectionArgs);
            case COORDINATE_ID:
                selection = LocationEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCoords(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateCoords(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(LocationEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return rowsUpdated;

    }

}
