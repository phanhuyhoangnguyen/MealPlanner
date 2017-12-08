package com.example.myapp.mealplanner.Object;

import java.util.List;

/**
 * Created by John.nguyen on 19/10/2017.
 */

public class Menu {
    public String id;

    public List<Recipe> recipes;
    public Menu(){

    }

    public Menu(String id, List<Recipe> recipes){
        this.id = id;
        this.recipes = recipes;
    }

    public String getId() {
        return id;
    }


    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
