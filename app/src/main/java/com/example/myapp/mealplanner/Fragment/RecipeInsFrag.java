package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.mealplanner.Activity.StartActivity;
import com.example.myapp.mealplanner.Object.Ingredient;
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

public class RecipeInsFrag extends Fragment {
    private Recipe recipe;
    private Menu mMenu;
    private DatabaseReference mDatabase;
    private String id;
    private static final int addImgBtnId = View.generateViewId();
    private static final int editRecipe = View.generateViewId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recipe_ins, container, false);

        loadDataToView(view);

        //Toolbar SetUp
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.empty_menu_items, menu);
        menu.add(android.view.Menu.NONE, addImgBtnId, android.view.Menu.NONE, "Add New Recipe").setIcon(R.drawable.ic_playlist_add_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(android.view.Menu.NONE, editRecipe, android.view.Menu.NONE, "Edit Recipe").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack("RecipeInsFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        } else if (id == editRecipe) {
            //this mFragListener variable is ref to Activity -> the method is executed in activity but not this Fragment
            mFragListener.OnEditRecipeRequest(recipe);
            return true;
        } else if (id == addImgBtnId) {
            addMenu(recipe);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loadDataToView(View view) {
        TextView name = view.findViewById(R.id.name_recipeIns_Frag);
        TextView country = view.findViewById(R.id.origin_recipeIns_Frag);
        TextView cal = view.findViewById(R.id.calTxt_recipeIns_Frag);
        TextView duration = view.findViewById(R.id.duration_recipeIns_Frag);
        TextView instruction = view.findViewById(R.id.insBody_recipeIns_Frag);
        TextView type = view.findViewById(R.id.foodType_recipeIns_Frag);
        cal.setOnClickListener(mOnClickListener);

        recipe = getArguments().getParcelable("RECIPE");

        name.setText(recipe.getName());
        country.setText(recipe.getOrigin());
        cal.setText(recipe.getCalories().concat(" Cal"));
        String foodDuration = duration.getText().toString().concat("\n").concat(recipe.getDuration());
        duration.setText(foodDuration);

        String ingAndIns = "Ingredient Required:\n";

        for (Ingredient i : recipe.getIngredientCountable()) {
            ingAndIns = ingAndIns.concat(i.getName()).concat(" x ")
                    .concat(i.getCurrentQuantity().concat(i.getCurrentMeasurement()).concat("\n"));
        }

        ingAndIns = ingAndIns.concat("\n").concat(recipe.getInstruction());

        instruction.setText(ingAndIns);
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
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.calTxt_recipeIns_Frag:
                    //by default, recipe will displayed in Calories
                    TextView calTxtVw = view.findViewById(R.id.calTxt_recipeIns_Frag);
                    String cal[] = calTxtVw.getText().toString().split(" ");
                    if (cal[1] != null) {
                        calTxtVw.setText(recipe.changeCalToKjTest(cal[1]));
                    }

                    /*String cal[] = calTxtVw.getText().toString().split(" ");
                    if (cal[1] != null){
                        if (cal[1].equalsIgnoreCase("Cal")){
                            calTxtVw.setText(String.valueOf(recipe.changeCalToKj(true)).concat(" kj"));
                        }
                        else{
                            calTxtVw.setText(String.valueOf(recipe.changeCalToKj(false)).concat(" Cal"));
                        }
                    }*/
                    break;

                default:
                    break;
            }

        }
    };

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

    private void logOut() {
        Intent startIntent = new Intent(getActivity(), StartActivity.class);
        startActivity(startIntent);
        getActivity().finish();
    }

    private OnFragInteractListener mFragListener;

    public interface OnFragInteractListener {
        void OnEditRecipeRequest(Recipe passRecipe);

    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        try {
            //reference back to its activity
            mFragListener = (OnFragInteractListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onMenuItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragListener = null;
    }
}
