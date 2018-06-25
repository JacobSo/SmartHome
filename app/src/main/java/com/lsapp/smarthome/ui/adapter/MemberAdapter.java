package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.databinding.ItemMemberBinding;
import com.lsapp.smarthome.databinding.ItemMessageBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener listener;
    private List<Account> members;

    public MemberAdapter(List<Account> members, OnRecycleViewItemClickListener listener) {
        this.members = members;
        this.listener = listener;

    }

    @Override
    public MemberAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemMemberBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_member,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), listener);
        holder.setBinding(binding);
        return holder;
    }

    /*    @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if(position==0){
                            return 3;
                        }else if(position%2==1){
                            return 2;
                        }else{
                            return 2;
                        }
                    }
                });
            }
        }*/
    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        viewHolder.getBinding().confirmCheck.setImageResource(members.get(i).getIsConfirm() == 0 ? R.drawable.check_help : R.drawable.check_ok);
        viewHolder.getBinding().setData(members.get(i));
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return members==null?0:members.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemMemberBinding binding;
        private OnRecycleViewItemClickListener listener;

        public BindingHolder(final View itemView, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            listener = clickListener;
            itemView.setOnClickListener(v -> listener.onItemClick(v, getLayoutPosition()));

        }

        public ItemMemberBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemMemberBinding binding) {
            this.binding = binding;
        }


    }
}