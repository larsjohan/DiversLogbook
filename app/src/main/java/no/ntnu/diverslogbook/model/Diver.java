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

    private String id;

    private String name;

    private String email;

    private String phone;


    public Diver(){
        this.id = "";
        this.name = "Unknown";
        this.email = "";
        this.phone = "";
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void copy(Diver source) {
        this.id = source.id;
        this.name = source.name;
        this.email = source.email;
        this.phone = source.phone;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Diver)obj).id);
    }

    @Override
    public String toString() {
        return String.format("Diver (%s) Name: %s, Email: %s, Phone: %s", this.id, this.name, this.email, this.phone);
    }
}
