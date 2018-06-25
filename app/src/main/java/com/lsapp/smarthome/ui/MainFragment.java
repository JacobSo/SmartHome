package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.lsapp.smarthome.R;

import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.base.Scene;

import com.lsapp.smarthome.databinding.FragmentMainBinding;
import com.lsapp.smarthome.event.QuickTurnEvent;
import com.lsapp.smarthome.event.SubToMainEvent;
import com.lsapp.smarthome.ui.model.MainModel;
import com.zuni.library.utils.zToastUtil;

import java.util.ArrayList;

import rx.Subscription;


/**
 * Created by Administrator on 2016/9/12.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "MainFragment";
    private FragmentMainBinding binding;
    private Animation animation;
    private MainModel model;
    private int mDownInScreenX = 0;
    private Subscription subscription;
    private Handler guideHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1003:
                    if (BaseApplication.get(getActivity()) != null && BaseApplication.get(getActivity()).getSelectScene() != null) {
                        if (model.getScene().getSpaceId().equals(BaseApplication.get(getActivity()).getSelectScene().getSpaceId())
                                && BaseApplication.get(getActivity()).isGuide())
                            if (binding.pieChart1.getVisibility() == View.VISIBLE) {
                                guideUtil.createMain(Const.GUIDE_MAIN_QUICK, binding.pieChart1,
                                        getString(R.string.guide_main_quick_title), getString(R.string.guide_main_quick_content), new TapTargetView.Listener() {
                                            @Override
                                            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                                                super.onTargetDismissed(view, userInitiated);
                                                guideHandler.postDelayed(() -> guideHandler.sendEmptyMessage(1004), 500);

                                            }
                                        });
                            }
                    }

                    break;
                case 1004:
                    guideUtil.createMain(Const.GUIDE_MAIN_SLIDE, binding.expandView,
                            getString(R.string.guide_main_slide_title), getString(R.string.guide_main_slide_content), null);
                    break;
            }
        }
    };

    @Override
    protected void initModel() {
        model = new MainModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    public static MainFragment newInstance() {
        Log.d(TAG, "newInstance: ");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BaseApplication.get(getActivity()) != null && BaseApplication.get(getActivity()).getSelectScene() != null &&
                BaseApplication.get(getActivity()).getSelectScene().getSpaceId().equals(model.getScene().getSpaceId())) {
            Log.d(TAG, "onResume: ");
            model.setScene(BaseApplication.get(getActivity()).getSelectScene());
            updateView(model.getScene());
        }
        initSubscription();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        model.setScene(BaseApplication.get(getActivity()).getSelectScene());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.errorBtn.setOnClickListener(this);
        binding.quickTurnSingleBtn.setOnClickListener(this);
        //  updateView(model.getScene());
        return binding.getRoot();
    }

    private void initSubscription() {
        subscription = RxBus.getDefault().toObserverable(SubToMainEvent.class).subscribe(subToMainEvent -> {
            if (subToMainEvent.getActionId() != null && model.getScene().getActionId().equals(subToMainEvent.getActionId())) {
                Log.d(TAG, "initSubscription: ");
                updateView(model.getScene());
            }
        }, Throwable::printStackTrace);
    }


    /**
     * 刷新
     * 1.本fragment onResume刷新
     * 2.sub子fragment滑动回来更新
     * 3.数据手动更新
     *
     * @param data
     */
    public void updateView(Scene data) {
        Log.d(TAG, "updateView: ");
        model.setScene(data);
        //初始化pie
        binding.pieChart1.setCenterText(model.getScene().getVoc() + "");
        binding.pieChart1.setNoDataTextDescription("");
        binding.pieChart1.setNoDataText("");
        binding.pieChart1.getLegend().setEnabled(false);
        //初始化全部隐藏
        binding.pieChart1.setVisibility(View.INVISIBLE);
        binding.runningFlag.setVisibility(View.INVISIBLE);
        binding.gradeText.setVisibility(View.INVISIBLE);
        binding.gradeTips.setVisibility(View.INVISIBLE);
        binding.errorLayout.setVisibility(View.INVISIBLE);
        binding.quickTurnSingleBtn.setVisibility(View.INVISIBLE);

        if (model.getScene().getDeviceId() == null) {//没有检测器
            if (model.getScene().getActionId() != null && model.getScene().getAirclearConnect() == 1) {//有净化器 留一键按钮
                if (model.getScene() != null &&//
                        model.getScene().getAirClerState() != null &&
                        model.getScene().getAirClerState().getPurifier() == 0) {//净化器关
                    binding.quickTurnSingleBtn.setVisibility(View.VISIBLE);
                } else {//净化器开
                    quickTurnOn();
                }
            } else {//没有净化器，优先提示净化器
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.errorTips.setText(getString(R.string.no_action_machine));
                binding.errorBtn.setText(getString(R.string.go_binding));
            }

        } else if (model.getScene().getSpaceData() == null || model.getScene().getSpaceData().size() == 0) {//有检测器，没有检测数据
            if (model.getScene().getActionId() != null && model.getScene().getAirclearConnect() == 1) {//有净化器
                if (model.getScene().getAirClerState() != null && model.getScene().getAirClerState().getPurifier() == 0) {//净化器关
                    binding.quickTurnSingleBtn.setVisibility(View.VISIBLE);
                } else {//净化器开
                    quickTurnOn();
                }
            } else {//没有净化器，但有检测器，优先提示检测器
                binding.errorLayout.setVisibility(View.VISIBLE);
                if (model.getScene().getCheckairConnect() == 0) {//检测器关
                    binding.errorTips.setText(getString(R.string.no_connect_check_machine));
                    binding.errorBtn.setText(getString(R.string.re_connect));
                } else {//检测器开
                    binding.errorTips.setText(getString(R.string.no_check_data));
                    binding.errorBtn.setText(getString(R.string.review));
                }
            }

        } else {//设备齐全

            binding.gradeTips.setVisibility(View.VISIBLE);
            binding.gradeText.setVisibility(View.VISIBLE);
            if (model.getScene().getAirClerState() != null &&
                    model.getScene().getAirClerState().getPurifier() != 0 &&
                    model.getScene().getAirclearConnect() != 0) {//净化器已开启
                quickTurnOn();
            } else {
                binding.pieChart1.setVisibility(View.VISIBLE);
                setChart(binding.pieChart1, model.getScene().getVoc());
            }
            guideHandler.postDelayed(() -> guideHandler.sendEmptyMessage(1003), 1000);

        }
        binding.setScene(model.getScene());
        binding.executePendingBindings();
        setViewColor();

    }

    //一键启动局部刷新
    public void setTurnView(boolean isSuccess) {
        if (isSuccess) {
            quickTurnOn();
            RxBus.getDefault().post(new QuickTurnEvent(model.getScene().getActionId()));
        } else {
            binding.pieChart1.setCenterTextSize(45f);
            binding.pieChart1.setCenterText(model.getScene().getVoc() + "");
            binding.pieChart1.invalidate();
        }
    }

    private void quickTurnOn() {
        binding.runningFlag.setVisibility(View.VISIBLE);
        binding.pieChart1.setVisibility(View.GONE);//button with check machine
        binding.quickTurnSingleBtn.setVisibility(View.GONE);//button without check machine
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_run);
        binding.runningFlag.startAnimation(animation);
        binding.gradeTips.setText(model.getScene().getVoc() + "");
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_tip);
        binding.gradeTips.startAnimation(animation);
    }


    public void setViewColor() {
        //good -mid- bad
        //outside:#3bb2e0,#3bb2e0,#7b97a2
        //inside:#c8ea9c,#fff38b,#ffd88e
        String weather = "#3bb2e0";
        String inside;
        if (BaseApplication.get(getActivity()).getWeatherData() != null &&
                BaseApplication.get(getActivity()).getWeatherData().getResult().get(0).getAirCondition().contains("污染")) {
            weather = "#7b97a2";
        }
        if (model.getScene().getVocType() == -1 || model.getScene().getVocType() == 0 || model.getScene().getVocType() == 1) {
            inside = "#c8ea9c";
        } else if (model.getScene().getVocType() == 2) {
            inside = "#fff38b";
        } else {
            inside = "#ffd88e";
        }
        int colors[] = {Color.parseColor(weather), Color.parseColor(inside), Color.parseColor(inside)};//分别为outside,inside,inside
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        binding.getRoot().setBackground(gradientDrawable);
        binding.gradeTips.setFillColor(model.getScene().getVocColor());

        binding.imageView.setImageResource(model.getScene().getSpaceAnim());
        if (model.getScene().getSpaceData() != null && model.getScene().getDeviceId() != null && model.getScene().getSpaceData().size() != 0) {
            AnimationDrawable animationDrawable = (AnimationDrawable) binding.imageView.getDrawable();
            animationDrawable.start();
        }
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_center);
        animation.setDuration(500L);
        binding.imageView.startAnimation(animation);
        if (binding.gradeTips.getVisibility() == View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_tip);
            binding.gradeTips.startAnimation(animation);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == binding.errorBtn) {
            startActivity(new Intent(getActivity(), DeviceListActivity.class).putExtra("scene", model.getScene()));//

        } else {
            //   if (BaseApplication.get(getActivity()).getPermission().equals("0")) {
            if (model.getScene().getIsShareSpace() == 1 && model.getScene().getIsAllowOperat() == 0) {
                zToastUtil.showSnack(binding.getRoot(), getString(R.string.no_permission));
                return;
            }
            model.turnOnWithOutCheck();
        }


    }

    private void setChart(PieChart pieChart, int value) {
        pieChart.setDescription("");
        pieChart.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownInScreenX = (int) motionEvent.getRawX();
                    break;
                case MotionEvent.ACTION_UP:
                    // if (BaseApplication.get(getActivity()).getPermission().equals("0")) {
                    if (model.getScene().getIsShareSpace() == 1 && model.getScene().getIsAllowOperat() == 0) {
                        zToastUtil.showSnack(binding.getRoot(), getString(R.string.no_permission));
                    } else {
                        if ((int) motionEvent.getRawX() < (mDownInScreenX - 10) || (int) motionEvent.getRawX() < (mDownInScreenX + 10)) {
                            if (model.getScene().getAirclearConnect() == 0) {
                                zToastUtil.showSnack(binding.getRoot(), getString(R.string.no_quick_turn));
                                //RxBus.getDefault().post(new QuickTurnEvent());
                            } else {
                                pieChart.setCenterTextSize(18f);
                                pieChart.setCenterText(getString(R.string.loading_quick_turn));
                                pieChart.invalidate();
                                model.turnOn();
                            }
                        }
                    }
                    break;
            }
            return true;
        });
        pieChart.setCenterTextSize(45f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setHoleRadius(95f);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.setNoDataText("");
        pieChart.setNoDataTextDescription("");
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) (100 - value), ""));
        entries.add(new PieEntry((float) value, ""));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#22000000"));
        colors.add(Color.WHITE);

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }


    public void selected(int which) {

    }

}
