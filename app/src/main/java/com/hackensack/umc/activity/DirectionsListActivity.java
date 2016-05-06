package com.hackensack.umc.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.DirectionsListAdapter;
import com.hackensack.umc.datastructure.DirectionList;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DirectionsListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<DirectionList> directionList;
    private DirectionsListAdapter mAdapter;
    public static final String EXTRA_DIRECTION_OBJ = "directionObj";
    private static View helpView;
    private static FrameLayout frameLayout;
    private RelativeLayout helpLl;
    private View mAppointmentView;
    private int right = 0, left = 0, bottom = 0, screenWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (screenWidth == 0) {
            Display display = ((WindowManager) this.getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
            screenWidth = display.getWidth();
        }*/
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_list, null, false);
        listView = (ListView) mainView.findViewById(R.id.directionList);
        listView.setOnItemClickListener(this);
        directionList = populateDirectionList();
        //Help screen
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                if (right == 0 || bottom == 0) {
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    mAppointmentView = listView.getChildAt(2);
                    right = mAppointmentView.getRight();
                    bottom = mAppointmentView.getBottom();
                    left = mAppointmentView.getLeft();
                }
                showHelpView(layoutInflater);

            }
        });

        mAdapter = new DirectionsListAdapter(this, directionList);
        //int coord[]=new int[2];
        int rowht = 0;
        if (mAdapter != null) {
            listView.setAdapter(mAdapter);
           /* View v = mAdapter.getView(3, null, null);
            if (helpLl != null) {

                v.getLocationInWindow(coord);
                //helpLl.setY(coord[1]);
            }*/

        } else Log.e("Test", "adapter is null");
        // int coord[]=new int[2];
       /* View v = listView.getChildAt(3);
        if(v!=null && helpLl!=null){
            //helpLl.setLeft(v.getLeft());
            v.getLocationInWindow(coord);

        }*/
        frameLayout.addView(mainView);


        //setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        (findViewById(R.id.find_doc_ll)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DirectionsListActivity.this, DoctorSearchActivity.class);
                startActivity(i);
            }
        });


        listView = (ListView) findViewById(R.id.directionList);
        listView.setOnItemClickListener(this);
        directionList = populateDirectionList();
        mAdapter = new DirectionsListAdapter(this, directionList);
        if (mAdapter != null)
            listView.setAdapter(mAdapter);
        else Log.e("Test", "adapter is null");

    }

    private void showHelpView(LayoutInflater layoutInflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_DIRECTION)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_DIRECTION, false);
            helpView = layoutInflater.inflate(R.layout.direction_help, null, false);
            helpLl = (RelativeLayout) helpView.findViewById(R.id.help_ll);
            helpLl.setY(bottom);
            TextView arrowTv = (TextView) helpView.findViewById(R.id.help_arrow);
            arrowTv.setTypeface(Typeface.createFromAsset(getAssets(), "font/ti-glyph.ttf"));
            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(Util.getFontSegoe(this));
            // arrowTv.setX((left+right) / 2);
            ((TextView) helpView.findViewById(R.id.help_tv)).setTypeface(Util.getFontSegoe(this));
            //((TextView) helpView.findViewById(R.id.help_tv)).setX((left+right) / 4);
            (helpView.findViewById(R.id.help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                }
            });
            frameLayout.addView(helpView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_directions_list, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getBaseContext(), DirectionMapsActivity.class);
        intent.putExtra(EXTRA_DIRECTION_OBJ, mAdapter.getItem(position));
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                isBackPressed = true;
                super.onBackPressed();
                return true;

            case R.id.action_search:
//                    Toast.makeText(DoctorSearchActivity.this, "click on search : ", Toast.LENGTH_LONG).show();

//                startActivity(new Intent(DoctorFilterActivity.this, DoctorResultActivity.class));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<DirectionList> populateDirectionList() {
        ArrayList<DirectionList> directions = new ArrayList<DirectionList>();
        directions.add((new DirectionList("Emergency Room", "20 Prospect Avenue, Hackensack NJ 07601", "Emergency Room", 40.884590, -74.056501)));
        directions.add((new DirectionList("Children's Hospital", "20 Prospect Avenue, Hackensack NJ 07601", "Children's Hospital", 40.884515, -74.055552)));
        directions.add((new DirectionList("Children's Hospital Parking", "73 Second Street, Hackensack NJ 07601", "Children's Hospital Parking", 40.884144, -74.054640)));
        directions.add((new DirectionList("Dental Center", "60 2nd St, Hackensack, NJ 07601", "Dental Center", 40.883883, -74.054259)));
        directions.add((new DirectionList("HackensackUMC at Pascack Valley", "250 Old Hook Road, Westwood, NJ 07675", "HackensackUMC at Pascack Valley", 40.985319, -74.015464)));
        directions.add((new DirectionList("HackensackUMC Mountainside", "1 Bay Ave, Glen Ridge, NJ 07028", "HackensackUMC Mountainside", 40.812238, -74.203805)));
        directions.add((new DirectionList("John Theurer Cancer Center", "252 Atlantic Ave, Hackensack, NJ 07601", "John Theurer Cancer Center", 40.884456, -74.053902)));
        directions.add((new DirectionList("Main Entrance", "20 Prospect Avenue, Hackensack NJ 07601", "Main Entrance", 40.884093, -74.056702)));
        directions.add((new DirectionList("Medical Plaza", "20 Prospect Avenue, Hackensack NJ 07601", "Medical Plaza", 40.884146, -74.057303)));
        directions.add((new DirectionList("Palisades Medical Center", "7600 River Rd, North Bergen, NJ 07047", "Palisades Medical Center", 40.793608, -73.996099)));
        directions.add((new DirectionList("Parking", "20 Prospect Avenue, Hackensack NJ 07601", "Parking", 40.883747, -74.056995)));
        directions.add((new DirectionList("Pediatric ER", "20 Prospect Avenue, Hackensack NJ 07601", "Pediatric ER", 40.884801, -74.056383)));
        directions.add((new DirectionList("Research Ctr for Tomorrow's Children", "20 Prospect Avenue, Hackensack NJ 07601", "Research Ctr for Tomorrow's Children", 40.884552, -74.056890)));
        directions.add((new DirectionList("Women's Hospital", "20 Prospect Avenue, Hackensack NJ 07601", "Women's Hospital", 40.885051, -74.056236)));
        return directions;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUserLogout) {
            finish();
        }
    }
}
