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
import com.hackensack.umc.datastructure.SymptomList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhagyashree_kumawat on 9/29/2015.
 */
public class SymptomListAdapter extends ArrayAdapter<SymptomList> implements Filterable {

    private List<SymptomList> mSymptomList = new ArrayList<SymptomList>();
    private List<SymptomList> filteredData = new ArrayList<SymptomList>();
    private Filter mFilter = new ItemFilter();
    private Context mContext;


    public SymptomListAdapter(Context context, List<SymptomList> symptomList) {

        super(context, -1, symptomList);
        mContext = context;
        mSymptomList = symptomList;
        filteredData = symptomList;

    }

    @Override
    public SymptomList getItem(int position) {
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
//        ImageView selectionImg = (ImageView)rowView.findViewById(R.id.selection_img);
        name.setText(filteredData.get(position).getTitle());

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

            final List<SymptomList> list = mSymptomList;

            int count = list.size();
            final ArrayList<SymptomList> nlist = new ArrayList<SymptomList>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                SymptomList symptomList = list.get(i);
                filterableString = symptomList.getTitle();
                if (filterableString.toLowerCase().contains(filterString)) {
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
            filteredData = (ArrayList<SymptomList>) results.values;
            notifyDataSetChanged();
        }

    }
}
