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
import java.util.Calendar;
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

        Log.d("DiverApp", Arrays.toString(divers.toArray()));


        ListView diversList = view.findViewById(R.id.lv_diverlist);

        diversList.setAdapter(new DiverListAdapter(view.getContext(), divers));
        return view;
    }


    /**
     * Get a list of divers that has selected the gurrent user as a dive-guard
     * @return A list of divers
     */
    private List<Diver> getGuardedDivers(){
        List<Diver> guardedDivers = new ArrayList<>();

        String me = Database.getLoggedInDiver().getName();

        for(Diver diver : Database.getDivers()){
            for(DiveLog log : diver.getDiveLogs()){
                if(log.getSurfaceGuard().equals(me) && isToday(log.getDate())){
                    guardedDivers.add(diver);
                }
            }
        }

        return guardedDivers;
    }

    private boolean isToday(String date){
        Calendar now = Calendar.getInstance();

        date = date.trim();
        String[] dateParts = date.split("/");

        Calendar now2 = Calendar.getInstance();
        now2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
        now2.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
        now2.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));

        Log.d("DiverApp", "Today: " + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH));
        Log.d("DiverApp", "Some:" + now2.get(Calendar.YEAR) + "-" + now2.get(Calendar.MONTH) + "-" + now2.get(Calendar.DAY_OF_MONTH));

        return now.get(Calendar.YEAR) == now2.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == now2.get(Calendar.MONTH) &&
                now.get(Calendar.DAY_OF_MONTH) == now2.get(Calendar.DAY_OF_MONTH);
    }

}