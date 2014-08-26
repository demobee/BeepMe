package com.softxpliot.demob.beepme;

import android.provider.BaseColumns;

/**
 * Created by demob on 8/25/14.
 */
public final class Schema {
    private Schema(){
    }

    public static final class DeviceInfo implements BaseColumns{
        private DeviceInfo(){
        }

        public static final String TABLE_NAME = "deviceinfo";

        public static final String COLUMN_NAME_DEVICE_NAME = "devicename";

        public static final String COLUMN_NAME_DEVICE_ADDRESS = "deviceaddress";

        public static final String COLUMN_NAME_CONTENT_DESCRIPTION = "contentdescription";

        public static final String COLUMN_NAME_DATE_CREATED = "created";

        public static final String COLUMN_NAME_DATE_MODIFIED = "modified";

    }
    
}
