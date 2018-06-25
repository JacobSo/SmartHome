package com.lsapp.smarthome.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.ActivityRecyclerViewBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.DeviceListAdapter;
import com.lsapp.smarthome.ui.model.DeviceListModel;
import com.zuni.library.utils.zToastUtil;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Administrator on 2016/8/10.
 */
public class DeviceListActivity extends BaseActivity implements OnRecycleViewItemClickListener, OnRefreshListener {
    private ActivityRecyclerViewBinding binding;
    private DeviceListModel model;
    private Handler guiderHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            guideUtil.createToolbar(Const.GUIDE_DEVICE_ADD, binding.toolbar, R.id.action_scan, getString(R.string.guide_device_title), getString(R.string.guide_device_content));
        }
    };

    @Override
    protected void initModel() {
        model = new DeviceListModel(this);
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
        model.setScene((Scene) getIntent().getSerializableExtra("scene"));
//        zToastUtil.show(this,model.getScene().getUserId());
        setTitle(binding.toolbar, model.getScene().getSpaceName(), true);
        getSupportActionBar().setSubtitle( getString(R.string.device_list_s));
        binding.toolbar.inflateMenu(R.menu.menu_scan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.swipeContainer.setOnRefreshListener(this);
        Glide.get(this).clearMemory();
    }

    public void setView() {
        binding.swipeContainer.setRefreshing(false);
        if (model.getDeviceList() != null && model.getDeviceList().size() != 0) {
            viewVisibleAnim(binding.emptyView, View.GONE);
            binding.setIsLost(model.getDeviceList().size()==1);
            binding.setIsAction(model.getDeviceList().get(0).getItemType()!=1);
            binding.executePendingBindings();
        } else {
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
            binding.emptyBtn.setText(getString(R.string.add_device));
        }

        binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(new DeviceListAdapter(model.getDeviceList(), this)));
        guiderHandler.postDelayed(() -> guiderHandler.sendEmptyMessage(0), 1000);
    }

    public void onEmpty(View v) {
        model.connectDialog();
    }

    public void onSkipNotice(View v){
        binding.noticeLayout.setVisibility(View.GONE);
    }
    public void onReconnect(View v){
        model.connectDialog();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        } else if (item.getItemId() == R.id.action_recovery) {
            startActivity(new Intent(this,RecoveryListActivity.class));
        } else {
          //  if (BaseApplication.get(this).getPermission().equals("0")) {
            if(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0)
                zToastUtil.showSnack(binding.getRoot(), getString(R.string.no_permission));
             else
                model.connectDialog();

        }
        //model.connectDialog();
        return true;
    }

    @Override
    public void onItemClick(View v, int position) {
        ImageView iv = (ImageView) v.findViewById(R.id.device_image);
        Intent intent = new Intent(this, DeviceDetailActivity.class);
        intent.putExtra("device", model.getDeviceList().get(position));
        intent.putExtra("scene", model.getScene());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, iv, "share").toBundle());
        } else {
            startActivity(intent);//.putExtra("device",deviceId));
        }
    }



    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(true);
        model.get();
    }


}
