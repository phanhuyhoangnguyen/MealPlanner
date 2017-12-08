package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by John.nguyen on 07/12/2017.
 */

public class IngredientCountable extends Ingredient {
    public IngredientCountable(){
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public IngredientCountable(String name, HashMap<String, String> melCal){
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
    public void changeQuantityMeasurement(int index) {
        Collection<String>
        getMeasurementDict().getCollection()
    }
}
