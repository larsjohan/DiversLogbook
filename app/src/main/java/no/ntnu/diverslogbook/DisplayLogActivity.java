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
         * SATURATION
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
        ((TextView) findViewById(R.id.tv_displaylog_date)).setText("01/01/2018");
        ((TextView) findViewById(R.id.tv_displaylog_surfaceguard)).setText(diveLog.getSurfaceGuard());
        ((TextView) findViewById(R.id.tv_displaylog_divebuddy)).setText(diveLog.getDiveBuddy());
        ((TextView) findViewById(R.id.tv_displaylog_divetype)).setText(diveLog.getDiveType());
        ((TextView) findViewById(R.id.tv_displaylog_planneddepth)).setText(Integer.toString(diveLog.getPlannedDepth()) + " meters");


        // Handling HoursAndMinutes class input.
        DiveLog.HoursAndMinutes lastDiveTmp = diveLog.getTimeSinceLastDive();
        DiveLog.HoursAndMinutes lastAlcoholTmp = diveLog.getTimeSinceAlcoholIntake();
        // Set strings to "-" if over 24h since last..
        String lastDive = (lastDiveTmp.hours >= 24) ? "-" : lastDiveTmp.hours + "h, " + lastDiveTmp.minutes + "min.";
        String lastAlcohol = (lastAlcoholTmp.hours >= 24) ? "-" : lastAlcoholTmp.hours + "h, " + lastAlcoholTmp.minutes + "min.";

        ((TextView) findViewById(R.id.tv_displaylog_tsld)).setText(lastDive);
        ((TextView) findViewById(R.id.tv_displaylog_tslai)).setText(lastAlcohol);


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
            String stopText = index + duration + " minutes at " + depth + " meters.";

            // Set text and increment counter.
            tv_stop.setText(stopText);
            counter++;

            // Add TextView to layout.
            securityStopContainer.addView(tv_stop);


        } // end loop


        ((TextView) findViewById(R.id.tv_displaylog_planneddivetime)).setText(Integer.toString(diveLog.getPlannedDiveTime()) + " minutes");

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
        String usedPressureAndAir = Integer.toString(usedPressure) + " bar (" + Integer.toString(airUsage) + " l/min)";

        ((TextView) findViewById(R.id.tv_displaylog_actualdivetime)).setText(Integer.toString(actualDiveTime) + " minutes");
        ((TextView) findViewById(R.id.tv_displaylog_tanksize)).setText(Integer.toString(tankSize) + " litre");
        ((TextView) findViewById(R.id.tv_displaylog_startpressure)).setText(Integer.toString(startTankPressure) + " bar");
        ((TextView) findViewById(R.id.tv_displaylog_endpressure)).setText(Integer.toString(endTankPressure) + " bar");
        ((TextView) findViewById(R.id.tv_displaylog_usedpressure)).setText(usedPressureAndAir);

        ((TextView) findViewById(R.id.tv_displaylog_divegas)).setText(diveLog.getDiveGas());
        ((TextView) findViewById(R.id.tv_displaylog_weather)).setText(diveLog.getWeather());

        // TODO: implement functionality which converts from metric to imperial. Also change text from C to F.
        ((TextView) findViewById(R.id.tv_displaylog_tempsurf)).setText(Float.toString(diveLog.getTempSurface()) + " °C");
        ((TextView) findViewById(R.id.tv_displaylog_tempwater)).setText(Float.toString(diveLog.getTempWater()) + " °C");


        ((TextView) findViewById(R.id.tv_displaylog_current)).setText("fix this");
        ((TextView) findViewById(R.id.tv_displaylog_notes)).setText(diveLog.getNotes());




    }


    /**
     * Returns time difference between two Date objects in minutes.
     *
     * @param d1
     * @param d2
     * @return
     */
    long timeDifference(Date d1, Date d2) {
        long diff = Math.abs(d1.getTime() - d2.getTime());
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        return minutes;
    }


}
