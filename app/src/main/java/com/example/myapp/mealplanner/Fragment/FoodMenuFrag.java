package com.example.myapp.mealplanner.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapp.mealplanner.CustomArrayAdapter.ArrAdaptRecipeRow;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodMenuFrag extends Fragment {
    private ListView mListViewAppetizer;
    private ListView mListViewEntree;
    private ListView mListViewDessert;

    private ArrayList<Recipe> appetizer;
    private ArrayList<Recipe> entree;
    private ArrayList<Recipe> dessert;

    public FoodMenuFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu_list_row, container, false);
        // Inflate the layout for this fragment
        /*mListViewAppetizer = (ListView) view.findViewById(R.id.foodMenu_list_appetizer);
        mListViewEntree = (ListView) view.findViewById(R.id.foodMenu_list_entree);
        mListViewDessert = (ListView) view.findViewById(R.id.foodMenu_list_dessert);*/

        appetizer = new ArrayList<>();
        entree = new ArrayList<>();
        dessert = new ArrayList<>();

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
                        Log.i("recipe name", menuObject.getId());
                        if (menuObject.getId().equalsIgnoreCase(id)) {
                            for (Recipe recipe : menuObject.getRecipes()) {
                                //Log.i("recipe name", recipe.getName());
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
                ArrAdaptRecipeRow itemsAdapterAppetizer = new ArrAdaptRecipeRow(getActivity(), appetizer);
                mListViewAppetizer.setAdapter(itemsAdapterAppetizer);

                ArrAdaptRecipeRow itemsAdapterEntree = new ArrAdaptRecipeRow(getActivity(), entree);
                mListViewEntree.setAdapter(itemsAdapterEntree);

                ArrAdaptRecipeRow itemsAdapterDessert = new ArrAdaptRecipeRow(getActivity(), dessert);
                mListViewDessert.setAdapter(itemsAdapterDessert);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return view;
    }
}
