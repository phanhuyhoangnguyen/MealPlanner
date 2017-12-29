package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EditRecipeInsFrag extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EXISTING_RECIPE";

    private Recipe existingRecipe;

    private OnFragmentInteractionListener mFragInteractListener;

    public EditRecipeInsFrag() {
        // Required empty public constructor
    }

    public static EditRecipeInsFrag newInstance(Recipe existingRecipe) {
        EditRecipeInsFrag fragment = new EditRecipeInsFrag();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, existingRecipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            existingRecipe = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // after this method called, this will return the 'view' and link it to getView() method,
        // hence the getView() only able to called after this finished
        View view = inflater.inflate(R.layout.frag_edit_recipe_ins, container, false);

        loadDataFromReceivingRecipe(view);

        Button saveBtn = view.findViewById(R.id.saveBtn_editRecipeIns_Frag);
        //Set up the button to be ready, able to execute the method when it is clicked
        saveBtn.setOnClickListener(mOnClickListener);

        TextView addIngBtn = view.findViewById(R.id.addIngTxtVw_editRecipeIns_Frag);
        addIngBtn.setOnClickListener(mOnClickListener);

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.saveBtn_createNewIng_Frag:
                    saveRecipeInfo();
                    break;

                case R.id.addIngTxtVw_editRecipeIns_Frag:
                    changeIngredientsRequest();
                    break;

                default:
                    break;
            }
        }
    };

    private void loadDataFromReceivingRecipe(View view) {
        EditText nameRecipe = view.findViewById(R.id.nameEditTxt_editRecipeIns_Frag);
        nameRecipe.setText(existingRecipe.getName());

        String origin[] = existingRecipe.getOrigin().replaceAll("\\s+", "").split(",");

        EditText originCityRecipe = view.findViewById(R.id.originCity_editRecipeIns_Frag);
        Spinner originCountryRecipeSpn = view.findViewById(R.id.originCountry_editRecipeIns_Frag);

        originCityRecipe.setText(origin[0]);

        ArrayAdapter<String> countrySpnAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countryListName_array));

        countrySpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        originCountryRecipeSpn.setAdapter(countrySpnAdapter);

        Spinner foodTypeSpn = view.findViewById(R.id.foodTypeSpn_editRecipeIns_Frag);
        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.foodType_array));

        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodTypeSpn.setAdapter(foodTypeAdapter);

        Spinner menuTypeSpn = view.findViewById(R.id.menuTypeSpn_editRecipeIns_Frag);
        ArrayAdapter<String> menuTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.menuItmType_array));

        menuTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuTypeSpn.setAdapter(menuTypeAdapter);

        // Alternative method 1: Using for loop using i to check every value of spinner item,
        // compare the value of every items (item.toString()), the matched value along with its index (i) is return and setSelection(i);
        // or 2:  Using for loop using i to check every value of Adapter item,
        // compare the value of every items (adapter.getItem(index).equals(value))), the matched value along with its index (i) is return and setSelection(i);
        // or 3: Using the array Country Name[] to find index: String[] countryName = getResources().getStringArray(R.array.countryListName_array);
        // mSpn.setSelection(Arrays.asList(countryName).indexOf(value_here));
        // or 4: using List: mSpinner.setSelection(yourList.indexOf("value"));
        // or 5 (Seem to be the best):
        Log.i("Origin", existingRecipe.getOrigin());
        Log.i("Origin Array Length", String.valueOf(origin.length));
        Log.i("Origin Array 0", origin[0]);
        if (origin.length > 1) {
            Log.i("Origin Array", origin[1]);

            String countryName = origin[1];

            if (countryName != null) {
                int spinnerPosition = countrySpnAdapter.getPosition(countryName);         // or: mySpinner.setSelection(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(countryName));
                originCountryRecipeSpn.setSelection(spinnerPosition);
            }
        }

        if (existingRecipe.getFoodType() != null)
            foodTypeSpn.setSelection(foodTypeAdapter.getPosition(existingRecipe.getFoodType()));

        if (existingRecipe.getMenuType() != null)
            menuTypeSpn.setSelection(menuTypeAdapter.getPosition(existingRecipe.getMenuType()));

        TextView foodCal = view.findViewById(R.id.calTxt_editRecipeIns_Frag);
        foodCal.setText(existingRecipe.getCalories());

        EditText servingNo = view.findViewById(R.id.servingNo_editRecipeIns_Frag);
        servingNo.setText(existingRecipe.getServingYield());

        EditText prepDur = view.findViewById(R.id.duration_editRecipeIns_Frag);
        prepDur.setText(existingRecipe.getDuration());

        TextView ingSelected = view.findViewById(R.id.addIngTxtVw_editRecipeIns_Frag);

        EditText ins = view.findViewById(R.id.insBody_editRecipeIns_Frag);
        ins.setText(existingRecipe.getInstruction());

        String ingList = ingSelected.getText().toString();

        for (Ingredient ing : existingRecipe.getIngredientCountable()) {
             ingList = ing.getName().concat(ingList);
        }

        ingSelected.setText(ingList);
    }

    private void saveRecipeInfo() {
        EditText nameRecipe = getView().findViewById(R.id.nameEditTxt_editRecipeIns_Frag);
        EditText originCityRecipe = getView().findViewById(R.id.originCity_editRecipeIns_Frag);
        Spinner originCountryRecipe = getView().findViewById(R.id.originCountry_editRecipeIns_Frag);
        TextView totalCalData = getView().findViewById(R.id.calTxt_editRecipeIns_Frag);

        EditText servings = getView().findViewById(R.id.servingNo_editRecipeIns_Frag);

        EditText prepDur = getView().findViewById(R.id.duration_editRecipeIns_Frag);

        Spinner foodType = getView().findViewById(R.id.foodTypeSpn_editRecipeIns_Frag);
        Spinner menuType = getView().findViewById(R.id.menuTypeSpn_editRecipeIns_Frag);

        EditText ins = getView().findViewById(R.id.insBody_editRecipeIns_Frag);


        if (nameRecipe.getText().toString().isEmpty()) {
            nameRecipe.setError("Please Enter Recipe Name.");
        } else if (originCityRecipe.getText().toString().isEmpty()) {
            originCityRecipe.setError("Please Enter Origin City of Recipe.");
        } else if (originCountryRecipe.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please Select Origin Country of Recipe", Toast.LENGTH_SHORT).show();
        } else if (totalCalData.getText().toString().isEmpty()) {
            Log.i("", "");
            //.setError("Please Add Ingredients to Recipe.");
        } else if (prepDur.getText().toString().isEmpty()) {
            prepDur.setError("Please Enter Preparation Time.");
        } else if (servings.getText().toString().isEmpty()) {
            servings.setError("Please Enter Servings for this recipe");
        } else if (ins.getText().toString().isEmpty()) {
            ins.setError("Please Add Ingredients and Instruction");
        } else if (foodType.getSelectedItemPosition() == 0) {
            ins.setError("Please Select Food Type");
        } else if (menuType.getSelectedItemPosition() == 0) {
            ins.setError("Please Select Menu Type");
        } /*else if (selectedIngList.size() < 1) {
            ins.setError("Please Select Menu Type");
        } */else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes");

            existingRecipe.setName(nameRecipe.getText().toString());
            existingRecipe.setOrigin(originCityRecipe.getText().toString(), originCountryRecipe.getSelectedItem().toString());
            existingRecipe.setCalories(totalCalData.getText().toString());
            existingRecipe.setDuration(prepDur.getText().toString());
            existingRecipe.setServingYield(servings.getText().toString());
            existingRecipe.setInstruction(ins.getText().toString());
            existingRecipe.setFoodType(foodType.getSelectedItem().toString());
            existingRecipe.setMenuType(menuType.getSelectedItem().toString());
            //existingRecipe.setIngredientCountable();

            /*Recipe nRecipe = new Recipe(selectedIngList);

            Map<String, Object> newRecipe = new HashMap<>();
            newRecipe.put(id, nRecipe);
            mDatabase.updateChildren(newRecipe);*/
        }
    }

    private void changeIngredientsRequest() {
        if (mFragInteractListener != null) {
            //Communicate to Activity request to open new Fragment
            mFragInteractListener.OnChangeIngRequest(existingRecipe.getIngredientCountable());
        }
    }

    public void changeIngredients(List<IngredientCountable> newIngs){
        existingRecipe.setIngredientCountable(newIngs);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mFragInteractListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragInteractListener = null;
    }

    public interface OnFragmentInteractionListener {
        void OnChangeIngRequest(List<IngredientCountable> existingList);
    }
}
