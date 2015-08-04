package com.denimexpertexpo.denimexpo.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by ratul on 8/2/2015.
 */
public class ScheduleProvider extends ContentProvider {

    private DbScheduleHelper dbScheduleHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(Contract.AUTHORITY_SCHEDULE, ScheduleContract.TABLE_NAME, ScheduleContract.SCHEDULE_DIR);
        uriMatcher.addURI(Contract.AUTHORITY_SCHEDULE, ScheduleContract.TABLE_NAME + "/#", ScheduleContract.SCHEDULE_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbScheduleHelper = new DbScheduleHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ScheduleContract.TABLE_NAME);

        switch(uriMatcher.match(uri))
        {
            case ScheduleContract.SCHEDULE_DIR:
            {

            }
            break;

            case ScheduleContract.SCHEDULE_ITEM:
            {
                queryBuilder.appendWhere(ScheduleContract.Column.ID+ "=" + uri.getLastPathSegment());

            }
            break;

            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }

        String orderBy = (TextUtils.isEmpty(sortOrder)) ? ScheduleContract.DEFAULT_SORT_ORDER : sortOrder;

        SQLiteDatabase db = dbScheduleHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    //no update implementation needed because this functionality is not supported for the schedule provider
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri ret = null;

        if(uriMatcher.match(uri) != ScheduleContract.SCHEDULE_DIR)
        {
            throw new IllegalArgumentException("Illegal uri" + uri);
        }

        SQLiteDatabase db = dbScheduleHelper.getWritableDatabase();
        long rowID = db.insertWithOnConflict(ScheduleContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        //was inserted successfully
        if(rowID != -1)
        {
            long id = contentValues.getAsLong(ScheduleContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String where;

        //calculate the where clause
        switch (uriMatcher.match(uri))
        {
            case ScheduleContract.SCHEDULE_DIR:
            {
                where = (selection == null) ? "1" : selection;
            }
            break;

            case ScheduleContract.SCHEDULE_ITEM:
            {
                long id = ContentUris.parseId(uri);
                where = ScheduleContract.Column.ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " and (" + selection + ")");
            }
            break;

            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }

        SQLiteDatabase db = dbScheduleHelper.getWritableDatabase();
        int ret = db.delete(ScheduleContract.TABLE_NAME, where, selectionArgs);

        if(ret > 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }

    @Override
    //no update implementation needed because this functionality is not supported for the schedule provider
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
