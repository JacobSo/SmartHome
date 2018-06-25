package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.data.base.DateCheck;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.ActivityCalendarBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.CaldroidSampleCustomAdapter;
import com.lsapp.smarthome.ui.adapter.HistoryItemAdapter;
import com.lsapp.smarthome.ui.model.HistoryModel;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/4.
 */
public class HistoryActivity extends BaseActivity implements OnRecycleViewItemClickListener, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private ActivityCalendarBinding binding;
    private View tempView;
    private HistoryModel model;
    private CaldroidSampleCustomFragment caldroidFragment;
    private boolean isExpnad = true;
    private int selectMonth = -1;
    private Handler guiderHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            guideUtil.create(Const.GUIDE_CALENDAR,binding.toolbarTitle, getString(R.string.guide_calendar_title), getString(R.string.guide_calendar_content),null);
        }
    };
    @Override
    protected void initModel() {
        model = new HistoryModel(this);
        model.setScene((Scene) getIntent().getSerializableExtra("scene"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (caldroidFragment != null) {
            caldroidFragment.getMonthTitleTextView().setVisibility(View.GONE);
            caldroidFragment.getLeftArrowButton().setVisibility(View.GONE);
            caldroidFragment.getRightArrowButton().setVisibility(View.GONE);
            caldroidFragment.getWeekdayGridView().setHorizontalSpacing(0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        setTitle(binding.toolbar, "", true);
        binding.toolbar.setSubtitle("检测记录");
        caldroidFragment = new CaldroidSampleCustomFragment();
        Bundle args = new Bundle();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(java.util.Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(java.util.Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);

        caldroidFragment.setArguments(args);
        binding.toolbarTitleLayout.setOnClickListener(this);
        binding.appbar.addOnOffsetChangedListener(this);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        caldroidFragment.setCaldroidListener(listener);

        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //     binding.calendarDetailTitle.setText(model.getDateFormat().format(new Date()) + getString(R.string.result_check));
        // model.calendar(null);
        model.data(model.getDateFormat().format(new Date()));


    }

    public void setCalendar() throws ParseException {
        for (int i = 0; i < caldroidFragment.getDatePagerAdapters().size(); i++) {
            if (selectMonth == caldroidFragment.getDatePagerAdapters().get(i).getDatetimeList().get(15).getMonth()) {
                CaldroidSampleCustomAdapter adapter = (CaldroidSampleCustomAdapter) caldroidFragment.getDatePagerAdapters().get(i);
                adapter.setData(model.getCalendarData());
            }
        }

        for (DateCheck d : model.getCalendarData()) {
            Log.v("date",d.getDt());
            Date colorDate = model.getDateFormat().parse(d.getDate());
            if (caldroidFragment != null) {
                caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(d.getVocColor()), colorDate);
                caldroidFragment.setTextColorForDate(R.color.white, colorDate);
            }
        }
        if (caldroidFragment != null) {
            caldroidFragment.refreshView();
        }
        guiderHandler.postDelayed(() -> guiderHandler.sendEmptyMessage(0),1000);
    }

    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            //    binding.calendarDetailTitle.setText(model.getDateFormat().format(date) + getString(R.string.result_check));
            model.data(model.getDateFormat().format(date));
            if (tempView != null) {
                tempView.findViewById(R.id.cell_background).setBackgroundColor(Color.TRANSPARENT);
            }
            tempView = view;
            view.findViewById(R.id.cell_background).setBackground(getResources().getDrawable(R.drawable.calendar_select));
            binding.toolbarTitle.setText(model.getCnFormat().format(date));
        }

        @Override
        public void onChangeMonth(int month, int year) {
            super.onChangeMonth(month, year);
            selectMonth = month;
            model.calendar(year + getString(R.string.dash) + (month));
            binding.toolbarTitle.setText(year + getString(R.string.year) + month + getString(R.string.month));
        }

        @Override
        public void onCaldroidViewCreated() {
            super.onCaldroidViewCreated();
        }
    };

    public void setView() {
        binding.recyclerView.setAdapter(new HistoryItemAdapter(model.getDataList(), this));
        if (model.getDataList() == null || model.getDataList().size() == 0) {
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
        } else {
            viewVisibleAnim(binding.emptyView, View.GONE);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        startActivity(new Intent(this, ChartDetailActivity.class)
                .putExtra("device", model.getScene().getDeviceId())
                .putExtra("name", model.getDataList().get(position).getName())
                .putExtra("type", model.getDataList().get(position).getDataType())
                .putExtra("unit", model.getDataList().get(position).getUnit()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (isExpnad) {
            ViewCompat.animate(binding.toolbarArrow).rotation(180).start();
            binding.appbar.setExpanded(false, true);
            isExpnad = false;
        } else {
            ViewCompat.animate(binding.toolbarArrow).rotation(0).start();
            binding.appbar.setExpanded(true, true);
            isExpnad = true;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0 || -verticalOffset <= binding.toolbar.getHeight()) {
            isExpnad = true;
            ViewCompat.animate(binding.toolbarArrow).rotation(0).start();
        } else {
            ViewCompat.animate(binding.toolbarArrow).rotation(180).start();
            isExpnad = false;

        }
    }

    public void onEmpty(View v) {
        ViewCompat.animate(binding.toolbarArrow).rotation(0).start();
        binding.appbar.setExpanded(true, true);
        isExpnad = true;
    }
}
