package com.inau.ctxph.itucontextphoneapp.data.local.entities

import android.database.Cursor
import com.inau.ctxph.itucontextphoneapp.data.local.SqlLiteHelper

import java.util.Date

/**
 * Created by Ivan on 03-03-2016.
 */
class ContextEntity : CursorBuildable<ContextEntity> {

    var id: Long? = null
    var type: String? = null
    var sensor: String? = null
    var value: String? = null
    var timeStamp: Date? = null

    override fun createFromCursor(cursor: Cursor): ContextEntity? {
        val result = ContextEntity()
        result.id = cursor.getLong( cursor.getColumnIndex(SqlLiteHelper.COL_id) )
        result.type = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_ctx_type) )
        result.sensor = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_ctx_sensor) )
        result.value = cursor.getString( cursor.getColumnIndex(SqlLiteHelper.COL_ctx_val) )
        result.timeStamp = Date(cursor.getLong( cursor.getColumnIndex(SqlLiteHelper.COL_time) ) )
        return result
    }
}
