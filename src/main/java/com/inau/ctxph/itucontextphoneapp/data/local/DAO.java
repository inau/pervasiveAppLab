package com.inau.ctxph.itucontextphoneapp.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.inau.ctxph.itucontextphoneapp.data.local.entities.CursorBuildable;
import com.inau.ctxph.itucontextphoneapp.utils.Q;

import java.lang.reflect.Array;
import java.util.Observable;

/**
 * Created by Ivan on 25-02-2016.
 */
public class DAO extends Observable {
    protected final String TAG = "DataAccessObjects";
    protected static Q.ObserverData<Void> DefaultMessage = new Q.ObserverData<>(Q.ObserverNotificationType.SQL, null);
    protected static SqlLiteHelper _helper;
    protected static SQLiteDatabase _db;

    public static Cursor Query(String tbl, String[] cols, String select, String[] selectArgs, String groupBy, String having, String orderBy) {
        return isOpen() ? _db.query(tbl, cols, select, selectArgs, groupBy, having, orderBy) : null;
    }

    public static void open(Context c) {
        _helper = new SqlLiteHelper( c.getApplicationContext() );
        _db = _helper.getWritableDatabase();
    }

    public static void close() {
        _db.close(); _db = null;
    }

    public static boolean isOpen() {
        return _db != null;
    }

    /**
     * Expects raw cursor after query, and will handle all the different row instantiations
     * @param c
     * @param returnType
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected <T extends CursorBuildable> T[] cursorToListOfObjects(Cursor c, Class<T> returnType) throws InstantiationException, IllegalAccessException {
        T[] rows = (T[]) Array.newInstance(returnType, c.getCount());
        int i = 0;
        c.moveToFirst();
        while( !c.isAfterLast() ) {
            rows[i++] = cursorToObject(c, returnType);
            c.moveToNext();
        }
        return rows;
    }

    /**
     * Cursor needs to be set properly to the given object
     * @param c
     * @param returnType
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    protected <T extends CursorBuildable> T cursorToObject(Cursor c, Class<T> returnType) throws IllegalAccessException, InstantiationException {
        return (T) returnType.newInstance().createFromCursor(c);
    }
}
