package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.databinding.ActivityChartDetailBinding;
import com.lsapp.smarthome.ui.model.ChartDetailModel;
import com.lsapp.smarthome.utils.ColorUtil;


public class ChartDetailActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, OnChartValueSelectedListener {
    private ActivityChartDetailBinding binding;
    private ChartDetailModel model;
    private int color;
    private Handler guiderHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
           guideUtil.createToolbarHide(Const.GUIDE_CHART, binding.toolbar,"辅助工具", "图表支持触控放大缩小，辅助工具可以代替多点触控，按钮实现图表放大缩小功能");
        }
    };

    @Override
    protected void initModel() {
        model = new ChartDetailModel(this);
        model.setType(getIntent().getIntExtra("type", 1));
        model.setDeviceId(getIntent().getStringExtra("device"));
        model.setUnit(getIntent().getStringExtra("unit"));
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chart_detail);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.today_chart)).setTag(1));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.week_chart)).setTag(7));
        binding.tabLayout.addOnTabSelectedListener(this);
        color = ColorUtil.getDarkTypeColor(model.getType());
        setTitle(binding.toolbar, getIntent().getStringExtra("name") + getString(R.string.data), true);
        setStatusBarColor(color);
        binding.setColor(color);
        binding.chart1.setOnChartValueSelectedListener(this);
        model.get(1);
    }

    public void setView() {
        model.setChart(binding.chart1, model.getDeviceDetailData());
        viewVisibleAnim(binding.chart1, View.VISIBLE);
        viewVisibleAnim(binding.noChartLayout, View.GONE);
        viewVisibleAnim(binding.progressBar, View.GONE);
        guiderHandler.postDelayed(() -> guiderHandler.sendEmptyMessage(0),1000);
    }

    public void zoomIn(View v) {
        binding.chart1.zoomIn();
    }

    public void zoomOut(View v) {
        binding.chart1.zoomOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        else
            binding.zoomBar.setVisibility(binding.zoomBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            model.get(1);
            binding.setStr(tab.getText() + getString(R.string.result_check));
        } else {
            model.week();
            binding.setStr(tab.getText() + getString(R.string.result_check));
        }
        binding.executePendingBindings();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        String temp = model.getDeviceDetailData().getList().get(h.getDataSetIndex()).getDataList().get((int) e.getX()).getLevel();
        if (temp != null && !temp.equals("")) {
            switch (Integer.valueOf(temp)) {
                case 0://优
                    binding.levelText.setBackgroundResource(R.drawable.corners_btn_green);
                    break;
                case 1://良
                    binding.levelText.setBackgroundResource(R.drawable.corners_btn_blue);
                    break;
                case 2://轻度
                case 3://中度
                    binding.levelText.setBackgroundResource(R.drawable.corners_btn_yellow);
                    break;
                case 4://重度
                case 5://严重
                    binding.levelText.setBackgroundResource(R.drawable.corners_btn_red);
                    break;
            }
        }
        binding.setData(model.getDeviceDetailData().getList().get(h.getDataSetIndex()).getDataList().get((int) e.getX()));
        binding.setUnit(model.getUnit());
        binding.executePendingBindings();
    }

    @Override
    public void onNothingSelected() {

    }

    public void setEmptyView() {
        viewVisibleAnim(binding.noChartLayout, View.VISIBLE);
        viewVisibleAnim(binding.progressBar, View.GONE);
    }

    public void onRefreshChart(View v) {
        viewVisibleAnim(binding.noChartLayout, View.GONE);
        viewVisibleAnim(binding.progressBar, View.VISIBLE);
        if (binding.tabLayout.getSelectedTabPosition() == 0) {
            model.get(1);
        } else {
            model.week();
        }
    }



}
