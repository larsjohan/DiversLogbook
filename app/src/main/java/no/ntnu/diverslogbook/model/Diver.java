package no.ntnu.diverslogbook.model;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.diverslogbook.DiveLog;


/**
 * This class represents a diver/user.
 * Keeps a reference to the authenticated frebase-user nad keeps information that is stored about the user
 *
 * @author Lars Johan
 * @see no.ntnu.diverslogbook.DiveLog
 */
public class Diver implements Serializable{



    private final FirebaseUser user;

    private final String name;

    private Bitmap profileImage;

    private List<DiveLog> logs;




    public Diver(final FirebaseUser user) {
        this.user = user;

        this.name = this.user.getDisplayName();

        this. profileImage = null;

        this.logs = new ArrayList<>();
    }


}
