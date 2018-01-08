package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myapp.mealplanner.Activity.CreateNewRecipeFragCtn;
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
    //private ArrayList<Recipe> items;

    private ProgressBar progressSpnBar;
    private DatabaseReference mDatabase;
    private ArrAdaptRecRwVwClickListener.OnItemClickListener mListener;

    private static final int ADD_RECIPE_MENU_ITEM_ID = View.generateViewId();

    private ArrAdaptRecRwVwClickListener adapter;

    private View view;

    public interface onFragInteractListener {
        //After the onAttack is called, every time this is executed, this will be perform by Activity,
        //with the parameter of this Fragment
        void onRecipeItmClicked(Recipe item);
    }

    private onFragInteractListener mFragListener;

    public RecipeListRowFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_recipe_list_row_layout, container, false);

        //Tool SetUp
        setHasOptionsMenu(true);

        progressSpnBar = view.findViewById(R.id.progressBar_recipeListRow_Frag);

        // Trigger Async Task (onPreExecute method)
        //new DownloadRecipeFromFirebase().execute(url);

        //Recycle View
        mRecycleView = view.findViewById(R.id.recycleVw_recipeListRow_Frag);
        mListener = new ArrAdaptRecRwVwClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe item) {
                //this will be executed by the activity but not the fragment, after the onAttach is called
                mFragListener.onRecipeItmClicked(item);
            }
        };

        loadDataAndPopulateToList();

        return view;
    }

    private void loadDataAndPopulateToList() {
        showProgressBar(true);
        final ArrayList<Recipe> items = new ArrayList<>();

        //Show the progress Spinner bar: turn on
        progressSpnBar.setVisibility(View.VISIBLE);
        String url = "Recipes";
        mDatabase = FirebaseDatabase.getInstance().getReference(url);

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

                //turn off progressSpinner
                //progressSpnBar.setVisibility(View.GONE);
                //method 1: using animation
                showProgressBar(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showProgressBar(boolean swt) {
        ImageView loading = view.findViewById(R.id.progressImage_recipeListRow_Frag);

        // load the animation
        Animation animRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        // start the animation
        if (swt) {
            loading.startAnimation(animRotate);
        } else {
            loading.clearAnimation();
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.empty_menu_items, menu);

        //Add Menu Item into Empty Menu Programmatically
        //add(int groupId, int itemId, int order, CharSequence title) == menuItem
        menu.add(Menu.NONE, ADD_RECIPE_MENU_ITEM_ID, Menu.NONE, "Add Recipe").setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(android.view.Menu.NONE, ADD_RECIPE_MENU_ITEM_ID + 1, Menu.NONE, "Log Out");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack("FoodTypeListRowFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        } else if (id == ADD_RECIPE_MENU_ITEM_ID) {


            Intent createRecipeIntent = new Intent(getActivity(), CreateNewRecipeFragCtn.class);
            //This suppose to be startActivityForResult() and return new Recipe to this Activity
            startActivity(createRecipeIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

    // Starting a new asynctask to show custom two progress bars working when click on button.
// One horizontal style and one spinner style.
    public class ShowCustomProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {
        int myProgress;

        //After finish the task
        @Override
        protected void onPostExecute(Void result) {
            //textview.setText("Finish work with custom ProgressBar");
            //button1.setEnabled(true);
            //Alternative: find the view by id in every functions instead of using Global Variables
            progressSpnBar.setVisibility(View.INVISIBLE);   //Alternative: View.GONE
        }

        @Override
        protected void onPreExecute() {
            //button1.setEnabled(false);
            //textview.setText("Start work with custom ProgressBar");
            myProgress = 0;
            progressSpnBar.setSecondaryProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (myProgress < 100) {
                myProgress++;
                publishProgress(myProgress);
                SystemClock.sleep(100);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressSpnBar.setProgress(values[0]);
            progressSpnBar.setSecondaryProgress(values[0] + 5);
        }
    }

    /* Background Async Task to download file */
    class DownloadRecipeFromFirebase extends AsyncTask<String, String, ArrayList<Recipe>> {
        /*  Before starting background thread. Show Progress Bar Dialog */
        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressSpnBar.setVisibility(View.VISIBLE);
            Log.i("onPreExecute", "executing...");
        }

        /* Downloading file in background thread */
        @Override
        protected ArrayList<Recipe> doInBackground(String... url) {
            final ArrayList<Recipe> items = new ArrayList<>();
            Log.i("doInBackground", "executing...");
            try {
                mDatabase = FirebaseDatabase.getInstance().getReference(url[0]);

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        items.clear();
                        for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                            Recipe recipeObject = mealSnapshot.getValue(Recipe.class);
                            items.add(recipeObject);
                            //System.out.println(recipeObject.getName());
                            Log.i("doInBackground", "downloading, adding...");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> items) {
            super.onPostExecute(items);
            progressSpnBar.setVisibility(View.GONE);

            mRecycleView.setHasFixedSize(true);

            mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new ArrAdaptRecRwVwClickListener(items, mListener);

            mRecycleView.setAdapter(adapter);
            Log.i("onPostExecute", "executing...");
            Log.i("items Size", String.valueOf(items.size()));
        }
    }
}
