package no.ntnu.diverslogbook.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.ntnu.diverslogbook.DiveLog;
import no.ntnu.diverslogbook.Globals;
import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.SecurityStops;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.model.Location;
import no.ntnu.diverslogbook.util.Database;


/**
 * Manages the planning of a dive.
 */
public class PlanFragment extends Fragment {


    /**
     * All users that uses the application.
     */
    private List<String> USERS = new ArrayList<>();

    /**
     * All locations in the application.
     */
    private List<String> LOCATIONS = new ArrayList<>();

    /**
     * The fragment view.
     */
    private View view;

    /**
     * Result tag.
     */
    private int RESULT = 0;


    /**
     * List of security stops.
     */
    private ArrayList<DiveLog.SecurityStop> securityStops = new ArrayList<>();


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

        getActivity().setTitle(R.string.plan_title);
        initializePlanView();

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Does all of the initialization for the plan view.
     */
    private void initializePlanView() {
        // Initialize the search list for users and locations.
        getUsers();
        getLocations();

        // Set date to today's date.
        EditText date = view.findViewById(R.id.dateChoice);
        Date d = new Date();
        String today  = (String) DateFormat.format("dd/MM/yyyy ", d.getTime());
        date.setText(today);

        // Set onclick listener on the "choose security" button.
        Button chooseSecurityButton = view.findViewById(R.id.chooseSecurity);
        chooseSecurityButton.setOnClickListener((v) -> chooseSecurity(v));

        // Set onclick listener on the "create plan" button.
        Button createPlanButton = view.findViewById(R.id.createPlan);
        createPlanButton.setOnClickListener((v) -> createPlan(v));

        // Initialize auto complete text inputs.
        setAdapterAndListener(USERS, R.id.buddy, R.id.buddyIcon, R.drawable.ic_person_green_24dp);
        setAdapterAndListener(USERS, R.id.guard, R.id.guardIcon, R.drawable.ic_person_green_24dp);
        setAdapterAndListener(LOCATIONS, R.id.loaction, R.id.locationIcon, R.drawable.ic_location_on_green_24dp);

        // Set listeners on the checkboxes for 24h or more.
        CheckBox dive24h = view.findViewById(R.id.lastDiveCheckbox);
        dive24h.setOnCheckedChangeListener((buttonView, isChecked) -> onCheckBoxChange(isChecked, R.id.lastDive));

        CheckBox alcohol24h = view.findViewById(R.id.lastAlcoholCheckbox);
        alcohol24h.setOnCheckedChangeListener((buttonView, isChecked) -> onCheckBoxChange(isChecked, R.id.lastAlcohol));
    }


    /**
     * Whenever there's a change in a checkbox, the change
     * is validated. If it is checked, do not enable numeric
     * input.
     *
     * @param isChecked If the checkbox is checked or not
     * @param inputId View id of input to manage
     */
    private void onCheckBoxChange(boolean isChecked, int inputId) {
        EditText input = view.findViewById(inputId);
        input.setEnabled(!isChecked);
    }


    /**
     * Opens a modal and retrieves data about chosen security stops.
     *
     * @param view The view
     */
    public void chooseSecurity(View view) {
        Intent intent = new Intent(getActivity(), SecurityStops.class);
        intent.putExtra(Globals.SECURITYSTOPS, securityStops);
        startActivityForResult(intent, RESULT);
    }


    /**
     * Retrieve data from the SecurityStops activity.
     *
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data Data received from activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActivity();

        // Check which request we're responding to
        if (requestCode == RESULT) {
            // Make sure the request was successful

            if (resultCode == Activity.RESULT_OK) {

                //String input = data.getStringExtra("result");
                securityStops = (ArrayList< DiveLog.SecurityStop>) data.getSerializableExtra(Globals.SECURITYSTOPS);
                ImageView icon = view.findViewById(R.id.securityIcon);
                icon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_security_green_24dp));

                Button securityButton = view.findViewById(R.id.chooseSecurity);
                securityButton.setText(R.string.edit);
            }
        }
    }


    /**
     * Get all users from the database.
     */
    private void getUsers() {
        List<Diver> divers = Database.getDivers();

        if (divers.size() == 0) {
            Database.registerObserver(changedObject -> {

                if (changedObject instanceof Diver){
                    USERS.add(((Diver) changedObject).getName());
                }
            });
        }
    }


