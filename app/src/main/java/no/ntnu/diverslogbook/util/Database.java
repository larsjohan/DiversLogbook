package no.ntnu.diverslogbook.util;

import android.widget.ListAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.diverslogbook.model.Diver;

public abstract class Database {

    private static final DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference();

    private static final DatabaseReference DIVERS = DATABASE.child("diver");

    private static final DatabaseReference LOGS = DATABASE.child("logs");

    private static final List<Diver> DIVERLIST = new ArrayList<>();





    public static boolean containsDiver(Diver diver){
        return DIVERLIST.contains(diver);
    }

    public static Diver getDiver(String id) {
        DatabaseReference diverData = DIVERS.child(id);
        return new Diver(
                diverData.getKey(),
                diverData.child("name").toString(),
                diverData.child("email").toString(),
                diverData.child("phone").toString()
        );
    }

    public static List<Diver> getDivers() {
        return DIVERLIST;
    }

    public static void createDiver(Diver diver) {
        DIVERS.push().setValue(diver);
    }


    private class OnDiverDatabaseValueChanged implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot data : dataSnapshot.getChildren()) {
                Diver diver = data.getValue(Diver.class);
                if(!DIVERLIST.contains(diver)){
                    DIVERLIST.add(diver);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
