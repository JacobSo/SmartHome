package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Device;
import com.lsapp.smarthome.databinding.ItemDeviceBinding;

import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener clickListener;
    private List<Device> deviceList;

    public DeviceListAdapter(List<Device> deviceList, OnRecycleViewItemClickListener listener) {
        clickListener = listener;
        this.deviceList = deviceList;
    }

    @Override
    public DeviceListAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemDeviceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_device,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(),clickListener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        Glide.with(viewHolder.getBinding().getRoot().getContext()).load(deviceList.get(i).getPicPath()).into(viewHolder.getBinding().deviceImage);
        viewHolder.getBinding().setData(deviceList.get(i));
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return deviceList==null?0:deviceList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemDeviceBinding binding;
        private OnRecycleViewItemClickListener mClickListener;
        public BindingHolder(final View itemView,  OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;
            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
        }

        public ItemDeviceBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemDeviceBinding binding) {
            this.binding = binding;
        }


    }
}