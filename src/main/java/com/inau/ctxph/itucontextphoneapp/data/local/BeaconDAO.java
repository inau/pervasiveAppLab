package com.inau.ctxph.itucontextphoneapp.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.inau.ctxph.itucontextphoneapp.data.local.entities.Beacon;

import java.util.Date;

/**
 * Created by Ivan on 25-02-2016.
 */
public class BeaconDAO extends DAO {
    //used internlly - db_name+version to update properly
    private final String tblName = SqlLiteHelper.TBL_beacon+SqlLiteHelper.version;

    private String[] allColumns = {
            SqlLiteHelper.COL_id,
            SqlLiteHelper.COL_beacon_uuid,
            SqlLiteHelper.COL_beacon_major,
            SqlLiteHelper.COL_beacon_minor,
            SqlLiteHelper.COL_beacon_type,
            SqlLiteHelper.COL_beacon_manufactorer,
            SqlLiteHelper.COL_beacon_data,
            SqlLiteHelper.COL_beacon_dist,
            SqlLiteHelper.COL_time
    };

    /**
     * Null id means not persisted
     * @param b
     */
    public Beacon createFromObject( Beacon b ) throws IllegalAccessException, InstantiationException {
        return createBeacon(b.getId(),b.getUuid(),b.getMajor(),b.getMinor(),b.getType(),b.getManufactorer(),b.getData(),b.getLastKnownDistance());
    }

    /**
     * @param id
     * @param uuid
     * @param major
     * @param minor
     * @param type
     * @param manufactorer
     * @param data
     * @param lastKnown
     * @return
     */
    public Beacon createBeacon(Long id, String uuid, String major, String minor, String type,
                               String manufactorer, String data, double lastKnown) throws InstantiationException, IllegalAccessException {
        ContentValues values = new ContentValues();
        if(id != null )values.put(SqlLiteHelper.COL_id, id);
        values.put(SqlLiteHelper.COL_beacon_uuid, uuid);
        values.put(SqlLiteHelper.COL_beacon_major, major);
        values.put(SqlLiteHelper.COL_beacon_minor, minor);
        values.put(SqlLiteHelper.COL_beacon_type, type);
        values.put(SqlLiteHelper.COL_beacon_manufactorer, manufactorer);
        values.put(SqlLiteHelper.COL_beacon_data, data);
        values.put(SqlLiteHelper.COL_beacon_dist, lastKnown);
        values.put(SqlLiteHelper.COL_time, new Date().getTime() );

        String[] wArg = {   uuid,
                            major,
                            minor
        };

        long insertId = id == null ? -1 : id;
        int i = _db.updateWithOnConflict(   tblName , values,
                        SqlLiteHelper.COL_beacon_uuid + " =? AND " +
                        SqlLiteHelper.COL_beacon_major + " =? AND " +
                        SqlLiteHelper.COL_beacon_minor + " =?",
                wArg, _db.CONFLICT_IGNORE);

        if(i == 0) insertId = _db.insert(tblName, null, values);

        Cursor cursor = _db.query(tblName, allColumns,
                        SqlLiteHelper.COL_beacon_uuid + " =? AND " +
                        SqlLiteHelper.COL_beacon_major + " =? AND " +
                        SqlLiteHelper.COL_beacon_minor + " =?",
                        wArg,
                        null, null, null);
        cursor.moveToFirst();
        Beacon newBeacon = cursorToObject(cursor, Beacon.class);

        cursor.close();
        setChanged();
        notifyObservers(DefaultMessage);
        return newBeacon;
    }

    /**
     * retrieve from db
     * @param id
     * @return Beacon object
     */
    public Beacon[] findBeaconsWithUuid(String id) {
        String[] wArg = { id };

        Cursor cursor = _db.query(tblName,
                allColumns, SqlLiteHelper.COL_beacon_uuid + " = ? COLLATE nocase", wArg,
                null, null, SqlLiteHelper.COL_beacon_major + ", " + SqlLiteHelper.COL_beacon_minor);

        Log.i(TAG, "findBeacons ["+id+"] = " + cursor.getCount());
        try {
            return cursorToListOfObjects(cursor, Beacon.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new Beacon[0];
    }

    /**
     * Retrieve all beacons
     * @return Beacon[]
     */
    public Beacon[] getAllBeacons() {
        Cursor cursor = _db.query(tblName,
                allColumns, null, null, null, null,
                SqlLiteHelper.COL_beacon_major + ", " + SqlLiteHelper.COL_beacon_minor);
        try {
            return cursorToListOfObjects(cursor, Beacon.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new Beacon[0];
    }

}
