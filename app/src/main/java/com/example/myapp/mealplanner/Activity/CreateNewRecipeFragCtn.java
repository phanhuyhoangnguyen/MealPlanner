package com.example.myapp.mealplanner.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.myapp.mealplanner.Fragment.NewRecipeFrag;
import com.example.myapp.mealplanner.Fragment.SelectIngFrag;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.R;

import java.util.List;

public class CreateNewRecipeFragCtn extends AppCompatActivity
        implements SelectIngFrag.OnFragInteractListener, NewRecipeFrag.OnFragInteractListener {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_new_recipe_frag_ctn);

        //Set Up Fragment
        manager = getSupportFragmentManager();
        defaultFragment();

        //Toolbar Set Up
        Toolbar mToolbar = findViewById(R.id.toolbar_createMenu_Act);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.empty_menu_items, menu);
        //change menu will be handle in Fragment, alternative method: change method from Activity
        //TODO: establish menu from here and able to move back to its previous Activity
        return true;
    }

    private void defaultFragment() {
        NewRecipeFrag newRecipeFragment = new NewRecipeFrag();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.createRecipeFrgCtn_Act, newRecipeFragment, "AddNewRecipeFrag");
        transaction.addToBackStack("AddNewRecipeFrag");
        transaction.commit();
    }

    @Override
    public void OnAddIngRequest(List<IngredientCountable> data) {
        //Pass data to fragment
        SelectIngFrag addIngredientsFragment = SelectIngFrag.newInstance(data);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.createRecipeFrgCtn_Act, addIngredientsFragment, "selectIngFrag");
        transaction.addToBackStack("selectIngFrag");
        transaction.commit();
    }

    @Override
    public void OnFinishedAddingIng(List<IngredientCountable> ingredientCountable) {
        //After finished selecting Ingredients, the Fragment kills itself and locate existing Fragment and pass this value to it
        NewRecipeFrag newRecipeFrag = (NewRecipeFrag) manager.findFragmentByTag("AddNewRecipeFrag");
        newRecipeFrag.passIngDataToFrag(ingredientCountable);
    }

    @Override
    public void OnCreateNewIngRequest() {
        Intent createNewIngIntent = new Intent(CreateNewRecipeFragCtn.this, CreateNewIngFragCtn.class);
        //This suppose to be startActivityForResult() and return new Ingredient to this Activity
        startActivity(createNewIngIntent);
    }

    @Override
    public void OnRecipeCreated() {
        //Pop back to the base fragments: Select Recipe: RecipeListRowFrag and finish this Activity
        //redundant: getSupportFragmentManager().popBackStack("RecipeInsFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        finish(); //this will kill the base Fragment as well
    }
}
