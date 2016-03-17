package com.inau.ctxph.itucontextphoneapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inau.ctxph.itucontextphoneapp.R;
import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link ContextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContextFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_context, container, false);

        return view;
    }

}
