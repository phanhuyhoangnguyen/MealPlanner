package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by John.nguyen on 07/12/2017.
 */

public abstract class Ingredient implements Parcelable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    /*public void setMeasurementDictMultiMap(MultiValueMap<String, String> measurementDictMultiMap) {
        this.measurementDictMultiMap = measurementDictMultiMap;
    }

    public MultiValueMap<String, String> getMeasurementDictMultiMap() {
        return measurementDictMultiMap;
    }*/

    //This can be replaced as HashMap <String, sub HashMap>
    //Or HashMap <String, List <Custom Object>
    //Or HashMap<String, List<String>>(), where List<String> values = new ArrayList<String>();
    //and this Array can hold as many as value if you want, each key of HashMap related to 1 Array
    //Or Array with each index has 2 value: String measurement, String defaultCalories == Custom Array with Custom Object
    //private MultiValueMap<String, String> measurementDictMultiMap; // HashMap is stored as array
    //or LinkedHashMap instead of HashMap for allowing values to be set in order
    //TODO: try to convert to LinkedHashMap
    private HashMap<String, String> measurementDictMultiMap;

    private String defaultCalories;
    private String quantity;
    private String defaultMeasure;


    public Ingredient() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public Ingredient(String name, HashMap<String, String> meCalMap) {
        //TODO: Ingredients are different Calories when under different cooking
        this.name = name;

        String firstKey = meCalMap.keySet().toArray()[0].toString(); //Alternative method: meCalMap.keySet().stream().findFirst().get();
        this.defaultMeasure = firstKey;
        this.defaultCalories = meCalMap.get(firstKey); //Alternative method: meCalMap.values().toArray()[0].toString();

        //this.measurementDictMultiMap = new MultiValueMap<>();
        this.measurementDictMultiMap = meCalMap;
        //Alternative method: using KeySet Iterator (instead of for), EntrySet (instead of keySet)
        /*int i=0;
        for (String key : meCalMap.keySet()) { //for (int i = 0; i < meCalMap.size(); i++){
            measurementDictMultiMap.put(String.valueOf(i), key);
            measurementDictMultiMap.put(String.valueOf(i), meCalMap.get(key));
            i++;
        }*/
        quantity = String.valueOf(1);
    }

    public abstract void changeQuantityMeasurement(String key);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {this.quantity = quantity;}


    public HashMap<String, String> getMeasurementDictMultiMap() {
        return measurementDictMultiMap;
    }

    public void setMeasurementDictMultiMap(HashMap<String, String> measurementDictMultiMap) {
        this.measurementDictMultiMap = measurementDictMultiMap;
    }

    public String getDefaultCalories() {
        return defaultCalories;
    }

    public void setDefaultCalories(String defaultCalories) {
        this.defaultCalories = defaultCalories;
    }

    public String getDefaultMeasure() {
        return defaultMeasure;
    }

    public void setDefaultMeasure(String defaultMeasure) {
        this.defaultMeasure = defaultMeasure;
    }
}
