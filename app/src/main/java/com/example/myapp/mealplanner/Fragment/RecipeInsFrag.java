package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.mealplanner.Activity.StartActivity;
import com.example.myapp.mealplanner.Object.Menu;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeInsFrag extends Fragment {
    private Recipe recipe;
    private Menu mMenu;
    private DatabaseReference mDatabase;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recipe_instruction, container, false);

        initializeUI(view);

        //Toolbar SetUp
        setHasOptionsMenu(true);

        return view;
    }

    private void sendUserToStart() {
        Intent startIntent = new Intent(getActivity(), StartActivity.class);
        startActivity(startIntent);
        getActivity().finish();
    }

    private void initializeUI(View view) {
        TextView name = view.findViewById(R.id.name_recipeIns_Frag);
        TextView country = view.findViewById(R.id.origin_recipeIns_Frag);
        TextView cal = view.findViewById(R.id.cal_recipeIns_Frag);
        TextView duration = view.findViewById(R.id.duration_recipeIns_Frag);
        TextView instruction = view.findViewById(R.id.insBody_recipeIns_Frag);
        TextView type = view.findViewById(R.id.foodType_recipeIns_Frag);
        Button addRecipeBtn = view.findViewById(R.id.addRecipedBtn_recipeIns_Frag);

        recipe = getArguments().getParcelable("RECIPE");

        name.setText(recipe.getName());
        country.setText(recipe.getOrigin());
        cal.setText(recipe.getCalories());
        String foodDuration = duration.getText().toString().concat("\n").concat(recipe.getDuration());
        duration.setText(foodDuration);
        instruction.setText(recipe.getInstruction());
        type.setText(recipe.getFoodType().concat("/").concat(recipe.getMenuType()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
        Calendar calendar = Calendar.getInstance();
        id = sdf.format(calendar.getTime());

        mDatabase = FirebaseDatabase.getInstance().getReference("Menu").child(id);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMenu = dataSnapshot.getValue(Menu.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMenu(recipe);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.cookingins_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_createMenu_Act);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("RecipeInsFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addNewDish:
                        return true;

                    default:
                        Log.i("onMenuItemClick", "default");
                        return false;
                }
            }
        });
    }

    private void addMenu(Recipe recipe) {
        if (mMenu != null && mMenu.getId().equalsIgnoreCase(id)) {
            mMenu.getRecipes().add(recipe);
        } else {
            ArrayList<Recipe> recipes = new ArrayList<>();
            recipes.clear();
            recipes.add(recipe);
            mMenu = new Menu(id, recipes);
        }
        mDatabase.setValue(mMenu);

        Toast.makeText(getActivity(), "Recipe added", Toast.LENGTH_SHORT).show();
        BackToSelectFood();
    }

    public void BackToSelectFood() {
        FragmentManager manager = getFragmentManager();
        manager.popBackStack("FoodTypeTableFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public RecipeInsFrag() {
        // Required empty public constructor
    }

    @Override
    public final void onAttach(Context context) {
        //This method avoid to call super.onAttach(context) if I'm not using api 23 or more
        super.onAttach(context);
        /*try {
            //reference back to its activity
            onMenuItemClickListener = (onMenuItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onMenuItemClickListener");
        }*/
    }
}
