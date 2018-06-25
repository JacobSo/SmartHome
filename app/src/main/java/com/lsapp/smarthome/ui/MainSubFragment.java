package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;


import com.ta.utdid2.android.utils.StringUtils;
import com.transitionseverywhere.TransitionManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentMainSubBinding;
import com.lsapp.smarthome.event.DragBackEvent;
import com.lsapp.smarthome.event.QuickTurnEvent;
import com.lsapp.smarthome.event.SubToMainEvent;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.MainItemAdapter;
import com.lsapp.smarthome.ui.model.MainSubModel;

import java.lang.reflect.Array;
import java.util.Arrays;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import rx.Subscription;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MainSubFragment extends BaseFragment implements View.OnClickListener, OnRecycleViewItemClickListener {
    private final static String TAG = "MainSubFragment";
    private FragmentMainSubBinding binding;
    private Subscription subscription;//quick turn
    //anim
    private boolean positionChanged = true;
    private int panelType = 0;
    private MainSubModel model;
    ViewGroup viewGroup;
    MainItemAdapter adapter;
    private Handler guideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1005:
                    guideUtil.createMain(Const.GUIDE_MAIN_PANEL, binding.btnFunctionWind,
                            getString(R.string.guide_main_panel_title), getString(R.string.guide_main_panel_content), null);
                    break;
                case 1006:
                    guideUtil.createMain(Const.GUIDE_MAIN_PANEL_SUB, binding.functionSubLevel3,
                            getString(R.string.guide_main_panel_sub_title), getString(R.string.guide_main_panel_sub_content), null);
                    break;
            }
        }
    };

    public static MainSubFragment newInstance() {
        MainSubFragment fragment = new MainSubFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initModel() {
        model = new MainSubModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
/*        StringBuilder sb = new StringBuilder();
        String temp = "cf:23:df:90:ad:ae";
        String[] a = temp.split(":");
        Arrays.sort(a);
        for (String b : a) {
            sb.append(b).append(":");
        }
        Log.d(TAG, "onResume: " + sb.substring(0, sb.length() - 1).toString());*/

        if (BaseApplication.get(getActivity()) != null && BaseApplication.get(getActivity()).getSelectScene() != null &&
                BaseApplication.get(getActivity()).getSelectScene().getSpaceId().equals(model.getScene().getSpaceId())) {
            if (BaseApplication.get(getActivity()).getSelectScene().getDeviceId() == null ||
                    TextUtils.isEmpty(BaseApplication.get(getActivity()).getSelectScene().getDeviceId())) {
                binding.recyclerView.setAdapter(new MainItemAdapter(null, this));
                model.getScene().setSpaceData(null);
            }
            model.setScene(BaseApplication.get(getActivity()).getSelectScene());
            binding.setScene(model.getScene());
            binding.executePendingBindings();
        }
        initSubscription();

    }

    public void selected(int which) {
        if (which == 1) {
            if (binding.controlPanel.getVisibility() == View.VISIBLE)
                guideHandler.postDelayed(() -> guideHandler.sendEmptyMessage(1005), 500);
        } else {
            if (binding.functionSubLayout.getVisibility() == View.VISIBLE) {
                if (panelType == 0) {
                    binding.btnFunctionWind.performClick();
                } else if (panelType == 1) {
                    binding.btnFunctionIon.performClick();
                } else if (panelType == 2) {
                    binding.btnFunctionTimer.performClick();
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model.setScene(BaseApplication.get(getActivity()).getSelectScene());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_main_sub, null, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.btnHistory.setOnClickListener(this);

        //main panel
        binding.controlPanel.setOnClickListener(this);
        binding.btnFunctionWind.setOnClickListener(this);
        binding.btnFunctionIon.setOnClickListener(this);
        binding.btnFunctionTimer.setOnClickListener(this);
        binding.btnFunctionCustomize.setOnClickListener(this);

        //sub panel
        binding.functionSubShut.setOnClickListener(this);
        binding.functionSubLevel1.setOnClickListener(this);
        binding.functionSubLevel2.setOnClickListener(this);
        binding.functionSubLevel3.setOnClickListener(this);

        binding.btnDevice.setOnClickListener(this);
        binding.btnClose.setOnClickListener(this);
        binding.btnReconnect.setOnClickListener(this);
        binding.errorBtn.setOnClickListener(this);
        binding.turnOnBtn.setOnClickListener(this);
        if (model.getScene() != null)
            updateView(model.getScene());
        changePosition(binding.btnFunctionTimer, binding.btnFunctionIon, binding.btnFunctionWind);
        viewGroup = container;
        return binding.getRoot();
    }

    public void initSubscription() {
        subscription = RxBus.getDefault().toObserverable(QuickTurnEvent.class).subscribe(quickTurnEvent -> {//一键启动响应ui
            if (quickTurnEvent.getActionId() != null && model.getScene().getActionId() != null && model.getScene().getActionId().equals(quickTurnEvent.getActionId())) {//设备id匹配才更新
                model.getScene().getAirClerState().setOnOff(1);
                if (model.getScene().getVocType() == 0 || model.getScene().getVocType() == 1) {
                    model.getScene().getAirClerState().setPurifier(2);
                    model.getScene().getAirClerState().setIonMode(0);
                } else if (model.getScene().getVocType() == 2) {
                    model.getScene().getAirClerState().setPurifier(1);
                    model.getScene().getAirClerState().setIonMode(1);
                } else {
                    model.getScene().getAirClerState().setPurifier(3);
                    model.getScene().getAirClerState().setIonMode(2);
                }
                updateView(model.getScene());
            }

        }, Throwable::printStackTrace);
    }

    //model result
    public void setControlView(boolean isFail) {
        if (BaseApplication.get(getActivity()) != null && BaseApplication.get(getActivity()).getSelectScene() != null &&
                BaseApplication.get(getActivity()).getSelectScene().getSpaceId().equals(model.getScene().getSpaceId())) {
            BaseApplication.get(getActivity()).getSelectScene().setAirClerState(model.getControl());//update total control status
            model.setScene(BaseApplication.get(getActivity()).getSelectScene());
            updateView(model.getScene());
            RxBus.getDefault().post(new SubToMainEvent(model.getScene().getActionId()));
            //close panel
            if (!isFail) {
                if (panelType == 0) {
                    binding.btnFunctionWind.performClick();
                } else if (panelType == 1) {
                    binding.btnFunctionIon.performClick();
                } else if (panelType == 2) {
                    binding.btnFunctionTimer.performClick();
                }
            }
        }
    }

    /**
     * 1.初始化
     * 2.main fragment 一键启动by subscription
     * 3.本fragment 操作面板
     * 4.手动刷新数据
     *
     * @param data
     */
    public void updateView(Scene data) {
        Log.d(TAG, "updateView: ");
        model.setScene(data);
        adapter = new MainItemAdapter(model.getScene().getSpaceData(), this);
        binding.recyclerView.setAdapter(adapter);
        binding.setScene(model.getScene());
        binding.setIsAdmin(!(model.getScene().getIsShareSpace()==1&&model.getScene().getIsAllowOperat()==0));
        binding.executePendingBindings();
        if (model.getScene().getAirClerState() != null) {
            binding.windImage.setImageResource(model.getScene().getAirClerState().getWindImage());
            binding.ionImage.setImageResource(model.getScene().getAirClerState().getIonImage());
            binding.timerImage.setImageResource(model.getScene().getAirClerState().getTimeImage());
        }
    }

    public void updateAdapterView() {
        adapter.notifyDataSetChanged();
        //   binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(new MainItemAdapter(model.getScene().getSpaceData(), this)));

    }

    @Override
    public void onClick(View view1) {
        //设备按钮/错误按钮/重连按钮/绑定按钮
        if (view1 == binding.turnOnBtn || view1 == binding.btnReconnect || view1 == binding.errorBtn) {
            startActivity(new Intent(getActivity(), DeviceListActivity.class).putExtra("reconnect", true).putExtra("scene", model.getScene()));
            //历史
        } else if (view1 == binding.btnDevice) {
            startActivity(new Intent(getActivity(), DeviceListActivity.class)
                    .putExtra("scene", model.getScene()));
        } else if (view1 == binding.btnHistory) {
            startActivity(new Intent(getActivity(), HistoryActivity.class).putExtra("scene", model.getScene()));
            //关闭
        } else if (view1 == binding.btnClose) {
            RxBus.getDefault().post(new DragBackEvent());
            //风速 打开面板
        } else if (view1 == binding.btnFunctionWind) {
            setWindSubLayout();
            changePosition(view1, binding.btnFunctionIon, binding.btnFunctionTimer);
            guideHandler.postDelayed(() -> guideHandler.sendEmptyMessage(1006), 500);
            //负离子 打开面板
        } else if (view1 == binding.btnFunctionIon) {
            setIonSubLayout();
            changePosition(view1, binding.btnFunctionWind, binding.btnFunctionTimer);
            //定时 打开面板
        } else if (view1 == binding.btnFunctionTimer) {
            setTimerSubLayout();
            changePosition(view1, binding.btnFunctionIon, binding.btnFunctionWind);
            //自定义 跳转页面
        } else if (view1 == binding.btnFunctionCustomize) {
            startActivity(new Intent(getActivity(), CustomizeListActivity.class).putExtra("scene", model.getScene()));
            //子面板
        } else if (view1 == binding.functionSubShut) {
            model.airControl(panelType, 0);
        } else if (view1 == binding.functionSubLevel1) {
            model.airControl(panelType, 1);
        } else if (view1 == binding.functionSubLevel2) {
            model.airControl(panelType, 2);
        } else if (view1 == binding.functionSubLevel3) {
            model.airControl(panelType, 3);
        }
    }


    private void changePosition(View showView, View hideView1, View hideView2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding.controlPanel);
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) showView.getLayoutParams();
        if (positionChanged) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_center);
            hideView2.setVisibility(View.VISIBLE);
            hideView1.setVisibility(View.VISIBLE);
            binding.btnFunctionCustomize.setVisibility(View.VISIBLE);

            hideView1.startAnimation(animation);
            hideView2.startAnimation(animation);
            binding.btnFunctionCustomize.startAnimation(animation);

            lp.gravity = Gravity.START;
            //

            animateButtonsOut();
            //   animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_center);

            binding.functionSubLayout.setVisibility(View.GONE);
            //     binding.functionSubLayout.startAnimation(animation);

        } else {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_center);
            hideView1.setVisibility(View.GONE);
            hideView2.setVisibility(View.GONE);
            binding.btnFunctionCustomize.setVisibility(View.GONE);

            hideView1.startAnimation(animation);
            hideView2.startAnimation(animation);
            binding.btnFunctionCustomize.startAnimation(animation);
            lp.gravity = Gravity.CENTER;

            binding.functionSubLayout.setVisibility(View.VISIBLE);

            animateButtonsIn();
        }
        positionChanged = !positionChanged;
        showView.setLayoutParams(lp);


    }

    private void animateButtonsIn() {
        Interpolator interpolator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            interpolator = AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in);
        }

        for (int i = 0; i < binding.functionSubLayout.getChildCount(); i++) {
            View child = binding.functionSubLayout.getChildAt(i);
            child.animate()
                    .setStartDelay(100 + i * 100)
                    .setInterpolator(interpolator)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1);
        }

    }

    private void animateButtonsOut() {
        Interpolator interpolator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            interpolator = AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in);
        }
        for (int i = 0; i < binding.functionSubLayout.getChildCount(); i++) {
            View child = binding.functionSubLayout.getChildAt(i);
            child.animate()
                    .setStartDelay(i)
                    .setInterpolator(interpolator)
                    .alpha(0)
                    .scaleX(0f)
                    .scaleY(0f);
        }
    }

    private void setWindSubLayout() {
        panelType = 0;
        binding.functionSubLevel3.setVisibility(View.VISIBLE);
        binding.functionSubLevel1Img.setImageResource(R.drawable.function_wind2_grey);
        binding.functionSubLevel2Img.setImageResource(R.drawable.function_wind1_grey);
        binding.functionSubLevel3Img.setImageResource(R.drawable.function_wind3_grey);
        binding.functionSubLevel1Txt.setText(getString(R.string.model_day));
        binding.functionSubLevel2Txt.setText(getString(R.string.model_sleep));
        binding.functionSubLevel3Txt.setText(getString(R.string.model_shot));
    }

    private void setIonSubLayout() {
        panelType = 1;
        binding.functionSubLevel3.setVisibility(View.GONE);
        binding.functionSubLevel1Img.setImageResource(R.drawable.function_on1_grey);
        binding.functionSubLevel2Img.setImageResource(R.drawable.function_on2_grey);
        binding.functionSubLevel1Txt.setText(getString(R.string.model_normal));
        binding.functionSubLevel2Txt.setText(getString(R.string.madel_strong));
    }

    private void setTimerSubLayout() {
        panelType = 2;
        binding.functionSubLevel3.setVisibility(View.VISIBLE);
        binding.functionSubLevel1Img.setImageResource(R.drawable.function_timer1_grey);
        binding.functionSubLevel2Img.setImageResource(R.drawable.function_timer2_grey);
        binding.functionSubLevel3Img.setImageResource(R.drawable.function_timer3_grey);
        binding.functionSubLevel1Txt.setText(getString(R.string.hour_1));
        binding.functionSubLevel2Txt.setText(getString(R.string.hour_2));
        binding.functionSubLevel3Txt.setText(getString(R.string.hour_3));
    }


    @Override
    public void onItemClick(View v, int position) {
/*        if (position == 0) {
            //    binding.test.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(viewGroup);
            }
            binding.topLayout.setVisibility(((CompoundButton) v).isChecked() ? View.GONE : View.VISIBLE);
            if (((CompoundButton) v).isChecked())
                model.loop();
            else
                model.stopLoop();*/
/*            binding.topLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), ((CompoundButton) v).isChecked() ? R.anim.fade_out_center : R.anim.fade_in_center));
            if (((CompoundButton) v).isChecked())
                binding.bottomLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom));*/
  //      } else {
            startActivity(new Intent(getActivity(), ChartDetailActivity.class)
                    .putExtra("name", model.getScene().getSpaceData().get(position).getName())
                    .putExtra("type", model.getScene().getSpaceData().get(position).getDataType())
                    .putExtra("device", model.getScene().getDeviceId())
                    .putExtra("unit", model.getScene().getSpaceData().get(position).getUnit())
            );
    //    }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }


}
