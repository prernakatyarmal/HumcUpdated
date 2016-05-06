package com.hackensack.umc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackensack.umc.R;
import com.hackensack.umc.adaptor.HackensackFeatureAdaptor;
import com.hackensack.umc.adaptor.MenuItemHolder;
import com.hackensack.umc.adaptor.PatientListAdapter;
import com.hackensack.umc.adaptor.SpecialtyListAdapter;
import com.hackensack.umc.datastructure.HttpResponse;
import com.hackensack.umc.datastructure.LoginUserData;
import com.hackensack.umc.datastructure.OnClearFromRecentService;
import com.hackensack.umc.datastructure.RssItem;
import com.hackensack.umc.datastructure.RssService;
import com.hackensack.umc.http.HttpDownloader;
import com.hackensack.umc.http.HttpUtils;
import com.hackensack.umc.http.ServerConstants;
import com.hackensack.umc.listener.HttpDownloadCompleteListener;
import com.hackensack.umc.listener.ILogoutInterface;
import com.hackensack.umc.listener.OnGridItemClickListener;
import com.hackensack.umc.patient_data.PatientMRN;
import com.hackensack.umc.util.BaseFeatureEnum;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import epic.mychart.android.library.open.IWPPatientAccess;
import epic.mychart.android.library.open.WPOpen;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HackensackUMCActivity extends BaseActivity implements OnGridItemClickListener.OnGridItemClickCallback, AdapterView.OnItemClickListener, HttpDownloadCompleteListener, OnGridItemClickListener.OnFeatureItemClickCallback, ILogoutInterface {

    /**
     * UI view variables
     */
    ImageView mUserImageView, mDrarIcon;

    ArrayList<MenuItemHolder> mMenuItems = new ArrayList<MenuItemHolder>();

    Button mLoginButton, mRegisterButton;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    //private HackensackGridAdaptor mAdapter;
    private HackensackFeatureAdaptor mAdapter;
    private ArrayAdapter<String> mAdapterNavDerawer;
    private ActionBarDrawerToggle mDrawerToggle;
    //Mychart Library variables
    private List<Map<String, Integer>> alertList;

//    private String mActivityTitle;

    private boolean isDrawerShown = false, isUserNameSet = false;

    private LoginUserData mPatient = null;
//    private ProgressDialog mProgressDialog;

    private LinearLayout mLoginLinearLayout;
    private RelativeLayout mMyDoctorLayout, mChangePatientLayout;
    private ImageView mUserImage;
    private int aptRight, aptBottom, apttop, drawerRight, drawerbottom, toolbarht, symBottom, myaptLeft, myaptTop;
    private FrameLayout frameLayout;
    private LayoutInflater layoutInflater;
    private TextView mUserNameTv;

    Intent mTaskCleanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
//Creating framelayout to populate help view
        frameLayout = new FrameLayout(this);
        // creating LayoutParams
        FrameLayout.LayoutParams frameLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(frameLayout, frameLayoutParam);
        layoutInflater = LayoutInflater.from(this);
        View mainView = layoutInflater.inflate(R.layout.activity_hackensack_umc, null, false);

        //setContentView(R.layout.activity_hackensack_umc);
        frameLayout.addView(mainView);

        mUserNameTv = (TextView) findViewById(R.id.user_name_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isUserLogout = false;
                startActivity(new Intent(HackensackUMCActivity.this, LoginActivity.class));
//             startActivity(new Intent(HackensackUMCActivity.this, ProfileSelfieActivity.class));
            }

        });
        mLoginButton.setVisibility(View.VISIBLE);
//        mRegisterButton = (Button) findViewById(R.id.register_button);
        mUserImage = (ImageView) findViewById(R.id.user_imageView);

        initialiseSlidenavigator();
        mDrarIcon = (ImageView) findViewById(R.id.drawer_icon);
        mDrarIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
                isDrawerShown = true;
            }

        });

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // mAdapter = new HackensackGridAdaptor(getMenuItemList(4,true), this);
        mAdapter = new HackensackFeatureAdaptor(BaseFeatureEnum.getGuestFeature(), this, HackensackUMCActivity.this);
        mRecyclerView.setAdapter(mAdapter);

