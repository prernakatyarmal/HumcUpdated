package com.hackensack.umc.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.datastructure.DirectionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhagyashree_kumawat on 9/29/2015.
 */
public class DirectionsListAdapter extends ArrayAdapter<DirectionList> {
    private List<DirectionList> directionsList;
    private Context mContext;

    public DirectionsListAdapter(Context context, List<DirectionList> directions) {
        super(context, -1, directions);
        mContext = context;
        directionsList = new ArrayList<DirectionList>();
        if (directions != null && !directions.isEmpty()) directionsList.addAll(directions);

    }

    @Override
    public DirectionList getItem(int position) {
        return directionsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.direction_tv);
        name.setText(getItem(position).getName());
        return rowView;

    }
}
