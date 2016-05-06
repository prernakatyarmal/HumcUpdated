package com.hackensack.umc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.activity.SymptomCheckerActivity;
import com.hackensack.umc.activity.SymptomListActivity;
import com.hackensack.umc.util.Constant;
import com.hackensack.umc.util.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gaurav_salunkhe on 9/28/2015.
 */
public class SymptomMaleFragment extends Fragment implements View.OnClickListener {

    private ImageView mBodyImage, mBodyHighlightImage;

    private TextView mResetText, mSkinText;

    private View mFrontTabLine, mBackTabLine;

    private RelativeLayout mBrainRelativeLayout, mEyesRelativeLayout, mNoseRelativeLayout, mEarsLeftRelativeLayout, mEarsRightRelativeLayout, mMouthRelativeLayout, mNeckRelativeLayout,
                        mHeadRelativeLayout, mChestRelativeLayout, mAbdomanRelativeLayout, mLeftHandRelativeLayout, mLeftShoulderRelativeLayout,
                        mRightHandRelativeLayout, mRightShoulderRelativeLayout, mGenitalRelativeLayout, mLegRelativeLayout, mFootRelativeLayout;

    private RelativeLayout mBodyTouchRelativeLayout, mHeadTouchRelativeLayout ;

    //private boolean isBodyFront  ;

    private boolean isHeadShow = false ;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_symptom, container, false);

        mBodyImage = (ImageView) rootView.findViewById(R.id.body_image);
        RelativeLayout mFrontRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.front_relative_layout);
        mFrontTabLine = mFrontRelativeLayout.findViewById(R.id.front_tab_line);
        mFrontRelativeLayout.setOnClickListener(this);

        RelativeLayout mBackRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.back_relative_layout);
        mBackTabLine = mBackRelativeLayout.findViewById(R.id.back_tab_line);
        mBackRelativeLayout.setOnClickListener(this);
        mSkinText = (TextView) rootView.findViewById(R.id.skin_text);
        mSkinText.setOnClickListener(this);
        if(SymptomCheckerActivity.isBodyFront){
            mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.male_body));
            mFrontTabLine.setVisibility(View.VISIBLE);
            mBackTabLine.setVisibility(View.INVISIBLE);
            mSkinText.setVisibility(View.GONE);
        }
        else{
            mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.male_back));
            mFrontTabLine.setVisibility(View.INVISIBLE);
            mBackTabLine.setVisibility(View.VISIBLE);
            mSkinText.setVisibility(View.VISIBLE);
        }

        mBodyHighlightImage = (ImageView) rootView.findViewById(R.id.body_highlight_image);

        mResetText = (TextView) rootView.findViewById(R.id.reset_text);
        mResetText.setOnClickListener(this);





//        Height﹕ Height : 686
//        Width﹕ Width : 284

//        Height﹕ Height : 914 /////// Done
//        Width﹕ Width : 378

//        Height: 1359﹕ Width: 567 /////// Done


        Log.e("Height", "Height : " + mBodyImage.getDrawable().getCurrent().getIntrinsicHeight());
        Log.e("Width", "Width : " + mBodyImage.getDrawable().getCurrent().getIntrinsicWidth());

        int height = mBodyImage.getDrawable().getCurrent().getIntrinsicHeight();
        int width = mBodyImage.getDrawable().getCurrent().getIntrinsicWidth();

        mBodyTouchRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_body_touch);
        mHeadTouchRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_head_touch);
        mBodyTouchRelativeLayout.setVisibility(View.VISIBLE);
        mHeadTouchRelativeLayout.setVisibility(View.GONE);

        mBrainRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_brain);
        RelativeLayout.LayoutParams brainLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            brainLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(120, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
            brainLayout.setMargins(0, (int) Util.convertDpToPixel(60, getActivity()), 0, 0);
        }else {
            brainLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(150, getActivity()), (int) Util.convertDpToPixel(60, getActivity()));
            brainLayout.setMargins(0, (int) Util.convertDpToPixel(100, getActivity()), 0, 0);
        }

        brainLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mBrainRelativeLayout.setLayoutParams(brainLayout);
        mBrainRelativeLayout.setOnClickListener(this);

        mEyesRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_eyes);
        RelativeLayout.LayoutParams eyesLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(120, getActivity()), (int) Util.convertDpToPixel(35, getActivity()));
        eyesLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        eyesLayout.addRule(RelativeLayout.BELOW, mBrainRelativeLayout.getId());
        mEyesRelativeLayout.setLayoutParams(eyesLayout);
        mEyesRelativeLayout.setOnClickListener(this);

        mNoseRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_nose);
        RelativeLayout.LayoutParams noseLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(80, getActivity()), (int) Util.convertDpToPixel(35, getActivity()));
        noseLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        noseLayout.addRule(RelativeLayout.BELOW, mEyesRelativeLayout.getId());
        mNoseRelativeLayout.setLayoutParams(noseLayout);
        mNoseRelativeLayout.setOnClickListener(this);

        mEarsLeftRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_ears_left);
