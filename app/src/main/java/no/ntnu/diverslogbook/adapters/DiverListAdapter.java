package no.ntnu.diverslogbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.google.firebase.auth.FirebaseUser;

public class DiverListAdapter extends ArrayAdapter<FirebaseUser> {


    public DiverListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
