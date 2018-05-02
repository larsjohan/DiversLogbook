package no.ntnu.diverslogbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.R;

/**
 * This is the adapter for the ListView in LogFragment.
 */
public class LogListAdapter extends ArrayAdapter<DiveLog> {

    /**
     * This should be the length of the number displaying
     * diving number / diving count.
     */
    private static final int NUMBERLENGTH = 4;

    /**
     * Constructor for this ArrayAdapter.
     */
    public LogListAdapter(@NonNull Context context, List<DiveLog> logs) {
        super(context, R.layout.listview_loglist, logs);
    }


    /**
     * This function gets the view so it can be modified.
     *
     * @param position the position of the element
     * @param convertView the view
     * @param parent the parent of the view
     * @return convertview, the view after modification
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if view is recycled.
        if(convertView == null) {
            // Create a new view.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_loglist, parent, false);
        }

        // Get a hold of the TextViews in the view.
        TextView diveNumber = convertView.findViewById(R.id.tv_loglist_diveNumber);
        TextView date = convertView.findViewById(R.id.tv_loglist_date);
        TextView location = convertView.findViewById(R.id.tv_loglist_location);

        // Get the dive log.
        DiveLog diveLog = getItem(position);

        // Convert the dive number from Integer to String.
        String diveNr = Integer.toString(diveLog.getDiveCount());
        // Make the string accurate length.
        diveNr = "#" + zeroPad(diveNr);

        // Set text in the View.
        diveNumber.setText(diveNr);
        date.setText(diveLog.getDate());
        location.setText(diveLog.getLocation());

        // returns view after is has been modified.
        return convertView;
    }


    /**
     * Prepends zeros to a string until number is of NUMBERLENGTH length.
     *
     * @param s string to zero pad.
     * @return {String} s, the zero-padded string.
     */
    private String zeroPad(String s) {

        // This pads 0's until the string is of NUMBERLENGTH length.
        while(s.length() < NUMBERLENGTH) {
            s = "0" + s;
        }

        return s;
    }
}