package com.example.myapp.mealplanner.Object;

/**
 * Created by John.nguyen on 15/12/2017.
 */

public class Measurement {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    private String calories;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private String quantity;

    public Measurement(){
        //Default Constructor for Firebase if needed
    }

    public Measurement(String name, String quantity){
        setName(name);
        setQuantity(quantity);
        setCalories("Need to Update");
    }

    public Measurement(String name, String quantity, String calories){
        this.name = name;
        this.quantity = quantity;
        this.calories = calories;
    }

}
