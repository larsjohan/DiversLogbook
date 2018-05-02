package no.ntnu.diverslogbook.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import no.ntnu.diverslogbook.activities.DisplayLogActivity;
import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.adapters.LogListAdapter;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.utils.Database;

/**
 * This is the log view, displays a list of all the logs for logged in user.
 */
public class LogFragment extends Fragment {

    /**
     * This should contain all the dive logs for the logged in user.
     */
    private ArrayList<DiveLog> diveLogs = new ArrayList<>();

    /**
     * This should be a reference to the adapter used on the ArrayList of DiveLog's.
     */
    private LogListAdapter adapter;

    /**
     * Required empty public constructor.
     */
    public LogFragment() {
        // Required empty public constructor
    }


    /**
     * This is the onCreate method called when the parent is launched.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * This is the onCreateView method called when the Fragment is launched/displayed/seen.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);


        // Set title of toolbar. (Causes all screens to have title Log..)
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Log");

        // Get a hold of the Listview.
        ListView logList = view.findViewById(R.id.log_lv);

        // Create an instance of the adapter.
        adapter = new LogListAdapter(view.getContext(), diveLogs);
        // Set the adapter to the ListView.
        logList.setAdapter(adapter);

        // Retrieve logs from the Database.
        getLogs(view);

        // Set an onClickListener on the ListView.
        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Inner function that handles on click event.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open a new activity with the divelog object.
                Intent myIntent = new Intent(getActivity(), DisplayLogActivity.class);
                // Put extra intent.
                myIntent.putExtra("LogToDisplay", diveLogs.get(position));
                // Start new activity with given intent.
                startActivity(myIntent);
            }
        });

        // Return the view.
        return view;
    }


    /**
     * Get all logs for logged in user from the database.
     */
    private void getLogs(View view) {
        // Check if there are any dive logs stored.
        ArrayList<DiveLog> tmp = Database.getDiver(Database.getLoggedInDiver().getId()).getDiveLogs();

        // If there was any logs.
        if (!tmp.isEmpty()) {

            // Update the list of logs.
            diveLogs.clear();
            diveLogs.addAll(tmp);
            adapter.notifyDataSetChanged();

        } else {
            // Get data from the Database by using the observer.
            Database.registerObserver(changedObject -> {

                // Finding the diver object that is equal to the logged in diver object.
                if (changedObject instanceof Diver && ((Diver) changedObject).getId().equals(Database.getLoggedInDiver().getId())){
                    // Stored the received Diver object.
                    Diver diver = (Diver) changedObject;
                    // Get the logs of this received diver.
                    ArrayList<DiveLog> logs = diver.getDiveLogs();


                    // Update the list of logs.
                    diveLogs.clear();
                    diveLogs.addAll(logs);
                    adapter.notifyDataSetChanged();

                    return true;
                }

                return false;
            });
        }


    }

}