//Help screen
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                aptRight = (mRecyclerView.getChildAt(0).findViewById(R.id.img_thumbnail)).getRight();
                aptBottom = mRecyclerView.getChildAt(0).getTop();
                //apttop =mLayoutManager.getDecoratedTop(mRecyclerView.getChildAt(0));
                drawerbottom = mDrarIcon.getTop();
                drawerRight = mDrarIcon.getRight();
                symBottom = mRecyclerView.getChildAt(3).getTop();
                toolbarht = (findViewById(R.id.tool_bar)).getHeight();
                //int left = mLayoutManager.getDecoratedLeft((mRecyclerView.getChildAt(1)));
                myaptLeft = mLayoutManager.getDecoratedLeft((mRecyclerView.getChildAt(1))); /*+ ((mLayoutManager.getDecoratedRight((mRecyclerView.getChildAt(1))) - left) / 2);*//*((mRecyclerView.getChildAt(3)).findViewById(R.id.img_thumbnail)).getRight();*//*left + (((mRecyclerView.getChildAt(1).findViewById(R.id.img_thumbnail)).getRight() - left) / 2)*/
                myaptTop = mLayoutManager.getDecoratedTop((mRecyclerView.getChildAt(0)));

                //showHelpOverlay(layoutInflater);
                // Here you can get the size :)
            }
        });
        Intent intent = getIntent();
        String calledClass = intent.getStringExtra(Constant.ACTIVITY_FLOW);
        if (intent != null && calledClass != null && calledClass.equals(Constant.SHOW_EMERGENCY_POP_UP)) {

            showSessionTimeOutPopUp = false;

            CountDownTimer delayCounter = new CountDownTimer(500, 500) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {

                    //  Util.showNetworkOfflineDialog(HackensackUMCActivity.this, "", "If you are experiencing a life-threading medical emergency or require immediate medical assistance. Please call 911 now or go to the nearest emergency room");

                    if (!isFinishing()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(HackensackUMCActivity.this);

                        final LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_network_offline, null);
                        builder.setView(dialogView);

                        (dialogView.findViewById(R.id.dialog_title)).setVisibility(View.GONE);
                        ((TextView) dialogView.findViewById(R.id.text_message)).setText(getString(R.string.life_threatening_message));

//                        ((TextView) dialogView.findViewById(R.id.button_dialog_cancel)).setVisibility(View.GONE);
                        TextView btnNo = (TextView) dialogView.findViewById(R.id.button_dialog_cancel);
                        btnNo.setText(getString(R.string.button_cancel));
                        btnNo.setVisibility(View.VISIBLE);
                        btnNo.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alert.dismiss();

                            }
                        });

                        Button btnOk = (Button) dialogView.findViewById(R.id.button_dialog_ok);
                        btnOk.setText(getString(R.string.call_text));
                        btnOk.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"));
                                startActivity(callIntent);

                                alert.dismiss();

                            }
                        });
                        alert = builder.show();
                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                showHelpOverlay(layoutInflater);
                            }
                        });

                    }

                }
            };

            delayCounter.start();
        }


        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linear_layout);
        mChangePatientLayout = (RelativeLayout) findViewById(R.id.change_patient_rl);
        mMyDoctorLayout = (RelativeLayout) findViewById(R.id.my_doctor_layout);
        mMyDoctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HackensackUMCActivity.this, FavoriteDoctorList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });


//        registerReceiver(mConnectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        intentfilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentfilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mSleepModeReceiver, intentfilter);

        mTaskCleanService = new Intent(getBaseContext(), OnClearFromRecentService.class);
        startService(mTaskCleanService);

    }

    private void initialiseSlidenavigator() {

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mActivityTitle = getString(R.string.title_activity_hackensack_umc);
        //Now called adddrawer in onresume()
        // addDrawerItems();
        setupDrawer();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_hackensack_umc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<MenuItemHolder> getMenuItemList(int noOfItem) {

        String[] menuItemsStringArray = null /*= getResources().getStringArray(R.array.menuitem_string_array)*/;
//        String[] menuItemsIconArray = getResources().getStringArray(R.array.menuitem_icon_array);
        int[] menuItemsIconArray = new int[]{R.drawable.appointment_icon, R.drawable.doctor_search_icon,
                R.drawable.symptom_checker, R.drawable.direction_icon,
                R.drawable.surveys_icon};

        ArrayList<MenuItemHolder> menuItems = new ArrayList<MenuItemHolder>();

        for (int count = 0; count < noOfItem; count++) {

            MenuItemHolder item = new MenuItemHolder();
            item.setName(menuItemsStringArray[count]);
            item.setThumbnail(menuItemsIconArray[count]);
            menuItems.add(item);

        }

        return menuItems;
    }

    @Override
    public void onGridItemClicked(View view, int position) {

//        mUserLoggingOutManually = false;

        switch (position) {

            case 0:
                if (Util.ifNetworkAvailableShowProgressDialog(this, "", false)) {
                    startActivity(new Intent(HackensackUMCActivity.this, ViewAppointmentActivity.class));
                }
                break;

            case 1:

                startActivity(new Intent(HackensackUMCActivity.this, DoctorSearchActivity.class));

                break;

            case 2:

                startActivity(new Intent(HackensackUMCActivity.this, SymptomCheckerActivity.class));

                break;

            case 3:

                startActivity(new Intent(HackensackUMCActivity.this, DirectionsListActivity.class));

                break;

            case 4:

                break;
        }


    }

    private void addDrawerItems() {
        String[] osArray = null;
        if (Util.isUserLogin(this) && Util.isPatientIdValid(this)) {
//            osArray = new String[]{"", "ER Wait Time", "About HackensackUMC", "HackensackUMC News", "Turn on Help", "View Profile", "Logout"};
            osArray = new String[]{"", "ER Wait Time", "About HackensackUMC", "HackensackUMC News", "Go to MyChart", "Turn on Help", "Send Feedback", "View Profile", "Logout"};
        }else if(isMyChartLogin){
            osArray = new String[]{"", "ER Wait Time", "About HackensackUMC", "HackensackUMC News", "Go to MyChart", "Turn on Help", "Send Feedback", "View Profile", "Logout"};
        } else {
//            osArray = new String[]{"", "ER Wait Time", "About HackensackUMC", "HackensackUMC News", "Turn on Help"};
            osArray = new String[]{"", "ER Wait Time", "About HackensackUMC", "HackensackUMC News", "Go to MyChart", "Turn on Help", "Send Feedback"};
        }
//        mAdapterNavDerawer = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
//        mAdapterNavDerawer = new ArrayAdapter<String>(this, R.layout.drawer_list_item, osArray);
        mAdapterNavDerawer = new SpecialtyListAdapter(this, osArray, false);
        mDrawerList.setAdapter(mAdapterNavDerawer);
        mDrawerList.setOnItemClickListener(this);
    }

    private void startService() {
        Intent intent = new Intent(this, RssService.class);
        intent.putExtra(RssService.RECEIVER, resultReceiver);
        this.startService(intent);
    }

    AlertDialog alert;
    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (!isFinishing()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HackensackUMCActivity.this);

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.dialog_er_time_custom, null);
                builder.setView(popupView);

