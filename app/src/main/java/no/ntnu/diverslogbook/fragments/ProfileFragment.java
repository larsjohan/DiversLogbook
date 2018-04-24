package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.util.Database;


public class ProfileFragment extends Fragment {

    private Diver diver;

    public ProfileFragment() {
        this.diver = Database.getLoggedInDiver();
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
}