    /**
     * Get all locations from the database.
     */
    private void getLocations() {
        List<Location> locations = Database.getLocations();

        if (locations.size() == 0) {
            Database.registerObserver(changedObject -> {

                if (changedObject instanceof Location){
                    LOCATIONS.add(((Location) changedObject).getName());
                }
            });
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


    /**
     * Creates a new plan object and saves it in the database.
     *
     * @param v The view
     */
    public void createPlan(View v) {
        // Date.
        EditText dateInput = view.findViewById(R.id.dateChoice);
        String date = dateInput.getText().toString();

        // Buddy.
        AutoCompleteTextView buddyInput = view.findViewById(R.id.buddy);
        String buddy = buddyInput.getText().toString();

        // Guard.
        AutoCompleteTextView guardInput = view.findViewById(R.id.guard);
        String guard = guardInput.getText().toString();

        // Location.
        AutoCompleteTextView locationInput = view.findViewById(R.id.loaction);
        String location = locationInput.getText().toString();

        // Dive type.
        Spinner diveTypeSpinner = view.findViewById(R.id.diveType);
        String diveType = diveTypeSpinner.getSelectedItem().toString();

        // Max depth.
        EditText depthInput = view.findViewById(R.id.depth);
        String depth = depthInput.getText().toString();

        // Diving time.
        EditText divingTimeInput = view.findViewById(R.id.divingTime);
        String divingTime = divingTimeInput.getText().toString();

        // Tank size.
        EditText tankSizeInput = view.findViewById(R.id.tankSize);
        String tankSize = tankSizeInput.getText().toString();

        // Tank pressure.
        EditText tankPressureInput = view.findViewById(R.id.tankPressure);
        String tankPressure = tankPressureInput.getText().toString();

        // Dive gas.
        Spinner diveGasSpinner = view.findViewById(R.id.diveGas);
        String diveGas = diveGasSpinner.getSelectedItem().toString();

        // Weather.
        Spinner weatherSpinner = view.findViewById(R.id.weather);
        String weather = weatherSpinner.getSelectedItem().toString();

        // Temp. on surface.
        EditText tempSurfaceInput = view.findViewById(R.id.tempSurface);
        String tempSurface = tempSurfaceInput.getText().toString();

        // Temp. in water.
        EditText tempWaterInput = view.findViewById(R.id.tempWater);
        String tempWater = tempWaterInput.getText().toString();

        // Time since last dive.
        CheckBox lastDive24 = view.findViewById(R.id.lastDiveCheckbox);
        String lastDive;

        if (lastDive24.isChecked()) {
            lastDive = Globals.MORETHAN24H;
        } else {
            EditText lastDiveInput = view.findViewById(R.id.lastDive);
            lastDive = lastDiveInput.getText().toString();
        }

        // Time since last alcohol intake.
        CheckBox lastAlcohol24 = view.findViewById(R.id.lastAlcoholCheckbox);
        String lastAlcohol;

        if (lastAlcohol24.isChecked()) {
            lastAlcohol = Globals.MORETHAN24H;
        } else {
            EditText lastAlcoholInput = view.findViewById(R.id.lastAlcohol);
            lastAlcohol = lastAlcoholInput.getText().toString();
        }

        // Notes.
        EditText notesInput = view.findViewById(R.id.notes);
        String notes = notesInput.getText().toString();

        // Check data and manage it.
        if (planDataIsValid(date, buddy, guard, location, depth,
                divingTime, tankSize, tankPressure,
                tempSurface, tempWater, lastDive, lastAlcohol)) {

            // Update database and give success message.
        } else {
            // Error message.
        }
    }


    private boolean planDataIsValid(String date, String buddy, String guard, String location, String depth,
                                    String divingTime, String tankSize, String tankPressure, String tempSurface,
                                    String tempWater, String lastDive, String lastAlcohol) {

        boolean allDataOk = true;

        Pattern pattern = Pattern.compile("\\d{1,2}(/)\\d{1,2}(/)\\d{4}");
        Matcher matcher = pattern.matcher(date);
        boolean dateIsValid = matcher.find();

        boolean buddyOk = USERS.contains(buddy);
        boolean guardOk = USERS.contains(guard);
        boolean buddyGuardTheSame = (buddy.equals(guard));
        boolean locationOk = LOCATIONS.contains(location);

        pattern = Pattern.compile("\\d*");
        matcher = pattern.matcher(depth);
        boolean depthOk = matcher.find();

        matcher = pattern.matcher(divingTime);
        boolean divingTimeOk = matcher.find();

        matcher = pattern.matcher(tankSize);
        boolean tankSizeOk = matcher.find();

        matcher = pattern.matcher(tankPressure);


        return true;
    }

}