//        RelativeLayout.LayoutParams earLeftLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));

        RelativeLayout.LayoutParams earLeftLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            earLeftLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(60, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }else {
            earLeftLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }

        earLeftLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        earLeftLayout.addRule(RelativeLayout.BELOW, mEyesRelativeLayout.getId());
        earLeftLayout.addRule(RelativeLayout.LEFT_OF, mNoseRelativeLayout.getId());
        mEarsLeftRelativeLayout.setLayoutParams(earLeftLayout);
        mEarsLeftRelativeLayout.setOnClickListener(this);

        mEarsRightRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_ears_right);
//        RelativeLayout.LayoutParams earRightLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        RelativeLayout.LayoutParams earRightLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            earRightLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(60, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }else {
            earRightLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }

        earRightLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        earRightLayout.addRule(RelativeLayout.BELOW, mEyesRelativeLayout.getId());
        earRightLayout.addRule(RelativeLayout.RIGHT_OF, mNoseRelativeLayout.getId());
        mEarsRightRelativeLayout.setLayoutParams(earRightLayout);
        mEarsRightRelativeLayout.setOnClickListener(this);

        mMouthRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_mouth);
        RelativeLayout.LayoutParams mouthLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(80, getActivity()), (int) Util.convertDpToPixel(45, getActivity()));
        mouthLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mouthLayout.addRule(RelativeLayout.BELOW, mNoseRelativeLayout.getId());
        mMouthRelativeLayout.setLayoutParams(mouthLayout);
        mMouthRelativeLayout.setOnClickListener(this);

        mNeckRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_neck);
        RelativeLayout.LayoutParams neckLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(100, getActivity()), (int) Util.convertDpToPixel(75, getActivity()));
        neckLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        neckLayout.addRule(RelativeLayout.BELOW, mMouthRelativeLayout.getId());
        mNeckRelativeLayout.setLayoutParams(neckLayout);
        mNeckRelativeLayout.setOnClickListener(this);



        mHeadRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_head);
        RelativeLayout.LayoutParams headLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            headLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(65, getActivity()), (int) Util.convertDpToPixel(60, getActivity()));
        }else {
            headLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(70, getActivity()));
        }

        headLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mHeadRelativeLayout.setLayoutParams(headLayout);
        mHeadRelativeLayout.setOnClickListener(this);

        mChestRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_chest);
//        RelativeLayout.LayoutParams chestLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(70, getActivity()));
        RelativeLayout.LayoutParams chestLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            chestLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(60, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }else {
            chestLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(70, getActivity()));
        }

        chestLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        chestLayout.addRule(RelativeLayout.BELOW, mHeadRelativeLayout.getId());
        mChestRelativeLayout.setLayoutParams(chestLayout);
        mChestRelativeLayout.setOnClickListener(this);

        RelativeLayout.LayoutParams skinLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        skinLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        skinLayout.addRule(RelativeLayout.BELOW, mHeadRelativeLayout.getId());
        mSkinText.setLayoutParams(skinLayout);

        mAbdomanRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_abdomen);
