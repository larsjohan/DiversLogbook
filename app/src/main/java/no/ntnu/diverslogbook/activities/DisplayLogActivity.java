package no.ntnu.diverslogbook.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.utils.Database;
import no.ntnu.diverslogbook.utils.DiveTable;


/**
 * This class is used to display all content of a finished plan/log.
 */
public class DisplayLogActivity extends AppCompatActivity {

    /**
     * Tag used in log statements.
     */
    private static final String TAG = "DisplayLogActivity";

    /**
     * Reference to the current diveLog. Log to be displayed.
     */
    private DiveLog diveLog;

    /**
     * Required empty constructor.
     */
    public  DisplayLogActivity() {
        // Needed empty constructor.
    }


    /**
     * This is the onCreate method called when the Activity is launched/displayed/seen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylog);

        // Get intent from previous screen. (Getting the log).
        Intent intent = getIntent();
        diveLog = (DiveLog) intent.getSerializableExtra("LogToDisplay");


        // Set toolbar text. (Dive number + Location)
        String diveNr = (Integer.toString(diveLog.getDiveCount()));
        try {
            getSupportActionBar().setTitle(diveNr + ": " +  diveLog.getLocation());
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException at setTitle." + e.getMessage());
        }

        // Calls an init method that will set the text of all the TextViews in the View.
        init();
    }


    /**
     * This method initializes everything in the View. All the data is set from this method.
     */
    private void init() {
        // Updating the view with data from object:
        setText(R.id.tv_displaylog_date, diveLog.getDate());
        setText(R.id.tv_displaylog_surfaceguard, diveLog.getSurfaceGuard());
        setText(R.id.tv_displaylog_divebuddy, diveLog.getDiveBuddy());
        setText(R.id.tv_displaylog_divetype, diveLog.getDiveType());


        // Form planned depth output text.
        String plannedDepthOutput = (Integer.toString(diveLog.getPlannedDepth()) + " " + getString(R.string.displayloga_meters));
        setText(R.id.tv_displaylog_planneddepth, plannedDepthOutput);


        int actualDepth = diveLog.getActualDepth();
        setText(R.id.tv_displaylog_actualdepth, (Integer.toString(actualDepth)) + " " + getString(R.string.displayloga_meters));


        // Handling HoursAndMinutes class input.
        DiveLog.HoursAndMinutes lastDiveTmp = diveLog.getTimeSinceLastDive();
        DiveLog.HoursAndMinutes lastAlcoholTmp = diveLog.getTimeSinceAlcoholIntake();

        // Get strings to display.
        String lastDive =  lastTime(lastDiveTmp);
        String lastAlcohol = lastTime(lastAlcoholTmp);

        setText(R.id.tv_displaylog_lastdive, lastDive);
        setText(R.id.tv_displaylog_lastalcohol, lastAlcohol);

        // Add all security stops.
        addSecurityStops();

        setText(R.id.tv_displaylog_planneddivetime, Integer.toString(diveLog.getPlannedDiveTime()) + " " + getString(R.string.displayloga_minutes));

        // Calculate actual dive time:
        Date startDiveTime = diveLog.getStartTime();
        Date endDiveTime = diveLog.getEndTime();
        // In minutes.
        int actualDiveTime = (int) (timeDifference(startDiveTime, endDiveTime));


        // Calculate used pressure and liters of oxygen per hour.
        int tankSize = diveLog.getTankSize();
        int startTankPressure = diveLog.getStartTankPressure();
        int endTankPressure = diveLog.getEndTankPressure();
        int usedPressure = startTankPressure - endTankPressure;
        // Liter per minutes.
        int airUsage = (usedPressure * tankSize) / actualDiveTime;

        // Build String.
        StringBuilder tmp = new StringBuilder();
        tmp.append(Integer.toString(usedPressure));
        tmp.append(" ");
        tmp.append(getString(R.string.displayloga_bar));
        tmp.append(" (");
        tmp.append(Integer.toString(airUsage));
        tmp.append(" ");
        tmp.append(getString(R.string.displayloga_litrePerMinute));
        tmp.append(")");
        String usedPressureAndAir = tmp.toString();

        setText(R.id.tv_displaylog_actualdivetime,Integer.toString(actualDiveTime)      + " " + getString(R.string.displayloga_minutes));
        setText(R.id.tv_displaylog_tanksize,      Integer.toString(tankSize)            + " " + getString(R.string.displayloga_litre));
        setText(R.id.tv_displaylog_startpressure, Integer.toString(startTankPressure)   + " " + getString(R.string.displayloga_bar));
        setText(R.id.tv_displaylog_endpressure,   Integer.toString(endTankPressure)     + " " + getString(R.string.displayloga_bar));
        setText(R.id.tv_displaylog_usedpressure,       usedPressureAndAir);

        setText(R.id.tv_displaylog_saturation,         getSaturation());

        setText(R.id.tv_displaylog_divegas, diveLog.getDiveGas());
        setText(R.id.tv_displaylog_weather, diveLog.getWeather());

        // Checks whether to use celsius or fahrenheit.
        String degreeType = (usingMetric() ? getString(R.string.displayloga_celcius) : getString(R.string.displayloga_fahrenheit));
        setText(R.id.tv_displaylog_tempsurf,  Float.toString(diveLog.getTempSurface()) + " " + degreeType);
        setText(R.id.tv_displaylog_tempwater, Float.toString(diveLog.getTempWater())   + " " + degreeType);

        setText(R.id.tv_displaylog_current, diveLog.getCurrent());

        // There are two notes, adding them together into 1.
        String notes = diveLog.getNotes();
        notes += "\n\n" + diveLog.getNotesAfter();
        setText(R.id.tv_displaylog_notes, notes);

    }

