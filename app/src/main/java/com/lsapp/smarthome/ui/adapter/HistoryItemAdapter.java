package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.CheckData;
import com.lsapp.smarthome.data.base.DateData;
import com.lsapp.smarthome.databinding.ItemHistoryDetailBinding;
import com.lsapp.smarthome.databinding.ItemMainDetailBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.ArrayList;


/**
 * Created by Administrator on 2015/7/24.
 */
public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener mClickListener;
    private ArrayList<DateData> list;

    public HistoryItemAdapter(ArrayList<DateData> list, OnRecycleViewItemClickListener listener) {
        this.list = list;
        mClickListener = listener;
    }

    @Override
    public HistoryItemAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemHistoryDetailBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_history_detail,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), mClickListener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        viewHolder.getBinding().setData(list.get(i));
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemHistoryDetailBinding binding;
        private OnRecycleViewItemClickListener mClickListener;

        public BindingHolder(final View itemView, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;

            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
        }

        public ItemHistoryDetailBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemHistoryDetailBinding binding) {
            this.binding = binding;
        }


    }
}