//                ((ProgressBar) popupView.findViewById(R.id.progress_bar)).setVisibility(View.GONE);
//                ((ListView) popupView.findViewById(R.id.list_specialty)).setVisibility(View.GONE);
//                ((TextView) popupView.findViewById(R.id.button_dialog_cancel)).setVisibility(View.GONE);

                Button btnDismiss = (Button) popupView.findViewById(R.id.button_dialog_ok);
                btnDismiss.setVisibility(View.GONE);
//                btnDismiss.setOnClickListener(new Button.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        alert.dismiss();
//                    }
//                });

                List<RssItem> items = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
//                ((LinearLayout) popupView.findViewById(R.id.er_wait_time_list)).setVisibility(View.VISIBLE);
                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {

                        TextView title1 = (TextView) popupView.findViewById(R.id.title1);
                        TextView description1 = (TextView) popupView.findViewById(R.id.discription1);
                        TextView title2 = (TextView) popupView.findViewById(R.id.title2);
                        TextView description2 = (TextView) popupView.findViewById(R.id.discription2);

                        ((TextView) popupView.findViewById(R.id.dialog_title)).setText(items.get(0).getTitle().toString());
                        title1.setText(items.get(1).getTitle().toString());
                        description1.setText(getAdjustedTimeString(Integer.parseInt(items.get(1).getDescrString())));
                        title2.setText(items.get(2).getTitle().toString());
                        description2.setText(getAdjustedTimeString(Integer.parseInt(items.get(2).getDescrString())));

                    }
//                    alert = builder.show();
                } else {
                    (popupView.findViewById(R.id.title1)).setVisibility(View.GONE);
                    (popupView.findViewById(R.id.discription1)).setVisibility(View.GONE);
                    (popupView.findViewById(R.id.title2)).setVisibility(View.GONE);
                    ((TextView) popupView.findViewById(R.id.dialog_title)).setText("HackensackUMC");
                    TextView description2 = (TextView) popupView.findViewById(R.id.discription2);
                    description2.setText(getString(R.string.error_description));
                }
                alert = builder.show();
            }

        }

        ;
    };

    private String getAdjustedTimeString(int minutes) {
        String adjustedTimeString = "";
        if (minutes <= 15) {
            adjustedTimeString = "Less than 15 minutes";
        } else if (minutes <= 30) {
            adjustedTimeString = "Less than 30 minutes";
        } else if (minutes <= 45) {
            adjustedTimeString = "Less than 45 minutes";
        } else if (minutes <= 60) {
            adjustedTimeString = "Less than 1 hour";
        } else if (minutes <= 75) {
            adjustedTimeString = "Less than 1 hour and 15 minutes";
        } else if (minutes <= 90) {
            adjustedTimeString = "Less than 1 hour and 30 minutes";
        } else if (minutes <= 105) {
            adjustedTimeString = "Less than 1 hour and 45 minutes";
        } else if (minutes <= 120) {
            adjustedTimeString = "Less than 2 hours";
        } else if (minutes > 120) {
            adjustedTimeString = "more than 2 hours";
        }
        return adjustedTimeString;
    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0) {

            /** Called when a drawer has settled in a completely open state. */
            @SuppressLint("NewApi")
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            @SuppressLint("NewApi")
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

//        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (isDrawerShown) {
            mDrawerLayout.closeDrawers();
            isDrawerShown = false;
            return;
        }
