package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.Object.Measurement;
import com.example.myapp.mealplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NewIngFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String newCalInputTag = "newCalInput";
    private static final String newSpinnerTag = "newSpinner";
    private EditText newCalInput;
    private Spinner newSpinner;
    private int measurementInput = 1;

    private String mParam1;
    private String mParam2;

    private Map<String, Measurement> measurementsMap;

    private OnFragInteractListener mFragInteractListener;

    public NewIngFrag() {
        // Required empty public constructor
    }

    public static NewIngFrag newInstance(String param1, String param2) {
        NewIngFrag fragment = new NewIngFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_ing, container, false);

        retrieveData();

        //Toolbar SetUp
        setHasOptionsMenu(true);

        //push HashMap of Multi-Measurement Dictionary Database to server
        //pushMultiMeasureDict();

        TextView gramsLabel = view.findViewById(R.id.measurementTxtVw_createNewIng_Frag);
        gramsLabel.setText(R.string.gram);

        Button saveIngInfoBtn = view.findViewById(R.id.saveBtn_createNewIng_Frag);
        saveIngInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewIng();
                //onAddIngBtnClicked();
            }
        });

        return view;
    }

    private void pushMultiMeasureDict() {
        Map<String, Object> MultiMeasureDict = new HashMap<>();
        MultiMeasureDict.put("Countable (1, 2…)", new Measurement("Countable (1, 2…)", "1"));
        MultiMeasureDict.put("Cup (C)", new Measurement("Cup (C)", "1"));
        //MultiMeasureDict.put("Custom", new Measurement("Custom (anything)", "1"));
        MultiMeasureDict.put("Drop (dr)", new Measurement("Drop (dr)", "1"));
        MultiMeasureDict.put("Gallon (gal)", new Measurement("Gallon (gal)", "1"));
        MultiMeasureDict.put("Gram (g)", new Measurement("Gram (g)", "100"));
        MultiMeasureDict.put("Litre (l)", new Measurement("Litre (l)", "1"));
        MultiMeasureDict.put("Ounces (oz)", new Measurement("Ounces (oz)", "1"));
        MultiMeasureDict.put("Pinch (pn)", new Measurement("Pinch (pn)", "1"));
        MultiMeasureDict.put("Pint (pt)", new Measurement("Pint (pt)", "1"));
        MultiMeasureDict.put("Pottle (pot)", new Measurement("Pot (pot)", "1"));
        MultiMeasureDict.put("Quart (qt)", new Measurement("Quart (qt)", "1"));
        MultiMeasureDict.put("Tablespoon (tbs)", new Measurement("Tablespoon (tbs)", "1"));
        MultiMeasureDict.put("Teaspoon (tps)", new Measurement("Teaspoon (tps)", "1"));
        MultiMeasureDict.put("ml", new Measurement("ml", "100"));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Measurements");
        mDatabase.updateChildren(MultiMeasureDict);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Alternative method: CustomArrayAdapter with ListView and Custom Sub Object containing only: calories and measurement
        final Spinner measurementNoSpinner = getView().findViewById(R.id.measurementNoSpn_createNewIng_Frag);

        ArrayAdapter<String> measurementNoInputAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.measurementNo_array));
        measurementNoInputAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        measurementNoSpinner.setAdapter(measurementNoInputAdapter);

        measurementNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Check and handling when user re-selecting spinner: hidden all the unnecessary rows
                //Alternative method: while (it.hasNext()) instead of "measurementNoSpinner.getAdapter().getCount()"
                measurementInput = 1;
                measurementInput += position;

                for (int i = 0; i <= measurementNoSpinner.getAdapter().getCount(); i++) {

                    if (i < position) {
                        newCalInput = getView().findViewWithTag(newCalInputTag.concat(String.valueOf(i + 1)));
                        newSpinner = getView().findViewWithTag(newSpinnerTag.concat(String.valueOf(i + 1)));

                        //check if 1st, 2nd row are existed
                        if (newCalInput == null) {
                            createNewInputRows(i);
                        } else {
                            newCalInput.setVisibility(View.VISIBLE);
                            newSpinner.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // i == index of Next
                        Spinner nextSpinner = getView().findViewWithTag(newSpinnerTag.concat(String.valueOf(i + 1)));
                        EditText nextCalInput = getView().findViewWithTag(newCalInputTag.concat(String.valueOf(i + 1)));

                        if (nextCalInput != null && nextSpinner != null) {
                            //This easier than delete: set null, remove view...
                            nextSpinner.setVisibility(View.GONE);
                            nextCalInput.setVisibility(View.GONE);
                        } else {
                            //Turn off Switch for For Loop
                            i = measurementNoSpinner.getAdapter().getCount() + 1;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createNewInputRows(int id) {
        //Handling when user first select number of measurement inputs: create new rows
        newCalInput = new EditText(getActivity());
        newSpinner = new Spinner(getActivity());

        //Alternative method: id doesn't have to be unique, can get the view using SetTag
        //Alternative method 2: using Google API generateViewId()
        //Alternative method 3: using external additional file in res/values/ids.xml
        newCalInput.setTag("newCalInput".concat(String.valueOf(id + 1)));
        newSpinner.setTag("newSpinner".concat(String.valueOf(id + 1)));

        LinearLayout linearContainer = getView().findViewById(R.id.lnLayoutContainerSpn_createNewIng_Frag);
        //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear.getLayoutParams();

        LinearLayout newLinearIndividually = new LinearLayout(getActivity());
        newLinearIndividually.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.weight = 1f;
        newCalInput.setLayoutParams(params);

        params.weight = 1f;
        newSpinner.setLayoutParams(params);

        //Alternative method: retrieve data from Firebase instead of using external array in res folder:
        // String[] measurementNameArrays = getResources().getStringArray(R.array.volumeMeasurement_array);

        String[] measurementNameArrays = measurementsMap.keySet().toArray(new String[measurementsMap.keySet().size()]);

        //Alternative method: Collection.sort(measurementNameArrays);
        Arrays.sort(measurementNameArrays);

        ArrayAdapter<String> newSpAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, measurementNameArrays);
        newSpAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        newSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    String[] itemTags = newSpinner.getSelectedItem().toString().split(" ");
                    String hints = String.valueOf("Enter Calories per ");
                    if (position == 4) {
                        newCalInput.setHint(hints.concat("100g"));
                    } else
                        newCalInput.setHint(String.valueOf("Enter Calories per ").concat(itemTags[0]));
                } else
                    newCalInput.setHint("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        newSpinner.setAdapter(newSpAdapter);
        newSpinner.setSelection(id);

        newLinearIndividually.addView(newCalInput);
        newLinearIndividually.addView(newSpinner);
        linearContainer.addView(newLinearIndividually);
    }

    private void createNewIng() {
        TextInputLayout ingName = getView().findViewById(R.id.ingNameInput_createNewIng_Frag);
        TextInputLayout ingCal = getView().findViewById(R.id.ingCalInput_createNewIng_Frag);

        //TODO: fix all these warning
        //TODO: put format constraint for name to be camelCase
        String name = ingName.getEditText().getText().toString();
        String cal = ingCal.getEditText().getText().toString();
        TextView gramsLabel = getView().findViewById(R.id.measurementTxtVw_createNewIng_Frag);

        if (!hasIng(name)) {
            if (name.isEmpty()) {
                ingName.setError("Please Enter Ingredient Name!");
            } else if (cal.isEmpty()) {
                ingName.setError("Please Enter Ingredient Calories!");
            } else {
                Ingredient newIng;
                //Alternative method: using List instead of HashMap<String, Measurement>
                HashMap<String, Measurement> melCal = new HashMap<>();
                Measurement measurement = measurementsMap.get(gramsLabel.getText().toString());
                Log.i( "measurementsMap Size: ", String.valueOf(measurementsMap.size()));
                for (String i: measurementsMap.keySet()){
                    Log.i("Key Set", i);
                    Log.i("search: ", gramsLabel.getText().toString());
                    if (i.equalsIgnoreCase(gramsLabel.getText().toString())){
                        measurement = measurementsMap.get(gramsLabel.getText().toString());
                        Log.i("Matched: ", "add");
                    }
                }
                if (measurement != null) {
                    if (measurement.getCalories() != null) {
                        measurement.setCalories(cal);
                    } else {
                        Toast.makeText(getActivity(), "Calories is null", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("Measurement","is null");
                }
                melCal.put(measurement.getName(), measurement);

                for (int i = 1; i < measurementInput; i++){
                    Spinner newSpinner = getView().findViewWithTag(newSpinnerTag.concat(String.valueOf(i)));
                    EditText newInputCal = getView().findViewWithTag(newCalInputTag.concat(String.valueOf(i)));

                    if (newSpinner == null || newInputCal == null){
                        Log.i("null found", ""+i);
                    }
                    else {
                        if (newInputCal.getText().toString().equals("")) {
                            newInputCal.setError("Please Enter Ingredient Calories!");
                            //Turn off Switch
                            i = measurementInput;
                        } else {
                            //TODO: Check this: re-allocate memory location, override the new value but not affect the existing value
                            measurement = measurementsMap.get(newSpinner.getSelectedItem().toString());
                            measurement.setCalories(newInputCal.getText().toString());
                            melCal.put(measurement.getName(), measurement);
                        }
                    }
                }

                //This line should specify Ingredient is Countable or Uncountable clearly
                newIng = new IngredientCountable(name, melCal);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ingredients");

                Map<String, Object> newIngredient = new HashMap<>();
                newIngredient.put(newIng.getName(), newIng);
                mDatabase.updateChildren(newIngredient);
                Toast.makeText(getActivity(), "New Ingredient is added!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Unsuccessful " + name + " is already existed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveData() {
        //retrieve MeasurementData
        measurementsMap = new HashMap<>();
        //Alternative method 1: Using ArrayList<Measurement> and use loop to add
        DatabaseReference mMeasurementDatabase = FirebaseDatabase.getInstance().getReference("Measurements");
        mMeasurementDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot measurementSnapshot : dataSnapshot.getChildren()) {
                    Measurement obj = (Measurement) measurementSnapshot.getValue(Measurement.class);
                    if (obj != null) {
                        measurementsMap.put(obj.getName(), obj);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //retrieve Ingredient Data
        //Alternative method, using HashMap<String, String>, this might be a bit better for search performance
        /*IngredientDb = new ArrayList<>();
        IngredientDb.clear();
        DatabaseReference mIngDatabase = FirebaseDatabase.getInstance().getReference("Ingredients");
        mIngDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ingredientSnapshot : dataSnapshot.getChildren()) {
                    Ingredient ingObj = (IngredientCountable) ingredientSnapshot.getValue(IngredientCountable.class);
                    if (ingObj != null) {
                        IngredientDb.add(ingObj);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
    }

    private boolean hasIng(String name) {
        //Alternative method 1: change to hashMap and use get() to retrieve data for better performance
        /*if (IngredientDb != null) {
            for (Ingredient ing : IngredientDb) {
                if (ing.getName().equalsIgnoreCase(name))
                    return true;
            }
        }*/
        return false;
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.empty_menu_items, menu);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_createMenu_Act);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("NewIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    public void onAddIngBtnClicked() {
        if (mFragInteractListener != null) {
            mFragInteractListener.onAddIngBtnClicked();
            getActivity().getSupportFragmentManager().popBackStack("NewIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragInteractListener) {
            mFragInteractListener = (OnFragInteractListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragInteractListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragInteractListener = null;
    }

    public interface OnFragInteractListener {
        void onAddIngBtnClicked();
    }
}