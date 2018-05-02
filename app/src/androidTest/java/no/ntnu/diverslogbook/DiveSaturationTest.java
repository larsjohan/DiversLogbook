package no.ntnu.diverslogbook;


import android.provider.ContactsContract;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.utils.Database;



public class DiveSaturationTest {



    /**
     * List of dive logs.
     */
    private ArrayList<DiveLog> diveLogList;


    /**
     * Public constructor.
     */
    public DiveSaturationTest() {
        this.diveLogList = new ArrayList<>();
    }





    /**
     * Setup functionality.
     */
    @Before
    public void setup() {
        this.diveLogList.clear();


        DiveLog log1 = new DiveLog();
        DiveLog log2 = new DiveLog();
        DiveLog log3 = new DiveLog();


        // Dive 1
        Date startDiveTime1 = new Date();
        Date endDiveTime1 = new Date();
        endDiveTime1.setMinutes(startDiveTime1.getMinutes() + 10);
        log1.setStartTime(startDiveTime1);
        log1.setEndTime(endDiveTime1);

        log1.setActualDepth(30);
        log1.setDiveCount(1);
        DiveLog.HoursAndMinutes sinceLastDive1 = new DiveLog.HoursAndMinutes(5, 20);
        log1.setTimeSinceLastDive(sinceLastDive1);

        // Dive 2
        Date startDiveTime2 = new Date();
        Date endDiveTime2 = new Date();
        endDiveTime2.setMinutes(startDiveTime2.getMinutes() + 10);
        log2.setStartTime(startDiveTime2);
        log2.setEndTime(endDiveTime2);

        log2.setActualDepth(30);
        log2.setDiveCount(2);
        DiveLog.HoursAndMinutes sinceLastDive2 = new DiveLog.HoursAndMinutes(25, 20);
        log2.setTimeSinceLastDive(sinceLastDive2);

        // Dive 3
        Date startDiveTime3 = new Date();
        Date endDiveTime3 = new Date();
        endDiveTime3.setMinutes(startDiveTime3.getMinutes() + 20);
        log3.setStartTime(startDiveTime3);
        log3.setEndTime(endDiveTime3);

        log3.setActualDepth(18);
        log3.setDiveCount(3);
        DiveLog.HoursAndMinutes sinceLastDive3 = new DiveLog.HoursAndMinutes(1, 20);
        log3.setTimeSinceLastDive(sinceLastDive3);


        this.diveLogList.add(log1);
        this.diveLogList.add(log2);
        this.diveLogList.add(log3);


        //Database database = Mockito.mock(Database.class, Mockito.CALLS_REAL_METHODS);
        //Diver diver = Mockito.mock(Diver.class, Mockito.CALLS_REAL_METHODS);

       // Mockito.when(database.getLoggedInDiver()).thenReturn(diver);
        Mockito.when(Database.getDiver(Database.getLoggedInDiver().getId()).getDiveLogs()).thenReturn(this.diveLogList);

    }

    @Test
    public void siTest() {
        Log.d("tasdsads", Arrays.toString(Database.getLoggedInDiver().getDiveLogs().toArray()));
    }

}
