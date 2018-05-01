package no.ntnu.diverslogbook.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import no.ntnu.diverslogbook.exceptions.DangerousDiveException;
import no.ntnu.diverslogbook.exceptions.TableScopeException;

public class DiveTable {

    public enum Table {
        PADI_IMPERIAL,
        PADI_METRIC,
        NDF_IMPARIAL,
        NDF_METRIC
    }

    private final Table tableType;

    private JSONObject table = null;






    public DiveTable(Context context, Table tableType) {
        this.tableType = tableType;
        loadTable(context.getResources(), context.getPackageName());
    }


    private void loadTable(Resources resources, String packageName){

        final String resourceType = "raw";
        int dataID = -1;

        switch (this.tableType){
            case PADI_METRIC:   dataID = resources.getIdentifier("table_padi_v1_metric", resourceType, packageName);    break;
            case PADI_IMPERIAL: dataID = resources.getIdentifier("table_padi_v1_imperial", resourceType, packageName);  break;
            case NDF_METRIC: break;
            case NDF_IMPARIAL: break;
            default:
                throw new RuntimeException("Can't load table: Undefined Table Type");
        }

        try(InputStream is = resources.openRawResource(dataID)){
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            this.table = new JSONObject(new String(buffer));
        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch the saturation group from the specified dive table for a dive that has
     * no surface interval (first dive of the day)
     * @param depth the depth in m/ft, depending on the specified units
     * @param totalDiveTimeMinutes The total length of the dive in minutes
     * @return The saturation group for this dive: A character, usually A-Z (dependent on the table)
     */
    public String getPreSISaturationGroup(int depth, int totalDiveTimeMinutes){
        try {
            JSONObject tableEntries = this.table.getJSONObject("table");
            JSONArray preSIEntries = tableEntries.getJSONArray("preSI");

            // Find the right depth
            for(int i = 1; i < preSIEntries.length(); i++){
                JSONObject depthColumn = null;
                JSONObject previousEntry = (JSONObject) preSIEntries.get(i - 1);
                JSONObject currentEntry = (JSONObject) preSIEntries.get(i);

                if(depth <= previousEntry.getInt("depth")){
                    depthColumn = previousEntry;
                } else if(depth > previousEntry.getInt("depth") && depth <= currentEntry.getInt("depth")) {
                    depthColumn = currentEntry;
                }


                if(depthColumn != null){
                    JSONArray saturationGroups = depthColumn.getJSONArray("saturation");

                    // Find the right time
                    for(int j = 1; j < saturationGroups.length(); j++) {
                        JSONObject previous = (JSONObject) saturationGroups.get(j - 1);
                        JSONObject current = (JSONObject) saturationGroups.get(j);

                        // Return the group (round time up)
                        if(totalDiveTimeMinutes <= previous.getInt("time")){
                            return previous.getString("group");
                        } else if(totalDiveTimeMinutes > previous.getInt("time") && totalDiveTimeMinutes <= current.getInt("time")){
                            return current.getString("group");
                        }
                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        throw new TableScopeException("Values out of scope for this table");
    }

    /**
     * Given a saturation-group, find the saturation group after a given amount of time.
     * @param startSaturation The saturation group after the dive (before interval)
     * @param intervalHours The interval in hours (not including minutes)
     * @param intervalMinutes The interval in minutes (not including hours)
     * @return The saturation group after the surface interval
     */
    public String getSISaturation(String startSaturation, int intervalHours, int intervalMinutes){

        int siSeconds = intervalHours * 60 + intervalMinutes;

        if(siSeconds < 0) {
            throw new TableScopeException("Negative surface interval is not allowed");
        }

        try {
            // Find SI-array for given start-saturation group
            JSONObject tableEntries = this.table.getJSONObject("table");
            JSONArray si = tableEntries.getJSONArray("SI");

            // Find saturation group after ended interval
            for(int i = 0; i < si.length(); i++) {
                JSONObject current = si.getJSONObject(i);

                if(current.getString("saturationStart").equals(startSaturation)){
                    JSONArray intervals = current.getJSONArray("intervals");
                    Log.d("DiverApp", "Current SI Array:" + current.toString());

                    for(int j = 0; j < intervals.length(); j++) {
                        JSONObject currentInterval = intervals.getJSONObject(j);
                        JSONObject start = currentInterval.getJSONObject("start");
                        JSONObject end = currentInterval.getJSONObject("end");

                        int startH = start.getInt("hours");
                        int startM = start.getInt("minutes");
                        int endH = end.getInt("hours");
                        int endM = end.getInt("minutes");

                        int startSeconds = startH * 60 + startM;
                        int endSeconds = endH * 60 + endM;

                        String saturation = currentInterval.getString("saturationEnd");

                        if(startSeconds <= siSeconds && siSeconds <= endSeconds) {
                            return saturation;
                        } else if (siSeconds > endSeconds && j == intervals.length() - 1){
                            return "A";
                        }
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        throw new TableScopeException("Unable to calculate surface interval with the current input");
    }

        /**
     * Fetch the Saturation group for a continued dive.
     * @param postSISaturation The saturation group for a dive AFTER the surface-interval
     * @param totalDiveTimeMinutes The length of the dive in minutes
     * @param depth The depth of the dive in m/ft (depends on units)
     * @return The saturation group, adjusted for Residual Nitrogen Time (saturation from previous dive)
     */
    public String getPostSISaturationGroup(String postSISaturation, int totalDiveTimeMinutes, int depth) {

        if(totalDiveTimeMinutes < 0 || depth < 0) {
            throw new TableScopeException("Invalid start-values");
        }

        try {
            // Find TBT-array for given Saturation group
            JSONObject table = this.table.getJSONObject("table");
            JSONArray postSI = table.getJSONArray("postSI");

            for(int i = 0; i < postSI.length(); i++) {

                JSONObject currentCol = postSI.getJSONObject(i);

                String startSaturation = currentCol.getString("saturation");
                if(startSaturation.equals(postSISaturation)) {

                    JSONArray tbt = currentCol.getJSONArray("TBT");

                    for(int j = 1; j < tbt.length(); j++) {
                        JSONObject previousTBT = tbt.getJSONObject(j - 1);
                        JSONObject currentTBT = tbt.getJSONObject(j);

                        int previousMaxDepth = previousTBT.getInt("depth");
                        int currentMaxDepth = currentTBT.getInt("depth");

                        if(previousMaxDepth >= depth) {
                            int rnt = previousTBT.getInt("RNT");
                            totalDiveTimeMinutes += rnt;
                            int abt = previousTBT.getInt("ABT");

                            if(totalDiveTimeMinutes > abt) {
                                throw new DangerousDiveException("This dive exceeds the no-decompression limit and should only be performed by licenced divers");
                            }

                            return getPreSISaturationGroup(previousMaxDepth, totalDiveTimeMinutes);
                        } else if (previousMaxDepth < depth && depth <= currentMaxDepth) {
                            int rnt = currentTBT.getInt("RNT");
                            totalDiveTimeMinutes += rnt;
                            int abt = currentTBT.getInt("ABT");

                            if(totalDiveTimeMinutes > abt) {
                                throw new DangerousDiveException("This dive exceeds the no-decompression limit and should only be performed by licenced divers");
                            }

                            return getPreSISaturationGroup(currentMaxDepth, totalDiveTimeMinutes);
                        } else if(tbt.length() - 1 == j && depth > currentMaxDepth) {
                            throw new DangerousDiveException("This dive exceeds the no-decompression limit and should only be performed by licenced divers");
                        }
                    }

                }
            }

            // Find TBT-entry for given depth
            // Add totalDiveTime to RNT (= ABT) and make sure it's not greater than adjusted no-deco time
            // run the calculated time for the given depth through #getPreSISaturation() and return the answer
        } catch (JSONException e) {
            e.printStackTrace();
        }

        throw new TableScopeException("The values for this dive doesn't match any entry in this table");
    }




}
