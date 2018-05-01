package no.ntnu.diverslogbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.ntnu.diverslogbook.model.DiveLog;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.util.Database;


/**
 * Manages the last attributes that should be added to the unfinished dive plan/log.
 * At the moment the user has to finish the plan before (s)he can do anything else.
 */
public class FinishPlan extends AppCompatActivity {

    /**
     * The dive log to finish.
     */
    private DiveLog diveLog;

    /**
     * The logged in diver.
     */
    private Diver diver;

    /**
     * Input data.
     */
    private Spinner current;
    private EditText actualDepth;
    private EditText endTankPressure;
    private EditText notesAfter;


    /**
     * Retrieves the dive log object and sets the layout view.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_plan);
        setTitle(R.string.finish_plan);

        // Get log object.
        Intent intent = getIntent();
        diveLog = (DiveLog) intent.getSerializableExtra(Globals.FINISH_PLAN_LOG);
        diver = (Diver) intent.getSerializableExtra(Globals.FINISH_PLAN_DIVER);

        // Initialize date.
        TextView date = findViewById(R.id.date);
        date.setText(diveLog.getDate() + ":");

        // Initialize location.
        TextView location = findViewById(R.id.location);
        String locationName = diveLog.getLocation().split(",")[2];
        location.setText(locationName);

        // Initialize inputs.
        current = findViewById(R.id.current);
        actualDepth = findViewById(R.id.actualDepth);
        endTankPressure = findViewById(R.id.endTankPressure);
        notesAfter = findViewById(R.id.notesAfter);
    }


    /**
     * Checks if all the data is okay before the database is updated.
     *
     * @param view The view
     */
    public void finishPlan(View view) {
        if (allInputDataIsValid()) {
            Log.d("TAG", "Data is valid! Add to database.");
            updatePlanInDatabase();
        } else {
            Log.d("TAG", "Data is not valid..");
            Toast.makeText(this, R.string.plan_error, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Check if all the input values are okay.
     *
     * @return true if all inputs are valid
     */
    private boolean allInputDataIsValid() {
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher;

        boolean actualDepthOk;
        String depthValue = actualDepth.getText().toString();

        // There is no input in depth value.
        if (depthValue.equals("")) {
            actualDepthOk = false;
        } else {
            matcher = pattern.matcher(depthValue);
            actualDepthOk = matcher.find();
        }

        boolean endTankPressureOk;
        String pressureValue = endTankPressure.getText().toString();

        // There is no input in end tank pressure value.
        if (pressureValue.equals("")) {
            endTankPressureOk = false;
        } else {
            matcher = pattern.matcher(pressureValue);
            endTankPressureOk = matcher.find();
        }

        return (actualDepthOk && endTankPressureOk);
    }


    /**
     * Update the plan in the database.
     */
    private void updatePlanInDatabase() {
        Log.d("TAG", "TRYING TO UPDATE DATABASE..");

        ArrayList<DiveLog> logs = diver.getDiveLogs();

        // New values.
        String currentValue = current.getSelectedItem().toString();
        int actualDepthValue = Integer.valueOf(actualDepth.getText().toString());
        int tankPressureValue = Integer.valueOf(endTankPressure.getText().toString());
        String notes = notesAfter.getText().toString();

        // Update the dive logs.
        logs = removeLog(logs, diveLog);
        diveLog.setCurrent(currentValue);
        diveLog.setActualDepth(actualDepthValue);
        diveLog.setEndTankPressure(tankPressureValue);
        diveLog.setNotesAfter(notes);
        logs.add(diveLog);
        diver.setDiveLogs(logs);

        // Update database.
        Database.updateDiver(diver);
        Toast.makeText(this, R.string.plan_finish_success, Toast.LENGTH_LONG).show();

        // Navigate to tabs again.
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Globals.FINISH_PLAN_DIVER, diver);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }


    /**
     * Finds the correct log object in the list and removes it.
     *
     * @param logs List to search through
     * @param oldLog Log to search for
     * @return Updates list of logs
     */
    private ArrayList<DiveLog> removeLog(ArrayList<DiveLog> logs, DiveLog oldLog) {

        for (DiveLog log : logs) {
            if (log.getDiveCount() == oldLog.getDiveCount()) {
                logs.remove(log);
                break;
            }
        }

        return logs;
    }
}
