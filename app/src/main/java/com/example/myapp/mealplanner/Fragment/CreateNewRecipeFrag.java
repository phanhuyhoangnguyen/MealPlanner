package com.example.myapp.mealplanner.Fragment;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateNewRecipeFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    private String totalCalData, foodInsData;
    private List<Ingredient> selectedIngList;
    private ArrayList<Recipe> recipeDatabase;

    public CreateNewRecipeFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_new_recipe, container, false);

        //Toolbar SetUp
        setHasOptionsMenu(true);

        Button addRecipe = view.findViewById(R.id.addRecBtn_createNewRecipe_Frag);
        addRecipe.setOnClickListener(mOnClickListener);

        final Spinner fType = view.findViewById(R.id.foodTypeSpin_createNewRecipe_Frag);
        Spinner menuItmType = view.findViewById(R.id.menuItmTypeSpin_createNewRecipe_Frag);

        fType.setOnItemSelectedListener(this);

        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.foodType_array));

        ArrayAdapter<String> menuItmTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.menuItmType_array));

        foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        menuItmTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        fType.setAdapter(foodTypeAdapter);
        menuItmType.setAdapter(menuItmTypeAdapter);

        final EditText prepDuration = view.findViewById(R.id.prepDuInput_createNewRecipe_Frag);
        prepDuration.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String time = prepDuration.getText().toString();
                //TODO: implement ":" if possible
                /*if (time.length() == 2) {
                    time = time.concat(":");
                    prepDuration.setText(time);
                    prepDuration.setSelection(time.length());//using append can reduce the need of this line
                }

                if (time.length() == 3){
                    time = time.replaceFirst(":", "");
                    prepDuration.setText(time);
                    prepDuration.setSelection(time.length());
                }*/
                String time = charSequence.toString();
                if (prepDuration.isFocused() && !time.isEmpty()) {
                    if (!isTimeValid(time)) {
                        prepDuration.setError("Please Input with HH:mm Format");
                        Toast.makeText(getActivity(), time, Toast.LENGTH_SHORT).show();
                    } else
                        prepDuration.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*if (editable.length() == 5) {
                    fType.requestFocus();
                    fType.performClick();
                }*/

            }
        });

        prepDuration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!prepDuration.getText().toString().isEmpty()) {
                    if (hasFocus) {
                        convertTextToTime((EditText) view);
                    } else {
                        displayTimeFormat((EditText) view);
                    }
                }
            }
        });

        retrieveRecipeData();

        return view;
    }

    private boolean isTimeValid(String time) {
        //Check Time only
        String timeOnlyExpression = "([01]?[0-9]|2[0-3]):[0-5]?[0-9]";
        Pattern pattern = Pattern.compile(timeOnlyExpression);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();

        //Check Time with text: 11:00 hour
        /*String expression = "([01]?[0-9]|2[0-3]):[0-5][0-9]\\s?(hours?|min)?";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();*/
    }

    private void displayTimeFormat(EditText prepDuration) {
        String time = prepDuration.getText().toString();
        String timeArray[] = time.split(":");

        if (timeArray.length > 1) {
            int hour = Integer.valueOf(timeArray[0]);
            int min = Integer.valueOf(timeArray[1]);
            //We can also use String.concat
            //time = time.concat(":");

            StringBuilder strB = new StringBuilder("");
            if (hour == 1)
                strB.append(hour).append(" hour ");
            else if (hour > 1) {
                strB.append(hour).append(" hours ");
            }

            if (min > 0) {
                strB.append(min).append(" min");
            }
            prepDuration.setText(strB);
        }
    }

    private void convertTextToTime(EditText prepDuration) {
        String time = prepDuration.getText().toString();
        if (!time.isEmpty()) {
            String[] timeArray = time.split(" ");
            time = "";
            if (timeArray[0].length() == 1) {
                time = String.valueOf(0);
                timeArray[0] = time.concat(timeArray[0]);
            }
            switch (timeArray.length) {
                case 2:
                    time = timeArray[0].concat(":").concat(String.valueOf(0).concat(String.valueOf(0)));
                    break;
                case 4:
                    if (timeArray[2].length() == 1)
                        time = String.valueOf(0);
                    timeArray[2] = time.concat(timeArray[2]);
                    time = timeArray[0].concat(":").concat(timeArray[2]);
                    break;
            }
            prepDuration.setText(time);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final Button addIng = getView().findViewById(R.id.addIngBtn_createNewRecipe_Frag);
        addIng.setOnClickListener(mOnClickListener);

        //setText of Edit Text can't reset on onCreateView, hence must put in onResume
        updateInsWithIngData();
    }

    private void updateInsWithIngData() {
        final EditText foodInsEdit = getView().findViewById(R.id.insInput_createNewRecipe_Frag);
        final TextView calTxt = getView().findViewById(R.id.calInput_createNewRecipe_Frag);

        //Check if there is no existing IngredientUncountable data passed from the other Fragment
        if (foodInsData == null || totalCalData == null) {
            foodInsData = "";
        } else {
            calTxt.setVisibility(View.VISIBLE);
            StringBuilder strB = new StringBuilder("Calories Intake: ");
            calTxt.setText(strB.append(totalCalData));
        }

        //setText of Edit Text can't reset on onCreateView, hence must put in onResume
        foodInsEdit.setText(foodInsData);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.addIngBtn_createNewRecipe_Frag:
                    //mListener: this is variable linked to Activity, this will be executed by Activity but not this Fragment
                    //check if the Activity is implemented this Fragment or not
                    if (mFragInteractListener != null) {
                        //Communicate to Activity request to open new Fragment
                        mFragInteractListener.OnAddIngRequest(selectedIngList);
                    }
                    break;

                case R.id.addRecBtn_createNewRecipe_Frag:
                    createNewRecipe();
                    //addTestRecipeDataToFb();
                    break;
            }
        }
    };

    private void createNewRecipe() {
        EditText recipeName = getView().findViewById(R.id.newRecipe_dishNameEditTxt_frag);
        EditText prepDuration = getView().findViewById(R.id.prepDuInput_createNewRecipe_Frag);
        TextView calTxt = getView().findViewById(R.id.calInput_createNewRecipe_Frag);
        EditText servings = getView().findViewById(R.id.servingNoInput_createNewRecipe_Frag);
        EditText insTxt = getView().findViewById(R.id.insInput_createNewRecipe_Frag);
        Spinner fType = getView().findViewById(R.id.foodTypeSpin_createNewRecipe_Frag);
        Spinner menuItmType = getView().findViewById(R.id.menuItmTypeSpin_createNewRecipe_Frag);
        Button addIng = getView().findViewById(R.id.addIngBtn_createNewRecipe_Frag);


        String id = recipeName.getText().toString().toLowerCase();
        if (TextUtils.isEmpty(recipeName.getText().toString())) {
            recipeName.setError("Enter Dish Name");
        } else if (prepDuration.getText().toString().isEmpty()) {
            prepDuration.setError("Enter Preparation Duration");
        } else if (servings.getText().toString().isEmpty()) {
            servings.setError("Enter Number of Servings");
        } else if (insTxt.getText().toString().isEmpty()) {
            insTxt.setError("Enter Instruction and add IngredientUncountable Required");
        } else if (selectedIngList == null) {
            addIng.setError("Please Add Ingredients!");
            Toast.makeText(getActivity(), "Please Add Ingredients!", Toast.LENGTH_SHORT).show();
        } else {
            if (hasRecipe(id)) {
                recipeName.setError("Same Recipe is already existed");
                Toast.makeText(getActivity(), "Recipe is already existed!", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes");

                Recipe nRecipe = new Recipe(servings.getText().toString(), "test",
                        calTxt.getText().toString(),
                        prepDuration.getText().toString(),
                        recipeName.getText().toString(),
                        insTxt.getText().toString(),
                        fType.getSelectedItem().toString(),
                        menuItmType.getSelectedItem().toString(),
                        selectedIngList);

                Map<String, Object> newRecipe = new HashMap<>();
                newRecipe.put(id, nRecipe);
                mDatabase.updateChildren(newRecipe);

                Toast.makeText(getActivity(), "New Recipe is added!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack("AddNewRecipeFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mFragInteractListener.OnRecipeCreated();
            }
        }
    }

    /*private void addTestRecipeDataToFb() {
        EditText recipeName = getView().findViewById(R.id.newRecipe_dishNameEditTxt_frag);
        String id = recipeName.getText().toString().toLowerCase();
        if (hasRecipe(id)) {
            Toast.makeText(getActivity(), "Recipe is already existed!", Toast.LENGTH_SHORT).show();
        } else {
            //No need for reference database to child id, this is wrong, but need to be checked again
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes").child(id);
            IngredientUncountable ingredientUncountable1 = new IngredientUncountable("Onion", "40");
            IngredientUncountable ingredientUncountable2 = new IngredientUncountable("Lettuce", "15");
            List<IngredientUncountable> ingredientUncountableList = new ArrayList<>();
            ingredientUncountableList.add(ingredientUncountable1);
            ingredientUncountableList.add(ingredientUncountable2);

            Recipe nRecipe = new Recipe("Fried Rice", "test", "48",
                    "40", "Fried Rice", "Search on the Internet for instruction",
                    "Fried Dish", "Entree", ingredientUncountableList);
            Map<String, Object> recipeUpdates = new HashMap<>();
            recipeUpdates.put("Fried Rice", nRecipe);
            mDatabase.updateChildren(recipeUpdates);
            Toast.makeText(getActivity(), "New Recipe is added!", Toast.LENGTH_SHORT).show();
        }
    }*/

    private boolean hasRecipe(String recipeName) {
        if (recipeDatabase != null) {
            for (Recipe recipe : recipeDatabase) {
                if (recipe.getName().equalsIgnoreCase(recipeName))
                    return true;
            }
        }
        return false;
    }

    private void retrieveRecipeData() {
        recipeDatabase = new ArrayList<>();
        recipeDatabase.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipeObject = (Recipe) recipeSnapshot.getValue(Recipe.class);
                    if (recipeObject != null) {
                        recipeDatabase.add(recipeObject);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_createMenu_Act);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("AddNewRecipeFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }


    public void addIngDataToFrag(List<Ingredient> data) {
        //getView only able to called after onCreateView(), you can't use it inside onCreate() or onCreateView() methods of the fragment.
        //This method is called before onCreateView(), getView will always be null

        int totalCal = 0;
        StringBuilder name = new StringBuilder("");

        for (Ingredient i : data) {
            totalCal += Integer.valueOf(i.getDefaultCalories());
            name.append(i.getName()).append(" ").append(i.getQuantity()).append("g").append("\n");
        }

        selectedIngList = data;

        foodInsData = name.toString();
        totalCalData = String.valueOf(totalCal + "kj");
    }

    private OnFragInteractListener mFragInteractListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mFragInteractListener = null;
    }

    public interface OnFragInteractListener {
        void OnAddIngRequest(List<Ingredient> selectedIngList);
        void OnRecipeCreated();
    }

    /*private OnRecipeCreatedListener mOnRecipeCreatedListener;

    public interface OnRecipeCreatedListener {
        void OnRecipeCreated();
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewRecipeFrag.OnFragInteractListener) {
            mFragInteractListener = (OnFragInteractListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddIngListener");
        }

        /*if (context instanceof CreateNewRecipeFrag.OnRecipeCreatedListener) {
            mOnRecipeCreatedListener = (OnRecipeCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeCreatedListener");
        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //String item = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


}
