package com.lsapp.smarthome.ui.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.UserMessage;
import com.lsapp.smarthome.databinding.ItemMessageBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2015/7/24.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.BindingHolder> {
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;
    private OnRecycleViewItemClickListener mClickListener;
    private List<UserMessage> messageList;
    private List<UserMessage> itemsPendingRemoval;
    private boolean undoOn;
    private OnRecycleViewItemClickListener listener;
    private Handler handler = new Handler();
    private HashMap<UserMessage, Runnable> pendingRunnables = new HashMap<>();

    public MessageListAdapter(List<UserMessage> messageList, OnRecycleViewItemClickListener listener) {
        mClickListener = listener;
        this.messageList = messageList;
        itemsPendingRemoval = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public MessageListAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemMessageBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_message,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot(), binding.trash,binding.messageHelpImage, listener);
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int i) {
        if (itemsPendingRemoval.contains(messageList.get(i))) {
            viewHolder.getBinding().messageLayout.setVisibility(View.GONE);
            viewHolder.getBinding().undoLayout.setVisibility(View.VISIBLE);
            viewHolder.getBinding().undoButton.setOnClickListener(v -> {
                Runnable pendingRemovalRunnable = pendingRunnables.get(messageList.get(i));
                pendingRunnables.remove(messageList.get(i));
                if (pendingRemovalRunnable != null)
                    handler.removeCallbacks(pendingRemovalRunnable);
                itemsPendingRemoval.remove(messageList.get(i));
                notifyItemChanged(messageList.indexOf(messageList.get(i)));
            });
        } else {
            viewHolder.getBinding().messageLayout.setVisibility(View.VISIBLE);
            viewHolder.getBinding().undoLayout.setVisibility(View.GONE);
            viewHolder.getBinding().setData(messageList.get(i));

            if (messageList.get(i).getSecondMsgType() == 4) {
                //yellow
                viewHolder.getBinding().setDark(Color.parseColor("#FFC107"));
                viewHolder.getBinding().big4.setFillColor(Color.parseColor("#FFECB3"));
                viewHolder.getBinding().msgImg.setImageResource(R.drawable.msg_repair);
            } else if (messageList.get(i).getSecondMsgType() == 2) {//red
                viewHolder.getBinding().setDark(Color.parseColor("#FF5252"));
                viewHolder.getBinding().big4.setFillColor(Color.parseColor("#FFCDD2"));
                viewHolder.getBinding().msgImg.setImageResource(R.drawable.msg_warnning);
            } else {//blue
                viewHolder.getBinding().setDark(Color.parseColor("#00BCD4"));
                viewHolder.getBinding().big4.setFillColor(Color.parseColor("#B2EBF2"));
                if(messageList.get(i).getSecondMsgType() == 0||messageList.get(i).getSecondMsgType() == 1){
                    viewHolder.getBinding().msgImg.setImageResource(R.drawable.msg_customize);
                }else if(messageList.get(i).getSecondMsgType() == 3){
                    viewHolder.getBinding().msgImg.setImageResource(R.drawable.user_member);
                }else{
                    viewHolder.getBinding().msgImg.setImageResource(R.drawable.msg_other);
                }
            }
        }

        viewHolder.getBinding().executePendingBindings();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final UserMessage item = messageList.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = () -> remove(messageList.indexOf(item));
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        mClickListener.onItemClick(null, position);

        UserMessage item = messageList.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (messageList.contains(item)) {
            messageList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        UserMessage item = messageList.get(position);
        return itemsPendingRemoval.contains(item);
    }


    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemMessageBinding binding;
        OnRecycleViewItemClickListener listener;

        public BindingHolder(final View itemView, View view, View help,OnRecycleViewItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            view.setOnClickListener(v -> listener.onItemClick(v, getLayoutPosition()));
            help.setOnClickListener(v -> listener.onItemClick(v, getLayoutPosition()));
        }

        public ItemMessageBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemMessageBinding binding) {
            this.binding = binding;
        }


    }
}