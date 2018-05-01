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

public class LogListAdapter extends ArrayAdapter<DiveLog> {

    private static final int NUMBERLENGTH = 4;


    public LogListAdapter(@NonNull Context context, List<DiveLog> logs) {
        super(context, R.layout.listview_loglist, logs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_loglist, parent, false);
        }


        TextView diveNumber = convertView.findViewById(R.id.tv_loglist_diveNumber);
        TextView date = convertView.findViewById(R.id.tv_loglist_date);
        TextView location = convertView.findViewById(R.id.tv_loglist_location);

        DiveLog diveLog = getItem(position);

        String diveNr = Integer.toString(diveLog.getDiveCount());
        diveNr = "#" + zeroPad(diveNr);
        diveNumber.setText(diveNr);
        date.setText(diveLog.getDate());
        location.setText(diveLog.getLocation());


        /** Mock data */
       /* DiveLog diveLog = getItem(position);
        String diveNr = "1";
        diveNumber.setText("#" + zeroPad(diveNr));
        date.setText("2018-04-25");
        location.setText("Langesund, Norway");*/
        /**           */





        return convertView;
    }


    /**
     * Prepends zeros to a string until number is of NUMBERLENGTH length.
     *
     * @param s string to zero pad.
     * @return {String} s, the zero-padded string.
     */
    private String zeroPad(String s) {

        while(s.length() < NUMBERLENGTH) {
            s = "0" + s;
        }

        return s;
    }
}