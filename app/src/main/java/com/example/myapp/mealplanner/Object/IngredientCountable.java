package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by John.nguyen on 07/12/2017.
 */

public class IngredientCountable extends Ingredient {
    public IngredientCountable() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public IngredientCountable(String name, HashMap<String, String> melCal) {
        super(name, melCal);
    }

    public static final Parcelable.Creator<IngredientCountable> CREATOR =
            new Parcelable.Creator<IngredientCountable>() {
                public IngredientCountable createFromParcel(Parcel in) {
                    return new IngredientCountable(in);
                }

                public IngredientCountable[] newArray(int size) {
                    return new IngredientCountable[size];
                }
            };

    private IngredientCountable(Parcel in) {
        setName(in.readString());
        //setMeasurementDict(in.readHashMap(String, String));
        setCalories(in.readString());
        setQuantity(in.readString());
        setMeasure(in.readString());
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getName());
        out.writeString(getCalories());
        out.writeString(getQuantity());
        out.writeString(getMeasure());
    }

    @Override
    public void changeQuantityMeasurement(int currentIndex) {
        /*Set<String> keys = getMeasurementDictMultiMap().keySet();
        for (String key : keys) {
        System.out.println("Key = " + key);
        System.out.println("Values = " + mMap.get(key));
        List<String> list = (List<String>) mMap.get(key);

        b = list.get(0);
        c = list.get(1);
        System.out.println("B : " + b);
        System.out.println("C : " + c);}*/

        int changeValue = currentIndex;
        changeValue += 1;
        //Check value index
        if (changeValue >= getMeasurementDictMultiMap().size()) {
            //reset
            changeValue = 0;
        }
        Collection newValues = (Collection) getMeasurementDictMultiMap().get(String.valueOf(changeValue)); //[1, Gram, 139];

        //Alternative method:  for (Object n: newValues)
        // or ListIterator (able to move backward instead of Iterator: while (it.hasNext()) it.next();
        // or Enumeration: Enumeration values = newValues.elements(); it.hasMoreElements())
        // Display element by element using Iterator
        //List<List<String>> yourList = new ArrayList<>(newValues);
        String[] values = (String[])newValues.toArray(new String[newValues.size()]); //Same with: String[] values = (String[]) newValues.toArray(new String[0]);

        if (values.length == 3){
            setMeasure(values[1]);
            setCalories(values[2]);
        }

        //return [1, Gram, 139];

        //Extra info on Map
        /*for (Map.Entry entry : multiMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " +entry.getValue());
        }*/

    }
}
