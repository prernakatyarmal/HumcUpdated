package com.hackensack.umc.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackensack.umc.R;
import com.hackensack.umc.listener.OnGridItemClickListener;

import java.util.List;

/**
 * Created by gaurav_salunkhe on 9/15/2015.
 */

public class HackensackGridAdaptor extends RecyclerView.Adapter<HackensackGridAdaptor.ViewHolder> {


    private OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback;


    List<MenuItemHolder> mItems;

    public HackensackGridAdaptor(List<MenuItemHolder> list, OnGridItemClickListener.OnGridItemClickCallback onItemClickCallback) {

        super();
        mItems = list ;
        this.onItemClickCallback = onItemClickCallback;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MenuItemHolder nature = mItems.get(position);
        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());

        viewHolder.itemArea.setOnClickListener(new OnGridItemClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvspecies;
        public RelativeLayout itemArea;

        public ViewHolder(View itemView) {

            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView) itemView.findViewById(R.id.tv_species);
            itemArea = (RelativeLayout) itemView.findViewById(R.id.top_layout);

        }

    }

}
