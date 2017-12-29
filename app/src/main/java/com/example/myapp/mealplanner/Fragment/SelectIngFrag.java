package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapp.mealplanner.CustomArrayAdapter.Alternative.ArrAdaptIngBtnListener;
import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.Object.IngredientCountable;
import com.example.myapp.mealplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SelectIngFrag extends Fragment {

    private static final String ARG_INGLIST = "existedIngredient";

    //IngredientUncountable AutoCompletion Code
    private HashMap<String, IngredientCountable> ingredientsMap;
    private ArrayList<String> ingNameData;
    private RecyclerView mRecycleView;
    private LinearLayout listLabelLn;
    private ArrayList<IngredientCountable> ingSelectedData;
    private ArrayAdapter<String> itemsAvailAdapter;

    //Alternative 1st Design
    private ArrAdaptIngBtnListener arrayIngredientAdapter;

    /*//Alternative 2nd
    private ArrAdaptIngEditTxt arrayIngredientAdapter;*/

    /*//Alternative design for handling Item Click and Long Click, this can be either implemented in the Fragment, Adapter
    //or even in the Activity:
    public interface OnFragItmClickListener {
        // this is the activity declaration for Long Click and Short Click, we need this code because
        // this will be implemented because: After the onAttach is called,
        // every time this is executed, this will be handled and executed by Activity, with the parameter of this Fragment
        void onFragIngItmClicked(IngredientUncountable item);
        void onFragIngItmLongClicked(IngredientUncountable item);
    }
    //Another design for Long Click's Handling: Activity's implementation for Long Click Handling
    private OnFragItmClickListener onFragItmClickListener;*/

    private static final int MENU_ITEM_ITEM1 = 1;

    public SelectIngFrag() {
        // Required empty public constructor
    }

    public static SelectIngFrag newInstance(List<IngredientCountable> existingData) {
        SelectIngFrag fragment = new SelectIngFrag();
        Bundle args = new Bundle();

        args.putParcelableArrayList(ARG_INGLIST, (ArrayList<IngredientCountable>) existingData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //ingSelectedData = new ArrayList<>();  //deleted
            ingSelectedData = getArguments().getParcelableArrayList(ARG_INGLIST);
        }
        //If call from here, this method will be only called once but not again when resume
        //however because the ingNameData is called from onCreateView, which is created new again
        //this will cause empty
        //retrieveIngredientData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_select_ing, container, false);

        //Toolbar SetUp
        setHasOptionsMenu(true);


        //IngredientUncountable AutoCompletion Code
        ingNameData = new ArrayList<>();
        ingredientsMap = new HashMap<>();

        mRecycleView = view.findViewById(R.id.recycleVw_selectIng_Frag);
        registerForContextMenu(mRecycleView);

        listLabelLn = view.findViewById(R.id.listLabelLinear_selectIng_Frag);
        listLabelLn.setVisibility(View.INVISIBLE);

        final AutoCompleteTextView acIngTxtVw = view.findViewById(R.id.autoText_selectIng_Frag);

        final Button saveIngList = view.findViewById(R.id.saveBtn_selectIng_Frag);

        //Retrieve database to AutoCompleteText List: ingNameData, Map: ingredientsMap
        //If called in here, this might be called more than once, so remember to clear the data, otherwise the data might duplicated
        retrieveIngredientData();

        //TODO: create custom ArrayAdapter for Auto-complete text, showing no result
        itemsAvailAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_dropdown_item_1line, ingNameData);

        //Automatically call notifySetChange instead of having to call it manually
        //itemsAvailAdapter.setNotifyOnChange(true);
        //No need this code because the itemsAvailAdapter is already setNotifyDataChange(true)
        //itemsAvailAdapter.notifyDataSetChanged();

        acIngTxtVw.setAdapter(itemsAvailAdapter);

        acIngTxtVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String ingName = parent.getItemAtPosition(position).toString();
                // we can change code to pass Ingredient instead of String: addSelectedIngredient(ingredientsMap.get(ingName));
                addSelectedIngredient(ingName);
                clearText();
            }
        });

        final ImageButton cancelTxt = view.findViewById(R.id.clearTxtImgBtn_selectIng_Frag);

        cancelTxt.setOnTouchListener(mOnTouchListener);

        acIngTxtVw.setOnTouchListener(mOnTouchListener);

        acIngTxtVw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused && hasText()) {
                    showClearButton(true);
                } else {
                    showClearButton(false);
                }
            }
        });

        //Another the method for display clear text btn is addFocusListener:
        // if has focus and having text, show clear text button, else, no focus -> hide clear button
        acIngTxtVw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (hasText()) {
                    showClearButton(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!hasText()) {
                    showClearButton(false);
                }
                /*if (!acIngTxtVw.isPerformingCompletion()){

                }*/
            }
        });

        if (ingSelectedData == null) {
            ingSelectedData = new ArrayList<>();
        } else {
            updateList();
        }

        saveIngList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //autoCompleteTextDebug();
                if (ingSelectedData.size() > 0) {
                    if (mFragListener != null) {
                        //this will be called in the activity but not this fragment
                        getActivity().getSupportFragmentManager().popBackStack("selectIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        mFragListener.OnFinishedAddingIng(ingSelectedData);
                    }
                } else {
                    Toast.makeText(getActivity(), "The List is Empty! Please Add IngredientUncountable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void autoCompleteTextDebug() {
        //Use For Debugging, can be deleted if the process is understood
        AutoCompleteTextView acIngTxtVw = getView().findViewById(R.id.autoText_selectIng_Frag);
        //Null is when forget to call new or allocate this onto certain memory location
                /*if (ingNameData != null) {
                    Log.i("ingNameData", "Not Null");
                }*/
        //Empty is when already created but forget to populate data
        if (ingNameData.isEmpty()) {
            Log.i("ingNameData", "is Empty");
        } else {
            Log.i("AC Adapter: itm Avail", "itemAvail is empty: "
                    + String.valueOf(acIngTxtVw.getAdapter().isEmpty()) + "has: "
                    + String.valueOf(acIngTxtVw.getAdapter().getCount()));
            Log.i("ingNameData of adapter", "is Not Empty. Has" + String.valueOf(ingNameData.size()));
        }
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //getActivity().invalidateOptionsMenu();
        //menu.clear(); //use to clear to apply the new toolbar design
        //inflater.inflate(R.menu.empty_menu_items, menu);

        //Add Menu Item into Empty Menu Programmatically
        //Alternative method: setTag("someName".concat(String.valueOf(customNumber)));
        menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "Add Ingredient");

        /*final Toolbar toolbar = getActivity().findViewById(R.id.toolbar_createRecipe_Act);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(null);
                toolbar.setNavigationOnClickListener(null);
                toolbar.setOnMenuItemClickListener(null);
                getActivity().getSupportFragmentManager().popBackStack("selectIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_ITEM_ITEM1:
                        mFragListener.OnCreateNewIngRequest();
                    return true;

                    default:
                        return false;
                }
            }
        });*/
    }

    //Alternative method: above
    //this toolbar here in Fragment and its Activity are the same toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //TODO: check program if it still function well when using back button
            case android.R.id.home:
                //same id but different implementation, this is able because of setHasOptionsMenu(true); has been called
                getActivity().getSupportFragmentManager().popBackStack("selectIngFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;

            case MENU_ITEM_ITEM1:
                mFragListener.OnCreateNewIngRequest();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.autoText_selectIng_Frag:
                    if (hasText()) {
                        showClearButton(true);
                        return true;
                    }
                    return false;

                case R.id.clearTxtImgBtn_selectIng_Frag:
                    clearText();
                    return true;

                default:
                    return false;
            }
        }
    };

    private boolean hasText() {
        AutoCompleteTextView acIngTxtVw = getView().findViewById(R.id.autoText_selectIng_Frag);
        return (acIngTxtVw.getText().length() > 0);
    }

    private void clearText() {
        AutoCompleteTextView acIngTxtVw = getView().findViewById(R.id.autoText_selectIng_Frag);
        acIngTxtVw.getText().clear();
    }

    private void showClearButton(boolean on) {
        ImageButton cancelTxt = getView().findViewById(R.id.clearTxtImgBtn_selectIng_Frag);
        if (on) {
            cancelTxt.setVisibility(View.VISIBLE);
        } else {
            cancelTxt.setVisibility(View.INVISIBLE);
        }
    }

    public void retrieveIngredientData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ingredients");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ingredientsMap = (Map) dataSnapshot.getValue();
                //Because this method is repeated more than once, we need to clear it, otherwise this can be put on the onCreate method
                //which will only call once
                itemsAvailAdapter.clear();

                for (DataSnapshot ingredientSnapshot : dataSnapshot.getChildren()) {
                    //TODO: review this, abstract class
                    Ingredient ingObj = (IngredientCountable) ingredientSnapshot.getValue(IngredientCountable.class);
                    if (ingObj != null) {
                        ingNameData.add(ingObj.getName());
                        //Create New Map from data retrieve
                        ingredientsMap.put(ingObj.getName(), (IngredientCountable) ingObj);
                        Log.i("ingNameData Update 1:", " add");

                        //2nd way: retrieve from Firebase
                        //ingredientsMap = (HashMap<String, IngredientUncountable>) dataSnapshot.getValue();
                        //Collection<IngredientUncountable> values = ingredientsMap.values();
                        //ArrayList<IngredientUncountable> ingData = new ArrayList<>(values);
                        //GenericTypeIndicator<List<IngredientUncountable>> t =
                        //new GenericTypeIndicator<List<IngredientUncountable>>() {};
                        //List<IngredientUncountable> ingredientList = dataSnapshot.getValue(t);*/
                    } else
                        System.out.println("The ingredient retrieved failed!");
                }
                if (itemsAvailAdapter.isEmpty()) {
                    itemsAvailAdapter.addAll(ingNameData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        Log.i("ingNameData Update 2:", " add");
        Toast.makeText(getActivity(), "add", Toast.LENGTH_SHORT).show();
    }

//Alternative: 1st Design (Simple Design): separate button click listener, there is 2 click listener in this implementation: not so good
    /*final ArrAdaptIngBtnListener.OnBtnClickListener mBtnListener = new ArrAdaptIngBtnListener.OnBtnClickListener() {
        @Override
        public void onBtnClick(View view, IngredientUncountable item) {
            switch (view.getId()) {
                case R.id.ing_listItm_ingQunBtn_Inc:
                    item.setCalories(String.valueOf((Float.valueOf(item.getCalories()) * (Float.valueOf(item.getQuantity()) + 50))
                            / Float.valueOf(item.getQuantity())));
                    item.setQuantity(String.valueOf(Float.valueOf(item.getQuantity()) + 50));
                    break;

                case R.id.ing_listItm_ingQunBtn_Ded:
                    if (Float.valueOf(item.getQuantity()) > 50) {
                        item.setCalories(String.valueOf((Float.valueOf(item.getCalories()) * (Float.valueOf(item.getQuantity()) - 50))
                                / Float.valueOf(item.getQuantity())));
                        item.setQuantity(String.valueOf(Float.valueOf(item.getQuantity()) - 50));
                    }
                    break;

                default:
                    break;
            }
            arrayIngredientAdapter.notifyDataSetChanged();
        }
    };

    final ArrAdaptIngBtnListener.OnItemClickListener mItmFragListener = new ArrAdaptIngBtnListener.OnItemClickListener() {
        @Override
        public void onItemClick(IngredientUncountable item) {
            Toast.makeText(getActivity(), "create dialog and handling actions", Toast.LENGTH_SHORT).show();
        }
    };*/

    /*Alternative: 1st Design: Middle Upgrade Pass the execution to be handled by Activity
    final ArrAdaptIngBtnListener.OnItmClickListener mItmFragListener = new ArrAdaptIngBtnListener.OnItmClickListener() {
        @Override
        public void onItemLongClick(View view, IngredientUncountable item) {
            //this will be executed by the activity but not the fragment, after the onAttach is called
            onFragItmClickListener.onFragIngItmLongClicked(item);

        }

        @Override
        public void onItemClick(View view, IngredientUncountable item) {
            //this will be executed by the activity but not the fragment, after the onAttach is called
            onFragItmClickListener.onFragIngItmClicked(item);
        }

    };*/

    //Alternative: 1st Design: Final Design, also this handle the action directly but not pass to the activity
    final ArrAdaptIngBtnListener.OnItmClickListener mItmListener = new ArrAdaptIngBtnListener.OnItmClickListener() {
        @Override
        public void onItemClick(View view, Ingredient localItm) {
            //get constant value from Firebase based on local Item's Name
            Ingredient serverItm = ingredientsMap.get(localItm.getName()); //get correct Ingredient
            //get correct base quantity's measurement name, from local current measurement -> get quantity value from server
            float baseQuantity = Float.valueOf(serverItm.getMeasurementDictMultiMap().get(localItm.getCurrentMeasurement()).getQuantity());
            float i = baseQuantity / 2;
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);

            //This is only triggered when the button with set onClickListener is clicked
            //Toast.makeText(getActivity(), "Short Click", Toast.LENGTH_SHORT).show();
            switch (view.getId()) {
                //TODO: reformat number quantity to be correct: 1.0 -> 1
                case R.id.ingQunBtnInc_listItm_Layout:
                    Log.i("locItm currentQty + i: ", localItm.getCurrentQuantity() + "+" + i);
                    localItm.setCurrentCalories(String.valueOf((Float.valueOf(localItm.getCurrentCalories()) *
                            (Float.valueOf(localItm.getCurrentQuantity()) + i) / Float.valueOf(localItm.getCurrentQuantity()))));
                    //String qun = String.format(Locale.US, "%.2f", Double.valueOf(localItm.getCurrentCalories()));
                    localItm.setCurrentQuantity(String.valueOf(Float.valueOf(localItm.getCurrentQuantity()) + i));
                    break;

                case R.id.ingQunBtnDed_listItm_Layout:
                    Log.i("locItm currentQty - i: ", localItm.getCurrentQuantity() + "-" + i);
                    if ((Float.valueOf(localItm.getCurrentQuantity()) - i) > 0) {
                        localItm.setCurrentCalories(String.valueOf((Float.valueOf(localItm.getCurrentCalories()) *
                                (Float.valueOf(localItm.getCurrentQuantity()) - i) / Float.valueOf(localItm.getCurrentQuantity()))));
                        localItm.setCurrentQuantity(String.valueOf(Float.valueOf(localItm.getCurrentQuantity()) - i));
                    }
                    break;

                default:
                    localItm.changeQuantityMeasurement(localItm.getCurrentMeasurement());
                    Log.i("check Measure", localItm.getCurrentMeasurement());
                    Log.i("check Cal", localItm.getCurrentCalories());
                    break;
            }
            arrayIngredientAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemLongClick(View view, Ingredient item) {
            view.showContextMenu();
        }
    };

    /*//Alternative: 2nd Design
    final ArrAdaptIngEditTxt.OnEditTextFinishedListener mEditTextListener = new ArrAdaptIngEditTxt.OnEditTextFinishedListener() {

        @Override
        public void active(Editable text, IngredientUncountable item, final int position) {
            String currentText = text.toString().replaceAll("\\D+", "");
            item.setQuantity(currentText);
            //updateList();

            /*mRecycleView.post(new Runnable() {
                @Override
                public void run() {
                    //can't call notifyDataChange in onBindViewHolder, because this method will call onBindViewHolder again -> loop
                    arrayIngredientAdapter.notifyItemChanged(position);
                    Log.i("run", "1");
                }
            });*/

            /*Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    arrayIngredientAdapter.notifyDataSetChanged();
                }
            };
            handler.post(r);
        }
    };*/


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.select_ing_frag, menu);

        //Alternative method: declare onCreateContextMenu in the Activity, handling differently to
        //different fragment
        /*if (v.getId() == R.id.addIng_recycleView_frag) {*/

        //this is also can be called in here, but not only from ArrayAdapter
        //1st way: Add manually with the menu define layout: Not recommend because v.getId() is always the same
        menu.setHeaderTitle("Select Action");
        menu.add(Menu.NONE, R.id.delete_option, Menu.NONE, "Replace with Alternative Ingredients");
        //menu.add(Menu.NONE, v.getId(), Menu.NONE, "Test Item 1");
        //menu.add(Menu.NONE, v.getId(), Menu.NONE, "Test Item 2");

        /*//2nd way: Add automatically using loop with array
        String[] menuItems = getResources().getStringArray(R.array.menu);
        for (int i = 0; i < menuItems.length; i++) {
              menu.add(Menu.NONE, i, i, menuItems[i]);
        }*/
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = ((ArrAdaptIngBtnListener) mRecycleView.getAdapter()).getPosition();
        Ingredient ingredientUncountable = ingSelectedData.get(position);

        //1st method: Using switch
        switch (item.getItemId()) {
            case R.id.delete_option:
                //TODO: fix this, check why the item removed but still recorded in memory but not creating new
                ingSelectedData.remove(ingredientUncountable);
                Toast.makeText(getActivity(), "Delete!", Toast.LENGTH_SHORT).show();
                updateList();
                return true;
            default:
                return super.onContextItemSelected(item); // == return false
        }
    }

    private void updateList() {
        //Change Display of Label
        if (ingSelectedData.size() > 0)
            listLabelLn.setVisibility(View.VISIBLE);
        else
            listLabelLn.setVisibility(View.GONE);

        //Display IngredientUncountable List
        displaySelectedIngredientList();
    }

    public void addSelectedIngredient(String ingName) {
        // addIngredientTestData();
        // We use the Map in order to simplified passing parameter String instead of Passing Ingredient
        // This way, instead of having create or retrieve specific Ingredient, we can just look that ingredient,
        // which is already created and stored in memory.

        IngredientCountable ing = ingredientsMap.get(ingName);
        /*if (!ingSelectedData.contains(ing)) {
            ingSelectedData.add(ing);
        }*/
        Log.i("addSelectedIngredient", ing.getName());
        if (ingSelectedData.size() > 0) {
            if (ingredientAdded(ingName)) {
                Toast.makeText(getActivity(), "The IngredientUncountable is already added!", Toast.LENGTH_SHORT).show();
            } else {
                ingSelectedData.add(ing);
            }
        } else {
            ingSelectedData.add(ing);
            Log.i("addSelectedIngredient", "size = 0. Added!");
        }
        updateList();
    }

    private boolean ingredientAdded(String ingName) {
        for (Ingredient i : ingSelectedData) {
            //Log.i("addSelectedIngredient", "size > 0");
            //Log.i("addSelectedIngredient", i.getName());
            if (i.getName().equalsIgnoreCase(ingName)) {
                //Log.i("addSelectedIngredient", "existed!");
                return true;
            }
            //Log.i("addSelectedIngredient", "don't have");
        }
        return false;
    }

    public void displaySelectedIngredientList() {
        //we can refresh to update data by either calling new ArrayAdaptIngEditTxt again or
        //using notifyDataSetChanged

        //Alternative: 1st Design
        arrayIngredientAdapter = new ArrAdaptIngBtnListener(
                getActivity(), ingSelectedData, mItmListener);

        /*//Alternative: 2nd Design
        arrayIngredientAdapter = new ArrAdaptIngEditTxt(
                getActivity(), ingSelectedData, mEditTextListener);*/

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecycleView.setAdapter(arrayIngredientAdapter);
        mRecycleView.setHasFixedSize(true);
    }

    private OnFragInteractListener mFragListener;

    public interface OnFragInteractListener {
        void OnFinishedAddingIng(List<IngredientCountable> ingredientCountable);

        void OnCreateNewIngRequest();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragInteractListener) {
            mFragListener = (OnFragInteractListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRetrieveListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragListener = null;
    }
}
