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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.models.DiveLog;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.tasks.DownloadProfileImageTask;
import no.ntnu.diverslogbook.tasks.Timer;
import no.ntnu.diverslogbook.utils.Database;
import no.ntnu.diverslogbook.utils.ImageCache;

/**
 * FIXME: WARNING: THE CODE IN THIS CLASS IS HORRIBLE AND UNMAINTAINABLE DUE TO TIME CONSTRAINTS.
 * A MAJOR OVERHAUL AND REFACTORING IS NEEDED WHEN THE APP IS DEVELOPED FURTHER.
 *
 */
public class DiverListAdapter extends ArrayAdapter<Diver> {

    /**
     * The diver that was last clicked
     */
    private Diver lastClickedDiver = null;

    /**
     * The button that was last clicked
     */
    private ImageButton lastClickedButton = null;

    /**
     * A map with all the timers for the different divers
     */
    private final HashMap<String, Timer> timers = new HashMap<>();


    /**
     * Constructor.
     * Defines the divers that should be displayed in the list
     * @param context The application context
     * @param divers The list of divers to guard
     */
    public DiverListAdapter(@NonNull Context context, List<Diver> divers) {
        super(context, R.layout.listview_diverlist, divers);
    }

    /**
     * {@inheritDoc}
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get or create the view for a list-entry
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_diverlist, parent, false);
        }


        ImageView profileImage = convertView.findViewById(R.id.iv_diverslist_diver_image);
        TextView name = convertView.findViewById(R.id.tv_diverslist_diver_name);
        TextView timeRemaining = convertView.findViewById(R.id.tv_diverslist_time_remaining);
        ImageButton startStopDiveButton = convertView.findViewById(R.id.b_diverslist_start_stop_dive);

        // Load the profile image from cache or web
        Diver diver = getItem(position);
        Bitmap image = ImageCache.get(convertView.getContext(), diver.getId());
        if(image == null) {
            new DownloadProfileImageTask(profileImage, diver.getId()).execute(diver.getProfilePhotoURI());
        } else {
            profileImage.setImageBitmap(image);
        }

        // Set default values
        name.setText(diver.getName());
        timeRemaining.setText("00:00");
        startStopDiveButton.setImageResource(R.drawable.ic_start_dive);
        startStopDiveButton.setTag("start");

        // Create the timer for this diver if not done before
        if(!timers.containsKey(diver.getId())) {
            DiveLog thisLog = getCurrentDiveLog(diver);

            long time = (thisLog != null) ? (thisLog.getPlannedDiveTime() * 60 * 1000) : 0;

            timers.put(diver.getId(), new Timer(timeRemaining, time));
        }

        startStopDiveButton.setOnClickListener(this::onClick);


        return convertView;
    }


    /**
     * When the start-/stop-dive button is clicked
     * @param view The button that is clicked
     */
    private void onClick(View view) {
        ImageButton button = view.findViewById(R.id.b_diverslist_start_stop_dive);

        TextView diver = ((View) view.getParent()).findViewById(R.id.tv_diverslist_diver_name);

        // Find the diver object that corresponds to this diver
        // And store a reference for use in dialog
        for(int i = 0; i < getCount(); i++) {
            Diver current = getItem(i);
            if(current.getName().equals(diver.getText())) {
                this.lastClickedDiver = current;
                break;
            }
        }

        // Store reference to the clicked button
        // for use in dialog
        this.lastClickedButton = button;

        // Start the timer if first time clicked
        toggleButton();
    }

    /**
     * {@inheritDoc}
     * Remove the timers as well, if the diverlist is cleared
     */
    @Override
    public void clear() {
        super.clear();
        timers.clear();
    }

    /**
     * Fired when the guard stops the dive
     * @param dialog The dialog where the button is pressed
     * @param id The id of the button
     */
    private void onStopDive(DialogInterface dialog, int id) {
        // Fetch reference to diver and timer
        Timer timer = timers.remove(this.lastClickedDiver.getId());
        Diver clickedDiver = this.lastClickedDiver;

        // Calculate remaining time and display it. Remove the start/stop button
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

        // Update the log/database
        DiveLog thisLog = getCurrentDiveLog(clickedDiver);
        clickedDiver.getDiveLogs().remove(thisLog);

        Date start = new Date();
        Date end = new Date();
        start.setTime(end.getTime() - (timer.getTotalTime() - remainingTime));

        thisLog.setStartTime(start);
        thisLog.setEndTime(end);
        clickedDiver.getDiveLogs().add(thisLog);


        // FIXME: MAJOR BUG! When updating:
        // The logged in diver is replaced by the diver that is executing the dive
        // And the the objects are restored when the app is restarted,
        // removing the updated information from the database


        //Database.updateDiver(clickedDiver);
    }

    /**
     * Fired if the guard doesn't want to finish the dive
     * @param dialog The dialog where the button is
     * @param id The ID of the button
     */
    private void onContinueDive(DialogInterface dialog, int id){
        Log.d("DiverApp", "Continuing dive: " + timers.size());
        // Add a new timer continuing from where the previous left off
        Timer timer = timers.remove(this.lastClickedDiver.getId());


        timers.put(this.lastClickedDiver.getId(), new Timer(timer.getTextView(), timer.getRemainingMillis()));

        // Start the timer
        toggleButton();
    }


    /**
     * Changes the layout of the button.
     * Starts the tiemr if it's a startbutton.
     * Shows a confirm-dialog of it's a stop-button, and starts the update-process.
     *
     */
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

    /**
     * Fetch the divelog to update
     * @param diver The diver this log is stored under
     * @return The DiveLog or null
     */
    private DiveLog getCurrentDiveLog(Diver diver){
        for(DiveLog log : Database.getDiver(diver.getId()).getDiveLogs()) {
            if(log.getSurfaceGuard().equals(Database.getLoggedInDiver().getName()) &&
                    log.getEndTankPressure() == 0 &&
                    log.getActualDepth() == 0) {
                return log;
            }
        }
        return null;
    }
}
