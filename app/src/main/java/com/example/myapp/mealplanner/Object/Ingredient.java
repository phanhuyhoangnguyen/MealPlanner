package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by John.nguyen on 07/12/2017.
 */

public abstract class Ingredient implements Parcelable {

    private String name;

    private String currentCalories;
    private String currentQuantity;
    private String currentMeasurement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public void setMeasurementDictMultiMap(MultiValueMap<String, String> measurementDictMultiMap) {
        this.measurementDictMultiMap = measurementDictMultiMap;
    }

    public MultiValueMap<String, String> getMeasurementDictMultiMap() {
        return measurementDictMultiMap;
    }*/

    //This can be replaced as HashMap <String, sub HashMap> : Firebase don't support
    // or HashMap<String, String>: support but only 1 key and 1 value, we sometimes need 2 values
    // or HashMap <String, List <Custom Object>
    // or HashMap<String, List<String>>(), where List<String> values = new ArrayList<String>();
    // and this Array can hold as many as value if you want, each key of HashMap related to 1 Array
    // or Array with each index has 2 value: String measurement, String currentCalories == Custom Array with Custom Object
    // private MultiValueMap<String, String> measurementDictMultiMap; // HashMap is stored as array
    public String getCurrentMeasurement() {
        return currentMeasurement;
    }

    // or LinkedHashMap instead of HashMap for allowing values to be set in order

    void setCurrentMeasurement(String currentMeasurement) {
        this.currentMeasurement = currentMeasurement;
    }

    private HashMap<String, Measurement> measurementDictMultiMap;

    public String getCurrentCalories() {
        return currentCalories;
    }

    public void setCurrentCalories(String currentCalories) {
        this.currentCalories = currentCalories;
    }

    public Ingredient() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public Ingredient(String name, HashMap<String, Measurement> meCal) {
        //TODO: after testing Ingredients are different Calories when under different cooking
        this.name = name;

        String firstKey = meCal.keySet().toArray()[0].toString(); //Alternative method: meCalMap.keySet().stream().findFirst().get();
        this.currentMeasurement = firstKey;
        this.currentCalories = meCal.get(firstKey).getCalories(); //Alternative method: meCalMap.values().toArray()[0].toString();
        this.currentQuantity = meCal.get(firstKey).getQuantity();

        this.measurementDictMultiMap = meCal;
    }

    public abstract void changeQuantityMeasurement(String key);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public String getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(String currentQuantity) {this.currentQuantity = currentQuantity;}

    //The public label allow Firebase to access and get This value to push it up to database,
    //Any data written in this method will be pushed up to server.
    public HashMap<String, Measurement> getMeasurementDictMultiMap() {
        return measurementDictMultiMap;
    }

    public void setMeasurementDictMultiMap(HashMap<String, Measurement> measurementDictMultiMap) {
        this.measurementDictMultiMap = measurementDictMultiMap;
    }
}
