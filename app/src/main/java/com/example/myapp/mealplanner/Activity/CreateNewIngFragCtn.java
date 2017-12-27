package com.example.myapp.mealplanner.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapp.mealplanner.Fragment.NewIngFrag;
import com.example.myapp.mealplanner.R;

public class CreateNewIngFragCtn extends AppCompatActivity
        implements NewIngFrag.OnFragInteractListener {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_new_ing_frag_ctn);
        manager = getSupportFragmentManager();

        //Set Up Toolbar
        Toolbar mToolbar = findViewById(R.id.toolbar_createIng_Act);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        defaultFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.empty_menu_items, menu);
        return true;
    }

    private void defaultFragment() {
        NewIngFrag neIngredientFragment = new NewIngFrag();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.createIngFrgCtn_Act, neIngredientFragment, "NewIngFrag");
        transaction.addToBackStack("NewIngFrag");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAddIngBtnClicked() {
        //Finish this Activity and pass return new Ingredient to its previous Activity
        //redundant: getSupportFragmentManager().popBackStack("NewIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        finish(); //this will kill the base Fragment as well

        //Nothing because there is no data need to be passed between these fragment
        //Can pass new IngredientUncountable to the previous fragment, however because this app use Firebase, real-time
        //database, hence the need is reduced
    }
}
