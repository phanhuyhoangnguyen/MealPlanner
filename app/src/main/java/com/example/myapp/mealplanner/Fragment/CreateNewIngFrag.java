package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNewIngFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int measurementInput = 1;

    private String mParam1;
    private String mParam2;

    private ArrayList<Ingredient> IngredientDb;

    private OnFragInteractListener mFragInteractListener;

    public CreateNewIngFrag() {
        // Required empty public constructor
    }

    public static CreateNewIngFrag newInstance(String param1, String param2) {
        CreateNewIngFrag fragment = new CreateNewIngFrag();
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
        final View view = inflater.inflate(R.layout.frag_create_new_ing, container, false);

        //Toolbar SetUp
        setHasOptionsMenu(true);

        Button saveIngInfoBtn = view.findViewById(R.id.saveBtn_createNewIng_Frag);
        saveIngInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewIng();
                //onAddIngBtnClicked();
            }
        });

        //retrieveIngData();

        //Alternative method: CustomArrayAdapter with ListView and Custom Sub Object containing only: calories and measurement
        final Spinner measurementSpinner = view.findViewById(R.id.measurementSpn_createNewIng_Frag);
        final Spinner measurementNoSpinner = view.findViewById(R.id.measurementNoSpn_createNewIng_Frag);
        measurementNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    measurementInput = Integer.valueOf(measurementNoSpinner.getSelectedItem().toString());
                }
                if (measurementInput > 1) {
                    for (int i = 0; i > measurementInput; i++) {
                        EditText newCalInput = new EditText(getActivity());
                        Spinner newSpinner = new Spinner(getActivity());
                        //TODO: fix this, id is duplicated
                        newCalInput.setId(i);
                        newSpinner.setId(i);
                        LinearLayout linear = view.findViewById(R.id.lnLayoutContainerSpn_createNewIng_Frag);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        newCalInput.setLayoutParams(params);
                        newSpinner.setLayoutParams(params);

                        ArrayAdapter<String> newSpAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.volumeMeasurement_array));
                        newSpAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        newSpinner.setAdapter(newSpAdapter);

                        linear.addView(newCalInput);
                        linear.addView(newSpinner);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> measurementInputAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.volumeMeasurement_array));

        ArrayAdapter<String> measurementNoInputAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.measurementNo_array));

        measurementInputAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        measurementNoInputAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        measurementSpinner.setAdapter(measurementInputAdapter);
        measurementNoSpinner.setAdapter(measurementInputAdapter);

        //Set Default Value
        //measurementSpinner.setSelection(0);

        return view;
    }

    private void createNewIng() {
        //TODO: fix all these warning
        TextInputLayout ingName = getView().findViewById(R.id.ingNameInput_createNewIng_Frag);
        TextInputLayout ingCal = getView().findViewById(R.id.ingCalInput_createNewIng_Frag);
        //RadioGroup radioGrp = getView().findViewById(R.id.countRadioGroup_createNewIng_Frag);

        //TODO: put format constraint for name to be camelCase
        String name = ingName.getEditText().getText().toString();
        String cal = ingCal.getEditText().getText().toString();
        Spinner measurementSpinner = getView().findViewById(R.id.measurementSpn_createNewIng_Frag);
        String measurement = measurementSpinner.getSelectedItem().toString();

        if (!hasIng(name)) {
            if (name.isEmpty()) {
                ingName.setError("Please Enter Ingredient Name!");
            } else if (cal.isEmpty()) {
                ingName.setError("Please Enter Ingredient Calories!");
            }
            else if (measurement.equalsIgnoreCase(getResources().getStringArray(R.array.volumeMeasurement_array)[0])) {
                Toast.makeText(getActivity(), "Please Select Measurement", Toast.LENGTH_SHORT).show();
            } else {
                Ingredient newIng;
                HashMap<String, String> melCal = new HashMap<>();
                for (int i = 0; i > measurementInput; i++){
                    melCal.put(measurement, cal);
                }

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

    private void retrieveIngData() {
        IngredientDb = new ArrayList<>();
        IngredientDb.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ingredients");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ingredientSnapshot : dataSnapshot.getChildren()) {
                    Ingredient ingObj = (Ingredient) ingredientSnapshot.getValue(Ingredient.class);
                    if (ingObj != null) {
                        IngredientDb.add(ingObj);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean hasIng(String name) {
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
                getActivity().getSupportFragmentManager().popBackStack("CreateNewIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    public void onAddIngBtnClicked() {
        if (mFragInteractListener != null) {
            mFragInteractListener.onAddIngBtnClicked();
            getActivity().getSupportFragmentManager().popBackStack("CreateNewIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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