package no.ntnu.diverslogbook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.toIntExact;

public class DisplayLogActivity extends AppCompatActivity {

    public static final String TAG = "DisplayLogActivity";


    public  DisplayLogActivity() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylog);


        // TODO:
        /*
         *
         * DATE
         * SATURATION
         * CURRENT
         *
         */

        // Get intent from previous screen.
        Intent intent = getIntent();
        DiveLog diveLog = (DiveLog) intent.getSerializableExtra("LogToDisplay");

        // Set toolbar text. (Dive number + Location)
        String diveNr = "#0001";
        try {
            getSupportActionBar().setTitle(diveNr + ": " +  diveLog.getLocation());
        }
        catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException at setTitle." + e.getMessage());
        }


        // Updating the view with data from object:
        // TODO: Get date from object.
        setText(R.id.tv_displaylog_date, "01/01/2018");
        setText(R.id.tv_displaylog_surfaceguard, diveLog.getSurfaceGuard());
        setText(R.id.tv_displaylog_divebuddy, diveLog.getDiveBuddy());
        setText(R.id.tv_displaylog_divetype, diveLog.getDiveType());

        // Form planned depth output text.
        String plannedDepthOutput = (Integer.toString(diveLog.getPlannedDepth()) + " " + getString(R.string.displayloga_meters));
        setText(R.id.tv_displaylog_planneddepth, plannedDepthOutput);


        // Handling HoursAndMinutes class input.
        DiveLog.HoursAndMinutes lastDiveTmp = diveLog.getTimeSinceLastDive();
        DiveLog.HoursAndMinutes lastAlcoholTmp = diveLog.getTimeSinceAlcoholIntake();

        // Get strings to display.
        String lastDive =  lastTime(lastDiveTmp);
        String lastAlcohol = lastTime(lastAlcoholTmp);

        setText(R.id.tv_displaylog_tsld, lastDive);
        setText(R.id.tv_displaylog_tslai, lastAlcohol);


        // Security stops
        // Loop array list and create TextViews.
        ArrayList<DiveLog.SecurityStop> securityStops = diveLog.getSecurityStops();
        LinearLayout securityStopContainer = findViewById(R.id.ll_displaylog_securitystopcontainer);

        // Counter used to print an index for each security stop.
        int counter = 1;
        for (DiveLog.SecurityStop stop: securityStops) {

            // TextView to put in layout.
            TextView tv_stop = new TextView(this.getApplicationContext());

            // String to use.
            String duration = Integer.toString(stop.duration);
            String depth = Integer.toString(stop.depth);
            String index = Integer.toString(counter) + ".  ";

            // TODO: check for metric / imperial. Also could put this in strings.xml?
            String stopText = index + duration + " " + getString(R.string.displayloga_stopDuration) + " " + depth + " " + getString(R.string.displayloga_meters) + ".";

            // Set text and increment counter.
            tv_stop.setText(stopText);
            counter++;

            // Add TextView to layout.
            securityStopContainer.addView(tv_stop);


        } // end loop


        setText(R.id.tv_displaylog_planneddivetime, Integer.toString(diveLog.getPlannedDiveTime()) + " " + getString(R.string.displayloga_minutes));

        // Calculate actual dive time:
        Date startDiveTime = diveLog.getStartTime();
        Date endDiveTime = diveLog.getEndTime();
        // In minutes.
        int actualDiveTime = toIntExact(timeDifference(startDiveTime, endDiveTime));


        // Calculate used pressure and liters of oxygen per hour.
        int tankSize = diveLog.getTankSize();
        int startTankPressure = diveLog.getStartTankPressure();
        int endTankPressure = diveLog.getEndTankPressure();
        int usedPressure = startTankPressure - endTankPressure;
        // Liter per minutes.
        int airUsage = (usedPressure * tankSize) / actualDiveTime;
        String usedPressureAndAir = Integer.toString(usedPressure) + " " + getString(R.string.displayloga_bar) + " (" + Integer.toString(airUsage) + " " + getString(R.string.displayloga_litrePerMinute) + ")";

        setText(R.id.tv_displaylog_actualdivetime,Integer.toString(actualDiveTime)      + " " + getString(R.string.displayloga_minutes));
        setText(R.id.tv_displaylog_tanksize,      Integer.toString(tankSize)            + " " + getString(R.string.displayloga_litre));
        setText(R.id.tv_displaylog_startpressure, Integer.toString(startTankPressure)   + " " + getString(R.string.displayloga_bar));
        setText(R.id.tv_displaylog_endpressure,   Integer.toString(endTankPressure)     + " " + getString(R.string.displayloga_bar));
        setText(R.id.tv_displaylog_usedpressure, usedPressureAndAir);

        setText(R.id.tv_displaylog_saturation, "FIX THiS ");
        setText(R.id.tv_displaylog_divegas, diveLog.getDiveGas());
        setText(R.id.tv_displaylog_weather, diveLog.getWeather());

        // TODO: implement functionality which converts from metric to imperial. Also change text from C to F. R.string.displayloga_fahrenheit.
        setText(R.id.tv_displaylog_tempsurf, Float.toString(diveLog.getTempSurface()) + " " + getString(R.string.displayloga_celcius));
        setText(R.id.tv_displaylog_tempwater, Float.toString(diveLog.getTempWater()) + " " + getString(R.string.displayloga_celcius));

        setText(R.id.tv_displaylog_current, "FIX THiS ");
        setText(R.id.tv_displaylog_notes, diveLog.getNotes());



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
     * @param time
     * @return {String} that
     */
    private String lastTime(DiveLog.HoursAndMinutes time) {
        // Set string to be "-" while time since last is over 24h..
        String last = "-";

        if (time.hours < 24) {
            last = time.hours + getString(R.string.displayloga_hourShort) + " " + time.minutes + getString(R.string.displayloga_minuteShort);
        }

        return last;
    }


    /**
     * Returns time difference between two Date objects in minutes.
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


}
