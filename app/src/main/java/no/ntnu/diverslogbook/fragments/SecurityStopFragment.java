package no.ntnu.diverslogbook.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ntnu.diverslogbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityStopFragment extends Fragment {


    public SecurityStopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security_stop, container, false);
    }

}
