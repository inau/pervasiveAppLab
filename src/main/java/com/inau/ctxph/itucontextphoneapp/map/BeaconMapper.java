package com.inau.ctxph.itucontextphoneapp.map;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan on 03-03-2016.
 */
public class BeaconMapper {

    class BeaconLocationMapping {
        int major;
        String minor;

        Location coords;

        BeaconLocationMapping(int major, String minor, Location coords) {

        }
    }

    Map<Long, BeaconLocationMapping> mappings = new HashMap<>();


}
