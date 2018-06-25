package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.databinding.FragmentMessageBinding;
import com.lsapp.smarthome.event.NewFeedEvent;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.MessageListAdapter;
import com.lsapp.smarthome.ui.adapter.ShopEventAdapter;
import com.lsapp.smarthome.ui.model.MessageModel;
import com.zuni.library.utils.zToastUtil;


/**
 * Created by Administrator on 2016/8/9.
 */
public class MessageFragment extends BaseFragment implements OnRecycleViewItemClickListener, SwipeRefreshLayout.OnRefreshListener ,View.OnClickListener{
    private final static String TAG = "MessageFragment";
    private FragmentMessageBinding binding;
    private MessageModel model;
    private MessageListAdapter adapter;
    private int lastVisibleItem;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initModel() {
        model = new MessageModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        binding.swipeContainer.setOnRefreshListener(this);
        binding.messageNews.setOnClickListener(this);
        binding.messageNotification.setOnClickListener(this);

        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.swipeContainer.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (adapter != null && lastVisibleItem + 1 == adapter.getItemCount()) {
                      //  if (model.getMessageList().size() > 9) {
                            model.get(model.getNextTime());
                            binding.swipeContainer.setRefreshing(true);
                     //   }
                    }
                }
            }
        });

        onRefresh();

        return binding.getRoot();
    }

    @Override
    public void onItemClick(View v, int position) {
        if(v==null){
            model.delete(position);
        }else{
/*            if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                return 0;
            }*/
            if(v instanceof FrameLayout){
                model.help();
            }else{
                if (adapter.isUndoOn()) {
                    adapter.pendingRemoval(position);
                } else {
                    adapter.remove(position);
                }
            }

        }
    }

    public void setRefreshFinish() {
        adapter = new MessageListAdapter(model.getMessageList(), this);
        binding.recyclerView.setAdapter(adapter);
        viewVisibleAnim(binding.recyclerView, View.VISIBLE);
        //  binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
        binding.swipeContainer.setRefreshing(false);
        if (model.getMessageList() == null || model.getMessageList().size() == 0) {
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
        } else {
            viewVisibleAnim(binding.emptyView, View.GONE);
        }
        adapter.setUndoOn(true);

    }

    public void setOverLoading(){
        binding.swipeContainer.setRefreshing(false);
        zToastUtil.showSnack(binding.getRoot(),getString(R.string.message_full));

    }

    public void setLoadFinish() {
        binding.swipeContainer.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    public void setFailedView() {
        binding.swipeContainer.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        RxBus.getDefault().post(new NewFeedEvent(false));
        binding.swipeContainer.setRefreshing(true);
        model.get(0);
        viewVisibleAnim(binding.recyclerView, View.GONE);

    }

    @Override
    public void onClick(View view) {
        Animation in = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_bottom);

        if(view==binding.messageNotification){
            binding.messageNews.setTextColor(getResources().getColor(R.color.secondary_text));
            binding.messageNotification.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.recyclerView.setAdapter(adapter);
        }else{
            binding.recyclerView.setAdapter(new ShopEventAdapter(this));
            binding.messageNews.setTextColor(getResources().getColor(R.color.colorAccent));
            binding.messageNotification.setTextColor(getResources().getColor(R.color.secondary_text));
        }
        binding.recyclerView.startAnimation(in);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }
}
