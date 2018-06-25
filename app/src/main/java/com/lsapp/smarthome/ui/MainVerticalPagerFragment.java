package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentVerticalPagerBinding;
import com.lsapp.smarthome.event.DragBackEvent;
import com.lsapp.smarthome.event.DraggableEvent;
import com.lsapp.smarthome.event.PagerScrollEnableEvent;
import com.lsapp.smarthome.event.WeatherVisibleEvent;
import com.lsapp.smarthome.ui.widget.DragLayout;

import rx.Subscription;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MainVerticalPagerFragment extends BaseFragment {
    private final static String TAG = "MainVerticalPagerFragment";
    private FragmentVerticalPagerBinding binding;
    private Scene scene;
    private MainSubFragment subFragment;
    private MainFragment mainFragment;

    private Subscription subscription;//main pager called and  exit
    private Subscription touchSubscription;//touch enable

    public static MainVerticalPagerFragment newInstance() {
        MainVerticalPagerFragment fragment = new MainVerticalPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initModel() {

    }


    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vertical_pager, container, false);
        scene = BaseApplication.get(getActivity()).getSelectScene();
        mainFragment = MainFragment.newInstance();
        subFragment = MainSubFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.first, mainFragment).replace(R.id.second, subFragment)
                .commitNow();

        DragLayout.ShowNextPageNotifier nextIntf = type -> {
            Log.d(TAG, "onCreateView: " + type);
            RxBus.getDefault().post(new WeatherVisibleEvent(type == 0));//main pager fragment
            RxBus.getDefault().post(new PagerScrollEnableEvent(type == 0));//main pager fragment
            if (mainFragment != null)
                mainFragment.selected(type);
            if (subFragment != null)
                subFragment.selected(type);

        };
        binding.dragLayout.setNextPageListener(nextIntf);
        initSubscription();
        return binding.getRoot();
    }


    public void initSubscription() {
        touchSubscription = RxBus.getDefault().toObserverable(DraggableEvent.class).subscribe(draggableEvent -> {
            binding.dragLayout.setOnTouchListener(draggableEvent.getListener());
        }, Throwable::printStackTrace);
        subscription = RxBus.getDefault().toObserverable(DragBackEvent.class).subscribe(dragBackEvent -> {
            binding.dragLayout.goIndex();
            RxBus.getDefault().post(new WeatherVisibleEvent(true));
        }, Throwable::printStackTrace);
    }

    public void update(Scene data) {
        scene = data;
        if (mainFragment != null)
            mainFragment.updateView(scene);
        else
            mainFragment = MainFragment.newInstance();


        if (subFragment != null)
            subFragment.updateView(scene);
        else
            subFragment = MainSubFragment.newInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        //  scene = BaseApplication.get(getActivity()).getSelectScene();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (touchSubscription != null && !touchSubscription.isUnsubscribed()) {
            touchSubscription.unsubscribe();
        }
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
