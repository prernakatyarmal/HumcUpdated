package com.hackensack.umc.adaptor;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackensack.umc.R;

import java.util.List;

import epic.mychart.android.library.open.IWPPatientAccess;

/**
 * Created by bhagyashree_kumawat on 3/1/2016.
 */
public class PatientListAdapter extends ArrayAdapter<IWPPatientAccess> {
    private Context mContext;
    private List<IWPPatientAccess> patientList;

    public PatientListAdapter(Context context, List<IWPPatientAccess> objects) {
        super(context, -1, objects);
        mContext = context;
        patientList = objects;
    }

    @Override
    public IWPPatientAccess getItem(int position) {
        return patientList.get(position);
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.direction_tv);
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setText(patientList.get(position).getName());
        return rowView;
    }
}
