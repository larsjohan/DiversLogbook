package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.adapters.DiverListAdapter;
import no.ntnu.diverslogbook.utils.Database;

/**
 * Defines the GUI and other logic that should happen during a dive
 *
 * @author Lars Johan
 */
public class DiveFragment extends Fragment {

    /**
     * Constructor
     */
    public DiveFragment() {
        // Required empty public constructor
    }


    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * {@inheritDoc}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dive, container, false);

        ListView diversList = view.findViewById(R.id.lv_diverlist);

        diversList.setAdapter(new DiverListAdapter(view.getContext(), Database.getDivers()));
        return view;
    }

}