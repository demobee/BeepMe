package com.softxpliot.demob.beepme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by demob on 8/25/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "beepme.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = "Contact";

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Schema.DeviceInfo.TABLE_NAME + " ("
                + Schema.DeviceInfo._ID + " INTEGER PRIMARY KEY,"
                + Schema.DeviceInfo.COLUMN_NAME_DEVICE_NAME + " TEXT,"
                + Schema.DeviceInfo.COLUMN_NAME_CONTENT_DESCRIPTION + " INTEGER,"
                + Schema.DeviceInfo.COLUMN_NAME_DEVICE_ADDRESS + " TEXT,"
                + Schema.DeviceInfo.COLUMN_NAME_DATE_CREATED + " INTEGER,"
                + Schema.DeviceInfo.COLUMN_NAME_DATE_MODIFIED + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS deviceinfo");

        onCreate(db);
    }
}