//        RelativeLayout.LayoutParams abdomanLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(85, getActivity()));

        RelativeLayout.LayoutParams abdomanLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            abdomanLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(60, getActivity()), (int) Util.convertDpToPixel(45, getActivity()));
        }else {
            abdomanLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(75, getActivity()), (int) Util.convertDpToPixel(85, getActivity()));
        }

        abdomanLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        abdomanLayout.addRule(RelativeLayout.BELOW, mChestRelativeLayout.getId());
        mAbdomanRelativeLayout.setLayoutParams(abdomanLayout);
        mAbdomanRelativeLayout.setOnClickListener(this);

        mLeftShoulderRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_left_shoulder);
//        RelativeLayout.LayoutParams leftShoulderLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        RelativeLayout.LayoutParams leftShoulderLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            leftShoulderLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(40, getActivity()), (int) Util.convertDpToPixel(40, getActivity()));
        }else {
            leftShoulderLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }
        leftShoulderLayout.addRule(RelativeLayout.BELOW, mHeadRelativeLayout.getId());
        leftShoulderLayout.addRule(RelativeLayout.LEFT_OF, mChestRelativeLayout.getId());
        mLeftShoulderRelativeLayout.setLayoutParams(leftShoulderLayout);
        mLeftShoulderRelativeLayout.setOnClickListener(this);

        mLeftHandRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_left_hand);
//        RelativeLayout.LayoutParams leftHandLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(150, getActivity()));
        RelativeLayout.LayoutParams leftHandLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            leftHandLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(40, getActivity()), (int) Util.convertDpToPixel(100, getActivity()));
        }else {
            leftHandLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(150, getActivity()));
        }
        leftHandLayout.addRule(RelativeLayout.BELOW, mLeftShoulderRelativeLayout.getId());
        leftHandLayout.addRule(RelativeLayout.LEFT_OF, mAbdomanRelativeLayout.getId());
        mLeftHandRelativeLayout.setLayoutParams(leftHandLayout);
        mLeftHandRelativeLayout.setOnClickListener(this);

        mRightShoulderRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_right_shoulder);
//        RelativeLayout.LayoutParams rightShoulderLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        RelativeLayout.LayoutParams rightShoulderLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            rightShoulderLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(40, getActivity()), (int) Util.convertDpToPixel(40, getActivity()));
        }else {
            rightShoulderLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        }

        rightShoulderLayout.addRule(RelativeLayout.BELOW, mHeadRelativeLayout.getId());
        rightShoulderLayout.addRule(RelativeLayout.RIGHT_OF, mChestRelativeLayout.getId());
        mRightShoulderRelativeLayout.setLayoutParams(rightShoulderLayout);
        mRightShoulderRelativeLayout.setOnClickListener(this);

        mRightHandRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_right_hand);
//        RelativeLayout.LayoutParams rightHandLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(150, getActivity()));

        RelativeLayout.LayoutParams rightHandLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            rightHandLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(40, getActivity()), (int) Util.convertDpToPixel(100, getActivity()));
        }else {
            rightHandLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(50, getActivity()), (int) Util.convertDpToPixel(150, getActivity()));
        }

        rightHandLayout.addRule(RelativeLayout.BELOW, mRightShoulderRelativeLayout.getId());
        rightHandLayout.addRule(RelativeLayout.RIGHT_OF, mAbdomanRelativeLayout.getId());
        mRightHandRelativeLayout.setLayoutParams(rightHandLayout);
        mRightHandRelativeLayout.setOnClickListener(this);

        mGenitalRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_genital);
        RelativeLayout.LayoutParams thightLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(85, getActivity()), (int) Util.convertDpToPixel(40, getActivity()));
        thightLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        thightLayout.addRule(RelativeLayout.BELOW, mAbdomanRelativeLayout.getId());
        mGenitalRelativeLayout.setLayoutParams(thightLayout);
        mGenitalRelativeLayout.setOnClickListener(this);

        mLegRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_leg);
