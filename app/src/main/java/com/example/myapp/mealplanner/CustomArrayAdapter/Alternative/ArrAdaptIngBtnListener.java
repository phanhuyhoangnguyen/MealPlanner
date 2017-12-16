package com.example.myapp.mealplanner.CustomArrayAdapter.Alternative;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.R;

import java.util.List;

/**
 * Created by John.nguyen on 18/11/2017.
 */

public class ArrAdaptIngBtnListener extends RecyclerView.Adapter<ArrAdaptIngBtnListener.IngredientRowHolder> {

    private List<Ingredient> listItems;
    private LayoutInflater inflater;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    public interface OnItmClickListener {
        // Whichever written in here will be implemented in Activity and also in this class: Interface control the installation part
        // however, the class doesn't define the actual mechanism but only reference and linked to Activity
        // The purpose to do this is because, the array hold reference to the current item, which is clicked
        // the array can be middle man to link this current item and activity to perform the desire actions
        void onItemLongClick(View view, Ingredient item);
        void onItemClick(View view, Ingredient item);
    }

    private final OnItmClickListener itmListener;

    /* private final OnBtnClickListener btnListener;
    public interface OnBtnClickListener {
        void onBtnClick(View view, IngredientCountable ing);
    }*/

    public ArrAdaptIngBtnListener(Context context, List<Ingredient> listItm, OnItmClickListener itmListener
                                             /*OnBtnClickListener btnListener,*/ ) {
        this.inflater = LayoutInflater.from(context);

        this.listItems = listItm;
        this.itmListener = itmListener;

        //this.btnListener = btnListener;
    }

