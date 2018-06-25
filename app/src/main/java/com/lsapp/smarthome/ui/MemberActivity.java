package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.databinding.ActivityRecyclerViewBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.MemberAdapter;
import com.lsapp.smarthome.ui.model.MemberModel;
import com.zuni.library.utils.zToastUtil;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Administrator on 2016/8/22.
 */
public class MemberActivity extends BaseActivity implements OnRecycleViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ActivityRecyclerViewBinding binding;
    private MemberModel model;
    private Handler guiderHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            guideUtil.createToolbar(Const.GUIDE_MEMBER_ADD, binding.toolbar, R.id.action_add,
                    getString(R.string.guide_member_title), getString(R.string.guide_member_content));
        }
    };

    @Override
    protected void initModel() {
        model = new MemberModel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        setTitle(binding.toolbar, getString(R.string.family_member), true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.swipeContainer.setOnRefreshListener(this);
        binding.toolbar.inflateMenu(R.menu.menu_add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) finish();
        else
            model.addMember();
        return true;
    }

    @Override
    public void onItemClick(View v, int position) {
        model.disconnectMember(position);
    }

    public void setView() {
        binding.swipeContainer.setRefreshing(false);
        if (model.getMembers() == null || model.getMembers().size() == 0) {
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
        } else {
            viewVisibleAnim(binding.emptyView, View.GONE);
        }
        binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(new MemberAdapter(model.getMembers(), this)));
        guiderHandler.postDelayed(() -> guiderHandler.sendEmptyMessage(0), 1000);
    }

    public void showSnack(String str){
        zToastUtil.showSnackIndefiniteWithButton(binding.getRoot(), str, "查看", view -> model.disconnectMember(-1));
    }

    public void onEmpty(View v) {
        model.addMember();
    }

    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(true);
        model.get();
    }
}
