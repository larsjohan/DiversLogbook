package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.model.Diver;


public class ProfileFragment extends Fragment {

    private Diver diver;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }
}