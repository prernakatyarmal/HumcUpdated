package com.hackensack.umc.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackensack.umc.R;

/**
 * Created by Bhagyashree_kumawat on 9/29/2015.
 */
public class SpecialtyListAdapter extends ArrayAdapter<String> {

    private String[] directionsList;
    private Context mContext;
    private boolean isSpecialty;

    public SpecialtyListAdapter(Context context, String[] directions, boolean isSpecialty) {
        super(context, -1, directions);
        mContext = context;
        directionsList = directions;
        this.isSpecialty = isSpecialty;
    }

    @Override
    public String getItem(int position) {
        return directionsList[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        if (isSpecialty) {

            rowView = inflater.inflate(R.layout.list_item, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.direction_tv);
            name.setText(directionsList[position]);

        } else {

            if (position == 0) {
                rowView = inflater.inflate(R.layout.drawer_image, parent, false);
                rowView.setPadding(0, 0, 0, 0);
            } else {
                rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
                TextView name = (TextView) rowView.findViewById(R.id.direction_tv);
                name.setText(directionsList[position]);
                if (position == 8) {
                    name.setTextColor(Color.RED);
                }
            }
        }

        return rowView;

    }
}
