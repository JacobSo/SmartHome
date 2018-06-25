package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Sensor;
import com.lsapp.smarthome.data.base.UserMessage;
import com.lsapp.smarthome.databinding.ItemMessageBinding;
import com.lsapp.smarthome.databinding.ItemSensorBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener mClickListener;
   private List<Sensor> sensorList;
    public SensorAdapter(List<Sensor> sensorList, OnRecycleViewItemClickListener listener) {
        mClickListener = listener;
        this.sensorList = sensorList;
    }

    @Override
    public SensorAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemSensorBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_sensor,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), mClickListener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        viewHolder.getBinding().setData(sensorList.get(i));
        viewHolder.getBinding().executePendingBindings();
    }



    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemSensorBinding binding;
        private OnRecycleViewItemClickListener mClickListener;

        public BindingHolder(final View itemView, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;

            itemView.setOnClickListener(v -> {
                mClickListener.onItemClick(v,getLayoutPosition());

            });
    }

        public ItemSensorBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemSensorBinding binding) {
            this.binding = binding;
        }


    }
}