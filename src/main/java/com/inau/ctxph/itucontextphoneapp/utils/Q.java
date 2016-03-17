package com.inau.ctxph.itucontextphoneapp.utils;

import com.inau.ctxph.itucontextphoneapp.data.local.BeaconDAO;
import com.inau.ctxph.itucontextphoneapp.data.local.DAO;

/**
 * Created by Ivan on 25-02-2016.
 */
public class Q {

    public static class ObserverData<T> {
        final ObserverNotificationType type;
        final T data;

        public ObserverData(ObserverNotificationType type, T data) {
            this.data = data;
            this.type = type;
        }

        public T getData() {
            return data;
        }

        public ObserverNotificationType getType() {
            return type;
        }
    }

    public enum ObserverNotificationType {
        SQL, WEB;
    }

    //list DAO's
    public static class SQL extends DAO {
        public static BeaconDAO BEACONS = new BeaconDAO();
    }

}
