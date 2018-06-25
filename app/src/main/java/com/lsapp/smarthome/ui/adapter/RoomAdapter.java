package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.ItemRoomBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener mClickListener;
    private List<Scene> sceneList;
    SparseArray<Boolean> checkMap;

    public RoomAdapter(List<Scene> sceneList, SparseArray<Boolean> checkMap, OnRecycleViewItemClickListener listener) {
        mClickListener = listener;
        this.sceneList = sceneList;
        this.checkMap = checkMap;
    }

    @Override
    public RoomAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemRoomBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_room,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), binding.roomModify, binding.roomDelete, mClickListener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        if (checkMap.get(i)) {
            Animation anim = AnimationUtils.loadAnimation(viewHolder.getBinding().getRoot().getContext(), R.anim.slide_in_right);
            anim.setDuration(200L);
            viewHolder.getBinding().layout.startAnimation(anim);
        }
        viewHolder.getBinding().roomImaage.setImageResource(sceneList.get(i).getEmptyImage());
        viewHolder.getBinding().setData(sceneList.get(i));
        viewHolder.getBinding().setFlag(checkMap.get(i));
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return sceneList==null?0:sceneList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemRoomBinding binding;
        private OnRecycleViewItemClickListener mClickListener;

        public BindingHolder(final View itemView, View mod, View del, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;

            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
            del.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
            mod.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));
        }

        public ItemRoomBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemRoomBinding binding) {
            this.binding = binding;
        }


    }
}