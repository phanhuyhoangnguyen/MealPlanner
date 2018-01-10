package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John.nguyen on 16/10/2017.
 */

public class Recipe implements Parcelable {

    //TODO: before testing: try to convert to private to see how Firebase react
    //todo: before testing: add author field later
    public String servingYield;

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String img;

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String calories;

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String duration;

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public String instruction;
    public String foodType;
    public String menuType;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setOrigin(String city, String country) {
        this.origin = city.concat(", ").concat(country);
    }

    public String origin;

    public List<IngredientCountable> getIngredientCountable() {
        return ingredientCountable;
    }

    public void setIngredientCountable(List<IngredientCountable> ingredientCountable) {
        this.ingredientCountable = ingredientCountable;
    }

    public List<IngredientCountable> ingredientCountable;     //Test: List<Ingredient> -> List<IngredientCountable>
    /*public HashMap<String, IngredientCountable> ingredientCountable;

    public HashMap<String, String> getIngredientUncountables() {
        return ingredientCountable;
    }

    public void setIngredientUncountables(HashMap<String, String> ingredientCountable) {
        this.ingredientCountable = ingredientCountable;
    }*/

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }


    public Recipe() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public Recipe(String img, String name, String city, String country, String calories, String duration,
                  String servingYield, String instruction, String fType, String mType,
                  List<IngredientCountable> ingredientCountable) {
        setServingYield(servingYield);
        setImg(img);
        setName(name);
        setOrigin(city, country);
        setDuration(duration);
        setCalories(calories);
        setInstruction(instruction);
        setFoodType(fType);
        setMenuType(mType);
        setIngredientCountable(ingredientCountable);
    }

    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>() {
                public Recipe createFromParcel(Parcel in) {
                    return new Recipe(in);
                }

                public Recipe[] newArray(int size) {
                    return new Recipe[size];
                }
            };

    // this will be called and new Object will be created to be passed between Activity, origin object and the object send by Intent
    // are different but not the same one.
    private Recipe(Parcel in) {
        //if variable is missing to be written in here, it will display as null when object is received from target Activity
        setImg(in.readString());
        setName(in.readString());
        setOrigin(in.readString());
        setServingYield(in.readString());
        setFoodType(in.readString());
        setMenuType(in.readString());
        setCalories(in.readString());

        setDuration(in.readString());
        // Alternative 1: Read Type List: in.readTypedList(ingredientCountable, IngredientCountable.CREATOR); // dest.writeTypedList(products);
        // or 2: read Value: in.readValue(this.getClass().getClassLoader()); // parcel.writeValue(**list**);
        ingredientCountable = new ArrayList<>();
        in.readList(ingredientCountable, IngredientCountable.class.getClassLoader()); //this method will pass the list value direct to the list variable
        setInstruction(in.readString());
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getImg());
        out.writeString(getName());
        out.writeString(getOrigin());
        out.writeString(getServingYield());
        out.writeString(getFoodType());
        out.writeString(getMenuType());
        out.writeString(getCalories());

        out.writeString(getDuration());
        out.writeList(getIngredientCountable());
        out.writeString(getInstruction());
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getCalories() {
        return calories;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getServingYield() {
        return servingYield;
    }

    public String changeCalToKjTest(String compare) {
        if (compare.equalsIgnoreCase("cal")) {
            return String.valueOf(Float.valueOf(getCalories()) * (float) 4.184 / 1000).concat(" kj");
        } else {
            return String.valueOf(getCalories()).concat(" Cal");
        }
    }

    public void setServingYield(String servingYield) {
        this.servingYield = servingYield;
    }
}
