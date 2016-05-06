package com.hackensack.umc;

import android.test.ActivityInstrumentationTestCase2;

import com.hackensack.umc.activity.HackensackUMCActivity;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class HackensackUMCActivityTests extends ActivityInstrumentationTestCase2<HackensackUMCActivity> {

    private HackensackUMCActivity mHackensackUMCActivity;


    public HackensackUMCActivityTests() {

        super(HackensackUMCActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();


        mHackensackUMCActivity = getActivity();

    }

    public void testActivityNull() {

        assertNotNull("HackensackUMCActivity is null", mHackensackUMCActivity);

    }
}
