package com.denimexpertexpo.denimexpo.Constants;

/**
 * Created by ratul on 8/12/2015.
 */

/**
 * All Denim related global related constant's are
 * intendted to rest here :)
 */
public class DenimContstants {

    /*
        SharedPreference's constants
     */
    public static final String SHARED_PREFS_NAME = DenimContstants.class.getName();
    public static final String SHARED_PREFS_LAST_EVENT_MAP_ID_KEY = "shared_pref_event_map_id";
    public static final String SHARED_PREFS_LAST_EVENT_MAP_LOCATION = "shared_pref_event_map_filesystem_path";


    //these values are intended to kept as hash values, but there is no time to do that :(
    public static final String SHARED_PREFS_USERNAME    = "shared_pref_secret_one";
    public static final String SHARED_PREFS_PASSWORD    = "shared_pref_secret_two";
    public static final String SHARED_PREFS_REGISTERED  = "shared_pref_registered";
    public static final String SHARED_PREFS_USER_TYPE   = "shared_pref_usertype";   //contains only 'visitor' 'exhibitor
    public static final String SHARED_PREFS_USER_ID     = "shared_prefs_user_id";

    public static final String USER_TYPE_VISITOR        = "visitor";
    public static final String USER_TYPE_EXHIBITOR      = "exhibitor";

    //VISITOR SUMMARY CONSTANTS
    public static final String SHARED_PREFS_VISITOR_SUMMARY_APPLICANT   = "shared_pref_visitor_summary_applicant";
    public static final String SHARED_PREFS_VISITOR_SUMMARY_VISITOR     = "shared_pref_visitor_summary_visitor";
    public static final String SHARED_PREFS_VISITOR_SUMMARY_COMPANY     = "shared_pref_visitor_summary_company";
    public static final String SHARED_PREFS_VISITOR_SUMMARY_COUNTRIES   = "shared_pref_visitor_summary_countries";

}
