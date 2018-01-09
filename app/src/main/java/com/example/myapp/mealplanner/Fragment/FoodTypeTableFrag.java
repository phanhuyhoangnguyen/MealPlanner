package com.example.myapp.mealplanner.Fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.mealplanner.R;

/**
 * Created by John.nguyen on 16/10/2017.
 */

public class FoodTypeTableFrag extends Fragment {

    public FoodTypeTableFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_food_table_type, container, false);

        ImageView imageSalad = view.findViewById(R.id.saladImgVw_foodTableType_Frag);
        TextView textSalad = view.findViewById(R.id.saladLabel_foodTableType_Frag);

        ImageView imageSoup = view.findViewById(R.id.soupImgVw_foodTableType_Frag);
        TextView textSoup = view.findViewById(R.id.soupLabel_foodTableType_Frag);

        ImageView imagePanfrying = view.findViewById(R.id.panFryingImgVw_foodTableType_Frag);
        TextView textPanfrying = view.findViewById(R.id.panFryingLabel_foodTableType_Frag);

        ImageView imageDessert = view.findViewById(R.id.dessertImgVw_foodTableType_Frag);
        TextView textDessert = view.findViewById(R.id.dessertLabel_foodTableType_Frag);

        imageSalad.setOnClickListener(mOnclickListener);
        imageSoup.setOnClickListener(mOnclickListener);
        imageDessert.setOnClickListener(mOnclickListener);
        imagePanfrying.setOnClickListener(mOnclickListener);

        textPanfrying.setOnClickListener(mOnclickListener);
        textDessert.setOnClickListener(mOnclickListener);
        textSoup.setOnClickListener(mOnclickListener);
        textSalad.setOnClickListener(mOnclickListener);

        //Tool SetUp
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_createMenu_Act);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(android.view.Menu menu) {
        menu.findItem(R.id.action_addMenu).setVisible(false);
        //menu.findItem(R.id.action_createMenu).setVisible(false);
        menu.findItem(R.id.action_editMenu).setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack("FoodTypeTableFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.saladLabel_foodTableType_Frag:
                case R.id.saladImgVw_foodTableType_Frag:
                    displayListRecipes(0);
                    break;

                case R.id.soupLabel_foodTableType_Frag:
                case R.id.soupImgVw_foodTableType_Frag:
                    displayListRecipes(1);
                    break;

                case R.id.panFryingImgVw_foodTableType_Frag:
                case R.id.panFryingLabel_foodTableType_Frag:
                    displayListRecipes(2);
                    break;

                case R.id.dessertImgVw_foodTableType_Frag:
                case R.id.dessertLabel_foodTableType_Frag:
                    displayListRecipes(3);
                    break;
            }
        }
    };

    private void displayListRecipes(int requestCode) {
        onDisplayRecipesRequestListener.displayListRecipes(requestCode);
    }

    public interface OnFragInteractListener {
        //After the onAttack is called, every time this is executed, this will be perform by Activity,
        //with the parameter of this Fragment
        void displayListRecipes(int requestCode);
    }

    private OnFragInteractListener onDisplayRecipesRequestListener;

    @Override
    public final void onAttach(Context context) {
        //This method avoid to call super.onAttach(context) if I'm not using api 23 or more
        super.onAttach(context);
        try {
            //reference back to its activity
            onDisplayRecipesRequestListener = (OnFragInteractListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnFragInteractListener");
        }
    }
}