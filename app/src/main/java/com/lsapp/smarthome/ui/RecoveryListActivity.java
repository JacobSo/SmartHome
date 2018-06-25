package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.databinding.ActivityRecyclerViewBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.RecoveryListAdapter;
import com.lsapp.smarthome.ui.model.RecoveryModel;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Administrator on 2016/8/10.
 */
public class RecoveryListActivity extends BaseActivity implements OnRecycleViewItemClickListener, OnRefreshListener {
    private ActivityRecyclerViewBinding binding;
    private RecoveryModel model;


    @Override
    protected void initModel() {
        model = new RecoveryModel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        setTitle(binding.toolbar,  getString(R.string.recovery_device), true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.swipeContainer.setOnRefreshListener(this);
    }

    public void setView() {
        binding.swipeContainer.setRefreshing(false);
        if (model.getList() != null && model.getList().size() != 0) {
            viewVisibleAnim(binding.emptyView, View.GONE);
        } else {
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
            binding.emptyBtn.setText(getString(R.string.no_data));
        }

        binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(new RecoveryListAdapter(model.getList(), this)));
    }

    public void onEmpty(View v) {

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onItemClick(View v, int position) {
        model.getScene(position);
    }


    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(true);
        model.get();
    }
}
