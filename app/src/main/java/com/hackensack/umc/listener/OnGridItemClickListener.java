package com.hackensack.umc.listener;

import android.view.View;

import com.hackensack.umc.util.BaseFeatureEnum;

/**
 * Created by gaurav_salunkhe on 9/21/2015.
 */
public class OnGridItemClickListener implements View.OnClickListener {

    private int position;
    private OnGridItemClickCallback mOnItemClickCallback;
    private  BaseFeatureEnum mFeature;
    private OnFeatureItemClickCallback mOnFeatureItemClickCallback;

    public OnGridItemClickListener(int position, OnGridItemClickCallback onItemClickCallback) {
        this.position = position;
        this.mOnItemClickCallback = onItemClickCallback;
    }

    public OnGridItemClickListener(BaseFeatureEnum feature, OnFeatureItemClickCallback onItemClickCallback) {
        mFeature = feature;
        mOnFeatureItemClickCallback= onItemClickCallback;
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickCallback!=null)
        mOnItemClickCallback.onGridItemClicked(view, position);
        if(mOnFeatureItemClickCallback!=null)
        mOnFeatureItemClickCallback.onFeatureItemClicked(view, mFeature);
    }

    public interface OnGridItemClickCallback {
        void onGridItemClicked(View view, int position);
    }
    public interface OnFeatureItemClickCallback {
        void onFeatureItemClicked(View view, BaseFeatureEnum feature);
    }
}
