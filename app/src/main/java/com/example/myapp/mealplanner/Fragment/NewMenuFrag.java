package com.example.myapp.mealplanner.Fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapp.mealplanner.Activity.StartActivity;
import com.example.myapp.mealplanner.CustomArrayAdapter.ArrAdaptRecipeRow;
import com.example.myapp.mealplanner.Object.Menu;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewMenuFrag extends Fragment {

    //String will allow better performance but Recipe will allow object being able to passed between Fragments
    private ArrayList<Recipe> appetizer;
    private ArrayList<Recipe> entree;
    private ArrayList<Recipe> dessert;

    private ListView mListViewAppetizer;
    private ListView mListViewEntree;
    private ListView mListViewDessert;

    private ArrAdaptRecipeRow itemsAdapterAppetizer;
    private ArrAdaptRecipeRow itemsAdapterEntree;
    private ArrAdaptRecipeRow itemsAdapterDessert;

    public NewMenuFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu_list_row, container, false);

        /*FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);*/

        //Toolbar SetUp
        setHasOptionsMenu(true);

        mListViewAppetizer = view.findViewById(R.id.appetizerItm_menuListRow_Frag);
        mListViewEntree = view.findViewById(R.id.entreeListItm_menuListRow_Frag);
        mListViewDessert = view.findViewById(R.id.dessertListItm_menuListRow_Frag);

        appetizer = new ArrayList<>();
        entree = new ArrayList<>();
        dessert = new ArrayList<>();

        itemsAdapterAppetizer = new ArrAdaptRecipeRow(getActivity(), appetizer);
        mListViewAppetizer.setAdapter(itemsAdapterAppetizer);

        itemsAdapterEntree = new ArrAdaptRecipeRow(getActivity(), entree);
        mListViewEntree.setAdapter(itemsAdapterEntree);

        itemsAdapterDessert = new ArrAdaptRecipeRow(getActivity(), dessert);
        mListViewDessert.setAdapter(itemsAdapterDessert);

        //display even if the list is empty
        DisplayFoodMenuData();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addMenu:
                onCreateNewMenuRequestedListener.onCreateNewMenuRequested(null, "onMenuItemClick: Create Menu clicked!");
                return true;

            case R.id.action_createMenu:
                createNewMenu();
                return true;

            case R.id.action_editMenu:
                return false;

            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                sendUserToStart();
                return true;

            default:
                Log.i("onMenuItemClick", "default");
                return super.onOptionsItemSelected(item);
        }
    }


    private void createNewMenu() {
        //TODO: rewrite this method
        Menu nMenu = null;
        DatabaseReference mDatabase;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
        Calendar calendar = Calendar.getInstance();
        String id = sdf.format(calendar.getTime());

        //1st method: Not recommended
        mDatabase = FirebaseDatabase.getInstance().getReference("Menu").child(id);
        mDatabase.setValue(nMenu);

        /*//2nd method: Using update function provided
        Map<String, Object> menuUpdates = new HashMap<>();
        menuUpdates.put(id, nMenu);
        mDatabase.updateChildren(menuUpdates);*/

        //update view - Menu Item
        DisplayFoodMenuData();
    }

    private void sendUserToStart() {
        Intent startIntent = new Intent(getActivity(), StartActivity.class);
        startActivity(startIntent);
        getActivity().finish();
    }

    private void DisplayFoodMenuData() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Menu");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Your Logic here
                appetizer.clear();
                entree.clear();
                dessert.clear();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
                Calendar calendar = Calendar.getInstance();
                String id = sdf.format(calendar.getTime());

                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                    Menu menuObject = (Menu) mealSnapshot.getValue(Menu.class);
                    if (menuObject != null) {
                        if (menuObject.getId().equalsIgnoreCase(id)) {
                            for (Recipe recipe : menuObject.getRecipes()) {
                                switch (recipe.getMenuType().toLowerCase()) {
                                    case "appetizer":
                                        appetizer.add(recipe);
                                        break;
                                    case "entree":
                                        entree.add(recipe);
                                        break;
                                    case "dessert":
                                        dessert.add(recipe);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }

                itemsAdapterAppetizer.notifyDataSetChanged();
                itemsAdapterEntree.notifyDataSetChanged();
                itemsAdapterDessert.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public interface OnFragInteractListener {
        //After the onAttack is called, every time this is executed, this will be perform by Activity,
        //with the parameter of this Fragment
        void onCreateNewMenuRequested(Menu newMenu, String msg);

    }

    private OnFragInteractListener onCreateNewMenuRequestedListener;

    @Override
    public final void onAttach(Context context) {
        //This method avoid to call super.onAttach(context) if I'm not using api 23 or more
        super.onAttach(context);
        try {
            //reference back to its activity
            onCreateNewMenuRequestedListener = (OnFragInteractListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnCreateMenuFinishedListener");
        }
    }
}
