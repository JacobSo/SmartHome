package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.WeatherData;
import com.lsapp.smarthome.databinding.ViewPagerLayoutBinding;
import com.lsapp.smarthome.event.DraggableEvent;
import com.lsapp.smarthome.event.PagerScrollEnableEvent;
import com.lsapp.smarthome.event.WeatherEvent;
import com.lsapp.smarthome.event.WeatherVisibleEvent;
import com.lsapp.smarthome.ui.model.MainPagerModel;
import com.lsapp.smarthome.utils.WeatherUtil;

import java.util.ArrayList;

import rx.Subscription;


/**
 * Created by Administrator on 2016/8/9.
 */
public class MainPagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = "MainPagerFragment";
    private ViewPagerLayoutBinding binding;
    private Subscription subscriptionWeather = null;//init weather
    private Subscription subscriptionWeatherVisible = null;//visible weather
    private Subscription subscriptionPagerScroll = null;//main sub pager scrollable
    private MainPagerModel model;
    private FragmentAdapter adapter;
    private boolean isRoomSet = false;
    private boolean isWeatherVisible = true;
    private boolean isPause = false;
    private Handler guideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    if (getActivity() != null) {
                        if (binding.weatherLayout.getVisibility() == View.VISIBLE && binding.roomTitle.getVisibility() == View.VISIBLE)
                            guideUtil.createMain(Const.GUIDE_MAIN_WEATHER, binding.weatherImage,
                                    getString(R.string.guide_main_outside_title), getString(R.string.guide_main_outside_content), null);
                    }
                    break;
            }
        }
    };


    public static MainPagerFragment newInstance() {
        MainPagerFragment fragment = new MainPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initModel() {
        model = new MainPagerModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (isRoomSet || isWeatherVisible) {
            binding.getRoot().setEnabled(false);
            onRefresh();
            isRoomSet = false;
        }

        isPause = false;


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_layout, container, false);
        AnimationDrawable cloudDrawable = (AnimationDrawable) binding.weatherView.getDrawable();
        cloudDrawable.start();
        binding.mPager.addOnPageChangeListener(this);
        binding.btnAddScene.setOnClickListener(this);
        binding.swipeContainer.setOnRefreshListener(this);
        binding.weatherLayout.setOnClickListener(this);
        initSubscription();
        return binding.getRoot();
    }

    private void initSubscription() {
        subscriptionPagerScroll = RxBus.getDefault().toObserverable(PagerScrollEnableEvent.class).subscribe(pagerScrollEnableEvent -> {
            binding.mPager.setPagingEnabled(pagerScrollEnableEvent.isEnable());
            binding.swipeContainer.setEnabled(pagerScrollEnableEvent.isEnable());
        }, Throwable::printStackTrace);
        subscriptionWeatherVisible = RxBus.getDefault().toObserverable(WeatherVisibleEvent.class).subscribe(weatherVisibleEvent -> {
            binding.swipeContainer.setRefreshing(false);
            isWeatherVisible = weatherVisibleEvent.isVisible();
            binding.strip.setVisibility(weatherVisibleEvent.isVisible()?View.VISIBLE:View.GONE);
            weatherVisible();
        }, Throwable::printStackTrace);
        subscriptionWeather = RxBus.getDefault().toObserverable(WeatherEvent.class).subscribe(weatherEvent -> {
            if (!isPause)
                setWeatherView(weatherEvent.getWeatherData());
        }, Throwable::printStackTrace);
        if (BaseApplication.get(getContext()).getWeatherData() != null) {
            setWeatherView(BaseApplication.get(getContext()).getWeatherData());
        }
    }

    /**
     * 天气布局可视化
     */
    private void weatherVisible() {
        // zToastUtil.show(getActivity(), "weather view was covered");
        binding.swipeContainer.setEnabled(isWeatherVisible);
        binding.weatherView.setVisibility(isWeatherVisible ? View.VISIBLE : View.GONE);
        binding.weatherLayout.setVisibility(isWeatherVisible ? View.VISIBLE : View.GONE);
        binding.roomTitle.setVisibility(isWeatherVisible && model.getSceneList() != null && model.getSceneList().size() != 0 ? View.VISIBLE : View.GONE);


    }

    public void setWeatherView(WeatherData weatherData) {
        binding.weatherView.setImageResource(WeatherUtil.getWeatherRoof(weatherData.getResult().get(0).getWeather()));
        binding.setData(weatherData);
        binding.executePendingBindings();
        AnimationDrawable animationDrawable = (AnimationDrawable) binding.weatherView.getDrawable();
        animationDrawable.start();
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_center);
        anim.setDuration(500L);
        binding.weatherView.setVisibility(View.VISIBLE);
        binding.weatherLayout.setVisibility(View.VISIBLE);
        binding.weatherView.startAnimation(anim);
        binding.weatherLayout.startAnimation(anim);
        binding.weatherImage.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(WeatherUtil.getWeatherImage(weatherData.getResult().get(0).getWeather())).into(binding.weatherImage);

        binding.controlTempLoading.setVisibility(View.GONE);
        binding.weatherNowLayout.setVisibility(View.VISIBLE);
        weatherVisible();


    }

    public void prepareRefreshWeather() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_center);
        anim.setDuration(500L);
        binding.weatherLayout.setVisibility(View.GONE);
        binding.weatherLayout.startAnimation(anim);
        binding.weatherImage.setVisibility(View.GONE);
        binding.controlTempLoading.setVisibility(View.VISIBLE);
        binding.weatherNowLayout.setVisibility(View.GONE);
    }

    public void setView(boolean isSomeoneDelete) {
        Log.d(TAG, "setView: ");
        if (!isPause) {
            binding.swipeContainer.setRefreshing(false);
            binding.swipeContainer.setEnabled(model.getSceneList().size() != 0);
            binding.roomTitle.setVisibility(model.getSceneList().size() == 0 ? View.GONE : View.VISIBLE);
            if (model.getSceneList().size() != 0) {
                binding.roomTitle.setText(model.getSceneList().get(isSomeoneDelete ? 0 : binding.mPager.getCurrentItem()).getSpaceName());
                viewVisibleAnim(binding.beginLayout, View.GONE);
            } else {
                viewVisibleAnim(binding.beginLayout, View.VISIBLE);
                binding.mPager.removeAllViews();
                binding.mPager.setAdapter(adapter = null);
            }

            if (adapter != null && binding.mPager.getChildCount() != 0) {
                if (isSomeoneDelete) {//room list size change refresh all pagers
                    adapter = new FragmentAdapter(getChildFragmentManager());
                    binding.mPager.setAdapter(adapter);
                    binding.strip.setViewPager(binding.mPager);
                    binding.roomTitle.setVisibility(View.VISIBLE);
                } else {//update pagers
                    adapter.notifyDataSetChanged();
                    adapter.update(BaseApplication.get(getActivity()).getSelectPosition());//new data update
                    binding.mPager.setCurrentItem(BaseApplication.get(getActivity()).getSelectPosition());
                    binding.strip.setViewPager(binding.mPager);
                }

            } else {
                binding.roomLoading.setVisibility(View.GONE);
                guideHandler.postDelayed(() -> guideHandler.sendEmptyMessage(1001), 1000);

                if (model.getSceneList().size() != 0) {
                    adapter = new FragmentAdapter(getChildFragmentManager());
                    binding.mPager.setAdapter(adapter);
                    binding.strip.setViewPager(binding.mPager);
                    binding.roomTitle.setVisibility(View.VISIBLE);
                }
            }

            RxBus.getDefault().post(new DraggableEvent(null));
            binding.mPager.setPagingEnabled(true);
            weatherVisible();
        }
    }

    public void setFailedView() {
        binding.swipeContainer.setRefreshing(false);
        binding.swipeContainer.setEnabled(isWeatherVisible);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: ");
        BaseApplication.get(getActivity()).setSelectPosition(position);//slide pager update
        binding.roomTitle.setText(model.getSceneList().get(position).getSpaceName());
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        anim.setDuration(500L);
        binding.roomTitle.startAnimation(anim);
        adapter.update(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        binding.swipeContainer.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isPause = true;
        if (subscriptionWeather != null && !subscriptionWeather.isUnsubscribed())
            subscriptionWeather.unsubscribe();
        if (subscriptionWeatherVisible != null && !subscriptionWeatherVisible.isUnsubscribed())
            subscriptionWeatherVisible.unsubscribe();
        if (subscriptionPagerScroll != null && !subscriptionPagerScroll.isUnsubscribed()) {
            subscriptionPagerScroll.unsubscribe();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.weatherLayout) {
            if (BaseApplication.get(getActivity()).getWeatherData() != null) {
                model.weatherDetailDialog();
            } else {
                //model.weather(BaseApplication.get(getActivity()).getWeatherData().getResult().get(0).getCity(), BaseApplication.get(getActivity()).getWeatherData().getResult().get(0).getProvince());

            }
        }  else {
            isRoomSet = true;
            startActivity(new Intent(getActivity(), GuideActivity.class));
        }

    }

    @Override
    public void onRefresh() {
        binding.mPager.setPagingEnabled(false);
        binding.swipeContainer.setRefreshing(true);
        RxBus.getDefault().post(new DraggableEvent((view, motionEvent) -> true));
        model.get();
    }

    class FragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<String> tagList = new ArrayList<>();
        private FragmentManager fragmentManager;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;

        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: ");
            return MainVerticalPagerFragment.newInstance();
        }

        @Override
        public int getCount() {
            if (model.getSceneList() == null) {
                return 0;
            } else
                return model.getSceneList().size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem: ");
            boolean exist = false;
            for (String s : tagList) {
                if (s.equals(makeFragmentName(container.getId(), getItemId(position)))) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                tagList.add(makeFragmentName(container.getId(), getItemId(position)));
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(TAG, "destroyItem: ");
            super.destroyItem(container, position, object);
            //tagList.remove(makeFragmentName(container.getId(), getItemId(position)));//把tag删掉

        }

        public void update(int position) {
            Log.d(TAG, "update: " + tagList);
            MainVerticalPagerFragment fragment = (MainVerticalPagerFragment) fragmentManager.findFragmentByTag(tagList.get(position));
            if (fragment == null) {
                return;
            }
            fragment.update(model.getSceneList().get(position));
        }

        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }
    }
}
