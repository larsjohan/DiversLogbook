package no.ntnu.diverslogbook;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.net.Inet4Address;
import java.util.ArrayList;


/**
 * Manages the adding of security stops.
 */
public class SecurityStops extends AppCompatActivity {


    /**
     * The table layout.
     */
    private TableLayout table;

    /**
     * Keeps track of the amount of rows.
     * Also used for row indexes.
     */
    private int rowCounter = 0;


    /**
     * The button that removes a security stop row.
     */
    private Button removeButton;


    /**
     * List of all security stops.
     */
    private ArrayList<DiveLog.SecurityStop> securityStops = new ArrayList<>();


    /**
     * Creates the layout for adding security stops.
     *
     * @param savedInstanceState The saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_stops);
        setTitle(R.string.plan_security_stops_title);

        initialize();
    }


    /**
     * Initialize all data and listeners.
     */
    private void initialize() {
        // Get and set table view.
        table = findViewById(R.id.stopTable);

        // Set onclick listener on the "add new stop" button.
        Button addButton = findViewById(R.id.addStop);
        addButton.setOnClickListener((v) -> addSpecificStop(0,0));

        // Set onclick listener on the "remove last stop" button.
        removeButton = findViewById(R.id.remove);
        removeButton.setEnabled(false);
        removeButton.setOnClickListener((v) -> removeStop(v));

        // Set onclick listener on the "save security stops" button.
        Button saveButton = findViewById(R.id.saveStops);
        saveButton.setOnClickListener((v) -> goBack(v));

        // Set security stops.
        Intent intent = getIntent();
        ArrayList<DiveLog.SecurityStop> stops = (ArrayList<DiveLog.SecurityStop>) intent.getSerializableExtra(Globals.SECURITYSTOPS);
        updateUIWithStops(stops);
    }


    /**
     * Update UI with security stops sent from planning tab.
     */
    private void updateUIWithStops(ArrayList<DiveLog.SecurityStop> stops) {
        if (stops.size() > 0) {
            securityStops.clear();

            for (DiveLog.SecurityStop stop : stops) {
                Log.d("TAG", "DEPTH: " + stop.getDepth() + ", DURATION: " + stop.getDuration());
                addSpecificStop(stop.getDepth(), stop.getDuration());
            }

        } else {
            Log.d("TAG", "THERE ARE NO SECURITY STOPS!");
            addSpecificStop(5,3);
        }
    }


    /**
     * Creates a new specific security stop.
     *
     * @param depthValue Depth in meters
     * @param durationValue Duration in minutes
     */
    private void addSpecificStop(int depthValue, int durationValue) {
        TableRow row = new TableRow(this);
        ++rowCounter;

        // Create text view which shows row number.
        TextView count = new TextView(this);
        count.setText(String.valueOf(rowCounter));
        count.setTextSize(18);
        count.setGravity(Gravity.CENTER);
        count.setWidth(20);

        // Create input field for depth.
        EditText depth = new EditText(this);
        depth.setWidth(150);
        depth.setGravity(Gravity.CENTER);
        depth.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        depth.setHint(R.string.depth);

        if (depthValue > 0) {
            depth.setText(String.valueOf(depthValue));
        }


        // Create input field for duration.
        EditText duration = new EditText(this);
        duration.setWidth(150);
        duration.setGravity(Gravity.CENTER);
        duration.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        duration.setHint(R.string.duration);

        if (durationValue > 0) {
            duration.setText(String.valueOf(durationValue));
        }

        // Add input fields to new row.
        row.addView(count, new TableRow.LayoutParams(0));
        row.addView(depth, new TableRow.LayoutParams(1));
        row.addView(duration, new TableRow.LayoutParams(2));

        // Add row to table and enable the remove button.
        table.addView(row);
        removeButton.setEnabled(true);
    }


    /**
     * Removes the last security stop row.
     * @param view
     */
    private void removeStop(View view) {
        boolean enableRemoveButton = false;

        if (rowCounter > 1) {
            table.removeViewAt(rowCounter);
            --rowCounter;

            if (rowCounter > 1) {
                enableRemoveButton = true;
            }
        }

        removeButton.setEnabled(enableRemoveButton);
    }


    /**
     * Returns the data from this activity to the previous one.
     *
     * @param view The view
     */
    private void goBack(View view){
        saveSecurityStops();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Globals.SECURITYSTOPS, securityStops);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }


    /**
     * Save all chosen security stops.
     */
    private void saveSecurityStops() {
        int size = table.getChildCount();

        for (int i = 1; i < size; i++) {
            TableRow row = (TableRow) table.getChildAt(i);

            // Get depth.
            EditText depthInput = (EditText) row.getChildAt(1);
            String depthString = depthInput.getText().toString();
            int depth = 0;

            if (!depthString.equals("")) {
                depth = Integer.valueOf(depthString);
            }


            // Get duration.
            EditText durationInput = (EditText) row.getChildAt(2);
            String durationString = durationInput.getText().toString();
            int duration = 0;

            if (!durationString.equals("")) {
                duration = Integer.valueOf(durationString);
            }

            // Add the security stop if it has valid input.
            if (depth > 0 && duration > 0) {
                DiveLog.SecurityStop stop = new DiveLog.SecurityStop(depth, duration);
                securityStops.add(stop);
            }


        }
    }
}