//        else {
//            isAppClose = true;
//        }

        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


        switch (position) {

//                        case 0:
//
//                            mDrawerLayout.closeDrawers();
//
//                            break;

            case 1:
                if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, "", false)) {
                    Constant.url = "http://mdnetap1.humcmd.net/resources/rssfeeds/edwt/humcedwt.xml";
                    startService();
                    mDrawerLayout.closeDrawers();
                }
                break;

            case 2:
                if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, "", false)) {
                    Intent aboutIintent = new Intent(HackensackUMCActivity.this, WebViewActivity.class);
                    aboutIintent.putExtra(Constant.IS_ABOUT, true);
                    startActivity(aboutIintent);

                    mDrawerLayout.closeDrawers();
                }
                break;

            case 3:
                if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, "", false)) {
                    Constant.url = "http://www.hackensackumc.org/rss/news.aspx";
                    Intent newsIntent = new Intent(HackensackUMCActivity.this, NewsActivity.class);
                    startActivity(newsIntent);
                    mDrawerLayout.closeDrawers();
                }

                break;
//            osArray = new String[]{"", "ER Wait Time", "About HackensackUMC", "HackensackUMC News", "Go to MyChart", "Turn on Help", "Send Feedback"};

            case 4:

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=epic.mychart.android"));
                    startActivity(intent);

                } catch (Exception e) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=epic.mychart.android"));
                    startActivity(intent);

                }

                break;

            case 5:

                Util.setAllHelpTrue(HackensackUMCActivity.this);
                mDrawerLayout.closeDrawers();
                showHelpOverlay(layoutInflater);

                break;

            case 6:

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"AppFeedback@hackensackumc.org"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Android HackensackUMC App");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
                /// use below 2 commented lines if need to use BCC an CC feature in email
                //emailIntent.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{to});
                ////use below 3 commented lines if need to send attachment
