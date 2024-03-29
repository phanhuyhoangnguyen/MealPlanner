package com.example.myapp.mealplanner.CustomArrayAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapp.mealplanner.Object.Ingredient;
import com.example.myapp.mealplanner.R;

import java.util.List;

/**
 * Created by John.nguyen on 20/11/2017.
 */

public class ArrAdaptIngEditTxt extends RecyclerView.Adapter<ArrAdaptIngEditTxt.IngredientRowHolder> {

    //Test IngredientCountable -> Ingredient
    //private List<IngredientCountable> listItems;
    private List<Ingredient> listItems;

    private LayoutInflater inflater;

    private String text;

    private final OnEditTextFinishedListener listener;

    public interface OnEditTextFinishedListener {
        void active(Editable text, Ingredient ing, int position);
    }

    public ArrAdaptIngEditTxt(Context context, List<Ingredient> listViews, OnEditTextFinishedListener listener) {
        this.listItems = listViews;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @Override
    public ArrAdaptIngEditTxt.IngredientRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.layout_ing_list_item_edit_text_qun, parent, false);
        return new ArrAdaptIngEditTxt.IngredientRowHolder(v, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(IngredientRowHolder holder, int position) {
        holder.bind(listItems.get(position), listener, position);
        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class IngredientRowHolder extends RecyclerView.ViewHolder {

        private TextView ingName;
        private TextView ingCal;
        private EditText ingQunEditTxt;
        private MyCustomEditTextListener myCustomEditTextListener;

        public IngredientRowHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);

            ingName = itemView.findViewById(R.id.ingName_listItm_Layout);
            ingCal = itemView.findViewById(R.id.ingCal_listItm_Layout);
            ingQunEditTxt = itemView.findViewById(R.id.ingQunInput_listItm_Layout);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.ingQunEditTxt.addTextChangedListener(myCustomEditTextListener);
        }

        public void bind(final Ingredient currentItm, final OnEditTextFinishedListener mListener, final int position) {
            myCustomEditTextListener.updatePosition(getAdapterPosition(), currentItm);
            if (text == null) {
                getInsCalories().setText(String.valueOf(Integer.valueOf(currentItm.getCurrentCalories())
                        * Integer.valueOf(currentItm.getCurrentQuantity()) / 100).concat("kj"));
            } else {
                ingQunEditTxt.setText(String.valueOf(Integer.valueOf(text)
                        * Integer.valueOf(currentItm.getCurrentQuantity()) / 100).concat("kj"));
            }
            //ingQunEditTxt.setText(mDataset[getAdapterPosition()]);

            getIngQuantity().setText(currentItm.getCurrentQuantity() + "g");
            getIngName().setText(currentItm.getName());
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
            return ingQunEditTxt;
        }

        public void setIngQuantity(EditText ingQuantity) {
            this.ingQunEditTxt = ingQuantity;
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;
        private String savedText;
        private Ingredient currentItm;

        public void updatePosition(int position, Ingredient currentItm) {
            this.position = position;
            this.savedText = currentItm.getCurrentCalories();
            this.currentItm = currentItm;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //mDataset[position] = charSequence.toString();
            //text = savedText;
            //notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            /*if (!secondCall) {*/
            //can't call notifyDataChange in onBindViewHolder, because this method will call onBindViewHolder again -> loop
            text = savedText;
            //notifyDataSetChanged();
            listener.active(editable, currentItm, position);
            //secondCall = true;
           /* }*/

        }
    }
}
