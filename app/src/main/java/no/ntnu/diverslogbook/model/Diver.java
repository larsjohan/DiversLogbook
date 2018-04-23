package no.ntnu.diverslogbook.model;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
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
public class Diver {

    private final String id;

    private final String name;

    private final String email;

    private final String phone;

    public Diver(final String id, final String name, final String email, final String phonenumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phonenumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
