package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        //allow Fragment to recreate, redraw, or interact with menu toolbar
        setHasOptionsMenu(true);

        loadDataFromReceivingRecipe(view);

        Button saveBtn = view.findViewById(R.id.saveBtn_editRecipeIns_Frag);
        //Set up the button to be ready, able to execute the method when it is clicked
        saveBtn.setOnClickListener(mOnClickListener);

        TextView addIngBtn = view.findViewById(R.id.addIngTxtVw_editRecipeIns_Frag);
        addIngBtn.setOnClickListener(mOnClickListener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void passIngDataToFrag(List<IngredientCountable> data) {
        //getView only able to called after onCreateView(), you can't use it inside onCreate() or onCreateView() methods of the fragment.
        //This method is called before onCreateView(), getView will always be null

        float totalCal = 0;
        StringBuilder name = new StringBuilder("");

        for (Ingredient i : data) {
            totalCal += Float.valueOf(i.getCurrentCalories());
            name.append(i.getName()).append(" ").append(i.getCurrentQuantity()).append(" ").append(i.getCurrentMeasurement()).append("\n");
        }

        existingRecipe.setIngredientCountable(data);
        existingRecipe.setCalories(String.valueOf(totalCal));
        //foodInsData = "Ingredients Required: \n".concat(name.toString().concat("\n"));
    }

    private void saveRecipeWithNewInfo() {
        EditText recipeName = getView().findViewById(R.id.dishNameEditTxt_createNewRecipe_Frag);
        EditText recipeCity = getView().findViewById(R.id.cityEditTxt_createNewRecipe_Frag);
        Spinner recipeCountry = getView().findViewById(R.id.countrySpn_createNewRecipe_Frag);
        EditText prepDuration = getView().findViewById(R.id.prepDuInput_createNewRecipe_Frag);
        EditText servings = getView().findViewById(R.id.servingNoInput_createNewRecipe_Frag);
        EditText insTxt = getView().findViewById(R.id.insInput_createNewRecipe_Frag);
        Spinner foodCatType = getView().findViewById(R.id.foodTypeSpn_createNewRecipe_Frag);
        Spinner menuItmType = getView().findViewById(R.id.menuItmTypeSpn_createNewRecipe_Frag);
        TextView ingSelected = getView().findViewById(R.id.ingSelected_createNewRecipe_Frag);

        String id = recipeName.getText().toString().replaceAll("\\s+", "").toLowerCase();
        if (TextUtils.isEmpty(recipeName.getText().toString())) {
            recipeName.setError("Enter Dish Name");
        } else if (recipeCity.getText().toString().isEmpty()) {
            recipeCity.setError("Enter Food's Original City");
        } else if (recipeCountry.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please Select Food's Original Country", Toast.LENGTH_SHORT).show();
        } else if (prepDuration.getText().toString().isEmpty()) {
            prepDuration.setError("Enter Preparation Duration");
        } else if (servings.getText().toString().isEmpty()) {
            servings.setError("Enter Number of Servings");
        } else if (foodCatType.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please Select Food Type", Toast.LENGTH_SHORT).show();
        } else if (menuItmType.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please Select Menu Item Type", Toast.LENGTH_SHORT).show();
        } else if (insTxt.getText().toString().isEmpty()) {
            insTxt.setError("Enter Instruction and add IngredientUncountable Required");
        } else if (existingRecipe.getIngredientCountable() == null) {
            ingSelected.setError("Please Add Ingredients!");
            Toast.makeText(getActivity(), "Please Add Ingredients!", Toast.LENGTH_SHORT).show();
        } else {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes");

                Recipe nRecipe = new Recipe("test", recipeName.getText().toString(),
                        recipeCity.getText().toString(), recipeCountry.getSelectedItem().toString(),
                        existingRecipe.getCalories(), prepDuration.getText().toString(),
                        servings.getText().toString(), insTxt.getText().toString(),
                        foodCatType.getSelectedItem().toString(),
                        menuItmType.getSelectedItem().toString(),
                        existingRecipe.getIngredientCountable());
                Map<String, Object> newRecipe = new HashMap<>();
                newRecipe.put(id, nRecipe);
                mDatabase.updateChildren(newRecipe);

                Toast.makeText(getActivity(), "New Recipe is added!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack("AddNewRecipeFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mFragInteractListener.OnRecipeModified();
        }
    }

    // implement this in Fragment instead of Activity because the Activity handle general actions but not specific Fragment's Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

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
        } */ else {
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

    public void changeIngredients(List<IngredientCountable> newIngs) {
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

        void OnRecipeModified();
    }
}
