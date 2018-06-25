package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.CustomTaskData;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.data.base.CustomTask;
import com.lsapp.smarthome.databinding.ItemFunctionBinding;
import com.lsapp.smarthome.databinding.ItemMemberBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.zuni.library.utils.zUnitUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener listener;
    private ArrayList<CustomTask> taskList;
    boolean isAdmin;
    public FunctionAdapter(ArrayList<CustomTask> taskList, OnRecycleViewItemClickListener listener,boolean isAdmin) {
        this.taskList = taskList;
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    @Override
    public FunctionAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemFunctionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_function,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), binding.functionClose,binding.functionSwitcher,listener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        viewHolder.getBinding().setData(taskList.get(i));
        viewHolder.getBinding().setIsAdmin(isAdmin);
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return taskList==null?0:taskList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemFunctionBinding binding;
        private OnRecycleViewItemClickListener listener;

        public BindingHolder(final View itemView,View close,View switcher, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            listener = clickListener;
            close.setOnClickListener(v -> listener.onItemClick(v, getLayoutPosition()));
            switcher.setOnClickListener(v -> listener.onItemClick(v, getLayoutPosition()));
    }
        public ItemFunctionBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemFunctionBinding binding) {
            this.binding = binding;
        }


    }
}