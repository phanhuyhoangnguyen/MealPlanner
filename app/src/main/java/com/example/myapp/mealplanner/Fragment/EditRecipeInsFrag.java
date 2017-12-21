package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;

public class EditRecipeInsFrag extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EXISTING_RECIPE";

    private Recipe existingRecipe;

    private OnFragmentInteractionListener mListener;

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

        Button addIngBtn = view.findViewById(R.id.addIngBtn_editRecipeIns_Frag);
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

                case R.id.addIngBtn_editRecipeIns_Frag:
                    changeIngredients();
                    break;

                default:
                    break;
            }
        }
    };

    private void loadDataFromReceivingRecipe(View view) {
        EditText nameRecipe = view.findViewById(R.id.nameEditTxt_editRecipeIns_Frag);
        nameRecipe.setText(existingRecipe.getName());

        EditText originCityRecipe = view.findViewById(R.id.originCity_editRecipeIns_Frag);
        Spinner originCountryRecipeSpn = view.findViewById(R.id.originCountry_editRecipeIns_Frag);

        String origin[] = existingRecipe.getOrigin().split(",");
        originCityRecipe.setText(origin[0]);
        String countryName = origin[1];

        ArrayAdapter<String> countrySpnAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countryListName_array));

        countrySpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        originCountryRecipeSpn.setAdapter(countrySpnAdapter);

        // Alternative method 1: Using for loop using i to check every value of spinner item,
        // compare the value of every items (item.toString()), the matched value along with its index (i) is return and setSelection(i);
        // or 2:  Using for loop using i to check every value of Adapter item,
        // compare the value of every items (adapter.getItem(index).equals(value))), the matched value along with its index (i) is return and setSelection(i);
        // or 3: mySpinner.setSelection(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(countryName));
        // or 4: get position directly using adapter function: int selectionPosition= adapter.getPosition("YOUR_VALUE");
        // or 5: Using the array Country Name[] to find index: String[] countryName = getResources().getStringArray(R.array.countryListName_array);
        // mSpn.setSelection(Arrays.asList(countryName).indexOf(value_here));
        // or 6: using List: mSpinner.setSelection(yourList.indexOf("value"));
        // or 7:
        if (countryName != null) {
            int spinnerPosition = countrySpnAdapter.getPosition(countryName);
            originCountryRecipeSpn.setSelection(spinnerPosition);
        }

        EditText servingNo = view.findViewById(R.id.servingNo_editRecipeIns_Frag);
        servingNo.setText(existingRecipe.getServingYield());

        EditText prepDur = view.findViewById(R.id.duration_editRecipeIns_Frag);
        prepDur.setText(existingRecipe.getDuration());

        EditText ins = view.findViewById(R.id.insBody_editRecipeIns_Frag);
        ins.setText(existingRecipe.getInstruction());
    }

    private void saveRecipeInfo() {
        EditText nameRecipe = getView().findViewById(R.id.nameEditTxt_editRecipeIns_Frag);
        EditText originCityRecipe = getView().findViewById(R.id.originCity_editRecipeIns_Frag);

        EditText servingNo = getView().findViewById(R.id.servingNo_editRecipeIns_Frag);

        EditText prepDur = getView().findViewById(R.id.duration_editRecipeIns_Frag);

        EditText ins = getView().findViewById(R.id.insBody_editRecipeIns_Frag);

        if (nameRecipe.getText().toString().isEmpty()) {
            nameRecipe.setError("Please Enter Recipe Name.");
        } else if (servingNo.getText().toString().isEmpty()) {
            originCityRecipe.setError("Please Enter Serving Number.");
        } else if (prepDur.getText().toString().isEmpty()) {
            prepDur.setError("Please Enter Preparation Time.");
        } else if (ins.getText().toString().isEmpty()) {
            ins.setError("Please Add Ingredients and Instruction");
        } else {
            onButtonPressed();
        }
    }

    private void changeIngredients() {
    }

    public void onButtonPressed() {
        //TODO: update info to object directly, without the need to pass it, this method should be void and/or receving parameter
        if (mListener != null) {
            // this method can just be finish editing without the having pass any data back
            // since the object is update directly into memory location ref
            mListener.sendBackNewInfoRecipe();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void sendBackNewInfoRecipe();
    }
}
