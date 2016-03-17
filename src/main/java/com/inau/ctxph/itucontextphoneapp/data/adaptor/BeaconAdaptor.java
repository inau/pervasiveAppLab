package com.inau.ctxph.itucontextphoneapp.data.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inau.ctxph.itucontextphoneapp.R;
import com.inau.ctxph.itucontextphoneapp.data.local.entities.Beacon;
import com.inau.ctxph.itucontextphoneapp.fragments.BeaconMonitoringFragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by Ivan on 20-12-2015.
 */
public class BeaconAdaptor extends BaseAdapter {

    static final Map<Character, Character> minorCodes;
    static {
        Map<Character, Character> aMap = new HashMap<>();
        aMap.put(Character.valueOf('1'), Character.valueOf('A'));
        aMap.put(Character.valueOf('2'), Character.valueOf('B'));
        aMap.put(Character.valueOf('3'), Character.valueOf('C'));
        aMap.put(Character.valueOf('4'), Character.valueOf('D'));
        aMap.put(Character.valueOf('5'), Character.valueOf('E'));
        minorCodes = Collections.unmodifiableMap(aMap);
    }

    protected HashSet<Long> openviews = new HashSet<>();

    Context context;
    Beacon[] data;
    private static LayoutInflater inflater = null;

    public BeaconAdaptor(Context context, Beacon[] data) {

        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return data[position].getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View vi = convertView == null ?
                inflater.inflate(R.layout.beacon_row, null)
                : convertView;

        final RelativeLayout info = (RelativeLayout) vi.findViewById(R.id.bc_info);
        vi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (vi.getId() == v.getId()) {
                    if (info.isShown()) openviews.remove(data[position].getId());
                    else openviews.add( data[position].getId() );

                    info.setVisibility(openviews.contains(info) ? View.VISIBLE : View.GONE);
                }
            }
        });

        info.setVisibility(openviews.contains( data[position].getId() ) ? View.VISIBLE : View.GONE);

        String uuid = data[position].getUuid();
        boolean isItu = uuid.equalsIgnoreCase(BeaconMonitoringFragment.ITU_UUID);

        ImageView img = (ImageView) vi.findViewById(R.id.bc_img);
        img.setBackground( isItu    ? vi.getResources().getDrawable(R.mipmap.ic_itu)
                                    : vi.getResources().getDrawable(R.mipmap.ic_launcher) );

        String name = isItu ? data[position].getMajor() + parseMinor(data[position].getMinor() )
                            : data[position].getMajor() + data[position].getMinor();

        TextView bname = ((TextView) vi.findViewById(R.id.bc_row_beaconName));
        bname.setText(name);
        bname.setTextColor(isItu    ? vi.getResources().getColor(android.R.color.holo_orange_dark)
                                    : vi.getResources().getColor(android.R.color.holo_blue_dark)
        );

        ((TextView) vi.findViewById(R.id.bc_info_uuid)).setText( uuid );
        ((TextView) vi.findViewById(R.id.bc_major_txt)).setText(data[position].getMajor());
        ((TextView) vi.findViewById(R.id.bc_minor_txt)).setText(data[position].getMinor() );
        ((TextView) vi.findViewById(R.id.bc_info_type)).setText( data[position].getType() );
        ((TextView) vi.findViewById(R.id.bc_info_manu)).setText( data[position].getManufactorer() );
        ((TextView) vi.findViewById(R.id.bc_info_data)).setText(data[position].getData());
        ((TextView) vi.findViewById(R.id.bc_distance_text)).setText( "" + data[position].getLastKnownDistance());
        ((TextView) vi.findViewById(R.id.bc_time_text)).setText( "" + data[position].getTime() );

        return vi;
    }

    public void setData(Beacon[] data) {
        this.data = data;
    }

    private String parseMinor(String s) {
        if (s == null || s == "" || s.length() < 4) return s;
        char[] translate = s.toCharArray();
        translate[0] = minorCodes.get( s.charAt(0) ) != null ?
                        minorCodes.get( s.charAt(0)) : '-';
        return new String(translate, 0, translate.length - 1);
    }

}
