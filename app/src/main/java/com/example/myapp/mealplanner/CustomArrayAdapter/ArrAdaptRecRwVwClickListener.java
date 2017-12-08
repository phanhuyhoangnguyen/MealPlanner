package com.example.myapp.mealplanner.CustomArrayAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapp.mealplanner.Object.Recipe;
import com.example.myapp.mealplanner.R;

import java.util.List;

public class ArrAdaptRecRwVwClickListener extends RecyclerView.Adapter<ArrAdaptRecRwVwClickListener.RecipeRowHolder> {

    private final List<Recipe> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Recipe item);
    }

    public ArrAdaptRecRwVwClickListener(List<Recipe> items, OnItemClickListener listener) {
        this.items = items;

        //Array's OnItemClickListener = Fragment's OnItemClickListener
        this.listener = listener;
    }

    @Override
    public RecipeRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item_row, parent, false);
        return new RecipeRowHolder(v, new MyCustomItmListener());
    }

    @Override
    public void onBindViewHolder(RecipeRowHolder holder, int position) {
        //In the old method: instead of create new MyCustomItmListener() in the onCreateViewHolder
        //we passed the local OnItemClickListener listener and assigned it in bind method
        holder.bind(items.get(position)/*, listener*/);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class RecipeRowHolder extends RecyclerView.ViewHolder {

        private TextView foodTitle;
        private TextView foodCalories;
        private TextView foodDuration;

        private MyCustomItmListener myCustomItmListener;

        public RecipeRowHolder(View itemView, MyCustomItmListener myCustomItmListener) {
            super(itemView);
            setFoodTitle((TextView) itemView.findViewById(R.id.name_recipeListItmRow_Layout));
            setFoodCalories((TextView) itemView.findViewById(R.id.cal_recipeListItmRow_Layout));
            setFoodDuration((TextView) itemView.findViewById(R.id.duration_recipeListItmRow_Layout));

            this.myCustomItmListener = myCustomItmListener;
            itemView.setOnClickListener(myCustomItmListener);
        }

        public void bind(final Recipe item/*, final OnItemClickListener listener*/) {

            getFoodTitle().setText(item.getName());
            String calories = item.getCalories() + "kj";
            getFoodCalories().setText(calories);

            //new approach: we create and assigned MyCustomItmListener myCustomItmListener directly
            //hence replace the need of final OnItemClickListener listener
            myCustomItmListener.update(item);

            /* Don't delete, this is a sub-step to understand how this works,
            create another inner class MyCustomItmListener in an upgrade for better performance,
            must understand and remember the below method first*/

            /*itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //This will be passed and executed in Fragment: Old method
                    listener.onItemClick(item);
                }
            });*/
        }

        public TextView getFoodTitle() {
            return foodTitle;
        }

        public void setFoodTitle(TextView foodTitle) {
            this.foodTitle = foodTitle;
        }

        public TextView getFoodCalories() {
            return foodCalories;
        }

        public void setFoodCalories(TextView foodCalories) {
            this.foodCalories = foodCalories;
        }

        public TextView getFoodDuration() {
            return foodDuration;
        }

        public void setFoodDuration(TextView foodDuration) {
            this.foodDuration = foodDuration;
        }

        /*public void setImg(Context context, String imgUri){
            ImageView imageView = (ImageView) mView.findViewById(R.id.list_item_foodList_img);
            Picasso.with(context).load(imgUri).into(imageView);
        }*/
    }

    private class MyCustomItmListener implements View.OnClickListener {
        private Recipe currentItm;

        public void update(Recipe currentItm) {
            this.currentItm = currentItm;
        }

        @Override
        public void onClick(View view) {
            //this is the same with the above old method
            listener.onItemClick(currentItm);
        }
    }
}
