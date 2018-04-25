package no.ntnu.diverslogbook.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.diverslogbook.model.Diver;

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
     * The GUID of the logged in user
     */
    private static String LOGGED_IN_DIVER_GUID = "";

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
    private static final DatabaseReference LOGS = DATABASE.child("logs");

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
    public static Diver getDiver(@NotNull String id) {
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
     * Create a diver if it doesn't exist
     * @param diver The diver to create
     * @see Diver
     */
    public static void createDiver(Diver diver) {
        if(!containsDiver(diver)) {
            DIVERS.child(diver.getId()).setValue(diver);
        }
    }

    /**
     * Return a reference to the diver that is logged in on this device
     * @return The currently logged in diver
     */
    public static Diver getLoggedInDiver() {
        return getDiver(LOGGED_IN_DIVER_GUID);
    }

    public static String getLoggedInDiverGuid(){
        return LOGGED_IN_DIVER_GUID;
    }

    public static void setLoggedInDiver(String guid) {
        LOGGED_IN_DIVER_GUID = guid;
    }


    /**
     * Starts listening for updates in the database
     */
    public static void init(){
        DIVERS.addValueEventListener(DIVERS_LISTENER);
    }

    /**
     * Stops listening for updates from the database
     */
    public static void deInit() {
        DIVERS.removeEventListener(DIVERS_LISTENER);
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
