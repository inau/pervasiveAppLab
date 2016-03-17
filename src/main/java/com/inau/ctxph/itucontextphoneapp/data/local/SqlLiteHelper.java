package com.inau.ctxph.itucontextphoneapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ivan on 25-02-2016.
 */
public class SqlLiteHelper extends SQLiteOpenHelper {
    final static int version = 2;

    final public static String      DBNAME = "ctxphone.db",
                                    TBL_beacon = "beacons",
                                    TBL_ctx = "contexts",
                                    COL_id = "_ID",
                                    COL_time = "last_update",
                                    COL_beacon_uuid = "uuid",
                                    COL_beacon_major = "major",
                                    COL_beacon_minor = "minor",
                                    COL_beacon_type = "typecode",
                                    COL_beacon_manufactorer = "manufactor",
                                    COL_beacon_data = "data",
                                    COL_beacon_dist = "dist",
                                    COL_ctx_type = "type",
                                    COL_ctx_sensor = "sensor",
                                    COL_ctx_val = "value"
    ;

    final String    create_beacon = "CREATE TABLE " + TBL_beacon + version + "(" +
                                    COL_id + " integer primary key, " +
                                    COL_beacon_uuid + " text, " +
                                    COL_beacon_major + " text, " +
                                    COL_beacon_minor + " text, " +
                                    COL_beacon_type + " text, " +
                                    COL_beacon_manufactorer + " text, " +
                                    COL_beacon_data + " text," +
                                    COL_time + " integer not null," +
                                    COL_beacon_dist + " real not null);";

    final String    create_context = "CREATE TABLE " + TBL_ctx + version + "(" +
                                    COL_id + " integer primary key, " +
                                    COL_ctx_type + " text, " +
                                    COL_ctx_sensor + " text, " +
                                    COL_ctx_val + " text, " +
                                    COL_time + " long not null);"
            ;


    public SqlLiteHelper(Context context ) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_beacon);
        db.execSQL(create_context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_beacon+oldVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TBL_ctx+oldVersion);
            db.execSQL(create_beacon);
            db.execSQL(create_context);
        }
    }
}
