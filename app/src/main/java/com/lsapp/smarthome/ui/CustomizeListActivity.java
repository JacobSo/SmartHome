package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.ActivityRecyclerViewBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.FunctionAdapter;
import com.lsapp.smarthome.ui.model.CustomizeModel;
import com.zuni.library.ui.CircularAnim;
import com.zuni.library.utils.zToastUtil;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CustomizeListActivity extends BaseActivity implements OnRecycleViewItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ActivityRecyclerViewBinding binding;
    private CustomizeModel model;
    private Switch selectSwitch;
    private Handler guiderHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            guideUtil.create(Const.GUIDE_CUSTOMIZE_ADD, binding.floatBtn, getString(R.string.guide_customize_title), getString(R.string.guide_customize_content),null);
        }
    };
    @Override
    protected void initModel() {
        model = new CustomizeModel(this);

    }
    @Override
    public View getBindingView() {
        return binding.getRoot();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_recycler_view);
        model.setScene((Scene) getIntent().getSerializableExtra("scene"));
        setTitle(binding.toolbar,model.getScene().getSpaceName()+getString(R.string.customize_function_s),true);
        binding.floatBtn.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

        binding.floatBtn.setOnClickListener(this);
        binding.swipeContainer.setOnRefreshListener(this);
        binding.emptyBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


    public void setView() {
        binding.swipeContainer.setRefreshing(false);
        if (model.getTaskList() == null || model.getTaskList().size() == 0) {
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
        } else {
            viewVisibleAnim(binding.emptyView, View.GONE);
        }
        binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(new FunctionAdapter(model.getTaskList(), this,!(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0))));
        guiderHandler.postDelayed(() -> guiderHandler.sendEmptyMessage(0),1000);
    }


    public void setFailedView(){
        binding.swipeContainer.setRefreshing(false);
        binding.swipeContainer.setEnabled(true);
    }
    @Override
    public void onItemClick(View v, int position) {
       // if(BaseApplication.get(this).getPermission().equals("0")){
        if(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0){
            zToastUtil.showSnack(binding.getRoot(),getString(R.string.no_permission));
            return ;
        }
        if (v instanceof Switch) {
            selectSwitch = (Switch) v;
            model.control(model.getTaskList().get(position).getDeviceId(), model.getTaskList().get(position).getCreateTime(), ((Switch) v).isChecked());
        } else {
            zToastUtil.showSnackWithButton(binding.getRoot(), model.getTaskList().get(position).getTriggerName() + getString(R.string.customize_delete_text),getString(R.string.confirm), view -> {
                model.delete(model.getTaskList().get(position).getCreateTime());
            });
        }
    }

    public void onFailSwitch() {
        selectSwitch.setChecked(!selectSwitch.isChecked());
    }

    @Override
    public void onClick(View view) {
            CircularAnim.fullActivity(this, binding.floatBtn)
                    .colorOrImageRes(R.color.float_btn)
                    .go(() -> startActivity(new Intent(this, CustomizeActivity.class).putExtra("scene", model.getScene())));
    }

    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(true);
        model.get();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


}
