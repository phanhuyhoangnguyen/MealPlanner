package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private static final int saveImgBtnId = 0;
    private Recipe existingRecipe;

    private String saveIngs;

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

        loadDataToView(view);

        TextView addIngBtn = view.findViewById(R.id.ingSelectedTxtVw_editRecipeIns_Frag);
        addIngBtn.setOnClickListener(mOnClickListener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, saveImgBtnId, Menu.NONE, "Save").setIcon(R.drawable.ic_save_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    // implement this in Fragment instead of Activity because the Activity handle general actions but not specific Fragment's Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

            case saveImgBtnId:
                saveRecipeWithNewInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadDataToView(View view) {
        EditText nameRecipe = view.findViewById(R.id.nameEditTxt_editRecipeIns_Frag);
        nameRecipe.setText(existingRecipe.getName());

        String origin[] = existingRecipe.getOrigin().replaceAll("\\s+", "").split(",");

        EditText originCityRecipe = view.findViewById(R.id.originCity_editRecipeIns_Frag);
        Spinner countryRecipeSpn = view.findViewById(R.id.originCountry_editRecipeIns_Frag);

        originCityRecipe.setText(origin[0]);

        ArrayAdapter<String> countrySpnAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countryListName_array));

        countrySpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryRecipeSpn.setAdapter(countrySpnAdapter);

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
                countryRecipeSpn.setSelection(spinnerPosition);
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

        EditText prepDuration = view.findViewById(R.id.duration_editRecipeIns_Frag);
        prepDuration.setText(existingRecipe.getDuration());

        final TextView ingSelected = view.findViewById(R.id.ingSelectedTxtVw_editRecipeIns_Frag);

        String ingList = ingSelected.getText().toString();

        for (Ingredient ing : existingRecipe.getIngredientCountable()) {
            ingList = ing.getCurrentQuantity().concat(" ")
                    .concat(ing.getCurrentMeasurement()).concat(" x ").concat(ing.getName()).concat("\n").concat(ingList);
        }

        ingSelected.setText(ingList);
        saveIngs = ingList;

        EditText ins = view.findViewById(R.id.insBody_editRecipeIns_Frag);
        ins.setText(existingRecipe.getInstruction());

        ins.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    ingSelected.setText("...");
                } else {
                    displaySelectedIng(saveIngs);
                }
            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ingSelectedTxtVw_editRecipeIns_Frag:
                    TextView ingSelected = view.findViewById(R.id.ingSelectedTxtVw_editRecipeIns_Frag);
                    if (ingSelected.getText().toString().contains("...")) {
                        displaySelectedIng(saveIngs);
                    } else {
                        changeIngredientsRequest();
                    }
                    break;

                default:
                    Log.i("EditRecipeInsFrag", "Default Switch");
                    break;
            }
        }
    };

    private void displaySelectedIng(String saveIngs) {
        TextView ingSelected = getView().findViewById(R.id.ingSelectedTxtVw_editRecipeIns_Frag);
        ingSelected.setText(saveIngs);
    }

    private void changeIngredientsRequest() {
        if (mFragInteractListener != null) {
            //Communicate to Activity request to open new Fragment
            mFragInteractListener.OnChangeIngRequest(existingRecipe.getIngredientCountable());
        }
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
        EditText nameRecipe = getView().findViewById(R.id.nameEditTxt_editRecipeIns_Frag);
        EditText cityRecipe = getView().findViewById(R.id.originCity_editRecipeIns_Frag);
        Spinner countryRecipe = getView().findViewById(R.id.originCountry_editRecipeIns_Frag);
        TextView totalCalData = getView().findViewById(R.id.calTxt_editRecipeIns_Frag);

        EditText servings = getView().findViewById(R.id.servingNo_editRecipeIns_Frag);

        EditText prepDuration = getView().findViewById(R.id.duration_editRecipeIns_Frag);

        Spinner foodType = getView().findViewById(R.id.foodTypeSpn_editRecipeIns_Frag);
        Spinner menuType = getView().findViewById(R.id.menuTypeSpn_editRecipeIns_Frag);

        TextView ingSelected = getView().findViewById(R.id.ingSelectedTxtVw_editRecipeIns_Frag);

        EditText ins = getView().findViewById(R.id.insBody_editRecipeIns_Frag);
        String id = nameRecipe.getText().toString().replaceAll("\\s+", "").toLowerCase();

        if (nameRecipe.getText().toString().isEmpty()) {
            nameRecipe.setError("Please Enter Recipe Name.");
        } else if (cityRecipe.getText().toString().isEmpty()) {
            cityRecipe.setError("Please Enter Recipe's Origin City.");
        } else if (countryRecipe.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please Select Recipe's Original Country", Toast.LENGTH_SHORT).show();
        } else if (totalCalData.getText().toString().isEmpty()) {
            Log.i("", "");
            //.setError("Please Add Ingredients to Recipe.");
        } else if (prepDuration.getText().toString().isEmpty()) {
            prepDuration.setError("Please Enter Preparation Time.");
        } else if (servings.getText().toString().isEmpty()) {
            servings.setError("Please Enter Number of Servings");
        } else if (foodType.getSelectedItemPosition() == 0) {
            ins.setError("Please Select Food Type");
        } else if (menuType.getSelectedItemPosition() == 0) {
            ins.setError("Please Select Menu Type");
        } /*else if (selectedIngList.size() < 1) {
            ins.setError("Please Select Menu Type");
        } */ else if (existingRecipe.getIngredientCountable() == null) {
            ingSelected.setError("Please Add Ingredients!");
            //Toast.makeText(getActivity(), "Please Add Ingredients!", Toast.LENGTH_SHORT).show();
        } else if (ins.getText().toString().isEmpty()) {
            ins.setError("Please Add Ingredients and Instruction");
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes");

            existingRecipe.setName(nameRecipe.getText().toString());
            existingRecipe.setOrigin(cityRecipe.getText().toString(), countryRecipe.getSelectedItem().toString());
            existingRecipe.setServingYield(servings.getText().toString());
            existingRecipe.setFoodType(foodType.getSelectedItem().toString());
            existingRecipe.setMenuType(menuType.getSelectedItem().toString());
            //existingRecipe.setCalories(totalCalData.getText().toString());  //this is update from above

            existingRecipe.setDuration(prepDuration.getText().toString());
            //existingRecipe.setIngredientCountable(); this is update from above
            existingRecipe.setInstruction(ins.getText().toString());

            Map<String, Object> newRecipe = new HashMap<>();
            newRecipe.put(id, existingRecipe);
            mDatabase.updateChildren(newRecipe);

            Toast.makeText(getActivity(), "Recipe is updated!", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack("EditRecipeInsFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mFragInteractListener.OnRecipeModified();
        }
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
