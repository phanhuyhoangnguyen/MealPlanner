package com.example.myapp.mealplanner.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.myapp.mealplanner.Fragment.NewMenuFrag;
import com.example.myapp.mealplanner.Fragment.FoodTypeTableFrag;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;
import com.example.myapp.mealplanner.Fragment.RecipeInsFrag;
import com.example.myapp.mealplanner.Fragment.RecipeListRowFrag;

public class CreateMenuFragCtn extends AppCompatActivity implements NewMenuFrag.OnFragInteractListener,
        FoodTypeTableFrag.OnFragInteractListener, RecipeListRowFrag.onFragInteractListener,
        RecipeInsFrag.OnFragInteractListener {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_menu);

        // Fragment Set Up
        manager = getSupportFragmentManager();
        defaultFragment();

        // Toolbar Set Up
        Toolbar mToolbar = findViewById(R.id.toolbar_createMenu_Act);
        // set Support Action Bar to allow getSupportActionBar() to modify action bar's
        // component later via provided functions
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate menu layout to this Activity Layout, without this line of code,
        // the Activity Toolbar will be empty just like its layout xml
        getMenuInflater().inflate(R.menu.create_menu_items, menu);
        return true;
    }

    private void defaultFragment() {
        NewMenuFrag newMenuFrag = new NewMenuFrag();
        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.add(R.id.frag_createMenu_Act, newMenuFrag, "NewMenuFrag");
        transaction.addToBackStack("NewMenuFrag");
        transaction.commit();
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
    public void OnEditRecipeRequest(Recipe targetRecipe) {
        Intent editRecipeIns = new Intent(this, EditRecipeFragInsCtn.class);

        Bundle data = new Bundle();
        data.putParcelable("EDIT_RECIPE", targetRecipe);
        editRecipeIns.putExtras(data);

        startActivity(editRecipeIns);
    }

    // todo: Update Food Database according to Country: Japan, Vietnamese, Korean, Thailand...
    // todo: Update with my own menu database
    // todo: Search function based on Countries, city or ingredients, condiments (eggs, black Soy sauce, oyster sauce)
    // todo: Timer widget for cooking preparation
    // todo: Update Ingredient Other Nutrition data: carbs, vitamin A...
    // todo: Recommended nutrition intakes including vital intakes vitamin A, vitamin B,... and bad food limitation intakes.
    //  + Random pick healthy food to add the meal to be fully healthy and nutrition: e.g. banana, nut, Atiso tea...
    // todo: Health Checker Function: allow users to check their symptoms according to their body parts:
    //  e.g. hurt in lower part of belly, make a assumptions based on unhealthy diet check, recommended for food intakes.
    // todo: Organs Improvements: allow users to specific enhance their target body parts/organ including muscles, eyes, livers, kidney...
    //  + Recommended healthy food intakes including banana, vegetable, cranberry, nuts... for extra health benefits.
    //  Recommendation for vital intake (AI for further enhance): no vegetable -> drink or alternative
    //  Supplement Recommendation for healthier life: medicine, fish oil (with correct usage) - advertisements with big brand medicine in future (or pro-edition to unlock)
    //  e.g. Ural
    // todo: Chart: display food nutrition intakes
    // todo: Taking out Food Option
    // todo: Taking out Drink Option
    // todo: Drink Recipe
    // todo: AI: recommended meal set
}
