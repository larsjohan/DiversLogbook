package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.adapters.DiverListAdapter;
import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.utils.Database;

/**
 * Defines the GUI and other logic that should happen during a dive
 *
 * @author Lars Johan
 */
public class DiveFragment extends Fragment {

    private DiverListAdapter adapter;

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

        List<Diver> divers = getGuardedDivers();

        if(divers.isEmpty()) {
            Database.registerObserver((newDiver) ->{
                if (newDiver instanceof Diver){
                    List<Diver> updatedDiverlist = getGuardedDivers();
                    adapter.clear();
                    adapter.addAll(updatedDiverlist);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            });
        }

        this.adapter = new DiverListAdapter(view.getContext(), divers);

        Log.d("DiverApp", Arrays.toString(divers.toArray()));


        ListView diversList = view.findViewById(R.id.lv_diverlist);

        diversList.setAdapter(this.adapter);
        return view;
    }


    /**
     * Get a list of divers that has selected the gurrent user as a dive-guard
     * TODO: Make sure that the divers are correctly selected
     * @return A list of divers
     */
    private List<Diver> getGuardedDivers(){
        List<Diver> guardedDivers = new ArrayList<>();

        String me = Database.getLoggedInDiver().getName();

        for(Diver diver : Database.getDivers()){
            for(DiveLog log : diver.getDiveLogs()){
                if(log.getSurfaceGuard().equals(me) &&      // Check if dive has ended:
                        log.getEndTankPressure() == 0 &&    // These values are set when the dive if finished
                        log.getActualDepth() == 0){
                    guardedDivers.add(diver);
                }
            }
        }

        return guardedDivers;
    }

}