//                emailIntent .setType("image/jpeg");
//                emailIntent .putExtra(Intent.EXTRA_SUBJECT, "My Picture");
//                emailIntent .putExtra(Intent.EXTRA_STREAM, Uri.parse("file://sdcard/captureimage.png"));
//
//                //need this to prompts email client only
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));

                break;

            case 7:

                if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, "", false)) {
                  if(!TextUtils.isEmpty(Util.getPatientMRNID(HackensackUMCActivity.this))){
                        Intent profileIntent = new Intent(HackensackUMCActivity.this, ViewProfileActivity.class);
                        startActivity(profileIntent);
                    }else{
                        Util.showProxyFormDialogue(HackensackUMCActivity.this);
                    }
                    mDrawerLayout.closeDrawers();
                }


                break;
            case 8:
                //Display Log Out
                Util.stopUserSession(this);
                WPOpen.initialize(HackensackUMCActivity.this);
                WPOpen.doLogOut(HackensackUMCActivity.this);
                Util.stopMychartTokenSync(HackensackUMCActivity.this);
                Util.stopDeviceSync(HackensackUMCActivity.this);
                if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, "", false)) {
                    if (Util.isNetworkAvailable(HackensackUMCActivity.this)) {
                        HttpDownloader http = new HttpDownloader(HackensackUMCActivity.this, ServerConstants.LOGOUT_URL, Constant.LOGOUT_DATA, HackensackUMCActivity.this);
                        http.startDownloading();
                        startProgress(this, "Logging out...");
                        userLoggingOutManually = true;
                    }
                    mDrawerLayout.closeDrawers();
                }
                break;

        }


        mDrawerLayout.closeDrawers();


        isDrawerShown = false;

    }


    @Override
    protected void onResume() {
//        super.onResume();
        addDrawerItems();

        if (showSessionTimeOutPopUp/* && Util.getLoggedInType(this) != Constant.UNKNOWN_LOGIN*/) {

            if (mDrawerLayout != null)
                mDrawerLayout.closeDrawers();

            Util.showNetworkOfflineDialog(this, "Session Timed Out", "Your session was ended due to inactivity");
            showSessionTimeOutPopUp = false;
            mAdapter = new HackensackFeatureAdaptor(BaseFeatureEnum.getGuestFeature(), this, HackensackUMCActivity.this);
            //mAdapter.notifyDataSetChanged();
            isMyChartLogin = false;
        }

        Util.isActivityFinished = false;
        isUserLogout = false;
        isLoginDone = false;
        isAppointmentDone = false;
        Util.isRegistrationFlowFinished = false;
        isConsentDone = false;

        try {
            String token = Util.getToken(this);
            Log.e("On Resume", "Token -> " + token);

            if (token != null && token.length() > 1) {
                String patientData = Util.getPatientJSON(this);
                try{
                mPatient = new LoginUserData(new JSONObject(patientData));}catch (JSONException e){
                    Log.e("readPatient","Exception occured while reading patient data",e);
                }
                
                if (WPOpen.getPatients() != null && WPOpen.getPatients().size() > 1) {

                    mChangePatientLayout.setVisibility(View.VISIBLE);
                    mChangePatientLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPatientsListDialog(WPOpen.getPatients());
                        }
                    });
                }else{
                    mChangePatientLayout.setVisibility(View.INVISIBLE);
                }
                if (!isUserNameSet) {
                    String mUsername = "";
                    if (patientData != null && patientData.length() > 1) {
                        mUsername = mPatient.getFirstName() + " " + mPatient.getLastName();
                        String basePhoto = mPatient.getPhoto();
                        if (basePhoto != null && basePhoto.length() > 1) {
                            if ((Util.getDeviceWidth(this) - 100) <= Constant.SCREEN_MIN_WIDTH && (Util.getDeviceHeight(this) - 100) <= Constant.SCREEN_MIN_HEIGHT) {
                                mUserImage.setImageBitmap(Util.getRoundedUserImage(this, 140, 120, mPatient.getPhoto()));
                            } else {
                                mUserImage.setImageBitmap(Util.getRoundedUserImage(this, 230, 200, mPatient.getPhoto()));
                            }
                        }
                        //Setting up grid for logged in USER
                        mAdapter = new HackensackFeatureAdaptor(BaseFeatureEnum.getAllFeature(), this, HackensackUMCActivity.this);
                        mLoginLinearLayout.setVisibility(View.VISIBLE);
                        mLoginButton.setVisibility(View.GONE);
                        mUserNameTv.setText("Hi " + mUsername);
                    } else if (isMyChartLogin && WPOpen.getCurrentPatient() != null) {
                        mUsername = WPOpen.getCurrentPatient().getName();
                        //Setting up grid for logged in USER
                        mAdapter = new HackensackFeatureAdaptor(BaseFeatureEnum.getAllFeature(), this, HackensackUMCActivity.this);
                        mLoginLinearLayout.setVisibility(View.VISIBLE);
                        mLoginButton.setVisibility(View.GONE);
                        mUserNameTv.setText("Hi " + mUsername);
                    } else {
                        mLoginLinearLayout.setVisibility(View.GONE);
                        mLoginButton.setVisibility(View.VISIBLE);
                    }
                }


                //Util.startUserSession(this);
                //  showHelpOverlay(layoutInflater);
            } else {

                if (!(token != null && token.length() > 1)) {
                    updateLogoutUI();
                } else {
                    mLoginLinearLayout.setVisibility(View.GONE);
                    mLoginButton.setVisibility(View.VISIBLE);
                }
                isMyChartLogin = false;
            }
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            Log.e("isUserLogin", "" + e.toString());
            updateLogoutUI();
        }

        mLoginLinearLayout.invalidate();
        mLoginButton.invalidate();
        mDrawerList.invalidate();
        mDrawerLayout.invalidate();

        super.onResume();

    }

    @Override
    public void HttpDownloadCompleted(HttpResponse data) {

//        Object obj = data.getDataObject();

        if ((data.getResponseCode() >= Constant.HTTP_OK && data.getResponseCode() < Constant.HTTP_REDIRECT)) {
//        if (obj != null && !isFinishing()) {

            if (!Util.isUserLogin(this) && !Util.isPatientIdValid(this)) {
                updateLogoutUI();
            }
            stopProgress();
            addDrawerItems();

            if (userLoggingOutManually) {
                Intent intent = new Intent();
                intent.setClass(HackensackUMCActivity.this, LoginActivity.class);
                intent.putExtra(Constant.ACTIVITY_FLOW, Constant.SHOW_LOGIN_SCREEN);
                startActivity(intent);
                userLoggingOutManually = false;
                isUserLogout = false;
            }

        } else {
            stopProgress();
            Util.showNetworkOfflineDialog(this, "Error", "You are not logout. Please try again later");
        }


    }

    public void updateLogoutUI() {

//        HttpDownloader http = new HttpDownloader(HackensackUMCActivity.this, Constant.LOGOUT_URL, Constant.LOGOUT_DATA, null);
//        http.startDownloading();
        isMyChartLogin = false;
        isUserNameSet = false;
        Log.e("Login Session", "updateLogoutUI : Landing Screen UI updated ");
        WPOpen.initialize(HackensackUMCActivity.this);
        WPOpen.doLogOut(HackensackUMCActivity.this);
        Util.stopMychartTokenSync(HackensackUMCActivity.this);
        Util.stopDeviceSync(HackensackUMCActivity.this);
        mAdapter = new HackensackFeatureAdaptor(BaseFeatureEnum.getGuestFeature(), this, HackensackUMCActivity.this);
        mAdapter.notifyDataSetChanged();

        mLoginLinearLayout.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.user_name_text)).setText(getString(R.string.hi_guest_text));
        Util.setToken(this, "", "", 0);
        Util.setPatientMRNID(this, "");
        Util.setPatientJSON(this, "");
        mUserImage.setImageDrawable(getResources().getDrawable(R.drawable.user));

        Util.setToken(this, "", "", 0, Constant.UNKNOWN_LOGIN);
