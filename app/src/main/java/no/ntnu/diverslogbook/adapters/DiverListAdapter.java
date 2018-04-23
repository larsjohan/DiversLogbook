package no.ntnu.diverslogbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.model.Diver;

public class DiverListAdapter extends ArrayAdapter<Diver> {

    public DiverListAdapter(@NonNull Context context, List<Diver> divers) {
        super(context, R.layout.listview_diverlist, divers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_diverlist, parent, false);
        }


        ImageView profileImage = convertView.findViewById(R.id.iv_diverslist_diver_image);
        TextView name = convertView.findViewById(R.id.tv_diverslist_diver_name);
        TextView timeRemailning = convertView.findViewById(R.id.tv_diverslist_time_remaining);
        ImageButton startStopDiveButton = convertView.findViewById(R.id.b_diverslist_start_stop_dive);

        Diver diver = getItem(position);

        name.setText(diver.getName());
        timeRemailning.setText("00:00");
        startStopDiveButton.setImageResource(R.drawable.ic_start_dive);



        return convertView;
    }

}
