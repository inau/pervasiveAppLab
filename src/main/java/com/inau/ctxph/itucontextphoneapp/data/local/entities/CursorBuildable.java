package com.inau.ctxph.itucontextphoneapp.data.local.entities;

import android.database.Cursor;

/**
 * Created by Ivan on 27-02-2016.
 */
public interface CursorBuildable<T> {

    public T createFromCursor(Cursor cursor);

}
