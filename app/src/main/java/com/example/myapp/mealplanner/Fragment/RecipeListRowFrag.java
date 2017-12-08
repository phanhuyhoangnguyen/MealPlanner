package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.Menu;

import com.example.myapp.mealplanner.CustomArrayAdapter.ArrAdaptRecRwVwClickListener;
import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeListRowFrag extends Fragment {

    private RecyclerView mRecycleView;
    private ArrayList<Recipe> items;

    private static final int MENU_ITEM_ITEM1 = 1;

    private ArrAdaptRecRwVwClickListener adapter;

    public interface onFragInteractListener {
        //After the onAttack is called, every time this is executed, this will be perform by Activity,
        //with the parameter of this Fragment
        void onRecipeItmClicked(Recipe item);
        void onCreateNewRecipeRequest();
    }

    private onFragInteractListener mFragListener;

    public RecipeListRowFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_recipe_list_row_layout, container, false);

        //Tool SetUp
        setHasOptionsMenu(true);

        items = new ArrayList<>();

        //Recycle View
        mRecycleView = view.findViewById(R.id.recycleVw_recipeListRow_Frag);
        final ArrAdaptRecRwVwClickListener.OnItemClickListener mListener = new ArrAdaptRecRwVwClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe item) {
                //this will be executed by the activity but not the fragment, after the onAttach is called
                mFragListener.onRecipeItmClicked(item);
            }
        };

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Recipes");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipeObject = mealSnapshot.getValue(Recipe.class);
                    items.add(recipeObject);
                    //System.out.println(recipeObject.getName());
                }
                adapter = new ArrAdaptRecRwVwClickListener(items, mListener);

                mRecycleView.setAdapter(adapter);
                mRecycleView.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty_menu_items, menu);

        //Add Menu Item into Empty Menu Programmatically
        menu.add(android.view.Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "Add Recipe");

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_createMenu_Act);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("FoodTypeListRowFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_ITEM_ITEM1:
                        mFragListener.onCreateNewRecipeRequest();
                        return true;
                    default:
                        return false;
                }
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public final void onAttach(Context context) {
        //This method avoid to call super.onAttach(context) if I'm not using api 23 or more
        super.onAttach(context);
        try {
            //reference back to its activity
            mFragListener = (onFragInteractListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnItemClickListener");
        }
    }

    public interface onMenuItemClickListener {
        //After the onAttack is called, every time this is executed, this will be perform by Activity,
        //with the parameter of this Fragment
    }
}