//        RelativeLayout.LayoutParams legLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(85, getActivity()), (int) Util.convertDpToPixel(150, getActivity()));

        RelativeLayout.LayoutParams legLayout = null ;

        if(width <= Constant.SCREEN_MIN_WIDTH && height <= Constant.SCREEN_MIN_HEIGHT ) {
            legLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(60, getActivity()), (int) Util.convertDpToPixel(120, getActivity()));
        }else {
            legLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(85, getActivity()), (int) Util.convertDpToPixel(150, getActivity()));
        }

        legLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        legLayout.addRule(RelativeLayout.BELOW, mGenitalRelativeLayout.getId());
        mLegRelativeLayout.setLayoutParams(legLayout);
        mLegRelativeLayout.setOnClickListener(this);

        mFootRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_foot);
        RelativeLayout.LayoutParams footLayout = new RelativeLayout.LayoutParams((int) Util.convertDpToPixel(85, getActivity()), (int) Util.convertDpToPixel(50, getActivity()));
        footLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        footLayout.addRule(RelativeLayout.BELOW, mLegRelativeLayout.getId());
        mFootRelativeLayout.setLayoutParams(footLayout);
        mFootRelativeLayout.setOnClickListener(this);

        //isBodyFront = true ;
        isHeadShow = false ;

//        mHeadRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_cheat);

//        tabHost = new FragmentTabHost(getActivity());
//        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment);
//
//        Bundle arg1 = new Bundle();
//        arg1.putInt("Arg for Frag1", 1);
//        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("FRONT"), MaleFrontImageFragment.class, arg1);
//
//        Bundle arg2 = new Bundle();
//        arg2.putInt("Arg for Frag2", 2);
//        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("BACK"), MaleBackImageFragment.class, arg2);
//
//        return tabHost;

        return rootView;

    }

    @Override
    public void onResume() {

        mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.dummy_image));
        mSkinText.setTextColor(getResources().getColor(R.color.primaryColor));

        super.onResume();
    }

    @Override
    public void onClick(View view) {

//        final ImageView iv = mBodyImage;
////        final TextView tv = (TextView)findViewById(R.id.size_label);
//        ViewTreeObserver vto = iv.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                // Remove after the first run so it doesn't fire forever
//                iv.getViewTreeObserver().removeOnPreDrawListener(this);
//                int finalHeight = iv.getMeasuredHeight();
//                int finalWidth = iv.getMeasuredWidth();
//                Log.e("Height: " + finalHeight, " Width: " + finalWidth);
//                return true;
//            }
//        });
        if(Util.ifNetworkAvailableShowProgressDialog(getActivity(),"",false)) {
        switch (view.getId()) {

            case R.id.front_relative_layout:

                mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.male_body));
                SymptomCheckerActivity.isBodyFront = true ;
                mFrontTabLine.setVisibility(View.VISIBLE);
                mBackTabLine.setVisibility(View.INVISIBLE);
                mSkinText.setVisibility(View.GONE);
                mHeadTouchRelativeLayout.setVisibility(View.GONE);
                mBodyTouchRelativeLayout.setVisibility(View.VISIBLE);
                if(mResetText.isShown()) {
                    mResetText.setVisibility(View.GONE);
                }

                break;

            case R.id.back_relative_layout:

                mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.male_back));
                SymptomCheckerActivity.isBodyFront = false ;
                mFrontTabLine.setVisibility(View.INVISIBLE);
                mBackTabLine.setVisibility(View.VISIBLE);
                mSkinText.setVisibility(View.VISIBLE);
                if(mResetText.isShown()) {
                    mResetText.setVisibility(View.GONE);
                }
                mHeadTouchRelativeLayout.setVisibility(View.GONE);
                mBodyTouchRelativeLayout.setVisibility(View.VISIBLE);

                break;