    @Override
    public IngredientRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.layout_ingredient_list_item_btn_qun, parent, false);
        return new IngredientRowHolder(v, new MyViewClickListener());
    }

    @Override
    public void onBindViewHolder(IngredientRowHolder holder, int position) {
        holder.bind(listItems.get(position)/*, itmListener /*, btnListener*/);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onViewRecycled(IngredientRowHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class IngredientRowHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView ingName;
        private TextView ingCal;
        private TextView ingQuantity;
        private Button ingQunIn;
        private Button ingQunDe;

        private MyViewClickListener MyViewClickListener;

        //private MyBtnListener myBtnListener;

        /*//2nd method of Create Context Menu:
        private IngredientCountable mData;*/

        public IngredientRowHolder(View itemView, MyViewClickListener MyViewClickListener) {
            super(itemView);

            ingName = itemView.findViewById(R.id.ingName_listItm_Layout);
            ingCal = itemView.findViewById(R.id.ingCal_listItm_Layout);
            ingQuantity = itemView.findViewById(R.id.ingQun_listItm_Layout);
            ingQunIn = itemView.findViewById(R.id.ingQunBtnInc_listItm_Layout);
            ingQunDe = itemView.findViewById(R.id.ingQunBtnDed_listItm_Layout);

            this.MyViewClickListener = MyViewClickListener;

            itemView.setOnLongClickListener(MyViewClickListener);
            itemView.setOnClickListener(MyViewClickListener);

            //Button is also setOnClickListener, using same variable
            ingQunIn.setOnClickListener(MyViewClickListener);
            ingQunDe.setOnClickListener(MyViewClickListener);

            //1st method: implements View.OnCreateContextMenuListener directly from this class
            itemView.setOnCreateContextMenuListener(this);

            /*//2nd method: create another custom variable which implement View.OnCreateContextMenuListener
            itemView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);*/

        }

        /*//2nd method of Create Context Menu separate custom variable:
        private final View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (mData!= null) {
                    MenuItem myActionItem = menu.add("My Context Action");
                    myActionItem.setOnMenuItemClickListener(mOnMyActionClickListener);
                }
            }
        };

        private final MenuItem.OnMenuItemClickListener mOnMyActionClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        };

        //3rd way: implement ContextMenu.ContextMenuInfo directly on this class instead of OnCreateContextMenuListener*/


        public void bind(final Ingredient listItems/*, final OnItmClickListener itmListener /*, final OnBtnClickListener btnListener*/) {
            getIngName().setText(listItems.getName());
            getIngQuantity().setText(listItems.getCurrentQuantity().concat(listItems.getCurrentMeasurement()));
            getInsCalories().setText(String.valueOf(Float.valueOf(listItems.getCurrentCalories())).concat( "kj"));

            MyViewClickListener.update(listItems);

            /*//2nd method of Create Context Menu:
            mData = listItems;*/

            /* Don't delete, this is a sub-step to understand how this works,
            create another inner class MyCustomItmListener in an upgrade for better performance,
            must understand and remember the below method first*/
            /*ingQunIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //this is passed to activity, which implement this method
                    btnListener.onBtnClick(view, listItems);
                }
            });

            ingQunDe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnListener.onBtnClick(view, listItems);
                }
            });*/
        }

        public TextView getIngName() {
            return ingName;
        }

        public void setIngName(TextView ingName) {
            this.ingName = ingName;
        }

        public TextView getInsCalories() {
            return ingCal;
        }

        public void setIngCalories(TextView ingCalories) {
            this.ingCal = ingCalories;
        }

        public TextView getIngQuantity() {
            return ingQuantity;
        }

        public void setIngQuantity(TextView ingQuanity) {
            this.ingQuantity = ingQuanity;
        }

        //1st method: implement View.OnCreateContextMenuListener directly on this class
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {
            /*//this is also can be called in here but not only from fragment
            //1st way: Add manually with the menu define layout
            menu.setHeaderTitle("Select Action");
            menu.add(android.view.Menu.NONE, R.id.delete_option, android.view.Menu.NONE, "Remove");
            menu.add(android.view.Menu.NONE, R.id.hidden_option, android.view.Menu.NONE, "Hidden");
            menu.add(android.view.Menu.NONE, v.getId(), android.view.Menu.NONE, "Test");*/

            /*//2nd way: Add automatically using loop with array
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }*/
        }
    }

    //Final Design For Click Handling: Using 1 Variable for both OnLongClick Item Row and OnClickListener for Button, this variable implements both but not
    //not only one
    //Old Approach will declare 2 separate variables which each implement 1 only: either OnLongClick for item row
    //another variable implement OnClickListener for button
    private class MyViewClickListener implements View.OnLongClickListener, View.OnClickListener {
        private Ingredient currentItm;

        public void update(Ingredient currentItm) {
            this.currentItm = currentItm;
        }

        @Override
        public void onClick(View view) {
            //even though this is called by the array, because the array know which item is requested to called
            //however, this is abstract method, the implementation will be declared in the activity
            //this called what method was declared in the interface in the above part
            itmListener.onItemClick(view, currentItm);

            //this can also be called in here but not only from fragment
            /*switch (view.getId()) {
                case R.id.ing_listItm_ingQunBtn_Inc:
                    item.setCalories(String.valueOf((Integer.valueOf(item.getCalories()) * (Integer.valueOf(item.getQuantity()) + 50))
                            / Integer.valueOf(item.getQuantity())));
                    item.setQuantity(String.valueOf(Integer.valueOf(item.getQuantity()) + 50));
                    break;

                case R.id.ing_listItm_ingQunBtn_Ded:
                    if (Integer.valueOf(item.getQuantity()) > 50) {
                        item.setCalories(String.valueOf((Integer.valueOf(item.getCalories()) * (Integer.valueOf(item.getQuantity()) - 50))
                                / Integer.valueOf(item.getQuantity())));
                        item.setQuantity(String.valueOf(Integer.valueOf(item.getQuantity()) - 50));
                    }
                    break;

                default:
                    break;
            }
            arrayIngredientAdapter.notifyDataSetChanged();*/
        }

        @Override
        public boolean onLongClick(View view) {
            setPosition(getPosition());
            //this can also be called in here but not only from fragment
            //view.showContextMenu();

            itmListener.onItemLongClick(view, currentItm);
            return true;
        }
    }

}
