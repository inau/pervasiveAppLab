package com.inau.ctxph.itucontextphoneapp.data.local.entities

import android.database.Cursor
import com.inau.ctxph.itucontextphoneapp.data.local.SqlLiteHelper

/**
 * Created by Ivan on 25-02-2016.
 */
class Beacon : CursorBuildable<Beacon> {
    override fun createFromCursor(cursor: Cursor?): Beacon? {
        val result = Beacon()
        result.id = cursor!!.getLong( cursor.getColumnIndex(SqlLiteHelper.COL_id) )
        result.uuid = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_uuid) )
        result.major = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_major) )
        result.minor = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_minor) )
        result.type = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_type) )
        result.manufactorer = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_manufactorer) )
        result.data = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_data) )
        result.lastKnownDistance = cursor.getFloat( cursor.getColumnIndex(SqlLiteHelper.COL_beacon_dist) )
        result.time = cursor.getInt( cursor.getColumnIndex(SqlLiteHelper.COL_time) )
        return result;
    }

    var id: Long = 0
    var uuid: String? = null
    var major: String? = null
    var minor: String? = null
    var type: String? = null
    var manufactorer: String? = null
    var data: String? = null
    var lastKnownDistance: Float = 0.toFloat()
    var time: Int? = null
}
