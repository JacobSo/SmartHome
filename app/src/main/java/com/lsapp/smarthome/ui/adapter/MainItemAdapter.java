package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.TextView;

import com.lsapp.smarthome.BR;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.CheckData;

import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import java.util.ArrayList;


/**
 * Created by Administrator on 2015/7/24.
 */
public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.BindingHolder> {
    private OnRecycleViewItemClickListener mClickListener;
    private ArrayList<CheckData> list;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;
    private boolean isHasFooter = true;
    private String time;
    private ViewGroup viewRoot;

    public MainItemAdapter(ArrayList<CheckData> list, OnRecycleViewItemClickListener listener) {
        this.list = list;
        mClickListener = listener;
    }

    @Override
    public MainItemAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        viewRoot = viewGroup;
        BindingHolder holder;
        if (i == TYPE_NORMAL) {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.getContext()),
                    R.layout.item_main_detail,
                    viewGroup,
                    false);
            holder = new BindingHolder(binding.getRoot(), mClickListener);
            holder.setBinding(binding);
        } else if (i == TYPE_HEADER) {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.getContext()),
                    R.layout.item_header,
                    viewGroup,
                    false);
            holder = new BindingHolder(binding.getRoot(), mClickListener);
            holder.setBinding(binding);
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.getContext()),
                    R.layout.item_main_footer,
                    viewGroup,
                    false);
            holder = new BindingHolder(binding.getRoot(), null);
            holder.setBinding(binding);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
       if (i == list.size() ) {
            if (list != null && list.size() == 0)  {
                viewHolder.getBinding().getRoot().setVisibility(View.GONE);
            } else {
                TextView value = (TextView) viewHolder.getBinding().getRoot().findViewById(R.id.time_text);
                value.startAnimation(AnimationUtils.loadAnimation(viewHolder.getBinding().getRoot().getContext(), R.anim.shake_it));
                value.setText(list.get(1).getDetectTime());
            }
        } else {
            viewHolder.getBinding().setVariable(BR.data, list.get(i ));
            viewHolder.getBinding().executePendingBindings();
            TextView value = (TextView) viewHolder.getBinding().getRoot().findViewById(R.id.value_text);
            value.startAnimation(AnimationUtils.loadAnimation(viewHolder.getBinding().getRoot().getContext(), R.anim.shake_it));
            TextView v = (TextView) viewHolder.getBinding().getRoot().findViewById(R.id.text_bg);
            v.startAnimation(AnimationUtils.loadAnimation(viewHolder.getBinding().getRoot().getContext(), R.anim.rotate_tip));
            v.setTextColor(Color.WHITE);

            switch (list.get(i).getLevel()) {
                case 0://优
                    v.setBackgroundResource(R.drawable.corners_btn_green);
                    break;
                case 1://良
                    v.setBackgroundResource(R.drawable.corners_btn_blue);
                    break;
                case 2://轻度
                case 3://中度
                    v.setBackgroundResource(R.drawable.corners_btn_yellow);
                    break;
                case 4://重度
                case 5://严重
                    v.setBackgroundResource(R.drawable.corners_btn_red);
                    break;
                default:
                    v.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() ) {
            return TYPE_FOOTER;
        } /*else if (position == 0) {
            return TYPE_HEADER;
        }*/ else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else {
            return list.size() + 1;
        }

    }

    public void setHasFooter(boolean footer) {
        isHasFooter = footer;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private OnRecycleViewItemClickListener mClickListener;

        public BindingHolder(final View itemView, OnRecycleViewItemClickListener clickListener) {
            super(itemView);
            if (clickListener != null) {
                mClickListener = clickListener;

/*                if (getLayoutPosition() == 0) {
                    Switch switcher = (Switch) itemView.findViewById(R.id.real_time);
                    switcher.setOnCheckedChangeListener((compoundButton, b) -> mClickListener.onItemClick(compoundButton, getLayoutPosition()));
                } else {*/
                    itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getLayoutPosition()));

  ///              }
            }

        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }


    }


}