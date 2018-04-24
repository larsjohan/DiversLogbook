package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.adapters.DiverListAdapter;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.util.Database;


public class DiveFragment extends Fragment {

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

        ListView diversList = view.findViewById(R.id.lv_diverlist);

        diversList.setAdapter(new DiverListAdapter(view.getContext(), Database.getDivers()));
        return view;
    }

}