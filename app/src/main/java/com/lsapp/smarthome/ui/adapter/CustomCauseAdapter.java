package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.databinding.ItemCauseBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.ArrayList;


/**
 * Created by Administrator on 2015/7/24.
 */
public class CustomCauseAdapter extends RecyclerView.Adapter<CustomCauseAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener clickListener;
    private ArrayList<String> customList;

    public CustomCauseAdapter(ArrayList<String> list, OnRecycleViewItemClickListener listener) {
        clickListener = listener;
        customList = list;
    }

    @Override
    public CustomCauseAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemCauseBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_cause,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), clickListener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        viewHolder.getBinding().setData(customList.get(i));
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return customList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemCauseBinding binding;
        private OnRecycleViewItemClickListener mClickListener;

        public BindingHolder(final View itemView, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;
            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
        }

        public ItemCauseBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemCauseBinding binding) {
            this.binding = binding;
        }


    }
}