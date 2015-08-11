package com.denimexpertexpo.denimexpo.DBHelper;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ratul on 8/8/2015.
 */
public class ExhibitorContract {

    public static final String DB_NAME  =   "denim_exhibitor.db";
    public static final int DB_VERSION  =   1;

    public static final String TABLE_NAME   =   "exhibitor";

    public static final String AUTHORITY_EXHIBITOR = "com.denimexpertexpo.denimexpo.DBHelper.ExhibitorProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_EXHIBITOR + "/" + TABLE_NAME);

    public static final int EXHIBITOR_ITEM   = 1;
    public static final int EXHIBITOR_DIR    = 2;

    public static final String DEFAULT_SORT_ORDER = Column.FIRST_NAME + " COLLATE NOCASE ASC";

    public class Column
    {
        public static final String  ID              = BaseColumns._ID;
        public static final String  GEN_ID          = "gen_id";
        public static final String  FIRST_NAME      = "first_name";
        public static final String  LAST_NAME       = "last_name";
        public static final String  EMAIL           = "email";
        public static final String  PHONE           = "phone";
        public static final String  COMPANY_NAME    = "company_name";
        public static final String  WEBSITE         = "website";
        public static final String  COMPANY_ADDRESS = "comapany_address";
        public static final String  MOBILE          = "mobile";
        public static final String  INDUSTRY_TYPE   = "industry_type";
        public static final String  ANNUAL_TURN_OVER= "annual_turnover";
        public static final String  NUM_OF_EMPLOYEE = "num_of_employee";
        public static final String  BUYER_NAME      = "buyer_name";
        public static final String  PRODUCT_DETAILS = "product_detail";
        public static final String  BUYER_COUNTRY   = "buyer_country";
        public static final String  BUSINESS        = "business";
        public static final String  PRODUCT         = "product";
    }

}
