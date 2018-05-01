package no.ntnu.diverslogbook;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import no.ntnu.diverslogbook.fragments.PlanFragment;
import no.ntnu.diverslogbook.model.DiveLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test whether or not all goes well when creating
 * a new diving plan and updating it so it becomes a log.
 */
public class CreateLogTest extends PlanFragment {

    /**
     * All possible inputs from PlanFragment and FinishPlan.
     */
    private String date = "27/04/2018";
    private String buddy = "Lars Johan Nybø";
    private String guard = "Lee Khan";
    private String location = "Oppland, Gjøvik, Mjøsa";
    private String diveType = "Work";
    private String plannedDepth = "30";
    private String actualDepth = "25";
    private String timeSinceLastDive = "19:50";
    private String timeSinceAlcoholIntake = Globals.MORETHAN24H;
    private ArrayList<DiveLog.SecurityStop> securityStops = new ArrayList<>();
    private String notes = "";
    private String notesAfter = "Very beautiful reefs here!";
    private String plannedDiveTime = "40";
    private LocalDateTime startTime = null;
    private LocalDateTime endTime = null;
    private String tankSize = "300";
    private String startTankPressure = "400";
    private int endTankPressure = 200;
    private String diveGas = "Air";
    private String weather = "Cloudy";
    private String current = "Strong";
    private String tempSurface = "22";
    private String tempWater = "12";


    @Test
    public void allDataIsValid() {
        assertTrue(planDataIsValid(date, buddy, guard, location, plannedDepth, plannedDiveTime, tankSize,
                startTankPressure, tempSurface, tempWater, timeSinceLastDive, timeSinceAlcoholIntake));
    }

    @Test
    public void returnsHoursAndMinutes() {
        DiveLog.HoursAndMinutes test = new DiveLog.HoursAndMinutes(19, 50);

        assertEquals(getNewHoursAndMinutes(timeSinceLastDive), test);
    }
}