    /**
     * This sets the text of a given TextView.
     *
     * @param id id of TextView to set text on
     * @param text text to set
     */
    private void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }


    /**
     * Returns a string saying "-" or "time.hours h. time.minutes min."
     *
     * @param time time since last ... (dive, alcohol intake, etc.)
     * @return {String} that
     */
    private String lastTime(DiveLog.HoursAndMinutes time) {
        // Set string to be "-" while time since last is over 24h..
        String last = "-";

        // Check if the time since last ... was less than 24 hours ago.
        if (time.getHours() < 24) {
            // Create a String to be displayed including hours and minutes.
            last = time.getHours() + getString(R.string.displayloga_hourShort) + " " + time.getMinutes() + getString(R.string.displayloga_minuteShort);
        }

        // Return the String.
        return last;
    }


    /**
     * Adds all the security stops to the view.
     */
    private void addSecurityStops() {

        // Getting all the stops.
        ArrayList<DiveLog.SecurityStop> securityStops = diveLog.getSecurityStops();
        // View to add stops to.
        LinearLayout securityStopContainer = findViewById(R.id.ll_displaylog_securitystopcontainer);

        // Counter used to print an index for each security stop.
        int counter = 1;
        for (DiveLog.SecurityStop stop: securityStops) {

            // Create a TextView to put in layout.
            TextView tv_stop = new TextView(this.getApplicationContext());

            // Strings to use.
            String duration = Integer.toString(stop.getDuration());
            String depth = Integer.toString(stop.getDepth());
            String index = Integer.toString(counter) + ".  ";

            // TODO: check for metric / imperial. Comment for further development.

            // Creating a StringBuilder to create the full String for the Security Stop.
            // Using default 16 character size. Knowing it will increase itself if needed.
            StringBuilder tmp = new StringBuilder();
            tmp.append(index);
            tmp.append(duration);
            tmp.append(" ");
            tmp.append(getString(R.string.displayloga_stopDuration));
            tmp.append(depth);
            tmp.append(" ");
            tmp.append(getString(R.string.displayloga_meters));
            tmp.append(".");

            String stopText = tmp.toString();

            // Set text of the TextView.
            tv_stop.setText(stopText);

            // Add TextView to layout.
            securityStopContainer.addView(tv_stop);

            // Increment counter.
            counter++;

        } // end loop

    }


    /**
     * Returns saturation the user should have after this dive.
     * It uses dive table to calculate and also checks for last dive time to account for
     * possible previous dives.
     *
     * @return {String} saturation
     */
    private String getSaturation() {
        // Temporaty variable to contain Saturation.
        String saturationGroup;

        // Calculate proper saturation.
        // TODO: check table & units from prefs. This can stay? Only PADI_METRIC and PADI_IMPERIAL is currently implemented

        // Get table to use in the calculation.
        DiveTable diveTable = new DiveTable(this, DiveTable.Table.PADI_METRIC);

        // Calculate actual dive time:
        // Get start and end time of dive.
        Date startDiveTime = diveLog.getStartTime();
        Date endDiveTime = diveLog.getEndTime();
        // Use a method to calculate the difference in minutes.
        int actualDiveTime = (int) (timeDifference(startDiveTime, endDiveTime));
        // Get actual depth of dive.
        int actualDepth = diveLog.getActualDepth();

        // If more than 24 hours since last dive.
        if (diveLog.getTimeSinceLastDive().getHours() >= 24) {
            // Calculate saturation using this method.
            saturationGroup = diveTable.getPreSISaturationGroup(actualDepth, actualDiveTime);
        } else {
            // If less than 24 hours since last dive, previous dives need to be taken into consideration.
            // Get last dive(s) saturation.
            saturationGroup = getPreviousDiveSaturation(diveTable, diveLog);
        }

        return saturationGroup;
    }


    /**
     * Returns time difference between two Date objects in minutes.
     * Using multiple variables for the simplicity of reading it.
     *
     * @param d1 date1 to compare with
     * @param d2 date2 to comapre with
     * @return {long} minutes between parameters
     */
    private long timeDifference(Date d1, Date d2) {
        long diff = Math.abs(d1.getTime() - d2.getTime());
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        return minutes;
    }


    /**
     * Function checks which measurement system the user has chosen.
     * True for metric.
     * False for imperial.
     *
     * @return {boolean} true if using metric system
     */
    private boolean usingMetric() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String measurement = prefs.getString("measurement", "");

        return (measurement.equals("metric"));

    }


    private String getPreviousDiveSaturation(DiveTable diveTable, DiveLog diveLog){

        DiveLog current = null, previous = null;

        List<DiveLog> logs = Database.getLoggedInDiver().getDiveLogs();
        for (DiveLog log: logs) {
            if (log.getDiveCount() == diveLog.getDiveCount()) {
                current = log;
            } else if (log.getDiveCount() == diveLog.getDiveCount() - 1) {
                previous = log;
            }
        }

        String previousSaturation = "";
        if (previous != null && previous.getTimeSinceLastDive().getHours() < 24) {
            previousSaturation = this.getPreviousDiveSaturation(diveTable, previous);
        }

        // Calculate actual dive time:
        // Get start and end time of dive.
        Date startDiveTime = current.getStartTime();
        Date endDiveTime = current.getEndTime();
        // Use a method to calculate the difference in minutes.
        int actualDiveTime = (int) (timeDifference(startDiveTime, endDiveTime));

        if (!previousSaturation.isEmpty()) {
            String adjustedSaturation = diveTable.getSISaturation(previousSaturation, current.getTimeSinceLastDive().getHours(), current.getTimeSinceLastDive().getMinutes());
            return diveTable.getPostSISaturationGroup(adjustedSaturation, actualDiveTime, current.getActualDepth());

        } else {
            return diveTable.getPreSISaturationGroup(current.getActualDepth(), actualDiveTime);
        }



//        // Temporary string that will be returned.
//        String saturationGroup = "";
//        // Get all logs.
//        ArrayList<DiveLog> logs = Database.getDiver(Database.getLoggedInDiver().getId()).getDiveLogs();
//        DiveLog lastDiveLog = null;
//
//        if (logs != null) {
//            // Find previous dive log.
//            for (DiveLog log: logs) {
//                if (log.getDiveCount() == diveLog.getDiveCount() - 1) {
//                    lastDiveLog = log;
//                    break;
//                }
//            }
//
//            // Make sure a log was found.
//            if (lastDiveLog != null) {
//
//
//                // Calculate actual dive time for previous dive log.
//                int lastDiveLogActualDiveTime = (int) (timeDifference(lastDiveLog.getStartTime(), lastDiveLog.getEndTime()));
//                // Calculate saturation for previous dive log.
//                String lastDiveLogSaturation = diveTable.getPreSISaturationGroup(lastDiveLog.getActualDepth(), lastDiveLogActualDiveTime);
//
//
//
//
//                // Calculate actual dive time:
//                // Get start and end time of dive.
//                Date startDiveTime = diveLog.getStartTime();
//                Date endDiveTime = diveLog.getEndTime();
//                // Use a method to calculate the difference in minutes.
//                int actualDiveTime = (int) (timeDifference(startDiveTime, endDiveTime));
//                // Get actual depth of dive.
//                int actualDepth = diveLog.getActualDepth();
//
//
//                // Calculate new saturation for this log.
//                String adjustedSaturation = diveTable.getSISaturation(lastDiveLogSaturation, diveLog.getTimeSinceLastDive().getHours(), diveLog.getTimeSinceLastDive().getMinutes());
//                saturationGroup = diveTable.getPostSISaturationGroup(adjustedSaturation, actualDiveTime, actualDepth);
//            } else {
//                // Something went horribly wrong. (should not really occur, happens when last
//                // alcohol intake is less than 24hrs ago but there is no previous log to check saturation of).
//                saturationGroup = "Couldn't calculate";
//            }
//        }
//
//
//        return saturationGroup;
    }


}
