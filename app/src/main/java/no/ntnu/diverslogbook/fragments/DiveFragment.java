package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import no.ntnu.diverslogbook.R;


public class DiveFragment extends Fragment {

    private ListView diverslist;

    public DiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dive, container, false);

        this.diverslist = view.findViewById(R.id.lv_diverlist);

        return view;
    }

}