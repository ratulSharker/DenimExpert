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
 * Created by ratul on 8/8/2015.
 */
public class ExhibitorProvider extends ContentProvider
{
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ExhibitorContract.AUTHORITY_EXHIBITOR, ExhibitorContract.TABLE_NAME, ExhibitorContract.EXHIBITOR_DIR);
        uriMatcher.addURI(ExhibitorContract.AUTHORITY_EXHIBITOR, ExhibitorContract.TABLE_NAME + "/#", ExhibitorContract.EXHIBITOR_ITEM);
    }

    private DbExhibitorHelper dbExhibitorHelper;

    @Override
    public boolean onCreate() {
        this.dbExhibitorHelper = new DbExhibitorHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ExhibitorContract.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ExhibitorContract.EXHIBITOR_DIR: {

            }
            break;

            case ExhibitorContract.EXHIBITOR_ITEM: {
                queryBuilder.appendWhere(ExhibitorContract.Column.ID + "=" + uri.getLastPathSegment());
            }
            break;

            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }

        String orderBy = (TextUtils.isEmpty(sortOrder)) ? ExhibitorContract.DEFAULT_SORT_ORDER : sortOrder;

        SQLiteDatabase db = dbExhibitorHelper.getWritableDatabase();
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

        if (uriMatcher.match(uri) != ExhibitorContract.EXHIBITOR_DIR) {
            throw new IllegalArgumentException("Illegal uri" + uri);
        }

        SQLiteDatabase db = dbExhibitorHelper.getWritableDatabase();
        long rowID = db.insertWithOnConflict(ExhibitorContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        //was inserted successfully
        if (rowID != -1) {
            long id = contentValues.getAsLong(ExhibitorContract.Column.ID);
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
            case ExhibitorContract.EXHIBITOR_DIR: {
                where = (selection == null) ? "1" : selection;
            }
            break;

            case ExhibitorContract.EXHIBITOR_ITEM: {
                long id = ContentUris.parseId(uri);
                where = ExhibitorContract.Column.ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " and (" + selection + ")");
            }
            break;

            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }

        SQLiteDatabase db = dbExhibitorHelper.getWritableDatabase();
        int ret = db.delete(ExhibitorContract.TABLE_NAME, where, selectionArgs);

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