//        if(BaseActivity.isUserLogout) {
//            Util.showNetworkOfflineDialog(this, "Session Timed Out", "Your session was ended due to inactivity");
//            isUserLogout = false;
//        }

//        mLoginLinearLayout.invalidate();
//        mLoginButton.invalidate();
//        mDrawerList.invalidate();
//        mDrawerLayout.invalidate();
    }

  /*  public void startProgress() {
        mProgressDialog.setMessage("Logging out...");
        mProgressDialog.show();
    }
*/

//    private boolean isDeviceSleep = false;
//    private boolean isDeviceScreenOn = false;


    private BroadcastReceiver mSleepModeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String id = Util.getPatientMRNID(context);

            if (intent != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                if (!isDeviceSleep) {
                    Log.e("Login Session", "In Sleep Mode");
                    isDeviceSleep = true;
                    Util.stopUserSession(context);
                }

            } else if (intent != null && (intent.getAction().equals(Intent.ACTION_USER_PRESENT) || intent.getAction().equals(Intent.ACTION_SCREEN_ON))) {

                if (isDeviceSleep) {
                    isDeviceSleep = false;
                    Log.e("Login Session", "In ACTION_USER_PRESENT Mode");


                    if (!Util.isUserLogin(context)) {

                        Log.e("Login Session", "In ACTION_USER_PRESENT Mode : User Logout");

//                        if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, getString(R.string.loading_text), false)) {
//                            HttpDownloader http = new HttpDownloader(context, Constant.LOGOUT_URL, Constant.LOGOUT_DATA, null);
//                            http.startDownloading();
//                        }

                        isUserLogout = true;

                        if (context instanceof HackensackUMCActivity) {
                            ((HackensackUMCActivity) context).updateLogoutUI();
                        }

                        Util.stopUserSession(context);

//                        if (BaseActivity.isAppWentToBg || BaseActivity.isDeviceSleep) {
//                            //Code for Session Management
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                        }
                    } else {

                        Util.startUserSession(context);

                    }
                }

            }
