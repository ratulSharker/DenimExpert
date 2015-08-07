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
 * Created by ratul on 8/7/2015.
 */
public class VisitorProvider extends ContentProvider {

    private DbVisitorHelper dbVisitorHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(VisitorContract.AUTHORITY_VISITOR, VisitorContract.TABLE_NAME, VisitorContract.VISITOR_DIR);
        uriMatcher.addURI(VisitorContract.AUTHORITY_VISITOR, VisitorContract.TABLE_NAME + "/#", VisitorContract.VISITOR_ITEM);
    }

    @Override
    public boolean onCreate() {
        this.dbVisitorHelper = new DbVisitorHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(VisitorContract.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case VisitorContract.VISITOR_DIR: {

            }
            break;

            case VisitorContract.VISITOR_ITEM: {
                queryBuilder.appendWhere(VisitorContract.Column.ID + "=" + uri.getLastPathSegment());

            }
            break;

            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }

        String orderBy = (TextUtils.isEmpty(sortOrder)) ? VisitorContract.DEFAULT_SORT_ORDER : sortOrder;

        SQLiteDatabase db = dbVisitorHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {


        Uri ret = null;

        if (uriMatcher.match(uri) != VisitorContract.VISITOR_DIR) {
            throw new IllegalArgumentException("Illegal uri" + uri);
        }

        SQLiteDatabase db = dbVisitorHelper.getWritableDatabase();
        long rowID = db.insertWithOnConflict(VisitorContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        //was inserted successfully
        if (rowID != -1) {
            long id = contentValues.getAsLong(VisitorContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ret;

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String where;

        //calculate the where clause
        switch (uriMatcher.match(uri)) {
            case VisitorContract.VISITOR_DIR: {
                where = (selection == null) ? "1" : selection;
            }
            break;

            case VisitorContract.VISITOR_ITEM: {
                long id = ContentUris.parseId(uri);
                where = VisitorContract.Column.ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " and (" + selection + ")");
            }
            break;

            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }

        SQLiteDatabase db = dbVisitorHelper.getWritableDatabase();
        int ret = db.delete(VisitorContract.TABLE_NAME, where, selectionArgs);

        if (ret > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ret;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
