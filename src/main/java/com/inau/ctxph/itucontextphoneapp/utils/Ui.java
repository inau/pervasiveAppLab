package com.inau.ctxph.itucontextphoneapp.utils;

import android.view.View;

/**
 * Created by Ivan on 18-02-2016.
 */
public class Ui {

    public static class CompositeView {
        final View[] components;

        public CompositeView(View... views) {
            components = views;
        }

        public void highlightedComponents(boolean val) {
            for (View v : components) v.setAlpha( val ? 1 : .5f);
        }

        //By convention we link listeners to the first view in the composite - overloads for specific sub views
        public void setOnClickListener(View.OnClickListener onClick) {
            setOnClickListener(onClick, 0);
        }
        public void setOnClickListener(View.OnClickListener onClick, int item) {
            components[item].setOnClickListener(onClick);
        }
        public long getId() { return getId(0); }

        public long getId(int item) {
            if(item > components.length) return -1;
            return components[item].getId();
        }

    }

}
