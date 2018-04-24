package no.ntnu.diverslogbook;

import android.app.Activity;
import android.content.Intent;
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
    private int rowCounter = 1;


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

        // Get and set table view.
        table = findViewById(R.id.stopTable);

        // Set onclick listener on the "add new stop" button.
        Button addButton = findViewById(R.id.addStop);
        addButton.setOnClickListener((v) -> addStop(v));

        // Set onclick listener on the "remove last stop" button.
        removeButton = findViewById(R.id.remove);
        removeButton.setEnabled(false);
        removeButton.setOnClickListener((v) -> removeStop(v));

        // Set onclick listener on the "save security stops" button.
        Button saveButton = findViewById(R.id.saveStops);
        saveButton.setOnClickListener((v) -> goBack(v));
    }


    /**
     * Creates a new security stop row.
     *
     * @param view
     */
    private void addStop(View view) {
        TableRow row = new TableRow(this);
        ++rowCounter;

        // Create text view which shows row number.
        TextView count = new TextView(this);
        count.setText(String.valueOf(rowCounter));
        count.setTextSize(18);
        count.setGravity(Gravity.CENTER);

        // Create input field for depth.
        EditText depth = new EditText(this);
        depth.setWidth(0);
        depth.setGravity(Gravity.CENTER_HORIZONTAL);
        depth.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        depth.setHint(R.string.depth);

        // Create input field for duration.
        EditText duration = new EditText(this);
        duration.setWidth(0);
        duration.setGravity(Gravity.CENTER_HORIZONTAL);
        duration.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        duration.setHint(R.string.duration);

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
        Intent returnIntent = new Intent();

        //returnIntent.putExtra("result", input);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
