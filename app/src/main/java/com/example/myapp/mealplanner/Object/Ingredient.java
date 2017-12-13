package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.collections4.map.MultiValueMap;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

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

    public void setMeasurementDictMultiMap(MultiValueMap<String, String> measurementDictMultiMap) {
        this.measurementDictMultiMap = measurementDictMultiMap;
    }

    public MultiValueMap<String, String> getMeasurementDictMultiMap() {
        return measurementDictMultiMap;
    }

    //This can be replaced as HashMap <String, sub HashMap>
    //Or HashMap <String, List <Custom Object>
    //Or HashMap<String, List<String>>(), where List<String> values = new ArrayList<String>();
    //and this Array can hold as many as value if you want, each key of HashMap related to 1 Array
    //Or Array with each index has 2 value: String measurement, String calories == Custom Array with Custom Object
    private MultiValueMap<String, String> measurementDictMultiMap;

    private String calories;

    private String quantity;

    private String measure;


    public Ingredient() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public Ingredient(String name, HashMap<String, String> meCalMap) {
        //TODO: Ingredients are different calories when under different cooking
        this.name = name;
        this.measurementDictMultiMap = new MultiValueMap<>();

        String firstKey = meCalMap.keySet().toArray()[0].toString(); //Alternative method: meCalMap.keySet().stream().findFirst().get();
        this.measure = firstKey;
        this.calories = meCalMap.get(firstKey); //Alternative method: meCalMap.values().toArray()[0].toString();

        //Alternative method: using KeySet Iterator (instead of for), EntrySet (instead of keySet)
        int i=0;
        for (String key : meCalMap.keySet()) { //for (int i = 0; i < meCalMap.size(); i++){
            measurementDictMultiMap.put(String.valueOf(i), key);
            measurementDictMultiMap.put(String.valueOf(i), meCalMap.get(key));
            i++;
        }



        quantity = String.valueOf(1);
    }

    public abstract void changeQuantityMeasurement(int index);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {this.quantity = quantity;}

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
