package com.example.myapp.mealplanner.Object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by John.nguyen on 07/12/2017.
 */

public class IngredientCountable extends Ingredient {
    public IngredientCountable() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public IngredientCountable(String name, HashMap<String, Measurement> melCal) {
        super(name, melCal);
    }

    public static final Parcelable.Creator<IngredientCountable> CREATOR =
            new Parcelable.Creator<IngredientCountable>() {
                public IngredientCountable createFromParcel(Parcel in) {
                    return new IngredientCountable(in);
                }

                public IngredientCountable[] newArray(int size) {
                    return new IngredientCountable[size];
                }
            };

    private IngredientCountable(Parcel in) {
        setName(in.readString());
        setCurrentCalories(in.readString());
        setCurrentQuantity(in.readString());
        setCurrentMeasurement(in.readString());
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getName());
        out.writeString(getCurrentCalories());
        out.writeString(getCurrentQuantity());
        out.writeString(getCurrentMeasurement());
    }

    @Override
    public void changeQuantityMeasurement(String currentKey) {
        // Method 1: Using Set or Collection or List
        // 1: Set<String> keys = getMeasurementDictMultiMap().keySet(); ["gram (g)","Cup (C)"]; //a = keys.get(0); ["gram"]; b = all.get(1); ["130"]
        // or 2: List<String> keys = (List<String>) getMeasurementDictMultiMap().keySet();
        // or 3: List<String> keys = new ArrayList<>(getMeasurementDictMultiMap().keySet());
        // or 4: List<String> yourList = new ArrayList<>(allInfo);
        // TODO: update alternative method with resources file
        // or 5:
        // Collection allInfo = (Collection) getMeasurementDictMultiMap().entrySet(); //[Gram, 139, Cup, 205];
        // Alternative we can use List<String> instead of array: List<String> values = getMeasurementDictMultiMap().values;
        // String[] values = (String[]) allInfo.toArray(new String[allInfo.size()]); //Same with: String[] values = (String[]) newValues.toArray(new String[0]);*/

        // Method 2: loop using each Map instead of each String
        // 1: using ForEach: getMeasurementDictMultiMap().keySet().forEach((key) -> System.out.println(key));
        // or 2: different way of for: for (Iterator<Map.Entry<String, String>> it = mapString.entrySet().iterator(); it.hasNext();) {
        // or 3: using Spliterator: Spliterator sit = getMeasurementDictMultiMap().entrySet().spliterator();
        // or 4: MapIterator<Integer, Integer> it = iterableMap.mapIterator();
        // or 5: for(Entry<String, String> e : m.entrySet()) { String key = e.getKey(); String value = e.getValue();
        // or 6: map.forEach((k,v)->System.out.println("Key: " + k + "Value: " + v));
        // or 7: Using Stream Api Java 8
        // or 8: Using MutableMap
        // or 9: Maps.transformEntries: special method which can help to convert value at the same time: e.g. [a, 2] -> [a, 8]
        // or 10: Using for loop with using Map variable: for (Map.Entry<String, String> entry : getMeasurementDictMultiMap().entrySet()) or KeySet()
        // or 11: Using for loop with String variable for (String key: getMeasurementDictMultiMap().keySet()) {
        // or 12: Display element by element using Iterator: while (it.hasNext()) it.next();
        // or 13: ListIterator (able to move backward instead of Iterator
        // or 14: Enumeration: Enumeration values = newValues.elements(); it.hasMoreElements())
        // or 15: Using For with counter i: for (int i = 0; i < getMeasurementDictMultiMap().keySet().size(); i++) { Object obj = keys.get(i);
        // or 16: Using For with counter i: int index = 0; for (Object key : map.keySet()) {Object value = map.get(key); ++index;

        if (getMeasurementDictMultiMap().size() > 1) {
            String newKey, checkKey;
            Iterator it = getMeasurementDictMultiMap().entrySet().iterator();
            int currentIndex = 0, index = 0;

            while (it.hasNext()) {
                checkKey = ((Map.Entry) it.next()).getKey().toString();
                Log.i("it.next()", checkKey);
                Log.i("currentKey", currentKey);
                if (!checkKey.equalsIgnoreCase(currentKey)) {
                    Log.i("equal", "false");

                    if (index > currentIndex) {
                        newKey = checkKey;

                        //Update to change value
                        setCurrentMeasurement(newKey);
                        setCurrentQuantity(getMeasurementDictMultiMap().get(newKey).getQuantity());
                        setCurrentCalories(getMeasurementDictMultiMap().get(newKey).getCalories());

                        break;
                    }
                } else {
                    Log.i("equal", "true");
                    currentIndex = index;
                }
                index++;
                if (index >= getMeasurementDictMultiMap().keySet().size()) {
                    //reset
                    it = getMeasurementDictMultiMap().entrySet().iterator();
                }
            }
        }
    }
}
