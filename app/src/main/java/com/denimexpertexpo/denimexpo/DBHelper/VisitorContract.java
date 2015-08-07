package com.denimexpertexpo.denimexpo.DBHelper;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ratul on 8/5/2015.
 */
public class VisitorContract {

    public static final String DB_NAME  =   "denim_visitor.db";
    public static final int DB_VERSION  =   1;

    public static final String TABLE_NAME   =   "visitor";

    public static final String AUTHORITY_VISITOR = "com.denimexpertexpo.denimexpo.DBHelper.VisitorProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_VISITOR + "/" + TABLE_NAME);

    public static final int VISITOR_ITEM   = 1;
    public static final int VISITOR_DIR    = 2;

    public static final String DEFAULT_SORT_ORDER = Column.ID + " DESC";

    public class Column
    {
        public static final String  ID              = BaseColumns._ID;
        public static final String  GEN_ID          = "gen_id";
        public static final String  FIRST_NAME      = "first_name";
        public static final String  LAST_NAME       = "last_name";
        public static final String  FULL_NAME       = "full_name";
        public static final String  EMAIL           = "email";
        public static final String  PHONE           = "phone";
        public static final String  COMPANY_NAME    = "company_name";
        public static final String  WEBSITE         = "website";
        public static final String  ADDRESS         = "address";
        public static final String  INDUSTRY_TYPE   = "industry_type";
        public static final String  JOB_TITLE       = "job_title";
        public static final String  DEPARTMENT      = "department";
        public static final String  DATE            = "date";
    }
}
