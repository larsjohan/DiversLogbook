package no.ntnu.diverslogbook;

import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Contains all data related to a dive.
 */
public class DiveLog {

    // Users.
    private String diver;
    private String surfaceGuard;
    private String diveBuddy;

    // General data.
    private String location;
    private DiveType diveType;
    private int plannedDepth;
    private HoursAndMinutes timeSinceLastDive;       // Blank if more than 24h.
    private HoursAndMinutes timeSinceAlcoholIntake;  // Blank if more than 24h.
    private ArrayList<SecurityStop> securityStops;
    private String notes;

    // Dive time.
    private int plannedDiveTime;    // In minutes.
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Tank data.
    private int tankSize;           // In liters.
    private int startTankPressure;  // Start and end pressure
    private int endTankPressure;    // calculates total air usage.

    // Set these if nitrox:
    private float fO2;
    private int o2Percent;

    // Environment.
    private Weather weather;
    private Current current;
    private float tempSurface;
    private float tempWater;


    /**
     * Creates a log object after planning a dive.
     *
     * @params Data about the planned dive.
     */
    public DiveLog(String diver, String guard, String buddy, String location, DiveType diveType, int plannedDepth,
                   HoursAndMinutes lastDive, HoursAndMinutes lastAlcohol, ArrayList<SecurityStop> stops, int plannedTime,
                   int tankSize, int startTankPressure, Weather weather, float tempSurface, float tempWater, String notes) {

        this.diver = diver;
        this.surfaceGuard = guard;
        this.diveBuddy = buddy;
        this.location = location;
        this.diveType = diveType;
        this.plannedDepth = plannedDepth;
        this.timeSinceLastDive = lastDive;
        this.timeSinceAlcoholIntake = lastAlcohol;
        this.securityStops = stops;
        this.plannedDiveTime = plannedTime;
        this.tankSize = tankSize;
        this.startTankPressure = startTankPressure;
        this.weather = weather;
        this.tempSurface = tempSurface;
        this.tempWater = tempWater;
        this.notes = notes;
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
    public void setCurrent(Current current) {
        this.current = current;
    }


    /**
     * If Nitrox was chosen as tank gas, set specific data.
     *
     * @param fO2
     * @param o2Percent
     */
    public void setNitroxData(float fO2, int o2Percent) {
        this.fO2 = fO2;
        this.o2Percent = o2Percent;
    }


    /**
     * Different types of water current.
     */
    public enum Current {
        NONE,
        LIGHT,
        MEDIUM,
        STRONG
    }


    /**
     * Different types of weather.
     */
    public enum Weather {
        KLART,
        NEDBØR,
        STORM,
        OVERKSYET
    }


    /**
     * Different types of gas for diving.
     */
    public enum DiveGas {
        AIR,
        NITROX
    }


    /**
     * All different types of diving.
     */
    public enum DiveType {
        RECREATION,
        WORK,
        PHOTOGRAPHY,
        SEARCH_AND_RESCUE
    }


    /**
     * Defines a security stop where the diver stays
     * at a certain depth for a specific amount of time.
     */
    public class SecurityStop {

        public int duration;    // In minutes.
        public int depth;       // In meters.


        /**
         * Create a new security stop object.
         * @param duration
         * @param depth
         */
        public SecurityStop(int duration, int depth) {
            this.duration = duration;
            this.depth = depth;
        }
    }


    /**
     * Defines a specified amount of hours and minutes.
     */
    public class HoursAndMinutes {

        public int hours;
        public int minutes;


        /**
         * Create a new object with hours and minutes.
         * @param hours
         * @param minutes
         */
        public HoursAndMinutes(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }
    }
}
