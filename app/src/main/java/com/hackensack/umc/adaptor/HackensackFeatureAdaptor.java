package com.hackensack.umc.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.listener.OnGridItemClickListener;
import com.hackensack.umc.util.BaseFeatureEnum;

import java.util.List;

/**
 * Created by bhagyashree_kumawat on 2/3/2016.
 */
public class HackensackFeatureAdaptor extends RecyclerView.Adapter<HackensackFeatureAdaptor.ViewHolder> {
    private OnGridItemClickListener.OnFeatureItemClickCallback onItemClickCallback;


    private List<BaseFeatureEnum> mFeatures;
    private Context mContext;

    public HackensackFeatureAdaptor(List<BaseFeatureEnum> list, OnGridItemClickListener.OnFeatureItemClickCallback onItemClickCallback, Context context) {

        super();
        mFeatures = list;
        this.onItemClickCallback = onItemClickCallback;
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        BaseFeatureEnum feature = mFeatures.get(position);
        viewHolder.featureTv.setText(feature.getName(feature, mContext));
        viewHolder.imgThumbnail.setImageResource(feature.getDrawable(feature));

        viewHolder.itemArea.setOnClickListener(new OnGridItemClickListener(feature, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return mFeatures.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView featureTv;
        public RelativeLayout itemArea;

        public ViewHolder(View itemView) {

            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            featureTv = (TextView) itemView.findViewById(R.id.tv_species);
            itemArea = (RelativeLayout) itemView.findViewById(R.id.top_layout);

        }

    }
}
