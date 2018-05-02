package no.ntnu.diverslogbook.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.tasks.DownloadProfileImageTask;
import no.ntnu.diverslogbook.tasks.Timer;
import no.ntnu.diverslogbook.utils.Database;
import no.ntnu.diverslogbook.utils.ImageCache;

public class DiverListAdapter extends ArrayAdapter<Diver> {

    private Diver lastClickedDiver = null;

    private ImageButton lastClickedButton = null;

    private final HashMap<String, Timer> timers = new HashMap<>();


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
        Bitmap image = ImageCache.get(convertView.getContext(), diver.getId());
        if(image == null) {
            new DownloadProfileImageTask(profileImage, diver.getId()).execute(diver.getProfilePhotoURI());
        } else {
            profileImage.setImageBitmap(image);
        }

        name.setText(diver.getName());
        timeRemaining.setText("00:00");
        startStopDiveButton.setImageResource(R.drawable.ic_start_dive);
        startStopDiveButton.setTag("start");

        if(!timers.containsKey(diver.getId())) {
            DiveLog thisLog = getCurrentDiveLog(diver);

            long time = (thisLog != null) ? (thisLog.getPlannedDiveTime() * 60 * 1000) : 0;

            timers.put(diver.getId(), new Timer(timeRemaining, time));
        }

        startStopDiveButton.setOnClickListener(this::onClick);


        return convertView;
    }


    private void onClick(View view) {
        ImageButton button = view.findViewById(R.id.b_diverslist_start_stop_dive);

        TextView diver = ((View) view.getParent()).findViewById(R.id.tv_diverslist_diver_name);

        for(int i = 0; i < getCount(); i++) {
            Diver current = getItem(i);
            if(current.getName().equals(diver.getText())) {
                this.lastClickedDiver = current;
                break;
            }
        }

        this.lastClickedButton = button;

        toggleButton();
    }

    @Override
    public void clear() {
        super.clear();
        timers.clear();
    }

    private void onStopDive(DialogInterface dialog, int id) {
        Log.d("DiverApp", "Stopping dive: " + timers.size());
        Timer timer = timers.remove(this.lastClickedDiver.getId());
        Diver clickedDiver = this.lastClickedDiver;

        long remainingTime = timer.getRemainingMillis() / 1000;
        timer.cancel();
        lastClickedButton.setVisibility(View.GONE);
        String newText = "Done! Time (hh:mm:ss): "
                + (remainingTime / 3600L)
                + ":"
                + (remainingTime / 60L)
                + ":"
                + (remainingTime % 60);
        timer.getTextView().setText(newText);

        Log.d("DiverApp", "Remaining Time: " + remainingTime);

        DiveLog thisLog = getCurrentDiveLog(clickedDiver);
        clickedDiver.getDiveLogs().remove(thisLog);
        clickedDiver.getDiveLogs().add(thisLog);
        Database.updateDiver(clickedDiver);
    }

    private void onContinueDive(DialogInterface dialog, int id){
        Log.d("DiverApp", "Continuing dive: " + timers.size());
        Timer timer = timers.remove(this.lastClickedDiver.getId());


        timers.put(this.lastClickedDiver.getId(), new Timer(timer.getTextView(), timer.getRemainingMillis()));
        toggleButton();
    }


    private void toggleButton(){
        boolean isStartButton = this.lastClickedButton.getTag().equals("start");

        if(isStartButton){
            this.lastClickedButton.setImageResource(R.drawable.ic_stop_black_24dp);
            this.lastClickedButton.setTag("stop");
            timers.get(this.lastClickedDiver.getId()).start();
        } else {
            this.lastClickedButton.setImageResource(R.drawable.ic_start_dive);
            this.lastClickedButton.setTag("start");

            AlertDialog.Builder builder = new AlertDialog.Builder(this.lastClickedButton.getContext());
            builder.setMessage("Do you want to end this dive?")
                    .setPositiveButton("Stop Dive", this::onStopDive)
                    .setNegativeButton("Continue Dive", this::onContinueDive)
                    .create()
                    .show();
        }
    }

    private DiveLog getCurrentDiveLog(Diver diver){
        DiveLog thisLog = null;
        for(DiveLog log : Database.getDiver(diver.getId()).getDiveLogs()) {
            if(log.getSurfaceGuard().equals(Database.getLoggedInDiver().getName()) &&
                    log.getEndTankPressure() == 0 &&
                    log.getActualDepth() == 0) {
                thisLog = log;
            }
        }
    }
}
