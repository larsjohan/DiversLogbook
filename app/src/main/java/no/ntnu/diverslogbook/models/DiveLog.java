package no.ntnu.diverslogbook.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Contains all data related to a dive.
 */
public class DiveLog implements Serializable {

    /**********************************************
     ************* MEMBER VARIABLES ***************
     **********************************************/

    private int diveCount;
    private String date;

    // Users.
    private String surfaceGuard;
    private String diveBuddy;

    // General data.
    private String location;
    private String diveType;
    private int plannedDepth;
    private int actualDepth;
    private HoursAndMinutes timeSinceLastDive;       // Null if more than 24h.
    private HoursAndMinutes timeSinceAlcoholIntake;  // Null if more than 24h.
    private ArrayList<SecurityStop> securityStops;
    private String notes;
    private String notesAfter;

    // Dive time.
    private int plannedDiveTime;    // In minutes.
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Tank data.
    private int tankSize;           // In liters.
    private int startTankPressure;  // Start and end pressure
    private int endTankPressure;    // calculates total air usage.
    private String diveGas;

    // Set these if nitrox:
    //private float fO2;
    //private int o2Percent;

    // Environment.
    private String weather;
    private String current;
    private float tempSurface;
    private float tempWater;


    /**********************************************
     **************** CONSTRUCTORS ****************
     **********************************************/

    /**
     * Creates an empty log object.
     */
    public DiveLog() {
        this.diveCount = 0;
        this.date = "";
        this.diveBuddy = "";
        this.surfaceGuard = "";
        this.location = "";
        this.diveType = "";
        this.plannedDepth = 0;
        this.timeSinceLastDive = null;
        this.timeSinceAlcoholIntake = null;
        this.securityStops = new ArrayList<>();
        this.plannedDiveTime = 0;
        this.tankSize = 0;
        this.startTankPressure = 0;
        this.diveGas = "";
        this.weather = "";
        this.tempSurface = 0;
        this.tempWater = 0;
        this.notes = "";

        // These should be set after the first initialization.
        this.startTime = null;
        this.endTime = null;
        this.current = "";
        this.actualDepth = 0;
        this.endTankPressure = 0;
        this.notesAfter = "";
    }


    /**
     * Creates a log object after planning a dive.
     *
     * @params Data about the planned dive.
     */
    public DiveLog(int diveCount, String date, String buddy, String guard, String location, String diveType, int plannedDepth,
                   HoursAndMinutes lastDive, HoursAndMinutes lastAlcohol, ArrayList<SecurityStop> stops, int plannedTime,
                   int tankSize, int startTankPressure, String diveGas, String weather, float tempSurface, float tempWater, String notes) {

        this.diveCount = diveCount;
        this.date = date;
        this.diveBuddy = buddy;
        this.surfaceGuard = guard;
        this.location = location;
        this.diveType = diveType;
        this.plannedDepth = plannedDepth;
        this.timeSinceLastDive = lastDive;
        this.timeSinceAlcoholIntake = lastAlcohol;
        this.securityStops = stops;
        this.plannedDiveTime = plannedTime;
        this.tankSize = tankSize;
        this.startTankPressure = startTankPressure;
        this.diveGas = diveGas;
        this.weather = weather;
        this.tempSurface = tempSurface;
        this.tempWater = tempWater;
        this.notes = notes;

        // These should be set after the first initialization.
        this.startTime = null;
        this.endTime = null;
        this.current = "";
        this.actualDepth = 0;
        this.endTankPressure = 0;
        this.notesAfter = "";
    }


    /**********************************************
     ************** SETTER METHODS ****************
     **********************************************/

    /**
     * Set start dive count.
     *
     * @param diveCount The dive count
     */
    public void setDiveCount(int diveCount) {
        this.diveCount = diveCount;
    }

    /**
     * Set start time of this dive.
     *
     * @param time Start time
     */
    public void setStartTime(LocalDateTime time) {
        this.startTime = time;
    }


    /**
     * Set end time of this dive.
     *
     * @param time End time
     */
    public void setEndTime(LocalDateTime time) {
        this.endTime = time;
    }


    /**
     * Set tank pressure at end of dive.
     *
     * @param pressure The tank pressure
     */
    public void setEndTankPressure(int pressure) {
        this.endTankPressure = pressure;
    }


    /**
     * Set current at the end of dive.
     *
     * @param current Experienced current during dive
     */
    public void setCurrent(String current) {
        this.current = current;
    }


    /**
     * Set the actual diving depth.
     *
     * @param depth Actual diving depth
     */
    public void setActualDepth(int depth) {
        this.actualDepth = depth;
    }


    /**
     * Set the notes from after the dive.
     *
     * @param notesAfter Notes
     */
    public void setNotesAfter(String notesAfter) {
        this.notesAfter = notesAfter;
    }


    /**
     * If Nitrox was chosen as tank gas, set specific data.
     *
     * @param fO2 The fO2 value
     * @param o2Percent The oxygen percentage

    public void setNitroxData(float fO2, int o2Percent) {
        this.fO2 = fO2;
        this.o2Percent = o2Percent;
    }
     */



