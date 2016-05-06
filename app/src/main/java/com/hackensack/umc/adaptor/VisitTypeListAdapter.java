package com.hackensack.umc.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hackensack.umc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhagyashree_kumawat on 9/29/2015.
 */
public class VisitTypeListAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> mSymptomList = new ArrayList<String>();
    private List<String> filteredData = new ArrayList<String>();
    private Filter mFilter = new ItemFilter();
    private Context mContext;


    public VisitTypeListAdapter(Context context, List<String> symptomList) {

        super(context, -1, symptomList);

        mContext = context;
        mSymptomList = symptomList;
        filteredData = symptomList;

    }

    @Override
    public String getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.direction_tv);

        name.setText(filteredData.get(position));

        return rowView;

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = mSymptomList;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                String symptomList = list.get(i);
                filterableString = list.get(i);//symptomList.getTitle();
                if (symptomList.toLowerCase().contains(filterString)) {
                    nlist.add(symptomList);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }
}
