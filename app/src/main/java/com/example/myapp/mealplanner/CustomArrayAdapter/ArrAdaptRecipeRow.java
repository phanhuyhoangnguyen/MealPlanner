package com.example.myapp.mealplanner.CustomArrayAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;

import java.util.ArrayList;

/**
 * Created by John.nguyen on 16/10/2017.
 */

public class ArrAdaptRecipeRow extends ArrayAdapter<Recipe> {


    public ArrAdaptRecipeRow(Activity context, ArrayList<Recipe> wordsList) {
        super(context, 0, wordsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_meal_list_item_menu, parent, false);
        }

        //Get the Word object located at this position in the list
        Recipe current = getItem(position);

        //Find the TextView in the list_item_food_list_food_list.xml with the ID verson_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_recipeListItmRow_Layout);

        //Get the version name from the current Word object and
        //set this text on the name TextView
        nameTextView.setText(current.getName());

        return listItemView;
    }
}