    /**********************************************
     ************** GETTER METHODS ****************
     **********************************************/

    /**
     * Get current dive count.
     *
     * @return Dive count
     */
    public int getDiveCount() {
        return diveCount;
    }


    /**
     * Get date of planned dive.
     *
     * @return Date of dive
     */
    public String getDate() {
        return date;
    }


    /**
     * Get name of planned diving buddy.
     *
     * @return Name of buddy
     */
    public String getDiveBuddy() {
        return diveBuddy;
    }


    /**
     * Get name of planned surface guard.
     *
     * @return Name of guard
     */
    public String getSurfaceGuard() {
        return surfaceGuard;
    }


    /**
     * Get name of chosen location.
     *
     * @return Name of location
     */
    public String getLocation() {
        return location;
    }


    /**
     * Get name of planned diving type.
     *
     * @return Type of dive
     */
    public String getDiveType() {
        return diveType;
    }


    /**
     * Get planned diving depth.
     *
     * @return Planned depth
     */
    public int getPlannedDepth() {
        return plannedDepth;
    }


    /**
     * Get actual diving depth.
     *
     * @return Actual depth
     */
    public int getActualDepth() {
        return actualDepth;
    }


    /**
     * Get hours and minutes since last dive.
     *
     * @return Time since last dive
     */
    public HoursAndMinutes getTimeSinceLastDive() {
        return timeSinceLastDive;
    }


    /**
     * Get hours and minutes since last alcohol intake.
     *
     * @return Time since last alcohol intake
     */
    public HoursAndMinutes getTimeSinceAlcoholIntake() {
        return timeSinceAlcoholIntake;
    }


    /**
     * Get a list of planned security stops for the dive.
     *
     * @return List of security stops
     */
    public ArrayList<SecurityStop> getSecurityStops() {
        return securityStops;
    }


    /**
     * Get notes for the planned dive.
     *
     * @return Notes
     */
    public String getNotes() {
        return notes;
    }


    /**
     * Get planned diving time in minutes.
     *
     * @return Diving time
     */
    public int getPlannedDiveTime() {
        return plannedDiveTime;
    }


    /**
     * Get the tank pressure from before the planned dive.
     *
     * @return Tank pressure
     */
    public int getStartTankPressure() {
        return startTankPressure;
    }


    /**
     * Get the tank pressure from after the planned dive.
     *
     * @return Tank pressure
     */
    public int getEndTankPressure() {
        return endTankPressure;
    }


    /**
     * Get chosen type of dive gas.
     *
     * @return Dive gas
     */
    public String getDiveGas() {
        return diveGas;
    }


    /**
     * Get the diving session's weather.
     *
     * @return Weather
     */
    public String getWeather() {
        return weather;
    }


    /**
     * Get current in water from the diving session.
     *
     * @return Current
     */
    public String getCurrent() {
        return current;
    }


    /**
     * Get temperature in water from the diving session.
     *
     * @return Water temperature
     */
    public float getTempWater() {
        return tempWater;
    }


    /**
     * Get temperature on surface from the diving session.
     *
     * @return Surface temperature
     */
    public float getTempSurface() {
        return tempSurface;
    }


    /**
     * Get notes from after the actual dive.
     *
     * @return Notes
     */
    public String getNotesAfter() {
        return notesAfter;
    }


    /**********************************************
     ****************** CLASSES *******************
     **********************************************/

    /**
     * Defines a security stop where the diver stays
     * at a certain depth for a specific amount of time.
     */
    public static class SecurityStop implements Serializable {

        private int duration;    // In minutes.
        private int depth;       // In meters.


        /**
         * Required constructor.
         */
        public SecurityStop() {
            this.duration = 0;
            this.depth = 0;
        }


        /**
         * Create a new security stop object.
         *
         * @param depth Depth in meters
         * @param duration Duration in minutes
         */
        public SecurityStop(int depth, int duration) {
            this.depth = depth;
            this.duration = duration;
        }

        /**
         * Get depth.
         *
         * @return depth
         */
        public int getDepth() {
            return this.depth;
        }


        /**
         * Get duration.
         *
         * @return duration
         */
        public int getDuration() {
            return this.duration;
        }
    }


    /**
     * Defines a specified amount of hours and minutes.
     */
    public static class HoursAndMinutes {

        private int hours;
        private int minutes;


        /**
         * Required constructor.
         */
        public HoursAndMinutes() {
            this.hours = 0;
            this.minutes = 0;
        }

        /**
         * Create a new object with hours and minutes.
         *
         * @param hours Total hours
         * @param minutes Total minutes
         */
        public HoursAndMinutes(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        /**
         * Get set hours.
         *
         * @return Hours
         */
        public int getHours() {
            return hours;
        }

        /**
         * Get set minutes.
         *
         * @return Minutes
         */
        public int getMinutes() {
            return minutes;
        }
    }
}
