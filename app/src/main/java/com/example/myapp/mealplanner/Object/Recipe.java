package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by John.nguyen on 16/10/2017.
 */

public class Recipe implements Parcelable {

    //TODO: try to convert to private
    public String servingYield;

    public String img;
    public String calories;
    public String duration;
    public String name;
    public String instruction;
    public String foodType;
    public String menuType;

    public List<Ingredient> getIngredientCountables() {
        return ingredientCountable;
    }

    public void setIngredientCountables(List<Ingredient> ingredientCountable) {
        this.ingredientCountable = ingredientCountable;
    }

    public List<Ingredient> ingredientCountable;
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

    public Recipe(String servingYield, String img,
                  String calories, String duration,
                  String name, String instruction,
                  String fType, String mType, List<Ingredient> ingredientCountable) {
        this.servingYield = servingYield;
        this.img = img;
        this.name = name;
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

    public void setServingYield(String servingYield) {
        this.servingYield = servingYield;
    }
}
