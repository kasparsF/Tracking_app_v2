package com.example.kasparsfisers.loginapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.data.LocationContract;
import com.example.kasparsfisers.loginapp.utils.Functions;

import java.io.IOException;


public class LocationCursorAdapter extends CursorAdapter {
    public LocationCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.placeName = (TextView) rowView.findViewById(R.id.placeName);
        holder.placePicture = (ImageView) rowView.findViewById(R.id.placePicture);
        rowView.setTag(holder);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find views
        ViewHolder holder = (ViewHolder) view.getTag();

        int nameIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_LOCNAME);
        int pictureIndex = cursor.getColumnIndex(LocationContract.LocationEntry.COLUMN_PICTURE_URI);

        String name = cursor.getString(nameIndex);
        String pictureStr = cursor.getString(pictureIndex);

        holder.placeName.setText(name);

        if (!Functions.isEmpty(pictureStr)) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(pictureStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] imageData = exif.getThumbnail();
            Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            holder.placePicture.setImageBitmap(thumbnail);

        } else {
            holder.placePicture.setImageResource(R.drawable.ic_room_black_24dp);
        }
    }

    private class ViewHolder {
        TextView placeName;
        ImageView placePicture;

    }

}