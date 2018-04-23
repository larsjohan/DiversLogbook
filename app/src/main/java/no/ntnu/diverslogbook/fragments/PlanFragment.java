package no.ntnu.diverslogbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.diverslogbook.R;


/**
 * Manages the planning of a dive.
 */
public class PlanFragment extends Fragment {


    /**
     * All users that uses the application.
     */
    private static String[] USERS = new String[]{};


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

        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        // Set onclick listener on the "choose security" button.
        Button chooseSecurityButton = view.findViewById(R.id.chooseSecurity);
        chooseSecurityButton.setOnClickListener((v) -> chooseSecurity(v));

        // Set onclick listener on the "create plan" button.
        Button createPlanButton = view.findViewById(R.id.createPlan);
        createPlanButton.setOnClickListener((v) -> createPlan(v));

        // Initialize the search list for users.
        USERS = this.getUsers();

        // Initialize auto complete text inputs.
        initChooseBuddy();
        initChooseGuard();
        initChooseLocation();

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
    private String[] getUsers() {
        String[] users = {"Lise", "Camilla", "Bjørn", "Siri", "Espen", "Knut"};
        return users;
    }


    /**
     * Initialize the auto complete list for choosing a buddy.
     */
    private void initChooseBuddy() {

    }


    /**
     * Initialize the auto complete list for choosing a guard.
     */
    private void initChooseGuard() {

    }


    /**
     * Initialize the auto complete list for choosing a location.
     */
    private void initChooseLocation() {

    }

}