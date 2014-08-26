package com.softxpliot.demob.beepme;

/**
 * Created by demob on 8/24/14.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pDevice;

public class Contact {
    public String deviceName;
    public String deviceAddress;
    public WifiP2pDevice device;
    public String statusMessage;
    public int contentDescription;
    public DatabaseHelper dbHelper;

    public Contact(WifiP2pDevice device, Context context) {
        deviceName = device.deviceName;
        deviceAddress = device.deviceAddress;
        dbHelper = new DatabaseHelper(context);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public boolean isAdded(String deviceAddress){
        return false;
    }

    public void insert(WifiP2pDevice device){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schema.DeviceInfo.COLUMN_NAME_DEVICE_NAME, device.deviceName);
        values.put(Schema.DeviceInfo.COLUMN_NAME_DEVICE_ADDRESS, device.deviceAddress);
        values.put(Schema.DeviceInfo.COLUMN_NAME_CONTENT_DESCRIPTION, device.describeContents());
        values.put(Schema.DeviceInfo.COLUMN_NAME_DATE_CREATED, Long.valueOf(System.currentTimeMillis()));
        values.put(Schema.DeviceInfo.COLUMN_NAME_DATE_MODIFIED,Long.valueOf(System.currentTimeMillis()));

        long newId;
        newId = db.insert(Schema.DeviceInfo.TABLE_NAME,null,values);
    }


}
