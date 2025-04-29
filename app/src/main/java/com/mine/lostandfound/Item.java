package com.mine.lostandfound;

public class Item {
    private int id;
    private String name, description, dateReported, location, phone, status;

    public Item(int id,
                String name,
                String description,
                String dateReported,
                String location,
                String phone,
                String status) {
        this.id           = id;
        this.name         = name;
        this.description  = description;
        this.dateReported = dateReported;
        this.location     = location;
        this.phone        = phone;
        this.status       = status;
    }

    // getters only:
    public int    getId()            { return id; }
    public String getName()          { return name; }
    public String getDescription()   { return description; }
    public String getDateReported()  { return dateReported; }
    public String getLocation()      { return location; }
    public String getPhone()         { return phone; }
    public String getStatus()        { return status; }
}
