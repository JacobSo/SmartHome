package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Device;
import com.lsapp.smarthome.data.base.RecoveryDevice;
import com.lsapp.smarthome.databinding.ItemDeviceBinding;
import com.lsapp.smarthome.databinding.ItemDeviceRecoveryBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class RecoveryListAdapter extends RecyclerView.Adapter<RecoveryListAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener clickListener;
    private List<RecoveryDevice> deviceList;

    public RecoveryListAdapter(List<RecoveryDevice> deviceList, OnRecycleViewItemClickListener listener) {
        clickListener = listener;
        this.deviceList = deviceList;
    }

    @Override
    public RecoveryListAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemDeviceRecoveryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_device_recovery,
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
        private ItemDeviceRecoveryBinding binding;
        private OnRecycleViewItemClickListener mClickListener;
        public BindingHolder(final View itemView,  OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;
            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
        }

        public ItemDeviceRecoveryBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemDeviceRecoveryBinding binding) {
            this.binding = binding;
        }


    }
}