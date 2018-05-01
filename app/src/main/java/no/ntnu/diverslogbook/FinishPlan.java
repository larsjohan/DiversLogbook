package no.ntnu.diverslogbook;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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
        Matcher matcher = pattern.matcher(actualDepth.getText().toString());
        boolean currentOk = matcher.find();

        matcher = pattern.matcher(endTankPressure.getText().toString());
        boolean endTankPressureOk = matcher.find();

        return (currentOk && endTankPressureOk);
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
        logs.remove(diveLog);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        /*
        Database.registerObserver(changedObject -> {

            // When the logged in user is found, get the logs,
            // update the correct one and update database.
            if (changedObject instanceof Diver && ((Diver) changedObject).getId().equals(Database.getLoggedInDiver().getId())) {
                Diver diver = (Diver) changedObject;
                ArrayList<DiveLog> logs = diver.getDiveLogs();

                // New values.
                String currentValue = current.getSelectedItem().toString();
                int actualDepthValue = Integer.valueOf(actualDepth.getText().toString());
                int tankPressureValue = Integer.valueOf(endTankPressure.getText().toString());
                String notes = notesAfter.getText().toString();


                // Update the dive logs.
                logs.remove(diveLog);
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
        */
    }
}
