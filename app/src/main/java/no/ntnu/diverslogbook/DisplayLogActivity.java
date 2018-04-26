package no.ntnu.diverslogbook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayLogActivity extends AppCompatActivity {

    public  DisplayLogActivity() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylog);

        Intent intent = getIntent();
        DiveLog diveLog = (DiveLog) intent.getSerializableExtra("LogToDisplay");

        // Set toolbar text. (Dive number + Location)
        String diveNr = "0001";
        getSupportActionBar().setTitle(diveNr + ": " +  diveLog.getLocation());


        // Updating the view with data from object:
        ((TextView) findViewById(R.id.tv_displaylog_date)).setText("01/01/2018");
        ((TextView) findViewById(R.id.tv_displaylog_surfaceguard)).setText(diveLog.getSurfaceGuard());
        ((TextView) findViewById(R.id.tv_displaylog_divebuddy)).setText(diveLog.getDiveBuddy());
        ((TextView) findViewById(R.id.tv_displaylog_divetype)).setText(diveLog.getDiveType());
        ((TextView) findViewById(R.id.tv_displaylog_planneddepth)).setText(Integer.toString(diveLog.getPlannedDepth()));


        // Handling HoursAndMinutes class input.
        DiveLog.HoursAndMinutes lastDiveTmp = diveLog.getTimeSinceLastDive();
        DiveLog.HoursAndMinutes lastAlcoholTmp = diveLog.getTimeSinceAlcoholIntake();
        // Set strings to "-" if over 24h since last..
        String lastDive = (lastDiveTmp.hours >= 24) ? "-" : lastDiveTmp.hours + "h, " + lastDiveTmp.minutes + "m.";
        String lastAlcohol = (lastAlcoholTmp.hours >= 24) ? "-" : lastAlcoholTmp.hours + "h, " + lastAlcoholTmp.minutes + "m.";

        ((TextView) findViewById(R.id.tv_displaylog_tsld)).setText(lastDive);
        ((TextView) findViewById(R.id.tv_displaylog_tslai)).setText(lastAlcohol);


        // Security stops
        // loop array list and create table rows.
        ArrayList<DiveLog.SecurityStop> securityStops = diveLog.getSecurityStops();
        TableLayout securityStopTable = findViewById(R.id.tl_displaylog_security);

        int counter = 1;
        for (DiveLog.SecurityStop stop: securityStops) {
            /* Create a new row to be added. */
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            // TextViews to be in row.
            TextView stopDuration = new TextView(this.getApplicationContext());
            TextView stopDepth = new TextView(this.getApplicationContext());

            String duration = Integer.toString(stop.duration) + " minutes at ";
            String depth = Integer.toString(stop.depth) + " meters.";
            duration = Integer.toString(counter) + ".  " + duration;
            stopDuration.setText(duration);
            stopDepth.setText(depth);
            counter++;

            // Set layout.
            stopDuration.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            stopDepth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

             /* Add text to row. */
            tr.addView(stopDuration);
            tr.addView(stopDepth);

            /* Add row to TableLayout. */
            securityStopTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        } // end loop


        ((TextView) findViewById(R.id.tv_displaylog_divetime)).setText(Integer.toString(diveLog.getPlannedDiveTime()));
        ((TextView) findViewById(R.id.tv_displaylog_tanksize)).setText(Integer.toString(diveLog.getTankSize()));
        ((TextView) findViewById(R.id.tv_displaylog_startpressure)).setText(Integer.toString(diveLog.getStartTankPressure()));
        ((TextView) findViewById(R.id.tv_displaylog_divegas)).setText(diveLog.getDiveGas());
        ((TextView) findViewById(R.id.tv_displaylog_weather)).setText(diveLog.getWeather());

        // TODO: implement functionality which converts from metric to imperial. Also change text from C to F.
        ((TextView) findViewById(R.id.tv_displaylog_tempsurf)).setText(Float.toString(diveLog.getTempSurface()) + " °C");
        ((TextView) findViewById(R.id.tv_displaylog_tempwater)).setText(Float.toString(diveLog.getTempWater()) + " °C");


        ((TextView) findViewById(R.id.tv_displaylog_current)).setText("fix this");
        ((TextView) findViewById(R.id.tv_displaylog_notes)).setText(diveLog.getNotes());




    }
}
