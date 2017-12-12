package com.example.myapp.mealplanner.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;

import com.example.myapp.mealplanner.Fragment.CreateMenuFrag;
import com.example.myapp.mealplanner.Fragment.CreateNewIngFrag;
import com.example.myapp.mealplanner.Fragment.CreateNewRecipeFrag;
import com.example.myapp.mealplanner.Fragment.FoodTypeTableFrag;
import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;
import com.example.myapp.mealplanner.Fragment.RecipeInsFrag;
import com.example.myapp.mealplanner.Fragment.RecipeListRowFrag;
import com.example.myapp.mealplanner.Fragment.SelectIngFrag;

import java.util.List;

public class CreateMenuFragContainerAct extends AppCompatActivity implements CreateMenuFrag.OnCreateNewMenuRequestListener,
        FoodTypeTableFrag.OnShowFoodViewRequestListener, RecipeListRowFrag.onFragInteractListener,
        CreateNewRecipeFrag.OnFragInteractListener,
        SelectIngFrag.OnFragInteractListener, CreateNewIngFrag.OnFragInteractListener {

    //TODO: rename all the interface of Fragment and method to be better meaning
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        //Set Up Fragment
        manager = getSupportFragmentManager();
        defaultFragment();

        //Toolbar Set Up
        Toolbar mToolbar = findViewById(R.id.toolbar_createMenu_Act);
        setSupportActionBar(mToolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("Create Account");=
    }

    private void defaultFragment() {
        CreateMenuFrag createMenuFrag = new CreateMenuFrag();
        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.add(R.id.frag_createMenu_Act, createMenuFrag, "CreateMenuFrag");
        transaction.addToBackStack("CreateMenuFrag");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.create_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (data != null){
            Bundle bundle = data.getExtras();
            String menuId = bundle.getString("menu_id");
        }*/
    }

    @Override
    public void onCreateNewMenuRequested(com.example.myapp.mealplanner.Object.Menu newMenu, String message) {
        //Execute next Fragment: FoodTypeTableFrag
        FoodTypeTableFrag foodTypeTableFrag = new FoodTypeTableFrag();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.popBackStack("FoodTypeTableFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.replace(R.id.frag_createMenu_Act, foodTypeTableFrag, "FoodTypeTableFrag");
        transaction.addToBackStack("FoodTypeTableFrag");
        transaction.commit();
    }

    @Override
    public void onShowFoodViewRequested(int requestCode) {
        manager = getSupportFragmentManager();
        RecipeListRowFrag recipeListRowFrag = new RecipeListRowFrag();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_createMenu_Act, recipeListRowFrag, "FoodTypeListRowFrag");
        transaction.addToBackStack("FoodTypeListRowFrag");
        transaction.commit();
    }

    @Override
    public void onRecipeItmClicked(Recipe item) {
        Bundle data = new Bundle();
        data.putParcelable("RECIPE", item);

        RecipeInsFrag recipeInsFrag = new RecipeInsFrag();
        recipeInsFrag.setArguments(data);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_createMenu_Act, recipeInsFrag, "RecipeInsFrag");
        transaction.addToBackStack("RecipeInsFrag");
        transaction.commit();
    }

    @Override
    public void onCreateNewRecipeRequest() {
        CreateNewRecipeFrag newRecipeFragment = new CreateNewRecipeFrag();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_createMenu_Act, newRecipeFragment, "AddNewRecipeFrag");
        transaction.addToBackStack("AddNewRecipeFrag");
        transaction.commit();
    }

    @Override
    public void OnAddIngRequest(List<Ingredient> data) {
        //Pass data to fragment
        SelectIngFrag addIngredientsFragment = SelectIngFrag.newInstance(data);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_createMenu_Act, addIngredientsFragment, "selectIngFrag");
        transaction.addToBackStack("selectIngFrag");
        transaction.commit();
    }

    @Override
    public void OnRetrieveIngRequest(List<Ingredient> ingredientCountables) {
        CreateNewRecipeFrag createNewRecipeFrag = (CreateNewRecipeFrag) manager.findFragmentByTag("AddNewRecipeFrag");
        createNewRecipeFrag.addIngDataToFrag(ingredientCountables);
    }

    @Override
    public void OnCreateNewIngRequest() {
        CreateNewIngFrag neIngredientFragment = new CreateNewIngFrag();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_createMenu_Act, neIngredientFragment, "CreateNewIngFrag");
        transaction.addToBackStack("CreateNewIngFrag");
        transaction.commit();
    }

    @Override
    public void OnRecipeCreated() {
        getSupportFragmentManager().popBackStack("RecipeInsFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onAddIngBtnClicked() {
        //Nothing because there is no data need to be passed between these fragment
        //Can pass new IngredientUncountable to the previous fragment, however because this app use Firebase, real-time
        //database, hence the need is reduced
    }
}
