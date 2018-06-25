package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Sensor;
import com.lsapp.smarthome.databinding.ItemEventBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class ShopEventAdapter extends RecyclerView.Adapter<ShopEventAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener mClickListener;
    public ShopEventAdapter( OnRecycleViewItemClickListener listener) {
        mClickListener = listener;

    }

    @Override
    public ShopEventAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        com.lsapp.smarthome.databinding.ItemEventBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_event,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), mClickListener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
    }



    @Override
    public int getItemCount() {
        return 10;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private com.lsapp.smarthome.databinding.ItemEventBinding binding;
        private OnRecycleViewItemClickListener mClickListener;

        public BindingHolder(final View itemView, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;

            itemView.setOnClickListener(v -> {
                mClickListener.onItemClick(v,getLayoutPosition());

            });
    }

        public com.lsapp.smarthome.databinding.ItemEventBinding getBinding() {
            return binding;
        }

        public void setBinding(com.lsapp.smarthome.databinding.ItemEventBinding binding) {
            this.binding = binding;
        }


    }
}