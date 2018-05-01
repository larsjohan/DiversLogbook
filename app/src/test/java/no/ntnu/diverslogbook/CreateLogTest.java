package no.ntnu.diverslogbook;

import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Arrays;
import java.util.List;

import no.ntnu.diverslogbook.fragments.PlanFragment;
import no.ntnu.diverslogbook.model.DiveLog;
import no.ntnu.diverslogbook.util.Globals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test whether or not all goes well when creating
 * a new diving plan and updating it so it becomes a log.
 */
public class CreateLogTest extends PlanFragment {


    /**
     * Tests the function that checks if all the input data is valid.
     */
    @Test
    public void allDataIsValid() {
        String date = "27/04/2018";
        String buddy = "Lars Johan Nybø";
        String guard = "Lee Khan";
        String location = "Oppland, Gjøvik, Mjøsa";
        String plannedDepth = "30";
        String timeSinceLastDive = "19:50";
        String timeSinceAlcoholIntake = Globals.MORETHAN24H;
        String plannedDiveTime = "40";
        String tankSize = "300";
        String startTankPressure = "400";
        String tempSurface = "22";
        String tempWater = "12";

        List<String> USERS = Arrays.asList("Lars Johan Nybø", "Lee Khan", "Linn Hege Kristensen");
        Whitebox.setInternalState(this, "USERS", USERS);

        List<String> LOCATIONS = Arrays.asList("Oppland, Gjøvik, Mjøsa", "Møre og Romsdal, Ålesund, Alnes", "Aust-Agder, Arendal, Bjelland");
        Whitebox.setInternalState(this, "LOCATIONS", LOCATIONS);

        assertTrue(planDataIsValid(date, buddy, guard, location, plannedDepth, plannedDiveTime, tankSize,
                startTankPressure, tempSurface, tempWater, timeSinceLastDive, timeSinceAlcoholIntake));
    }


    /**
     * Tests if the a correct HoursAndMinutes object is returned based on input provided.
     */
    @Test
    public void getNewHoursAndMinutesTest() {
        String timeSinceLastDive = "19:50";

        DiveLog.HoursAndMinutes test = new DiveLog.HoursAndMinutes(19, 50);
        DiveLog.HoursAndMinutes object = getNewHoursAndMinutes(timeSinceLastDive);

        assertEquals(object.getHours(), test.getHours());
        assertEquals(object.getMinutes(), test.getMinutes());
    }
}