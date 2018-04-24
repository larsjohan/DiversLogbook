package no.ntnu.diverslogbook.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.diverslogbook.DiveLog;
import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.SecurityStops;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.model.Location;
import no.ntnu.diverslogbook.util.Database;

import static android.app.Activity.RESULT_OK;


/**
 * Manages the planning of a dive.
 */
public class PlanFragment extends Fragment {


    /**
     * All users that uses the application.
     */
    private static List<String> USERS = new ArrayList<>();

    /**
     * All locations in the application.
     */
    private static List<String> LOCATIONS = new ArrayList<>();

    /**
     * The fragment view.
     */
    private static View view;

    /**
     * Result tag.
     */
    private static int RESULT = 0;


    /**
     * Required.
     */
    public PlanFragment() {
        // Required empty public constructor
    }


    /**
     * Creates a new view an initializes onclick listeners and other data.
     *
     * @param inflater An inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The new view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_plan, container, false);

        // Set onclick listener on the "choose security" button.
        Button chooseSecurityButton = view.findViewById(R.id.chooseSecurity);
        chooseSecurityButton.setOnClickListener((v) -> chooseSecurity(v));

        // Set onclick listener on the "create plan" button.
        Button createPlanButton = view.findViewById(R.id.createPlan);
        createPlanButton.setOnClickListener((v) -> createPlan(v));

        // Initialize the search list for users and locations.
        getUsers();
        getLocations();

        // Initialize auto complete text inputs.
        setAdapterAndListener(USERS, R.id.buddy, R.id.buddyIcon, R.drawable.ic_person_green_24dp);
        setAdapterAndListener(USERS, R.id.guard, R.id.guardIcon, R.drawable.ic_person_green_24dp);
        setAdapterAndListener(LOCATIONS, R.id.loaction, R.id.locationIcon, R.drawable.ic_location_on_green_24dp);

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Opens a modal and retrieves data about chosen security stops.
     *
     * @param view The view
     */
    public void chooseSecurity(View view) {
        Log.d("TAG", "CHOOSING SECURITY!!");
        Intent getString = new Intent(getActivity(), SecurityStops.class);
        startActivityForResult(getString, RESULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActivity();

        // Check which request we're responding to
        if (requestCode == RESULT) {
            // Make sure the request was successful

            if (resultCode == Activity.RESULT_OK) {

                //String input = data.getStringExtra("result");
                Log.d("TAG", "BACK FROM CHOOSING SECURITY STOPS!!");
            }
        }
    }


    /**
     * Creates a new plan object and saves it in the database.
     *
     * @param view The view
     */
    public void createPlan(View view) {
        Log.d("TAG", "CREATING PLAN!!");
    }


    /**
     * Get all users from the database.
     *
     * @return All users
     */
    private void getUsers() {
        List<Diver> divers = Database.getDivers();
        Log.d("TAG", "USERS IN DATABASE UNDER HERE!");

        for (Diver diver : divers) {
            USERS.add(diver.getName());
            Log.d("TAG", diver.getName());
        }
    }


    /**
     * Get all locations from the database.
     *
     * @return All locations
     */
    private void getLocations() {
        List<Location> locations = Database.getLocations();

        for (Location location : locations) {
            LOCATIONS.add(location.getName());
        }
    }


    /**
     * Set an adapter and a listener on an auto complete input field.
     *
     * @param items Items to search through
     * @param inputId Id of auto complete field
     * @param iconId Id of corresponding icon
     * @param newIcon Id of new icon to display
     */
    private void setAdapterAndListener(List<?> items, int inputId, int iconId, int newIcon) {
        ArrayAdapter<?> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);
        AutoCompleteTextView input = view.findViewById(inputId);
        input.setAdapter(adapter);

        input.setOnItemClickListener((parent, v, position, id) -> {

                    ImageView icon = view.findViewById(iconId);
                    icon.setImageDrawable(ContextCompat.getDrawable(getActivity(), newIcon));
                }
        );
    }

}