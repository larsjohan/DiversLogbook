package no.ntnu.diverslogbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.tasks.Timer;

public class DiverListAdapter extends ArrayAdapter<Diver> {


    private static final HashMap<String, Timer> timers = new HashMap<>();


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
        TextView timeRemaining = convertView.findViewById(R.id.tv_diverslist_time_remaining);
        ImageButton startStopDiveButton = convertView.findViewById(R.id.b_diverslist_start_stop_dive);

        Diver diver = getItem(position);

        name.setText(diver.getName());
        timeRemaining.setText("00:00");
        startStopDiveButton.setImageResource(R.drawable.ic_start_dive);
        startStopDiveButton.setTag("start");

        if(!timers.containsKey(diver.getName())) {
            timers.put(diver.getName(), new Timer(timeRemaining, 10000L));
        }

        startStopDiveButton.setOnClickListener(this::onClick);


        return convertView;
    }


    private void onClick(View view) {
        ImageButton button = view.findViewById(R.id.b_diverslist_start_stop_dive);
        boolean isStartButton = button.getTag().equals("start");

        TextView diver = ((View) view.getParent()).findViewById(R.id.tv_diverslist_diver_name);
        String diverName = (String) diver.getText();

        Timer timer = timers.get(diverName);
        Log.d("DiverApp", "TimerSizeStart:" + timers.size());

        if(isStartButton){
            button.setImageResource(R.drawable.ic_stop_black_24dp);
            button.setTag("stop");
            timer.start();
        } else {
            button.setImageResource(R.drawable.ic_start_dive);
            button.setTag("start");
            timer.cancel();

            if(timer.getRemainingMillis() > 0){
                timers.remove(diverName);
                timers.put(diverName, new Timer(timer.getTextView(), timer.getRemainingMillis()));
            } else {
                timers.remove(diverName);
            }
        }
        Log.d("DiverApp", "TimerSizeStop:" + timers.size());
    }

}
