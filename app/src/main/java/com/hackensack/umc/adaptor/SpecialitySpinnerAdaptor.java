package com.hackensack.umc.adaptor;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */

 public class SpecialitySpinnerAdaptor extends ArrayAdapter<SpecialityItemHolder> {

        public SpecialitySpinnerAdaptor(Context context, int resource, List<SpecialityItemHolder> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return super.getCount() - 1; // This makes the trick: do not show last item
        }

        @Override
        public SpecialityItemHolder getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

    }