package com.example.alertapp;

/**
 * Created by kamran on 3/14/2017.
 */

public class ClassListItems
{

    public String id; //Image URL
    public String name; //Name

    public ClassListItems(String name, String img)
    {
        this.id = img;
        this.name = name;
    }

    public String getImg() {
        return id;
    }

    public String getName() {
        return name;
    }
}
