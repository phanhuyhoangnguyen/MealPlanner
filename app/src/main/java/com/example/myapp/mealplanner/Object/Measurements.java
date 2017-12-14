package com.example.myapp.mealplanner.Object;

/**
 * Created by John.nguyen on 15/12/2017.
 */

public class Measurements {
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

    public Measurements(){
        //Default Constructor for Firebase if needed
    }

    public Measurements(String name, String quantity){
        this.name = name;
        this.quantity = quantity;
    }

}
