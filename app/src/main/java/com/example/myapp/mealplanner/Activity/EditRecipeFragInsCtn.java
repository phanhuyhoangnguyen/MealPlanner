package com.example.myapp.mealplanner.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapp.mealplanner.Fragment.EditRecipeInsFrag;
import com.example.myapp.mealplanner.Fragment.NewRecipeFrag;
import com.example.myapp.mealplanner.Fragment.SelectIngFrag;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;

import java.util.List;

public class EditRecipeFragInsCtn extends AppCompatActivity
        implements EditRecipeInsFrag.OnFragmentInteractionListener, SelectIngFrag.OnFragInteractListener{

    private FragmentManager manager;
    private static final int MENU_ITEM_ITEM1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_frag_ctn);

        manager = getSupportFragmentManager();

        setDefaultFragment();

        Toolbar mToolbar = findViewById(R.id.toolbar_editRecipeFragCtn_Act);
        //set Support Action Bar to allow modify its component
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);

        //Inflate menu layout to this Activity Layout, without this line of code, the Activity Toolbar will be empty just like its layout xml
        getMenuInflater().inflate(R.menu.empty_menu_items, menu);
        menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "Log Out");
        //change menu will be handle in Fragment, alternative method: change method from Activity
        return true;
    }

    // Implement this here because the Act handle general action
    @Override
    protected void onStart() {
        super.onStart();

        //this condition is true if we have called setSupportActionBar(mToolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // implement this in Activity because the Activity handle general actions but not specific Fragment's Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //todo: check this later, be careful with the same id with fragment, this may call problem
            case MENU_ITEM_ITEM1:
                Toast.makeText(this, "Log Out!", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setDefaultFragment() {
        Bundle dataBundle = getIntent().getExtras();
        if (dataBundle != null) {
            Recipe targetRecipe = (Recipe) dataBundle.getParcelable("EDIT_RECIPE");

            EditRecipeInsFrag editRecipeInsFrag = EditRecipeInsFrag.newInstance(targetRecipe);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frag_editRecipeFrgCtn_Act, editRecipeInsFrag, "EditRecipeInsFrag");
            transaction.addToBackStack("EditRecipeInsFrag");
            transaction.commit();
        }
        else {
            Log.i("EditRecipeFragInsCtn", "dataBundle is null");
        }

    }

    @Override
    public void OnChangeIngRequest(List<IngredientCountable> existingList) {
        SelectIngFrag addIngredientsFragment = SelectIngFrag.newInstance(existingList);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_editRecipeFrgCtn_Act, addIngredientsFragment, "SelectIngFrag");
        transaction.addToBackStack("SelectIngFrag");
        transaction.commit();
    }

    @Override
    public void OnRecipeModified() {
        finish(); //this will kill the base Fragment as well
    }

    @Override
    public void OnFinishedAddingIng(List<IngredientCountable> ingredientCountable) {
        //After finished selecting Ingredients, the Fragment kills itself and locate existing Fragment and pass this value to it
        EditRecipeInsFrag editRecipeFrag = (EditRecipeInsFrag) manager.findFragmentByTag("EditRecipeInsFrag");
        editRecipeFrag.passIngDataToFrag(ingredientCountable);
    }

    @Override
    public void OnCreateNewIngRequest() {
        //This suppose to be startActivityForResult() and return new Ingredient to this Activity
        startActivity(new Intent(this, CreateNewIngFragCtn.class));
    }
}
