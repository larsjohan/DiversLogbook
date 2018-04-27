package no.ntnu.diverslogbook.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.diverslogbook.model.DiveLog;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.model.Location;

/**
 * Provides global access to the database.
 * Everything in this class should be references statically, since the class is made to be global,
 * and should therefore not be instanciated.
 *
 * @author Lars Johan
 * @see Diver
 * @see FirebaseDatabase
 * @see DatabaseReference
 */
public abstract class Database {

    /**
     * The object of the logged in diver.
     */
    private static Diver LOGGED_IN_DIVER = null;

    /**
     * The root-element in the database
     */
    private static final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();

    /**
     * The root element for all divers in the database
     */
    private static final DatabaseReference DIVERS = DATABASE.child("diver");

    /**
     * Listener for divers-section of the database
     */
    private static final ValueEventListener DIVERS_LISTENER = new OnDiverDatabaseValueChanged();

    /**
     * The root element for all log-elements in the database
     */
    private static final DatabaseReference LOGS = DATABASE.child("log");

    /**
     * The root element for all log-elements in the database
     */
    private static final DatabaseReference LOCATIONS = DATABASE.child("location");

    /**
     * Listener for divers-section of the database
     */
    private static final ValueEventListener LOCATIONS_LISTENER = new OnLocationDatabaseValueChanged();

    /**
     * A list of locations that is stored in the database
     */
    private static final List<Location> LOCATIONLIST = new ArrayList<>();

    /**
     * A list of divers that is stored in the database
     */
    private static final List<Diver> DIVERLIST = new ArrayList<>();

    /**
     * Registered observers for
     */
    private static final ObserverManager OBSERVER_MANAGER = new ObserverManager();






    /**
     * Check if a diver exists
     * @param diver The diver to check for
     * @return true if the diver exists, false otherwise
     * @see Diver
     */
    public static boolean containsDiver(Diver diver){
        return DIVERLIST.contains(diver);
    }


    /**
     * Get a diver with the specified ID
     * @param id The divers unique ID
     * @return the Diver-instance, or null if not found
     * @see Diver
     */
    public static Diver getDiver(String id) {
        for (Diver diver : DIVERLIST) {
            if (diver.getId().equals(id)) {
                return diver;
            }
        }
        return null;
    }


    /**
     * Get a reference to all divers that has been loaded from the database
     * @return A List of divers
     * @see List
     * @see Diver
     */
    public static List<Diver> getDivers() {
        return DIVERLIST;
    }


    /**
     * Get a reference to all locations that has been loaded from the database
     *
     * @return A List of locations
     * @see List
     * @see Location
     */
    public static List<Location> getLocations() {
        return LOCATIONLIST;
    }

    /**
     * Adds a new location to the database
     *
     * @param location The location to add
     */
    private static void addLocation(Location location){
        LOCATIONS.push().setValue(location);
    }

    /**
     * Create a diver if it doesn't exist
     * @param diver The diver to create
     * @see Diver
     */
    public static void createDiver(Diver diver) {
        if (!containsDiver(diver)) {
            DIVERS.child(diver.getId()).setValue(diver);
        }
    }

    /**
     * Update a diver whenever there's been added a new dive log.
     *
     * @param diver The diver to update
     * @see Diver
     */
    public static void updateDiver(Diver diver) {
        DIVERS.child(LOGGED_IN_DIVER.getId()).setValue(diver);
    }

    /**
     * Return a reference to the diver that is logged in on this device
     * @return The currently logged in diver
     */
    public static Diver getLoggedInDiver() {
        return LOGGED_IN_DIVER;
    }

    public static void setLoggedInDiver(Diver diver) {
        LOGGED_IN_DIVER = diver;
    }


    /**
     * Starts listening for updates in the database
     */
    public static void init(){
        DIVERS.addValueEventListener(DIVERS_LISTENER);
        LOCATIONS.addValueEventListener(LOCATIONS_LISTENER);
    }


    /**
     * Stops listening for updates from the database
     */
    public static void deInit() {
        DIVERS.removeEventListener(DIVERS_LISTENER);
        LOCATIONS.removeEventListener(LOCATIONS_LISTENER);
    }

    public static void registerObserver(Observer observer) {
        OBSERVER_MANAGER.register(observer);
    }

    public static void unregisterObserver(Observer observer) {
        OBSERVER_MANAGER.unregister(observer);
    }

    /**
     * A listener for all changes to the Diver-section in the database.
     *
     * @author Lars Johan
     * @see ValueEventListener
     *
     */
    private static class OnDiverDatabaseValueChanged implements ValueEventListener {

        /**
         * {@inheritDoc}
         * @param dataSnapshot
         */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Diver diver = data.getValue(Diver.class);

                if (!DIVERLIST.contains(diver)) {
                    DIVERLIST.add(diver);
                    OBSERVER_MANAGER.notifyChange(diver);
                } else if (diver.equals(LOGGED_IN_DIVER)) {
                    LOGGED_IN_DIVER = diver;
                }
            }

            if (!DIVERLIST.contains(LOGGED_IN_DIVER)) {
                createDiver(LOGGED_IN_DIVER);
            }
        }

        /**
         * {@inheritDoc}
         * @param databaseError
         */
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("DiveApp", "Unable to load data from database");
        }
    }


    /**
     * A listener for all changes to the location section in the database.
     *
     * @see ValueEventListener
     *
     */
    private static class OnLocationDatabaseValueChanged implements ValueEventListener {

        /**
         * Listen for changes in the stored locations.
         *
         * {@inheritDoc}
         * @param dataSnapshot
         */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Location location = data.getValue(Location.class);

                if (!LOCATIONLIST.contains(location)){
                    LOCATIONLIST.add(location);
                    OBSERVER_MANAGER.notifyChange(location);
                }
            }
        }

        /**
         * {@inheritDoc}
         * @param databaseError
         */
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("DiveApp", "Unable to load data from database");
        }
    }
}
