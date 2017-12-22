package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by John.nguyen on 16/10/2017.
 */

public class Recipe implements Parcelable {

    //TODO: try to convert to private to see how Firebase react
    public String servingYield;

    public String img;
    public String calories;
    public String duration;
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

    public String origin;

    //Even if I didn't wrote code for this, Firebase would still able to create and push these up on Database Cloud
    //Firebase will push everything in this Object onto Cloud, either getter or setter with make up name for a variables and
    //automatically create anothers getter and setter on Database Cloud, this ended up dupplicated data
    public List<IngredientCountable> getIngredientCountable() {
        return ingredientCountable;
    }

    public void setIngredientCountable(List<IngredientCountable> ingredientCountable) {
        this.ingredientCountable = ingredientCountable;
    }

    //Test: List<Ingredient> -> List<IngredientCountable>
    public List<IngredientCountable> ingredientCountable;
    /*public HashMap<String, String> ingredientCountable;

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
        this.servingYield = servingYield;
        this.img = img;
        this.name = name;
        this.origin = city.concat(", ").concat(country);
        this.duration = duration;
        this.calories = calories;
        this.instruction = instruction;
        this.foodType = fType;
        this.menuType = mType;
        this.ingredientCountable = ingredientCountable;
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

    private Recipe(Parcel in) {
        name = in.readString();
        duration = in.readString();
        calories = in.readString();
        instruction = in.readString();
        img = in.readString();
        foodType = in.readString();
        menuType = in.readString();
        servingYield = in.readString();
        //ingredientCountable = in.readList(ingredientCountable, CREATOR);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(duration);
        out.writeString(instruction);
        out.writeString(calories);
        out.writeString(img);
        out.writeString(foodType);
        out.writeString(menuType);
        out.writeString(servingYield);
        //out.writeList(ingredientCountable);
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


    public float changeCalToKj(boolean request) {
        if (request){
            return Float.valueOf(getCalories()) * (float) 4.184;
        }
        else {
            return Float.valueOf(getCalories());
        }
    }

    public String changeCalToKjTest(String compare) {
        if (compare.equalsIgnoreCase("cal")){
            return String.valueOf(Float.valueOf(getCalories())  * (float) 4.184/1000).concat(" kj");
        }
        else {
            return String.valueOf(getCalories()).concat(" Cal");
        }
    }


    public void setServingYield(String servingYield) {
        this.servingYield = servingYield;
    }
}
