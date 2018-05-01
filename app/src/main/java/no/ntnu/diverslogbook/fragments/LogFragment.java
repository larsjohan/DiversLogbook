package no.ntnu.diverslogbook.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import no.ntnu.diverslogbook.DisplayLogActivity;
import no.ntnu.diverslogbook.model.DiveLog;
import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.adapters.LogListAdapter;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.util.Database;

/**
 * This is the log view, displays a list of all the logs for logged in user.
 */
public class LogFragment extends Fragment {

    //private Diver diver;
    private ArrayList<DiveLog> diveLogs = new ArrayList<>();

    private LogListAdapter adapter;

    public LogFragment() {
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
        View view = inflater.inflate(R.layout.fragment_log, container, false);


        // Set title of toolbar. (Causes all screens to have title Log..)
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Log");


        ListView logList = view.findViewById(R.id.log_lv);

        adapter = new LogListAdapter(view.getContext(), diveLogs);
        logList.setAdapter(adapter);


        getLogs(view);

        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open a new activity with the divelog object.
                Intent myIntent = new Intent(getActivity(), DisplayLogActivity.class);
                myIntent.putExtra("LogToDisplay", diveLogs.get(position));
                startActivity(myIntent);
            }
        });

        return view;
    }


    /**
     * Get all logs for logged in user from the database.
     */
    private void getLogs(View view) {
        // Check if there are any dive logs stored.
        ArrayList<DiveLog> tmp = Database.getDiver(Database.getLoggedInDiver().getId()).getDiveLogs();

        if (tmp.isEmpty()) {
            Database.registerObserver(changedObject -> {

                // Finding the diver object that is equal to the logged in diver object.
                if (changedObject instanceof Diver && ((Diver) changedObject).getId().equals(Database.getLoggedInDiver().getId())){
                    Diver diver = (Diver) changedObject;
                    ArrayList<DiveLog> logs = diver.getDiveLogs();


                    // Update the list of logs.
                    diveLogs.clear();
                    diveLogs.addAll(logs);
                    adapter.notifyDataSetChanged();

                    return true;
                }

                return false;
            });
        } else {
            // Update the list of logs.
            diveLogs.clear();
            diveLogs.addAll(tmp);
            adapter.notifyDataSetChanged();
        }


    }

}