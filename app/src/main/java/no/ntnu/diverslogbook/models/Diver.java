package no.ntnu.diverslogbook.models;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a diver/user.
 * Keeps a reference to the authenticated frebase-user nad keeps information that is stored about the user
 *
 * @author Lars Johan
 * @see DiveLog
 */
public class Diver implements Serializable {

    private String id;

    private String name;

    private String email;

    private String phone;

    private ArrayList<DiveLog> diveLogs;

    private String profilePhotoURI;


    public Diver(){
        this.id = "";
        this.name = "Unknown";
        this.email = "";
        this.phone = "";
        this.profilePhotoURI = null;
        this.diveLogs = new ArrayList<>();
    }

    public Diver(final String id, final String name, final String email, final String phoneNumber, final String profilePhotoURI) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phoneNumber;
        this.profilePhotoURI = profilePhotoURI;
        this.diveLogs = new ArrayList<>();
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

    public ArrayList<DiveLog> getDiveLogs() {
        return diveLogs;
    }

    public String getProfilePhotoURI() {
        return profilePhotoURI;
    }

    public void setProfilePhotoURI(String profilePhotoURI) {
        this.profilePhotoURI = profilePhotoURI;
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

    public void setDiveLogs(ArrayList<DiveLog> diveLogs) {
        this.diveLogs = diveLogs;
    }

    public void addDiveLog(DiveLog log) {
        diveLogs.add(log);
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
