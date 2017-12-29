package com.example.myapp.mealplanner.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.myapp.mealplanner.Fragment.EditRecipeInsFrag;
import com.example.myapp.mealplanner.Fragment.SelectIngFrag;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;

import java.util.List;

public class EditRecipeFragInsCtn extends AppCompatActivity implements EditRecipeInsFrag.OnFragmentInteractionListener {

    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_frag_ctn);

        manager = getSupportFragmentManager();

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        Bundle dataBundle = getIntent().getExtras();
        if (dataBundle != null) {
            Recipe targetRecipe = (Recipe) dataBundle.getParcelable("EDIT_RECIPE");

            EditRecipeInsFrag editRecipeInsFrag = EditRecipeInsFrag.newInstance(targetRecipe);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frag_editRecipeFrgCtn_Act, editRecipeInsFrag, "editRecipeInsFrag");
            transaction.addToBackStack("editRecipeInsFrag");
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
        transaction.replace(R.id.frag_editRecipeFrgCtn_Act, addIngredientsFragment, "selectIngFrag");
        transaction.addToBackStack("selectIngFrag");
        transaction.commit();
    }
}
