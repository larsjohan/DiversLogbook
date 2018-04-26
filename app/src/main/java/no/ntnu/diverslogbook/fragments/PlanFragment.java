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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.ntnu.diverslogbook.model.DiveLog;
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
     * Input data.
     */
    private EditText dateInput;
    private AutoCompleteTextView buddyInput;
    private String buddy;
    private AutoCompleteTextView guardInput;
    private AutoCompleteTextView locationInput;
    private Spinner diveTypeSpinner;
    private EditText depthInput;
    private EditText divingTimeInput;
    private EditText tankSizeInput;
    private EditText tankPressureInput;
    private Spinner diveGasSpinner;
    private Spinner weatherSpinner;
    private EditText tempSurfaceInput;
    private EditText tempWaterInput;
    private CheckBox lastDive24;
    private EditText lastDiveInput;
    private CheckBox lastAlcohol24;
    private EditText lastAlcoholInput;
    private EditText notesInput;

    /**
     * Buttons in this tab.
     */
    private Button chooseSecurityButton;
    private Button createPlanButton;

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

        // Initialize all input variables.
        initializeInputsAndButtons();

        // Set date to today's date.
        Date d = new Date();
        String today  = (String) DateFormat.format("dd/MM/yyyy ", d.getTime());
        dateInput.setText(today);

        // Set onclick listener on the "choose security" button and the "create plan" button.
        chooseSecurityButton.setOnClickListener((v) -> chooseSecurity(v));
        createPlanButton.setOnClickListener((v) -> createPlan(v));

        // Initialize auto complete text inputs.
        setAdapterAndListener(USERS, buddyInput, R.id.buddyIcon, R.drawable.ic_person_green_24dp);
        setAdapterAndListener(USERS, guardInput, R.id.guardIcon, R.drawable.ic_person_green_24dp);
        setAdapterAndListener(LOCATIONS, locationInput, R.id.locationIcon, R.drawable.ic_location_on_green_24dp);

        // Set listeners on the checkboxes for 24h or more.
        //lastDive24.setOnCheckedChangeListener((buttonView, isChecked) -> onCheckBoxChange(isChecked, R.id.lastDive));
        lastDive24.setOnCheckedChangeListener(((buttonView, isChecked) -> lastDiveInput.setEnabled(!isChecked)));
        lastAlcohol24.setOnCheckedChangeListener((buttonView, isChecked) -> lastAlcoholInput.setEnabled(!isChecked));
    }


    /**
     * Initialize all input fields and buttons.
     */
    private void initializeInputsAndButtons() {
        dateInput = view.findViewById(R.id.dateChoice);
        buddyInput = view.findViewById(R.id.buddy);
        guardInput = view.findViewById(R.id.guard);
        locationInput = view.findViewById(R.id.loaction);
        diveTypeSpinner = view.findViewById(R.id.diveType);
        depthInput = view.findViewById(R.id.depth);
        divingTimeInput = view.findViewById(R.id.divingTime);
        tankSizeInput = view.findViewById(R.id.tankSize);
        tankPressureInput = view.findViewById(R.id.tankPressure);
        diveGasSpinner = view.findViewById(R.id.diveGas);
        weatherSpinner = view.findViewById(R.id.weather);
        tempSurfaceInput = view.findViewById(R.id.tempSurface);
        tempWaterInput = view.findViewById(R.id.tempWater);
        lastDive24 = view.findViewById(R.id.lastDiveCheckbox);
        lastDiveInput = view.findViewById(R.id.lastDive);
        lastAlcohol24 = view.findViewById(R.id.lastAlcoholCheckbox);
        lastAlcoholInput = view.findViewById(R.id.lastAlcohol);
        notesInput = view.findViewById(R.id.notes);

        chooseSecurityButton = view.findViewById(R.id.chooseSecurity);
        createPlanButton = view.findViewById(R.id.createPlan);
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

                securityStops = (ArrayList<DiveLog.SecurityStop>) data.getSerializableExtra(Globals.SECURITYSTOPS);
                Log.d("TAG", "In fragment: " + securityStops.size() + " stops.");
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
     * @param input The auto complete field
     * @param iconId Id of corresponding icon
     * @param newIcon Id of new icon to display
     */
    private void setAdapterAndListener(List<?> items, AutoCompleteTextView input, int iconId, int newIcon) {
        ArrayAdapter<?> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, items);
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
        String date = dateInput.getText().toString();
        String buddy = buddyInput.getText().toString();
        String guard = guardInput.getText().toString();
        String location = locationInput.getText().toString();
        String diveType = diveTypeSpinner.getSelectedItem().toString();
        String depth = depthInput.getText().toString();
        String divingTime = divingTimeInput.getText().toString();
        String tankSize = tankSizeInput.getText().toString();
        String tankPressure = tankPressureInput.getText().toString();
        String diveGas = diveGasSpinner.getSelectedItem().toString();
        String weather = weatherSpinner.getSelectedItem().toString();
        String tempSurface = tempSurfaceInput.getText().toString();
        String tempWater = tempWaterInput.getText().toString();
        String lastDive = lastDive24.isChecked() ? Globals.MORETHAN24H : lastDiveInput.getText().toString();
        String lastAlcohol = lastAlcohol24.isChecked() ? Globals.MORETHAN24H : lastAlcoholInput.getText().toString();
        String notes = notesInput.getText().toString();

        // Check data and manage it.
        if (planDataIsValid(date, buddy, guard, location, depth,
                divingTime, tankSize, tankPressure,
                tempSurface, tempWater, lastDive, lastAlcohol)) {

            createNewDiveLog(date, buddy, guard, location, diveType, depth,
                    divingTime, tankSize, tankPressure, diveGas, weather,
                    tempSurface, tempWater, lastDive, lastAlcohol, notes);
        } else {
            Toast.makeText(getActivity(), R.string.plan_error, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Check all manual input and see if they are all valid or not,
     *
     * @param date Chosen date
     * @param buddy Chosen diving buddy
     * @param guard Chosen surface guard
     * @param location Chosen location
     * @param depth Planned depth
     * @param divingTime Planned diving time
     * @param tankSize Size of the tank
     * @param tankPressure Pressure in tank
     * @param tempSurface Temperature on surface
     * @param tempWater Temperature in water
     * @param lastDive Time passed since last dive
     * @param lastAlcohol Time passed since last alcohol intake
     *
     * @return true if all data is valid
     */
    private boolean planDataIsValid(String date, String buddy, String guard, String location, String depth,
                                    String divingTime, String tankSize, String tankPressure, String tempSurface,
                                    String tempWater, String lastDive, String lastAlcohol) {

        Pattern pattern = Pattern.compile("\\d{1,2}(/)\\d{1,2}(/)\\d{4}");
        Matcher matcher = pattern.matcher(date);
        boolean dateOk = matcher.find();

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
        boolean tankPressureOk = matcher.find();

        matcher = pattern.matcher(tempSurface);
        boolean tempSurfaceOk = matcher.find();

        matcher = pattern.matcher(tempWater);
        boolean tempWaterOk = matcher.find();

        pattern = Pattern.compile("\\d*(:)\\d*");
        boolean lastDiveOk;

        if (lastDive.equals(Globals.MORETHAN24H)) {
            lastDiveOk = true;
        } else {
            matcher = pattern.matcher(lastDive);
            lastDiveOk = matcher.find();
        }

        boolean lastAlcoholOk;

        if (lastAlcohol.equals(Globals.MORETHAN24H)) {
            lastAlcoholOk = true;
        } else {
            matcher = pattern.matcher(lastAlcohol);
            lastAlcoholOk = matcher.find();

            if (lastAlcoholOk) {
                String[] times = lastAlcohol.split(":");
                int hours = Integer.valueOf(times[0]);
                lastAlcoholOk = (hours > Globals.MAX_HOURS_SINCE_ALCOHOL);
            }
        }

        // Check if all data is ok or not.
        return (dateOk && buddyOk && guardOk && !buddyGuardTheSame && locationOk
                && depthOk && divingTimeOk && tankSizeOk && tankPressureOk
                && tempSurfaceOk && tempWaterOk && lastDiveOk && lastAlcoholOk);
    }


    /**
     * Save the diving log to the database and
     * update view.
     */
    private void createNewDiveLog(String date, String buddy, String guard, String location, String diveType,
                                  String depth, String  divingTime, String tankSize, String tankPressure,
                                  String diveGas, String weather, String tempSurface, String tempWater,
                                  String lastDive, String lastAlcohol, String notes) {

        // Get last dive and alcohol inputs in correct format.
        DiveLog.HoursAndMinutes lastDiveObject = getNewHoursAndMinutes(lastDive);
        DiveLog.HoursAndMinutes lastAlcoholObject = getNewHoursAndMinutes(lastAlcohol);

        if (lastDiveObject != null) {
            Log.d("TAG", lastDiveObject.getHours() + ":" + lastDiveObject.getMinutes());
        }

        // Create a new dive log.
        DiveLog newLog = new DiveLog(date, buddy, guard, location, diveType, Integer.valueOf(depth),
                                    lastDiveObject, lastAlcoholObject, securityStops,
                                    Integer.valueOf(divingTime), Integer.valueOf(tankSize), Integer.valueOf(tankPressure),
                                    diveGas, weather, Float.parseFloat(tempSurface), Float.parseFloat(tempWater), notes);

        // Try to retrieve logged in user.
        Diver diver = Database.getLoggedInDiver();

        // Update the user with the new dive log.
        if (diver == null) {
            Database.registerObserver(changedObject -> {
                if (changedObject instanceof Diver && ((Diver) changedObject).getId().equals(Database.getLoggedInDiverGuid())){

                    updateUser((Diver) changedObject, newLog);
                }
            });
        } else {
            updateUser(diver, newLog);
        }
    }


    /**
     * Create a new HoursAndMinutes object.
     *
     * @param input The time in string
     * @return New HoursAndMinutes object
     */
    private DiveLog.HoursAndMinutes getNewHoursAndMinutes(String input) {
        DiveLog.HoursAndMinutes object;

        if (!input.equals(Globals.MORETHAN24H)) {
            String[] lastDiveTimes = input.split(":");
            int hours = Integer.valueOf(lastDiveTimes[0]);
            int minutes = Integer.valueOf(lastDiveTimes[1]);
            object = new DiveLog.HoursAndMinutes(hours, minutes);
        } else {
            object = null;
        }

        return object;
    }


    /**
     * Update the user in Firebase and make GUI ready for finishing the plan.
     *
     * @param diver The diver to update
     * @param newLog The new dive log
     */
    private void updateUser(Diver diver, DiveLog newLog) {
        diver.addDiveLog(newLog);
        Database.updateDiver(diver);

        disableAllInputs();
        createPlanButton.setText(R.string.finish_plan);

        Toast.makeText(getActivity(), R.string.plan_success, Toast.LENGTH_LONG).show();
    }


    /**
     * Make all "planning" inputs disabled.
     */
    private void disableAllInputs() {
        dateInput.setEnabled(false);
        buddyInput.setEnabled(false);
        guardInput.setEnabled(false);
        locationInput.setEnabled(false);
        diveTypeSpinner.setEnabled(false);
        depthInput.setEnabled(false);
        divingTimeInput.setEnabled(false);
        tankSizeInput.setEnabled(false);
        tankPressureInput.setEnabled(false);
        diveGasSpinner.setEnabled(false);
        weatherSpinner.setEnabled(false);
        tempSurfaceInput.setEnabled(false);
        tempWaterInput.setEnabled(false);
        lastDive24.setEnabled(false);
        lastDiveInput.setEnabled(false);
        lastAlcohol24.setEnabled(false);
        lastAlcoholInput.setEnabled(false);
        notesInput.setEnabled(false);
        chooseSecurityButton.setEnabled(false);
    }


    /**
     * Clear all input fields.
     */
    private void clearAllInputs() {
        dateInput.setText("");
        dateInput.setEnabled(true);

        buddyInput.setText("");
        buddyInput.setEnabled(true);

        guardInput.setText("");
        guardInput.setEnabled(true);

        locationInput.setText("");
        locationInput.setEnabled(true);

        diveTypeSpinner.setSelection(0);
        diveTypeSpinner.setEnabled(true);

        depthInput.setText("");
        depthInput.setEnabled(true);

        divingTimeInput.setText("");
        divingTimeInput.setEnabled(true);

        tankSizeInput.setText("");
        tankSizeInput.setEnabled(true);

        tankPressureInput.setText("");
        tankPressureInput.setEnabled(true);

        diveGasSpinner.setSelection(0);
        diveGasSpinner.setEnabled(true);

        weatherSpinner.setSelection(0);
        weatherSpinner.setEnabled(true);

        tempSurfaceInput.setText("");
        tempSurfaceInput.setEnabled(true);

        tempWaterInput.setText("");
        tempWaterInput.setEnabled(true);

        lastDive24.setChecked(false);
        lastDive24.setEnabled(true);

        lastDiveInput.setText("");
        lastDiveInput.setEnabled(true);

        lastDive24.setChecked(false);
        lastAlcohol24.setEnabled(true);

        lastAlcoholInput.setText("");
        lastAlcoholInput.setEnabled(true);

        notesInput.setText("");
        notesInput.setEnabled(true);

        chooseSecurityButton.setEnabled(true);
        chooseSecurityButton.setText(R.string.choose);

        createPlanButton.setText(R.string.create_plan);

        // TODO: Hide the newest input fields
    }

}