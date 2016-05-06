package com.hackensack.umc.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackensack.umc.R;

/**
 * Created by gaurav_salunkhe on 9/29/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

//
////    public SpinnerAdapter(Context theContext, List<Object> objects) {
//    public SpinnerAdapter(Context theContext, String[] objects) {
//        super(theContext, R.id.text1, R.id.text1, objects);
//    }
//
//    public SpinnerAdapter(Context theContext, List<Object> objects, int theLayoutResId) {
//        super(theContext, theLayoutResId, R.id.text1, objects);
//    }

    private Context context;
    private String[] values;

    public SpinnerAdapter(Context context, int textViewResourceId,
                          String[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
//        int count = super.getCount();
//        return count > 0 ? count - 1 : count;
        return super.getCount();
    }

    public String getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
//        TextView label = new TextView(context);
//        label.setTextColor(Color.BLACK);
//        // Then you can get the current item using the values array (Users array) and the current position
//        // You can NOW reference each method you has created in your bean object (User class)
//        label.setText(values[position]);

//        // And finally return your dynamic (or custom) view for each spinner item
//        return label;

        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = vi.inflate(R.layout.spinner_item, null);

        TextView textView = (TextView) view.findViewById(R.id.name_text);
        textView.setText(values[position]);

//        if (position == 0) {
//            textView.setVisibility(View.GONE);
//        }

//        if (position == getCount()) {
////            ((TextView)textView.findViewById(android.R.id.text1)).setText("");
//            textView.setHint(getItem(getCount())); //"Hint to be displayed"
//        }

//        if(spinnerCompeticion.getSelectedItemPosition()>0){
//            t.setText(spinnerArrayCompeticiones.get(spinnerCompeticion.getSelectedItemPosition()));
//        }else{
//            t.setText("Competiciones");
//        }

        return view;

    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
//    @Override
//    public View getDropDownView(int position, View convertView,
//                                ViewGroup parent) {
//
//        TextView label = new TextView(context);
//        label.setTextColor(Color.BLACK);
//        label.setText(values[position]);
//
//        return label;
//    }

}