//            case R.id.body_image:
//
//                startActivity(new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class));
//
//                break;

            case R.id.reset_text:

                if(isHeadShow) {
                    if(SymptomCheckerActivity.isBodyFront) {
                        mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.male_body));
                    }else {
                        mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.male_back));
                        mSkinText.setVisibility(View.VISIBLE);
                    }
                    isHeadShow = false;
                    mResetText.setVisibility(View.GONE);
                    mBodyTouchRelativeLayout.setVisibility(View.VISIBLE);
                    mHeadTouchRelativeLayout.setVisibility(View.GONE);
                }

                break;

            case R.id.skin_text:

                mSkinText.setTextColor(Color.RED);

                Intent intent = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                intent.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Skin");
                intent.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(intent);

                break;

            case R.id.relative_head:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_head_highlight));

                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.dummy_image));
                                mBodyImage.setImageDrawable(getResources().getDrawable(R.drawable.head_face_male));
                            }
                        });
                    }
                }, 50);

                isHeadShow = true;
                mResetText.setVisibility(View.VISIBLE);
                mSkinText.setVisibility(View.GONE);
                mBodyTouchRelativeLayout.setVisibility(View.GONE);
                mHeadTouchRelativeLayout.setVisibility(View.VISIBLE);

                break;

            case R.id.relative_chest:

                Intent chestIntent = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                chestIntent.putExtra(Constant.MALE_BODY, Constant.MALE_BODY);
                if(SymptomCheckerActivity.isBodyFront) {
                    chestIntent.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Chest");
                    mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_chest_highlight));
                }else {
                    chestIntent.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Skin");
                }
                chestIntent.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(chestIntent);

                break;

            case R.id.relative_abdomen:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_abdomen_highlight));

                Intent intentAbdom = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                if(SymptomCheckerActivity.isBodyFront) {
                    intentAbdom.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyPart=Abdomen");
                }else {
                    intentAbdom.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyRegion=Back");
                }
                intentAbdom.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(intentAbdom);

                break;

//            case R.id.relative_thight:
//
//                Intent intentAbdom = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
//                intentAbdom.putExtra(Constant.SYMPTOM_BODY_PART, "Abdomen");
//                startActivity(intentAbdom);
//
//                break;

            case R.id.relative_genital:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_pelvis_higlight));

                Intent intentGenital = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                intentGenital.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Genitals%20or%20Urinary");
                intentGenital.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(intentGenital);

                break;

            case R.id.relative_leg:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_legs_highlight));

                Intent intentLeg = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                intentLeg.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyRegion=Leg");
                intentLeg.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(intentLeg);

                break;

            case R.id.relative_foot:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_legs_highlight));

                Intent intentFoot = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                intentFoot.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyPart=Foot");
                intentFoot.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(intentFoot);

                break;

            case R.id.relative_left_shoulder:
            case R.id.relative_right_shoulder:
            case R.id.relative_left_hand:
            case R.id.relative_right_hand:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_hand_higlight));

                Intent intentHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                intentHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyRegion=Arm");
                intentHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(intentHand);

                break;

            case R.id.relative_brain:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_brain_highlight));

                Intent brainHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                brainHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Head%20or%20Brain");
                brainHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(brainHand);

                break;

            case R.id.relative_eyes:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_eyes_highlight));

                Intent eyesHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                eyesHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Eye");
                eyesHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(eyesHand);

                break;

            case R.id.relative_ears_left:
            case R.id.relative_ears_right:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_ears_highlight));

                Intent earsLeftHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                earsLeftHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Ear");
                earsLeftHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(earsLeftHand);

                break;

            case R.id.relative_nose:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_nose_highlight));

                Intent noseHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                noseHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyArea=Nose");
                noseHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(noseHand);

                break;

            case R.id.relative_mouth:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_mouth_highlight));

                Intent mouthHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                mouthHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyPart=Mouth");
                mouthHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(mouthHand);

                break;

            case R.id.relative_neck:

                mBodyHighlightImage.setImageDrawable(getResources().getDrawable(R.drawable.male_neck_highlight));

                Intent neckHand = new Intent(SymptomMaleFragment.this.getActivity(), SymptomListActivity.class);
                neckHand.putExtra(Constant.SYMPTOM_BODY_PART, "?BodyPart=Neck");
                neckHand.putExtra(Constant.SYMPTOM_BODY_CLASSIFIED, Constant.MALE_BODY);
                startActivity(neckHand);

                break;

        }}
    }


}
