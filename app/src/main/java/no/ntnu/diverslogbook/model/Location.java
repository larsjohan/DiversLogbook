package no.ntnu.diverslogbook.model;


/**
 * Used to create new and retrieve location objects.
 */
public class Location {

    /**
     * Location id.
     */
    private int id;

    /**
     * Location name.
     */
    private String name;


    /**
     * Create a new location object.
     *
     * @param id Location id
     * @param name Name of location
     */
    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }


    /**
     * Retrieve location id.
     *
     * @return id
     */
    public int getId() {
        return this.id;
    }


    /**
     * Retrieve name of location.
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }
}
