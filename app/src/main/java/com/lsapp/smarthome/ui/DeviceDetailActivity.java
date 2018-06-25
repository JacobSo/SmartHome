package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.base.Device;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.ActivityDeviceDetailBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.SensorAdapter;
import com.lsapp.smarthome.ui.model.DeviceDetailModel;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/9/19.
 */
public class DeviceDetailActivity extends BaseActivity implements OnRecycleViewItemClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivityDeviceDetailBinding binding;
    private DeviceDetailModel model;
    @Override
    protected void onResume() {
        super.onResume();
        if (model.getDevice().getItemType() == 2 || model.getDevice().getItemType() == 3) {
            model.getDevice().setAirClerState(BaseApplication.get(this).getSelectScene().getAirClerState());
            binding.setData(model.getDevice());
            binding.executePendingBindings();
        }
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void initModel() {
        model = new DeviceDetailModel(this);
        model.setDevice((Device) getIntent().getExtras().getSerializable("device"));
        model.setScene((Scene) getIntent().getExtras().getSerializable("scene"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.secondary_text));

        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_detail);
        setTitle(binding.toolbar, "", true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setVisibility(View.VISIBLE);

        Glide.with(this).load(model.getDevice().getPicPath()).into(binding.deviceImage);
        binding.setData(model.getDevice());
        binding.setIsAdmin(!(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0));
        model.get();

    }

    @Override
    public void onItemClick(View v, int position) {

    }

    public void setControlView(){
        binding.setControlData(model.getControlDevice());
        binding.executePendingBindings();
        if (model.getControlDevice().getSensorInfo() != null)
            binding.recyclerView.setAdapter(new SensorAdapter(model.getControlDevice().getSensorInfo(), this));
        binding.lightSwitch.setOnCheckedChangeListener(this);
        binding.windLightSwitch.setOnCheckedChangeListener(this);
        binding.workLightSwitch.setOnCheckedChangeListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void onUploadTimeSet(View v) {
      //  if (BaseApplication.get(this).getPermission().equals("0")) {
        if(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0){
            zToastUtil.showSnack(binding.getRoot(),getString(R.string.no_permission));
            return;
        }
        model.setTimeDialog();
    }

    public void setView() {
        binding.detailTimeSet.setText(model.getControlDevice().getUpInterval() + getString(R.string.second));
    }

    public void setLightView() {
        binding.setControlData(model.getControlDevice());
        binding.executePendingBindings();
    }


    public void onUnbind(View v) {
        //if (BaseApplication.get(this).getPermission().equals("0")) {
        if(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0){
            zToastUtil.showSnack(binding.getRoot(),getString(R.string.no_permission));
            return;
        }
        model.unbind();
    }

    public void onFilterSwitch(View v){
     //   if (BaseApplication.get(this).getPermission().equals("0")) {
        if(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0){
            zToastUtil.showSnack(binding.getRoot(),getString(R.string.no_permission));
            return;
        }

        model.filter();

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == binding.lightSwitch)
            model.airControl(b ? 1 : 0);
        else if (compoundButton == binding.windLightSwitch)
            model.lightControl(b ? 1 : 0, model.getControlDevice().getWorklightOn());
        else if (compoundButton == binding.workLightSwitch)
            model.lightControl(model.getControlDevice().getFanOn(), b ? 1 : 0);


    }

}
