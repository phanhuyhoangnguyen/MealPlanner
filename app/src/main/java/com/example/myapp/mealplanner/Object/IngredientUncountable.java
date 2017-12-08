package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John.nguyen on 16/11/2017.
 */

// Abstract force the children class to implement its own method based on the parent signature
// whereas, normal parent class provide default implementation and allow children to rewrite if they need to.
/*public class IngredientUncountable extends Ingredient {

    public IngredientUncountable(){
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public IngredientUncountable(String name, String calories, String quantity){
        super(name, calories, quantity, "gram");
    }

    public IngredientUncountable(String name, String calories){
        super(name, calories, String.valueOf(100), "gram");
    }

    public static final Parcelable.Creator<IngredientUncountable> CREATOR =
            new Parcelable.Creator<IngredientUncountable>() {
                public IngredientUncountable createFromParcel(Parcel in) {
                    return new IngredientUncountable(in);
                }

                public IngredientUncountable[] newArray(int size) {
                    return new IngredientUncountable[size];
                }
            };

    private IngredientUncountable(Parcel in) {
        setName(in.readString());
        setCalories(in.readString());
        setCalories(in.readString());
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getName());
        out.writeString(getCalories());
        out.writeString(getQuantity());
    }
}*/
