package no.ntnu.diverslogbook;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Contains all data related to a dive.
 */
public class DiveLog {

    private String date;

    // Users.
    private String diver;
    private String surfaceGuard;
    private String diveBuddy;

    // General data.
    private String location;
    private String diveType;
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
    private String diveGas;

    // Set these if nitrox:
    private float fO2;
    private int o2Percent;

    // Environment.
    private String weather;
    private Current current;
    private float tempSurface;
    private float tempWater;


    /**
     * Creates a log object after planning a dive.
     *
     * @params Data about the planned dive.
     */
    public DiveLog(String date, String diver, String guard, String buddy, String location, String diveType, int plannedDepth,
                   HoursAndMinutes lastDive, HoursAndMinutes lastAlcohol, ArrayList<SecurityStop> stops, int plannedTime,
                   int tankSize, int startTankPressure, String diveGas, String weather, float tempSurface, float tempWater, String notes) {

        this.date = date;
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
        this.diveGas = diveGas;
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
     * Defines a security stop where the diver stays
     * at a certain depth for a specific amount of time.
     */
    public static class SecurityStop implements Serializable {

        public int duration;    // In minutes.
        public int depth;       // In meters.


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
