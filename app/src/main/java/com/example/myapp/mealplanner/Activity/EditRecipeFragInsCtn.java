package com.example.myapp.mealplanner.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapp.mealplanner.Fragment.EditRecipeInsFrag;
import com.example.myapp.mealplanner.Fragment.SelectIngFrag;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;

import java.util.List;

public class EditRecipeFragInsCtn extends AppCompatActivity
        implements EditRecipeInsFrag.OnFragmentInteractionListener, SelectIngFrag.OnFragInteractListener {

    private FragmentManager manager;
    private static final int LOG_OUT_MENU_ITEM_ID = View.generateViewId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_frag_ctn);

        manager = getSupportFragmentManager();

        setDefaultFragment();

        Toolbar mToolbar = findViewById(R.id.toolbar_editRecipeFragCtn_Act);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.empty_menu_items, menu);
        menu.add(Menu.NONE, LOG_OUT_MENU_ITEM_ID, Menu.NONE, "Log Out");
        return true;
    }

    // Implement this here because the Act handle general action
    @Override
    protected void onStart() {
        super.onStart();

        //this condition is true if we have called setSupportActionBar(mToolbar)
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // implement this in Activity because the Activity handle general actions but not specific Fragment's Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == LOG_OUT_MENU_ITEM_ID) {
            Toast.makeText(this, "Log Out!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
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
        } else {
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
