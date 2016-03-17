package com.inau.ctxph.itucontextphoneapp.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.inau.ctxph.itucontextphoneapp.data.local.DAO;
import com.inau.ctxph.itucontextphoneapp.data.local.SqlLiteHelper;

/**
 * Created by Ivan on 03-03-2016.
 */
public class BeaconProvider extends ContentProvider {

    public static final String resource_name = ""+BeaconProvider.class.getPackage().toString();

    //private static final UriMatcher sUriMatcher;
    /*
     * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
     * in the path
     */
    //sUriMatcher.addURI("com.example.app.provider", "table3", 1);

    /*
     * Sets the code for a single row to 2. In this case, the "#" wildcard is
     * used. "content://com.example.app.provider/table3/3" matches, but
     * "content://com.example.app.provider/table3 doesn't.
     */
    //sUriMatcher.addURI("com.example.app.provider", "table3/#", 2);

    @Override
    public boolean onCreate() {
        if( !DAO.isOpen() ) {
            DAO.open( getContext() );
        }
        return DAO.isOpen();
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return DAO.Query(SqlLiteHelper.TBL_beacon, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
