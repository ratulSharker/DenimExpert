package com.denimexpertexpo.denimexpo.DBHelper;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.security.PublicKey;

/**
 * Created by ratul on 7/31/2015.
 */
public class ScheduleContract{

    public static final String DB_NAME  =   "denim_schedule.db";
    public static final int DB_VERSION  =   1;

    public static final String TABLE_NAME   =   "schedule";
    public static final String AUTHORITY_SCHEDULE = "com.denimexpertexpo.denimexpo.DBHelper.ScheduleProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_SCHEDULE + "/" + TABLE_NAME);

    public static final int SCHEDULE_ITEM   = 1;
    public static final int SCHEDULE_DIR    = 2;

    public static final String DEFAULT_SORT_ORDER = Column.ID + " DESC";

    public class Column
    {
        public static final String  ID = BaseColumns._ID;
        public static final String  EVENT_NAME  = "event_name";
        public static final String  ADD_TIME    = "add_time";
        public static final String  START_TIME  = "start_time";
        public static final String  END_TIME    = "end_time";
        public static final String  DETAILS     = "details";
        public static final String  DURATION    = "duration";
    }
}