//            else if (intent != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//
//                if (isDeviceSleep) {
//                    isDeviceSleep = false;
//                    Log.e("Login Session", "In ACTION_SCREEN_ON Mode");
//
////                    if (id != null && id.length() > 5)
//                    {
//                        if (!Util.startUserSession(context)) {
//
//                            Log.e("Login Session", "In ACTION_SCREEN_ON Mode : User Logout");
//
//                            if (Util.ifNetworkAvailableShowProgressDialog(HackensackUMCActivity.this, getString(R.string.loading_text), false)) {
//                                HttpDownloader http = new HttpDownloader(context, Constant.LOGOUT_URL, Constant.LOGOUT_DATA, null);
//                                http.startDownloading();
//                            }
//
//                            isUserLogout = true;
//                            showSessionTimeOutPopUp = true;
//
//                            if (context instanceof HackensackUMCActivity) {
//                                ((HackensackUMCActivity) context).updateLogoutUI();
//                            }
//
//                            Util.stopUserSession(context);
//
////                            if (BaseActivity.isAppWentToBg || BaseActivity.isDeviceSleep) {
////                                //Code for Session Management
////                                android.os.Process.killProcess(android.os.Process.myPid());
////                            }
//                        }
//                    }
//                }
//            }
        }
    };

    @Override
    protected void onDestroy() {

        if (mTaskCleanService != null)
            stopService(mTaskCleanService);

        unregisterReceiver(mSleepModeReceiver);

        super.onDestroy();
    }

    @Override
    public void didLogout() {
        updateLogoutUI();
    }

    public void showHelpOverlay(LayoutInflater inflater) {
        if (Util.gethelpboolean(this, Constant.HELP_PREF_HACKENSACK)) {
            Util.storehelpboolean(this, Constant.HELP_PREF_HACKENSACK, false);

            Typeface arrowFont = Util.getArrowFont(this), segoeFont = Util.getFontSegoe(this);
            final View helpView = inflater.inflate(R.layout.landing_page_help_before_login, null, false);
            (helpView.findViewById(R.id.apt_help_ll)).setY(aptBottom + toolbarht);
            ((TextView) helpView.findViewById(R.id.apt_help_arrow)).setTypeface(arrowFont);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            (helpView.findViewById(R.id.apt_help_ll)).setX(aptRight);
            ((TextView) helpView.findViewById(R.id.apt_help_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.sym_help_arrow)).setTypeface(arrowFont);
            ((TextView) helpView.findViewById(R.id.sym_help_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.apt_help_tv)).setTypeface(segoeFont);
            ((TextView) helpView.findViewById(R.id.drawer_help_tv)).setTypeface(segoeFont);
            if ((Util.isUserLogin(this) && Util.isPatientIdValid(this))
                    || isMyChartLogin) {
                ((TextView) helpView.findViewById(R.id.apt_help_tv)).setText(R.string.landing_apt_help_login);
                ((TextView) helpView.findViewById(R.id.sym_help_tv)).setText(R.string.landing_apt_help_mychart);
                (helpView.findViewById(R.id.search_help_arrow)).setVisibility(View.VISIBLE);
                ((TextView) helpView.findViewById(R.id.search_help_arrow)).setTypeface(arrowFont);
                ((TextView) helpView.findViewById(R.id.drawer_help_tv)).setGravity(Gravity.CENTER_HORIZONTAL);
                ((TextView) helpView.findViewById(R.id.drawer_help_tv)).setText(getString(R.string.landing_apt_help_mychart));
                (helpView.findViewById(R.id.drawer_help_ll)).setY(myaptTop + (toolbarht / 2));
                (helpView.findViewById(R.id.drawer_help_ll)).setX(myaptLeft);
                (helpView.findViewById(R.id.sym_help_ll)).setVisibility(View.GONE);
                (helpView.findViewById(R.id.drawer_arrow)).setVisibility(View.GONE);


            } else {

                (helpView.findViewById(R.id.drawer_help_ll)).setVisibility(View.VISIBLE);
                (helpView.findViewById(R.id.sym_help_ll)).setY(symBottom + toolbarht);
                (helpView.findViewById(R.id.sym_help_ll)).setX(aptRight);
                (helpView.findViewById(R.id.drawer_help_ll)).setY(drawerbottom / 2);
                (helpView.findViewById(R.id.drawer_help_ll)).setX(drawerRight);

//                params.setMargins(0, 15, 10, 0);
//                ((RelativeLayout) helpView.findViewById(R.id.drawer_help_ll)).setLayoutParams(params);
                ((RelativeLayout.LayoutParams) (helpView.findViewById(R.id.drawer_help_ll)).getLayoutParams()).setMargins(0, 15, 10, 0);

                ((TextView) helpView.findViewById(R.id.drawer_arrow)).setTypeface(arrowFont);


            }

            ((TextView) helpView.findViewById(R.id.tap_tv)).setTypeface(segoeFont);
            //for dismissing anywhere you touch
            (helpView.findViewById(R.id.bef_login_help_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frameLayout.removeView(helpView);
                }
            });
            frameLayout.addView(helpView);
        }
    }

    @Override
    public void onFeatureItemClicked(View view, BaseFeatureEnum feature) {
        Intent intent = null;
        switch (feature.ordinal()) {
            case 0:
                //Make_Appointment
                if (Util.ifNetworkAvailableShowProgressDialog(this, "", false)) {
                    startActivity(new Intent(HackensackUMCActivity.this, ViewAppointmentActivity.class));
                }
                break;
            case 1:
                //My_Appointments
                intent = WPOpen.makeAppointmentsIntent(HackensackUMCActivity.this);
                if (intent == null) {
                    //showDialog unable to
                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
                } else {
                    startActivity(intent);
                }
                break;
            case 2:
                // Doctor_Search
                startActivity(new Intent(HackensackUMCActivity.this, DoctorSearchActivity.class));
                break;
            case 3:
                //Symptom_Checker
                startActivity(new Intent(HackensackUMCActivity.this, ProfileSelfieActivity.class));
                break;
            case 4:
                //Directions
                startActivity(new Intent(HackensackUMCActivity.this, DirectionsListActivity.class));
                break;
            case 5:
                //Test_Results
                intent = WPOpen.makeTestResultsIntent(HackensackUMCActivity.this);
                if (intent == null) {
                    //showDialog unable to
                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
                } else {
                    startActivity(intent);
                }
                break;
            case 6:
                //Health_Reminders
                intent = WPOpen.makeHealthRemindersIntent(HackensackUMCActivity.this);
                if (intent == null) {
                    //showDialog unable to
                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
                } else {
                    startActivity(intent);
                }
                break;
            case 7:
                //Messages
                intent = WPOpen.makeMessagesIntent(HackensackUMCActivity.this);
                if (intent == null) {
                    //showDialog unable to
                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
                } else {
                    startActivity(intent);
                }
                break;
            case 8:
                // Medication
                intent = WPOpen.makeMedicationsIntent(HackensackUMCActivity.this);
                if (intent == null) {
                    //showDialog unable to
                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
                } else {
                    startActivity(intent);
                }
                break;
            case 9:
                //Track_My_Health
                intent = WPOpen.makeHealthSummaryIntent(HackensackUMCActivity.this);
                if (intent == null) {
                    //showDialog unable to
                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
                } else {
                    startActivity(intent);
                }
                break;
//            case 10:
//                //Health_Summary
//                intent = WPOpen.makeHealthSummaryIntent(HackensackUMCActivity.this);
//                if (intent == null) {
//                    //showDialog unable to
//                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this,getString(R.string.sorry_str),getString(R.string.mychart_activity_not_supp_msg));
//                } else {
//                    startActivity(intent);
//                }
//                break;
//            case 10:
//                //My_Chart_Settings
//                intent = WPOpen.makeSettingsIntent(HackensackUMCActivity.this);
//                if (intent == null) {
//                    //showDialog unable to
//                    Util.showNetworkOfflineDialog(HackensackUMCActivity.this, getString(R.string.sorry_str), getString(R.string.mychart_activity_not_supp_msg));
//                } else {
//                    startActivity(intent);
//                }
//                break;
            case 10:

                intent = new Intent(HackensackUMCActivity.this, ConsentSearchActivity.class);
                startActivity(intent);

                break;

    /* case 12:
                //consent
        break;*/
        }
    }

    private void showPatientsListDialog(List<IWPPatientAccess> patientList) {

        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HackensackUMCActivity.this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.patient_list_custom, null);
            builder.setView(dialogView);

            ((TextView) dialogView.findViewById(R.id.patient_list_title)).setText("Patients");
            final PatientListAdapter patientListAdapter = new PatientListAdapter(HackensackUMCActivity.this, patientList);
            ListView mDialogListView = (ListView) dialogView.findViewById(R.id.patient_list);
            mDialogListView.setAdapter(patientListAdapter);

            ((ListView) dialogView.findViewById(R.id.patient_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    WPOpen.setPatient(patientListAdapter.getItem(i));
                    mUserNameTv.setText("Hi "+WPOpen.getCurrentPatient().getName());
                    isUserNameSet = true;
                    Log.e("hackensack","get MRN "+patientListAdapter.getItem(i).getAccountID());
                    new getPatientMRN(HackensackUMCActivity.this, patientListAdapter.getItem(i).getAccountID()).execute();
                    alert.dismiss();

                }
            });

            alert = builder.show();
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // isSpecialtyShown = false;
                }
            });

        }

    }

    private class getPatientMRN extends AsyncTask<Void, Void, ArrayList<PatientMRN>> {
        private String id = null;
        private Context mContext;

        public getPatientMRN(Context context, String patientId) {
            id = patientId;
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgress(HackensackUMCActivity.this, getResources().getString(R.string.loading_text));
        }

        @Override
        protected ArrayList<PatientMRN> doInBackground(Void... params) {
            ArrayList<PatientMRN> patientIdList = null;
            org.apache.http.HttpResponse response = HttpUtils.httpGetmethod(HackensackUMCActivity.this, ServerConstants.URL_GET_MRN_FOR_PROXY + "ID=" + id + "&IDType=WPRINTERNAL", createHeadersForCreatePassword());
            if (response.getStatusLine().getStatusCode() == 200) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<PatientMRN>>() {
                    }.getType();
                    patientIdList = gson.fromJson(EntityUtils.toString(response.getEntity()), listType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.v("", "response to URL_GET_MRN_FOR_PROXY::" + response);
            }
            return patientIdList;
        }

        @Override
        protected void onPostExecute(ArrayList<PatientMRN> patientIdList) {

            super.onPostExecute(patientIdList);

            if (patientIdList != null) {
                for (int i = 0; i < patientIdList.size(); i++) {
                    String id = patientIdList.get(i).getIdtype();
                    if (id == "HUMC" || id.equals("HUMC")) {
                        Util.setPatientMRNID(mContext, patientIdList.get(i).getId());
                    }
                }
                //patientMRN can give id and idType

            } else {
                //response Error
                Log.e("", "Unable to get MRN for proxy");
            }

            stopProgress();

        }
    }

    private List<NameValuePair> createHeadersForCreatePassword() {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHAREPREF_TAG, MODE_PRIVATE);
        ArrayList<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
//        headers.add(new BasicNameValuePair("Authorization", "Bearer " + sharedPreferences.getString(ServerConstants.ACCESS_TOKEN_EPIC, "")));
        headers.add(new BasicNameValuePair("Authorization", "Bearer " + Util.getToken(this)));
        return headers;
    }

}
