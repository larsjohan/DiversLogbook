package no.ntnu.diverslogbook;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


/**
 * Contains all data related to a dive.
 */
public class DiveLog implements Serializable {

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
    private Date startTime;
    private Date endTime;

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


    // Mock data
    public DiveLog() {
        this.diver = "Diver 1";
        this.surfaceGuard = "guard 1";
        this.diveBuddy = "Buddy 1";
        this.location = "Langesund, Norway";
        this.diveType = "Recreational";
        this.plannedDepth = 45;
        Date startDiveTime = new Date();
        Date endDiveTime = new Date();
        endDiveTime.setMinutes(startDiveTime.getMinutes() + 40);
        this.startTime = startDiveTime;
        this.endTime = endDiveTime;
        this.timeSinceLastDive = new HoursAndMinutes(0,30);
        this.timeSinceAlcoholIntake = new HoursAndMinutes(2,1);
        ArrayList<SecurityStop> stops = new ArrayList<>();
        stops.add(new SecurityStop(2, 5));
        stops.add(new SecurityStop(4, 20));
        this.securityStops = stops;
        this.plannedDiveTime = 1337;
        this.tankSize = 10;
        this.startTankPressure = 300;
        this.endTankPressure = 50;
        this.diveGas = "nitrox";
        this.weather = "Cloudy";
        this.tempSurface = 15.7f;
        this.tempWater = 4.7f;
        this.notes = "This is the best note ever. What happens when the info in here " +
                "get really really really long. Does the text jump down or does it go off screen?";
    }


    /**
     * Creates a log object after planning a dive.
     *
     * @params Data about the planned dive.
     */
    public DiveLog(String diver, String guard, String buddy, String location, String diveType, int plannedDepth,
                   HoursAndMinutes lastDive, HoursAndMinutes lastAlcohol, ArrayList<SecurityStop> stops, int plannedTime,
                   int tankSize, int startTankPressure, String diveGas, String weather, float tempSurface, float tempWater, String notes) {

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
        this.current = Current.MEDIUM;
        this.notes = notes;
    }


    /**
     * Set start time of this dive.
     *
     * @param time Start time
     */
    public void setStartTime(Date time) {
        this.startTime = time;
    }


    /**
     * Set end time of this dive.
     *
     * @param time End time
     */
    public void setEndTime(Date time) {
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

    public String getDiver() {
        return diver;
    }

    public String getSurfaceGuard() {
        return surfaceGuard;
    }

    public String getDiveBuddy() {
        return diveBuddy;
    }

    public String getLocation() {
        return location;
    }

    public String getDiveType() {
        return diveType;
    }

    public int getPlannedDepth() {
        return plannedDepth;
    }

    public HoursAndMinutes getTimeSinceLastDive() {
        return timeSinceLastDive;
    }

    public HoursAndMinutes getTimeSinceAlcoholIntake() {
        return timeSinceAlcoholIntake;
    }

    public ArrayList<SecurityStop> getSecurityStops() {
        return securityStops;
    }

    public String getNotes() {
        return notes;
    }

    public int getPlannedDiveTime() {
        return plannedDiveTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getTankSize() {
        return tankSize;
    }

    public int getStartTankPressure() {
        return startTankPressure;
    }

    public int getEndTankPressure() {
        return endTankPressure;
    }

    public String getDiveGas() {
        return diveGas;
    }

    public float getfO2() {
        return fO2;
    }

    public int getO2Percent() {
        return o2Percent;
    }

    public String getWeather() {
        return weather;
    }

    public Current getCurrent() {
        return current;
    }

    public float getTempSurface() {
        return tempSurface;
    }

    public float getTempWater() {
        return tempWater;
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
    public class SecurityStop implements Serializable{

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
    public class HoursAndMinutes implements Serializable{

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
