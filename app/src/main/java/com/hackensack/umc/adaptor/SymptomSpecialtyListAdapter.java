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
public class SymptomSpecialtyListAdapter extends ArrayAdapter<String> implements Filterable{

    private List<String> mSpecialtyList = new ArrayList<String>();
    private List<String> filteredData  = mSpecialtyList;
    private Context mContext;
    private Filter mFilter = new SpecialtyFilter();

    public SymptomSpecialtyListAdapter(Context context, List<String> specialtyList) {

        super(context, -1, specialtyList);
        mContext = context;
        mSpecialtyList = specialtyList ;
        filteredData = specialtyList;

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

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item,parent,false);
        TextView name = (TextView)rowView.findViewById(R.id.direction_tv);
//        ImageView selectionImg = (ImageView)rowView.findViewById(R.id.selection_img);
        name.setText(getItem(position));

        return rowView;

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private class SpecialtyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = mSpecialtyList;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
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
