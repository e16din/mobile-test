package com.e16din.goeurotest.screens.main;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.e16din.goeurotest.model.City;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends ArrayAdapter<String> {

    private List<String> mItems;

    public CitiesAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line);
    }

    public void addAll(ArrayList<City> originCollection) {
        List<String> newCollection = new ArrayList<>();
        for (City city : originCollection) {
            newCollection.add(city.getName());
        }

        mItems = newCollection;
        addAll(newCollection);
    }

    @Override
    public void clear() {
        super.clear();
        if (mItems != null) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            String mKeyword;

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return mKeyword;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mKeyword = constraint.toString();
                final FilterResults filterResults = new FilterResults();

                if (mItems != null) {
                    if (mItems.size() == 1 && mItems.get(0).equals(constraint)) {
                        //do nothing
                    } else {
                        filterResults.values = mItems;
                        filterResults.count = mItems.size();
                    }
                }

                return filterResults;
            }
        };
    }
}
