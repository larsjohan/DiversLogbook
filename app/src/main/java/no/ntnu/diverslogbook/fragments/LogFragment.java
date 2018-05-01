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
import java.util.List;

import no.ntnu.diverslogbook.DisplayLogActivity;
import no.ntnu.diverslogbook.DiveLog;
import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.adapters.LogListAdapter;


public class LogFragment extends Fragment {

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


        // Set title of toolbar. (Causes all screens to have title Log..
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Log");


        ListView logList = view.findViewById(R.id.log_lv);

        /** Mockdata */
        List<DiveLog> mockData = new ArrayList<>();

        mockData.add(new DiveLog());
        mockData.add(new DiveLog());
        mockData.add(new DiveLog());

        /** **/

        logList.setAdapter(new LogListAdapter(view.getContext(), mockData));

        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open a new activity with the divelog object.
                Intent myIntent = new Intent(getActivity(), DisplayLogActivity.class);
                myIntent.putExtra("LogToDisplay", mockData.get(position));
                startActivity(myIntent);
            }
        });

        return view;
    }

}